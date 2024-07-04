package tech.nmhillusion.mvn_buflo_builder.runner

import tech.nmhillusion.mvn_buflo_builder.command.MavenCommand
import tech.nmhillusion.mvn_buflo_builder.model.dependency.DependencyEntity
import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-03
 */
class MavenCommandRunner(
    dependencyEntity: DependencyEntity,
    workingFolder: String?
) {

    private val mavenCommand = MavenCommand()
    private val localRepoPath = Path.of(workingFolder ?: "", dependencyEntity.name, dependencyEntity.rootPath)

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
}