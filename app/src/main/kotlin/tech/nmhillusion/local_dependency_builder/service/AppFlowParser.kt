package tech.nmhillusion.local_dependency_builder.service

import tech.nmhillusion.local_dependency_builder.flow.BaseFlow
import tech.nmhillusion.local_dependency_builder.flow.BuilderFlow
import tech.nmhillusion.local_dependency_builder.flow.HelpFlow
import tech.nmhillusion.n2mix.helper.log.LogHelper
import tech.nmhillusion.n2mix.model.cli.ParameterModel

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
class AppFlowParser(private val parameters: List<ParameterModel>) {
    private val supportedFlows = listOf(
        HelpFlow(),
        BuilderFlow()
    )

    fun parse(): BaseFlow {
        if (parameters.isEmpty()) {
            LogHelper.getLogger(AppFlowParser::class).error("No arguments")
            throw Exception("type: -h or --help to see help how to use the app")
        }

        val flow_ = supportedFlows.find { it.isSupported(parameters) }

        if (null == flow_) {
            LogHelper.getLogger(AppFlowParser::class).error("Does not support arguments: $parameters")
            throw IllegalArgumentException("Does not support arguments: $parameters")
        }

        return flow_
    }
}