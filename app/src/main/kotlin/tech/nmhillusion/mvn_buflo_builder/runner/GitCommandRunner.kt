package tech.nmhillusion.mvn_buflo_builder.runner

import tech.nmhillusion.mvn_buflo_builder.command.GitCommand
import tech.nmhillusion.mvn_buflo_builder.model.LocalBuilderConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import java.nio.file.Path


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
    private val localRepoPath = Path.of(workingFolder, dependencyEntity.name, dependencyEntity.rootPath)

    fun cloneExec(): Int {
        val commandRunner = CommandRunner(
            command = gitCommand.cloneRepository(dependencyEntity, workingFolder, localBuilderConfig.accessToken),
        )
        return commandRunner.exec()
    }

    fun checkoutExec(): Int {
        val commandRunner = CommandRunner(
            command = gitCommand.checkoutCommand(dependencyEntity),
            workingDirectory = localRepoPath.toFile()
        )
        return commandRunner.exec()

    }

    fun fetchTagsExec(): Any {
        val commandRunner = CommandRunner(
            command = gitCommand.fetchTags,
            workingDirectory = localRepoPath.toFile()
        )
        return commandRunner.exec()
    }
}