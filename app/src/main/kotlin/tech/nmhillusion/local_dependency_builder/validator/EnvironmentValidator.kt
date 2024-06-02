package tech.nmhillusion.local_dependency_builder.validator

import tech.nmhillusion.local_dependency_builder.runner.CommandRunner


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class EnvironmentValidator {

    companion object {
        fun validateMvnCommand() {
            val mvnCommand = listOf("mvn.cmd", "-v")
            val mvnExitCode = CommandRunner(mvnCommand).exec()

            if (0 != mvnExitCode) {
                throw Exception("Invalid mvn command")
            }

            val gitCommand = listOf("git", "-v")
            val gitExitCode = CommandRunner(gitCommand).exec()

            if (0 != gitExitCode) {
                throw Exception("Invalid git command")
            }
        }
    }

}