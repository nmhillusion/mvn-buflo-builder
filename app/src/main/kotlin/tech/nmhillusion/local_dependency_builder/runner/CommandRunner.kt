package tech.nmhillusion.local_dependency_builder.runner

import tech.nmhillusion.n2mix.helper.log.LogHelper


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class CommandRunner(val command: List<String>) {
    fun exec(): Int {
        val builder = ProcessBuilder(command)
        builder.environment()["PATH"] = System.getenv("PATH")
        val process: Process = builder.start()

        process.inputStream.use {
            LogHelper.getLogger(this).info(it.readBytes().toString(Charsets.UTF_8))
        }

        process.errorStream.use {
            LogHelper.getLogger(this).error(it.readBytes().toString(Charsets.UTF_8))
        }

        return process.waitFor()
    }
}