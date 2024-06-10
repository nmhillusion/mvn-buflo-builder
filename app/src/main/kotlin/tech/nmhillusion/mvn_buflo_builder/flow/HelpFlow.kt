package tech.nmhillusion.mvn_buflo_builder.flow

import tech.nmhillusion.n2mix.model.cli.ParameterModel

/**
 * created by: nmhillusion
 *
 *
 * created date: 2024-06-09
 */
class HelpFlow : BaseFlow() {

    override fun preExec(parameters: List<ParameterModel>) {
        require(1 >= parameters.size) { "Invalid parameters: $parameters. Only one parameter is allowed for help." }
    }

    override fun acceptedParameterNames(): List<String> {
        return listOf("help", "h")
    }

    override fun doExec(parameters: List<ParameterModel>) {
        println(
            """
                Usage: local_dependency_builder -c <path/to/config/file.yml>
                or local_dependency_builder --configPath <path/to/config/file.yml>
                or local_dependency_builder <path/to/config/file.yml>
                or local_dependency_builder --help
                or local_dependency_builder -h

                Options:
                -c, --configPath: path to the config file
                -h, --help: show help
                
                """.trimIndent()
        )
    }
}
