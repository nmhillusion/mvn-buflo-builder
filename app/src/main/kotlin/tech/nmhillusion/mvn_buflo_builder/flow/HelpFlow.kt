package tech.nmhillusion.mvn_buflo_builder.flow

import tech.nmhillusion.mvn_buflo_builder.builder.BannerBuilder
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
        println(BannerBuilder().appBanner)

        println(
            """                
                Usage: ./app -c <path/to/config/file.yml>
                or ./app --configPath <path/to/config/file.yml>
                or ./app <path/to/config/file.yml>
                or ./app --help
                or ./app -h

                Options:
                -c, --configPath: path to the config file
                -h, --help: show help
                
                """.trimIndent()
        )
    }
}
