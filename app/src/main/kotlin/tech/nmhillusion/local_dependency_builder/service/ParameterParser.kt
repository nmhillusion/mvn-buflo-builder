package tech.nmhillusion.local_dependency_builder.service

import java.util.stream.Collectors

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
class ParameterParser(private val parameters: Array<String>) {
    fun parse(): Map<String, String> {

        return parameters
            .filter { it.contains("=") }
            .stream()
            .map {
                val pair = it.split("=")
                Pair(pair[0], pair[1])
            }
            .collect(Collectors.toMap(Pair<String, String>::first, Pair<String, String>::second))
    }
}