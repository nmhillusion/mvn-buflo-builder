package tech.nmhillusion.mvn_buflo_builder.validator

import tech.nmhillusion.mvn_buflo_builder.command.GitCommand
import tech.nmhillusion.mvn_buflo_builder.command.MavenCommand
import tech.nmhillusion.mvn_buflo_builder.runner.CommandRunner


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class EnvironmentValidator {

    companion object {
        fun validateRequiredCommand() {
            val mvnVersionCommand = MavenCommand().versionCommand
            val mvnExitCode = CommandRunner(mvnVersionCommand).exec()

            if (0 != mvnExitCode) {
                throw Exception("Invalid mvn command")
            }

            val gitVersionCommand = GitCommand().versionCommand
            val gitExitCode = CommandRunner(gitVersionCommand).exec()

            if (0 != gitExitCode) {
                throw Exception("Invalid git command")
            }
        }
    }

}