package tech.nmhillusion.mvn_buflo_builder.flow.dependency_flow

import tech.nmhillusion.mvn_buflo_builder.model.LocalBuilderConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.LocalDependencyEntity
import tech.nmhillusion.mvn_buflo_builder.runner.MavenCommandRunner


/**
 * date: 2024-07-07
 * author: nmhillusion
 */

class LocalDependencyBuilderFlow : DependencyBuilderFlow<LocalDependencyEntity> {
    override fun buildDependency(dependency_: LocalDependencyEntity, localBuilderConfig: LocalBuilderConfig) {

        val mavenCommandRunner = MavenCommandRunner(dependency_, localBuilderConfig.tempRepoPath)

        /// Mark: INSTALL LOCAL MAVEN JAR FILE
        val mvnInstallExitCode = mavenCommandRunner.installJarLocalExec()
        if (0 != mvnInstallExitCode) {
            throw Exception("Failed to install dependency")
        }
    }
}