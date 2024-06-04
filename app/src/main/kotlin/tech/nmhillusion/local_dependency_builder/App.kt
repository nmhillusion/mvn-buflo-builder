package tech.nmhillusion.local_dependency_builder

import tech.nmhillusion.local_dependency_builder.builder.FolderBuilder
import tech.nmhillusion.local_dependency_builder.model.DependencyEntity
import tech.nmhillusion.local_dependency_builder.model.LocalBuilderConfig
import tech.nmhillusion.local_dependency_builder.runner.GitCommandRunner
import tech.nmhillusion.local_dependency_builder.runner.MavenCommandRunner
import tech.nmhillusion.n2mix.helper.YamlReader
import tech.nmhillusion.n2mix.helper.log.LogHelper
import tech.nmhillusion.n2mix.model.ResultResponseEntity
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

class App(private val configPath: String) {
    private val dependencies = ArrayList<DependencyEntity>()
    private lateinit var localBuilderConfig: LocalBuilderConfig

    val name: String
        get() {
            return "Local Dependency Builder"
        }


    fun <T> getConfig(configKey: String, class2Cast: Class<T>): T {
        Files.newInputStream(Path.of(configPath)).use {
            return YamlReader(it).getProperty(configKey, class2Cast)
        }
    }

    private fun loadAppConfig() {
        this.localBuilderConfig = getConfig("config", Map::class.java).let {
            LocalBuilderConfig.fromMap(it)
        }
        LogHelper.getLogger(this).info("localBuilderConfig: $localBuilderConfig")

        getConfig("dependencies", List::class.java).forEach {
            if (it is Map<*, *>) {
                val dependencyEntity = DependencyEntity.fromMap(it)
                LogHelper.getLogger(this).info("dependencyEntity: $dependencyEntity")

                dependencies.add(dependencyEntity)
            }
        }
    }

    fun exec() {
        /// Mark: Load App Config
        loadAppConfig()

        /// Mark: EXEC ///////////////////////

        val createdTempFolder = FolderBuilder.createFolders(
            Path.of(localBuilderConfig.tempRepoPath)
        )

        LogHelper.getLogger(this).info("createdTempFolder: $createdTempFolder")

        val installResult = ArrayList<ResultResponseEntity<DependencyEntity>>(dependencies.size)
        dependencies.forEach {
            try {
                buildDependency(it)
                LogHelper.getLogger(this).info(">>> Successfully build dependency: ${it.name} <<<")

                installResult.add(
                    ResultResponseEntity<DependencyEntity>()
                        .setSuccess(true)
                        .setData(it)
                )
            } catch (ex: Exception) {
                LogHelper.getLogger(this).error("!!! Failed to build dependency: ${it.name} : ${ex.message}")
                installResult.add(
                    ResultResponseEntity<DependencyEntity>()
                        .setSuccess(false)
                        .setMessage(ex.message)
                        .setData(it)
                )
            }
        }

        /// Mark: POST EXEC
        println("[DEPENDENCY INSTALL RESULT]")
        installResult.forEach {
            println(
                """
                |===================================|
                |dependency: ${it.data?.name}
                |success : ${it.success}
                |message : ${it.message}
                |===================================|
            """.trimMargin()
            )
        }
    }

    private fun buildDependency(dependency_: DependencyEntity) {
        /// Mark: PREPARE FOLDER
        val localRepoPath = Path.of(localBuilderConfig.tempRepoPath, dependency_.name)
        LogHelper.getLogger(this).info("localRepoPath: $localRepoPath")

        if (localRepoPath.exists()) {
            LogHelper.getLogger(this).info("Deleting existing localRepoPath: $localRepoPath")
            localRepoPath.toFile().deleteRecursively()
        }

        /// Mark: GIT
        val gitCommandRunner = GitCommandRunner(dependency_, localBuilderConfig.tempRepoPath)
        val cloneExitCode = gitCommandRunner.cloneExec()

        LogHelper.getLogger(this).info("cloneExitCode: $cloneExitCode")

        if (0 != cloneExitCode) {
            throw Exception("Failed to clone repository")
        }

        /// Mark: MAVEN
        val mavenCommandRunner = MavenCommandRunner(dependency_, localBuilderConfig.tempRepoPath)

        val mvnCleanExitCode = mavenCommandRunner.cleanExec()
        LogHelper.getLogger(this).info("mvnCleanExitCode: $mvnCleanExitCode")

        if (0 != mvnCleanExitCode) {
            throw Exception("Failed to clean repository")
        }

        val mvnCompileExitCode = mavenCommandRunner.compileExec()
        LogHelper.getLogger(this).info("mvnCompileExitCode: $mvnCompileExitCode")

        if (0 != mvnCompileExitCode) {
            throw Exception("Failed to compile repository")
        }

        val mvnInstallExitCode = mavenCommandRunner.installExec()
        LogHelper.getLogger(this).info("mvnInstallExitCode: $mvnInstallExitCode")

        if (0 != mvnInstallExitCode) {
            throw Exception("Failed to install repository")
        }

        /// Mark: POST INSTALL
        if (localBuilderConfig.deleteAfterRun) {
            LogHelper.getLogger(this).warn("Do delete after run install dependency...")
            if (localRepoPath.exists()) {
                localRepoPath.toFile().deleteRecursively()
            }
        }
    }
}
