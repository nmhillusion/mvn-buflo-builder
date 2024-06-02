package tech.nmhillusion.local_dependency_builder.runner

import tech.nmhillusion.n2mix.helper.log.LogHelper


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class CommandRunner(private val command: List<String>) {
    fun exec(): Int {
        val builder = ProcessBuilder(command)
        builder.environment()["PATH"] = System.getenv("PATH")
        val process: Process = builder.start()

        process.inputStream.use {
            val readStream_ = it.readBytes()
            if (readStream_.isNotEmpty()) {
                LogHelper.getLogger(this).info(readStream_.toString(Charsets.UTF_8))
            }
        }

        process.errorStream.use {
            val errorBytes = it.readBytes()
            if (errorBytes.isNotEmpty()) {
                LogHelper.getLogger(this).error(errorBytes.toString(Charsets.UTF_8))
            }
        }

        return process.waitFor()
    }
}