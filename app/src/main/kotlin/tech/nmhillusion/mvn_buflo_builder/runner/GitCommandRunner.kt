package tech.nmhillusion.mvn_buflo_builder.runner

import tech.nmhillusion.mvn_buflo_builder.command.GitCommand
import tech.nmhillusion.mvn_buflo_builder.model.LocalBuilderConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-03
 */
class GitCommandRunner(
    private val dependencyEntity: GitDependencyEntity,
    private val localBuilderConfig: LocalBuilderConfig
) {
    private val gitCommand = GitCommand()
    private val workingFolder: String = localBuilderConfig.tempRepoPath

    fun cloneExec(): Int {
        val commandRunner = CommandRunner(
            command = gitCommand.cloneRepository(dependencyEntity, workingFolder, localBuilderConfig.accessToken)
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