package tech.nmhillusion.local_dependency_builder.helper

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class MavenHelper {
    private val mvnCommand = if (SystemHelper.isWindows()) "mvn.cmd" else "mvn"

    val versionCommand: List<String>
        get() = listOf(mvnCommand, "-v")

    val cleanCommand: List<String>
        get() = listOf(mvnCommand, "clean")

    val compileCommand: List<String>
        get() = listOf(mvnCommand, "compile")

    val installCommand: List<String>
        get() = listOf(mvnCommand, "install")
}