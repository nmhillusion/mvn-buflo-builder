package tech.nmhillusion.local_dependency_builder

import tech.nmhillusion.local_dependency_builder.service.ParameterParser
import tech.nmhillusion.local_dependency_builder.validator.EnvironmentValidator
import tech.nmhillusion.local_dependency_builder.validator.ParameterValidator
import tech.nmhillusion.n2mix.helper.log.LogHelper


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
fun main(args: Array<String>) {
    val parameterParser = ParameterParser(args)
    val parameters = parameterParser.parse()
    LogHelper.getLogger(App::class).info("Arguments: $parameters")

    EnvironmentValidator.validateRequiredCommand()

    val configPath = parameters["configPath"]

    if (!ParameterValidator.validateConfigPath(configPath)) {
        LogHelper.getLogger(App::class).error("Invalid configPath: $configPath")
        throw Exception("Invalid configPath: $configPath")
    }

    val app = configPath?.let { App(it) }

    if (null == app) {
        LogHelper.getLogger(App::class).error("app is null")
        throw Exception("app is null")
    }

    LogHelper.getLogger(app).info("... ${app.name} ...")

    app.exec()
}