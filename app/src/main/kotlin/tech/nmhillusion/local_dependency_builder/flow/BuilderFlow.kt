package tech.nmhillusion.local_dependency_builder.flow

import tech.nmhillusion.local_dependency_builder.builder.FolderBuilder
import tech.nmhillusion.local_dependency_builder.model.DependencyEntity
import tech.nmhillusion.local_dependency_builder.model.LocalBuilderConfig
import tech.nmhillusion.local_dependency_builder.runner.GitCommandRunner
import tech.nmhillusion.local_dependency_builder.runner.MavenCommandRunner
import tech.nmhillusion.n2mix.helper.YamlReader
import tech.nmhillusion.n2mix.helper.log.LogHelper
import tech.nmhillusion.n2mix.model.ResultResponseEntity
import tech.nmhillusion.n2mix.model.cli.ParameterModel
import tech.nmhillusion.n2mix.validator.StringValidator
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.notExists

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-09
 */
class BuilderFlow : BaseFlow() {
    private val dependencies = ArrayList<DependencyEntity>()
    private lateinit var configPath: String
    private lateinit var localBuilderConfig: LocalBuilderConfig

    override fun preExec(parameters: List<ParameterModel>) {
        require(1 >= parameters.size) { "Invalid parameters: $parameters. Only one parameter is allowed for help." }

        parameters.forEach {
            if (listOf("configPath", "c").contains(it.name)) {
                configPath = it.value
            }
        }

        if (StringValidator.isBlank(configPath)) {
            throw IllegalArgumentException("configPath is missing")
        }

        if (Path.of(configPath).notExists()) {
            throw IllegalArgumentException("Invalid configPath: $configPath")
        }
    }

    val name: String
        get() {
            return "Local Dependency Builder"
        }


    private fun <T> getConfig(configKey: String, class2Cast: Class<T>): T {
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

    override fun acceptedParameterNames(): List<String> {
        return listOf("configPath", "c")
    }

    override fun doExec(parameters: List<ParameterModel>) {
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
                LogHelper.getLogger(this).info(">>> Building dependency: ${it.name} <<<")
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

        if (dependency_.isNeedCheckout) {
            val checkoutExitCode = gitCommandRunner.checkoutExec()
            LogHelper.getLogger(this).info("checkoutExitCode: $checkoutExitCode")
        }

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

        val mvnInstallExitCode = mavenCommandRunner.installExec(dependency_.ignoredTest)
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