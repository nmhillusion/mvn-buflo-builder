package tech.nmhillusion.local_dependency_builder.helper

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class GitHelper {
    private val gitCommand = "git"

    val versionCommand: List<String>
        get() = listOf(gitCommand, "-v")
}