package tech.nmhillusion.local_dependency_builder.validator

import tech.nmhillusion.local_dependency_builder.helper.GitHelper
import tech.nmhillusion.local_dependency_builder.helper.MavenHelper
import tech.nmhillusion.local_dependency_builder.runner.CommandRunner


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class EnvironmentValidator {

    companion object {
        fun validateMvnCommand() {
            val mvnVersionCommand = MavenHelper().versionCommand
            val mvnExitCode = CommandRunner(mvnVersionCommand).exec()

            if (0 != mvnExitCode) {
                throw Exception("Invalid mvn command")
            }

            val gitVersionCommand = GitHelper().versionCommand
            val gitExitCode = CommandRunner(gitVersionCommand).exec()

            if (0 != gitExitCode) {
                throw Exception("Invalid git command")
            }
        }
    }

}