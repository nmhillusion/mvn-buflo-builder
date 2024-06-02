package tech.nmhillusion.local_dependency_builder.helper

import tech.nmhillusion.local_dependency_builder.model.DependencyEntity
import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class GitHelper {
    private val gitCommand = "git"

    val versionCommand: List<String>
        get() = listOf(gitCommand, "-v")


    val statusCommand: List<String>
        get() = listOf(gitCommand, "status")


    val branchCommand: List<String>
        get() = listOf(gitCommand, "branch")


    val logCommand: List<String>
        get() = listOf(gitCommand, "log")


    fun cloneRepository(dependencyEntity: DependencyEntity, containerPath: String): List<String> = listOf(
        gitCommand, "clone",
        dependencyEntity.url,
        Path.of(containerPath, dependencyEntity.name).toString()
    )
}