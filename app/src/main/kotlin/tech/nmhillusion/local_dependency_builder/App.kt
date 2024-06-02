/*
 * This source file was generated by the Gradle 'init' task
 */
package tech.nmhillusion.local_dependency_builder

import tech.nmhillusion.local_dependency_builder.builder.FolderBuilder
import tech.nmhillusion.local_dependency_builder.helper.GitHelper
import tech.nmhillusion.local_dependency_builder.helper.MavenHelper
import tech.nmhillusion.local_dependency_builder.model.DependencyEntity
import tech.nmhillusion.local_dependency_builder.model.LocalBuilderConfig
import tech.nmhillusion.local_dependency_builder.runner.CommandRunner
import tech.nmhillusion.n2mix.helper.YamlReader
import tech.nmhillusion.n2mix.helper.log.LogHelper
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

    fun loadAppConfig() {
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

        dependencies.forEach {
            buildDependency(it)
            LogHelper.getLogger(this).info(">>> Successfully build dependency: ${it.name} <<<")
        }
    }

    private fun buildDependency(dependency_: DependencyEntity) {
        val localRepoPath = Path.of(localBuilderConfig.tempRepoPath, dependency_.name)
        LogHelper.getLogger(this).info("localRepoPath: $localRepoPath")

        if (localRepoPath.exists()) {
            LogHelper.getLogger(this).info("Deleting existing localRepoPath: $localRepoPath")
            localRepoPath.toFile().deleteRecursively()
        }

        val cloneCommand = GitHelper().cloneRepository(
            dependency_,
            localBuilderConfig.tempRepoPath
        )

        val commandRunner = CommandRunner(cloneCommand)
        val cloneExitCode = commandRunner.exec()

        LogHelper.getLogger(this).info("cloneExitCode: $cloneExitCode")

        if (0 != cloneExitCode) {
            throw Exception("Failed to clone repository")
        }

        val mvnCleanCmd = MavenHelper().cleanCommand
        val mvnCompileCmd = MavenHelper().compileCommand
        val mvnInstallCmd = MavenHelper().installCommand

        val mvnCleanCommandRunner = CommandRunner(mvnCleanCmd, localRepoPath.toFile())
        val mvnCompileCommandRunner = CommandRunner(mvnCompileCmd, localRepoPath.toFile())
        val mvnInstallCommandRunner = CommandRunner(mvnInstallCmd, localRepoPath.toFile())

        val mvnCleanExitCode = mvnCleanCommandRunner.exec()
        LogHelper.getLogger(this).info("mvnCleanExitCode: $mvnCleanExitCode")

        if (0 != mvnCleanExitCode) {
            throw Exception("Failed to clean repository")
        }

        val mvnCompileExitCode = mvnCompileCommandRunner.exec()
        LogHelper.getLogger(this).info("mvnCompileExitCode: $mvnCompileExitCode")

        if (0 != mvnCompileExitCode) {
            throw Exception("Failed to compile repository")
        }

        val mvnInstallExitCode = mvnInstallCommandRunner.exec()
        LogHelper.getLogger(this).info("mvnInstallExitCode: $mvnInstallExitCode")

        if (0 != mvnInstallExitCode) {
            throw Exception("Failed to install repository")
        }
    }
}
