package tech.nmhillusion.mvn_buflo_builder.command

import tech.nmhillusion.mvn_buflo_builder.helper.SystemHelper
import tech.nmhillusion.mvn_buflo_builder.model.dependency.LocalDependencyEntity

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class MavenCommand {
    private val mvnCommand = if (SystemHelper.isWindows()) "mvn.cmd" else "mvn"

    val versionCommand: List<String>
        get() = listOf(mvnCommand, "-v")

    val cleanCommand: List<String>
        get() = listOf(mvnCommand, "clean")

    val compileCommand: List<String>
        get() = listOf(mvnCommand, "compile")

    fun installCommand(ignoredTest: Boolean = true): List<String> =
        if (ignoredTest) listOf(mvnCommand, "install", "-Dmaven.test.skip=true") else listOf(mvnCommand, "install")

    fun installJarLocalCommand(dependency_: LocalDependencyEntity): List<String> {
        return listOf(
            mvnCommand, "install:install-file",
            "-Dfile=${dependency_.path}",
            "-DgroupId=${dependency_.groupId}",
            "-DartifactId=${dependency_.artifactId}",
            "-Dversion=${dependency_.version}",
            "-Dpackaging=jar"
        )
    }
}