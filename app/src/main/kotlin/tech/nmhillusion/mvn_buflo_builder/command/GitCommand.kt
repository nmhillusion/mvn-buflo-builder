package tech.nmhillusion.mvn_buflo_builder.command

import tech.nmhillusion.mvn_buflo_builder.model.AccessTokenConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import tech.nmhillusion.n2mix.type.ChainList
import tech.nmhillusion.n2mix.util.StringUtil
import tech.nmhillusion.n2mix.validator.StringValidator
import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class GitCommand {
    private val gitCommand = "git"

    val fetchAll = listOf(gitCommand, "fetch", "--all")

    val fetchTags = listOf(gitCommand, "fetch", "--tags")

    val versionCommand: List<String>
        get() = listOf(gitCommand, "-v")


    val statusCommand: List<String>
        get() = listOf(gitCommand, "status")


    val branchCommand: List<String>
        get() = listOf(gitCommand, "branch")


    val logCommand: List<String>
        get() = listOf(gitCommand, "log")


    fun checkoutCommand(dependencyEntity: GitDependencyEntity): List<String> {
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
            .map {
                StringUtil.trimWithNull(it)
            }
            .filter {
                !StringValidator.isBlank(it)
            }
    }


    fun cloneRepository(
        dependencyEntity: GitDependencyEntity,
        containerPath: String,
        accessTokenConfig: AccessTokenConfig?
    ): List<String> {
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

        if (!dependencyEntity.isNeedCheckout) {
            command
                .chainAdd("--single-branch")
        }

        command
            .chainAdd(
                dependencyEntity.getUrl(accessTokenConfig)
            )
            .chainAdd(
                Path.of(containerPath, dependencyEntity.name).toString()
            )

        return command
    }
}