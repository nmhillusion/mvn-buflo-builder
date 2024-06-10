package tech.nmhillusion.mvn_buflo_builder

import tech.nmhillusion.mvn_buflo_builder.service.AppFlowParser


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No arguments")
        throw IllegalArgumentException("type: -h or --help to see help how to use the app")
    }

    val parameterModels = tech.nmhillusion.n2mix.helper.cli.ParameterParser.parse(args)
    val execFlow = AppFlowParser(parameterModels).parse()
    execFlow.exec(parameterModels)
}