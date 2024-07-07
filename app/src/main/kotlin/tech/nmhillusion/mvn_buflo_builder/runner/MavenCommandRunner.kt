package tech.nmhillusion.mvn_buflo_builder.runner

import tech.nmhillusion.mvn_buflo_builder.command.MavenCommand
import tech.nmhillusion.mvn_buflo_builder.model.dependency.DependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.LocalDependencyEntity
import tech.nmhillusion.n2mix.helper.log.LogHelper
import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-03
 */
class MavenCommandRunner(
    val dependencyEntity: DependencyEntity,
    workingFolder: String?
) {

    private val mavenCommand = MavenCommand()
    private val localRepoPath = if (dependencyEntity is GitDependencyEntity)
        Path.of(workingFolder ?: "", dependencyEntity.name, dependencyEntity.rootPath)
    else if (dependencyEntity is LocalDependencyEntity)
        Path.of(dependencyEntity.path).parent
    else
        Path.of(workingFolder ?: "")

    fun cleanExec(): Int {
        val commandRunner = CommandRunner(
            mavenCommand.cleanCommand,
            localRepoPath.toFile()
        )

        return commandRunner.exec()
    }

    fun compileExec(): Int {
        val commandRunner = CommandRunner(
            mavenCommand.compileCommand,
            localRepoPath.toFile()
        )

        return commandRunner.exec()
    }

    fun installExec(ignoredTest: Boolean = true): Int {
        val commandRunner = CommandRunner(
            mavenCommand.installCommand(ignoredTest),
            localRepoPath.toFile()
        )

        return commandRunner.exec()
    }

    fun installJarLocalExec(): Int {
        if (dependencyEntity !is LocalDependencyEntity) {
            LogHelper.getLogger(this).error("dependencyEntity is not LocalDependencyEntity")
            return -1
        }

        val commandRunner = CommandRunner(
            mavenCommand.installJarLocalCommand(dependencyEntity),
            localRepoPath.toFile()
        )

        return commandRunner.exec()
    }
}