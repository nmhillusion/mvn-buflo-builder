package tech.nmhillusion.mvn_buflo_builder.validator

import java.nio.file.Files
import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class ParameterValidator {
    companion object {
        fun validateConfigPath(configPath: String?): Boolean {


            return !configPath.isNullOrBlank()
                    && configPath.let {
                val path_ = Path.of(it)
                Files.exists(path_) && (path_.toString().endsWith(".yml") || path_.toString().endsWith(".yaml"))
            }
        }
    }
}