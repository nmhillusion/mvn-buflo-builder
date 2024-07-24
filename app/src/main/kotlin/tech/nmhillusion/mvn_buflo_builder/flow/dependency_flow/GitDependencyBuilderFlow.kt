package tech.nmhillusion.mvn_buflo_builder.flow.dependency_flow

import tech.nmhillusion.mvn_buflo_builder.model.LocalBuilderConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import tech.nmhillusion.mvn_buflo_builder.runner.GitCommandRunner
import tech.nmhillusion.mvn_buflo_builder.runner.MavenCommandRunner
import tech.nmhillusion.n2mix.helper.log.LogHelper
import java.nio.file.Path
import kotlin.io.path.exists

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-07-07
 */
class GitDependencyBuilderFlow : DependencyBuilderFlow<GitDependencyEntity> {
    override fun buildDependency(dependency_: GitDependencyEntity, localBuilderConfig: LocalBuilderConfig) {
        /// Mark: PREPARE FOLDER
        val localRepoPath = Path.of(localBuilderConfig.tempRepoPath, dependency_.name)
        LogHelper.getLogger(this).info("localRepoPath: $localRepoPath")

        if (localRepoPath.exists()) {
            LogHelper.getLogger(this).info("Deleting existing localRepoPath: $localRepoPath")
            localRepoPath.toFile().deleteRecursively()
        }

        /// Mark: GIT
        val gitCommandRunner = GitCommandRunner(dependency_, localBuilderConfig)

        val cloneExitCode = gitCommandRunner.cloneExec()
        LogHelper.getLogger(this).info("cloneExitCode: $cloneExitCode")

        if (dependency_.isNeedCheckout) {
            val fetchTagsExec = gitCommandRunner.fetchTagsExec()
            LogHelper.getLogger(this).info("fetchTagsExec: $fetchTagsExec")
            val checkoutExitCode = gitCommandRunner.checkoutExec()
            LogHelper.getLogger(this).info("checkoutExitCode: $checkoutExitCode")
        }

        if (0 != cloneExitCode) {
            throw Exception("Failed to clone repository")
        }

        /// Mark: MAVEN
        val mavenCommandRunner = MavenCommandRunner(dependency_, localBuilderConfig.tempRepoPath)

        val mvnCleanExitCode = mavenCommandRunner.cleanExec()
        LogHelper.getLogger(this).info("mvnCleanExitCode: $mvnCleanExitCode")

        if (0 != mvnCleanExitCode) {
            throw Exception("Failed to clean repository")
        }

        val mvnCompileExitCode = mavenCommandRunner.compileExec()
        LogHelper.getLogger(this).info("mvnCompileExitCode: $mvnCompileExitCode")

        if (0 != mvnCompileExitCode) {
            throw Exception("Failed to compile repository")
        }

        val mvnInstallExitCode = mavenCommandRunner.installExec(dependency_.ignoredTest)
        LogHelper.getLogger(this).info("mvnInstallExitCode: $mvnInstallExitCode")

        if (0 != mvnInstallExitCode) {
            throw Exception("Failed to install repository")
        }

        /// Mark: POST INSTALL
        if (localBuilderConfig.deleteAfterRun) {
            LogHelper.getLogger(this).warn("Do delete after run install dependency...")
            if (localRepoPath.exists()) {
                localRepoPath.toFile().deleteRecursively()
            }
        }
    }
}
