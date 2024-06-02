package tech.nmhillusion.local_dependency_builder.runner

import tech.nmhillusion.n2mix.helper.log.LogHelper
import java.io.*


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
class CommandRunner(private val command: List<String>, private val workingDirectory: File? = null) {
    fun exec(): Int {
        val builder = ProcessBuilder(command)
        builder.environment()["PATH"] = System.getenv("PATH")

        if (null != this.workingDirectory) {
            builder.directory(this.workingDirectory)
        }

        val process: Process = builder.start()


        // Create threads to read STDOUT and STDERR
        val stdOutThread = Thread {
            readStream(
                process.inputStream,
                "[INFO]"
            )
        }
        val stdErrThread = Thread {
            readStream(
                process.errorStream,
                "[ERROR]"
            )
        }


        // Start the threads
        stdOutThread.start()
        stdErrThread.start()

        return process.waitFor()
    }

    private fun readStream(stream: InputStream, type: String) {
        try {
            BufferedReader(InputStreamReader(stream)).use { reader ->
                var line: String
                while ((reader.readLine().also { line = it }) != null) {
                    println("$type: $line")
                }
            }
        } catch (e: IOException) {
            LogHelper.getLogger(this).error("Error reading $type".toString() + " stream: " + e.message)
        }
    }
}