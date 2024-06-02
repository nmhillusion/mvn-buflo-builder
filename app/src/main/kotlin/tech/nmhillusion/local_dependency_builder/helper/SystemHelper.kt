package tech.nmhillusion.local_dependency_builder.helper

import java.util.*


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class SystemHelper {
    companion object {
        fun isWindows(): Boolean {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            return osName.contains("win")
        }

        fun isLinux(): Boolean {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            return osName.contains("linux")
        }
    }
}