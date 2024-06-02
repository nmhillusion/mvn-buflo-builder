package tech.nmhillusion.local_dependency_builder.runner

import java.io.File
import java.io.InputStream
import java.util.*


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
                "stdout"
            )
        }
        val stdErrThread = Thread {
            readStream(
                process.errorStream,
                "stderr"
            )
        }


        // Start the threads
        stdOutThread.start()
        stdErrThread.start()

        return process.waitFor()
    }

    private fun readStream(stream: InputStream, type: String) {
        Scanner(stream).use { scanner ->
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                println("$type: $line")
            }
        }
    }
}