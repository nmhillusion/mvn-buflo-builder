package tech.nmhillusion.mvn_buflo_builder.runner

import tech.nmhillusion.mvn_buflo_builder.command.GitCommand
import tech.nmhillusion.mvn_buflo_builder.model.DependencyEntity


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-03
 */
class GitCommandRunner(
    private val dependencyEntity: DependencyEntity,
    private val workingFolder: String
) {
    private val gitCommand = GitCommand()

    fun cloneExec(): Int {
        val commandRunner = CommandRunner(
            command = gitCommand.cloneRepository(dependencyEntity, workingFolder)
        )
        return commandRunner.exec()
    }

    fun checkoutExec(): Int {
        val commandRunner = CommandRunner(
            command = gitCommand.checkoutCommand(dependencyEntity)
        )
        return commandRunner.exec()

    }
}