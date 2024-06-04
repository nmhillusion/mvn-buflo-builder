package tech.nmhillusion.local_dependency_builder.service

import tech.nmhillusion.n2mix.helper.log.LogHelper
import java.util.regex.Pattern

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
class ParameterParser(private val parameters: Array<String>) {
    private val VALID_COMMAND_PATTERN = Pattern.compile("-h|--help|.+\\.ya?ml")

    fun parse(): List<String> {
        return parameters
            .filter {
                if (VALID_COMMAND_PATTERN.matcher(it).matches()) {
                    true
                } else {
                    LogHelper.getLogger(ParameterParser::class).error("Invalid command: $it")
                    false
                }
            }
            .toList()
    }
}