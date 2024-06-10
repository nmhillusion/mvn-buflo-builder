package tech.nmhillusion.local_dependency_builder.command

import tech.nmhillusion.local_dependency_builder.model.DependencyEntity
import tech.nmhillusion.n2mix.type.ChainList
import tech.nmhillusion.n2mix.validator.StringValidator
import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class GitCommand {
    private val gitCommand = "git"

    val versionCommand: List<String>
        get() = listOf(gitCommand, "-v")


    val statusCommand: List<String>
        get() = listOf(gitCommand, "status")


    val branchCommand: List<String>
        get() = listOf(gitCommand, "branch")


    val logCommand: List<String>
        get() = listOf(gitCommand, "log")


    fun checkoutCommand(dependencyEntity: DependencyEntity): List<String> {
        val command = ChainList<String>()
            .chainAdd(gitCommand)
            .chainAdd("checkout")

        if (null != dependencyEntity.tag) {
            command
                .chainAdd("tags/${dependencyEntity.tag}")
        }

        if (null != dependencyEntity.branch) {
            command
                .chainAdd(dependencyEntity.branch)
        }

        return command
    }


    fun cloneRepository(dependencyEntity: DependencyEntity, containerPath: String): List<String> {
        val command = ChainList<String>()
            .chainAdd(gitCommand)
            .chainAdd("clone")
            .chainAdd("--depth")
            .chainAdd("1")

        if (!StringValidator.isBlank(dependencyEntity.branch)) {
            command
                .chainAdd("--branch")
                .chainAdd(
                    dependencyEntity.branch
                )
        }

        command
            .chainAdd("--single-branch")
            .chainAdd(
                dependencyEntity.url
            )
            .chainAdd(
                Path.of(containerPath, dependencyEntity.name).toString()
            )

        return command
    }
}