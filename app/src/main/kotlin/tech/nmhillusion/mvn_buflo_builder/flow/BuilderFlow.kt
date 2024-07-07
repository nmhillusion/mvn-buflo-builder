package tech.nmhillusion.mvn_buflo_builder.flow

import tech.nmhillusion.mvn_buflo_builder.builder.FolderBuilder
import tech.nmhillusion.mvn_buflo_builder.exception.UnsupportedDependencyException
import tech.nmhillusion.mvn_buflo_builder.flow.dependency_flow.GitDependencyBuilderFlow
import tech.nmhillusion.mvn_buflo_builder.flow.dependency_flow.LocalDependencyBuilderFlow
import tech.nmhillusion.mvn_buflo_builder.model.LocalBuilderConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.DependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.LocalDependencyEntity
import tech.nmhillusion.n2mix.helper.YamlReader
import tech.nmhillusion.n2mix.helper.log.LogHelper
import tech.nmhillusion.n2mix.model.ResultResponseEntity
import tech.nmhillusion.n2mix.model.cli.ParameterModel
import tech.nmhillusion.n2mix.validator.StringValidator
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.notExists

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-09
 */
class BuilderFlow : BaseFlow() {
    private val dependencies = ArrayList<DependencyEntity>()
    private lateinit var configPath: String
    private lateinit var localBuilderConfig: LocalBuilderConfig

    override fun preExec(parameters: List<ParameterModel>) {
        println(name)
        println("---------------------")

        require(1 >= parameters.size) { "Invalid parameters: $parameters. Only one parameter is allowed for help." }

        parameters.forEach {
            configPath = it.value
        }

        if (StringValidator.isBlank(configPath)) {
            throw IllegalArgumentException("configPath is missing")
        }

        if (Path.of(configPath).notExists()) {
            throw IllegalArgumentException("Invalid configPath: $configPath")
        }
    }

    val name: String
        get() {
            return "MVN Buflo Builder"
        }


    private fun <T> getConfig(configKey: String, class2Cast: Class<T>): T {
        Files.newInputStream(Path.of(configPath)).use {
            return YamlReader(it).getProperty(configKey, class2Cast)
        }
    }

    private fun loadAppConfig() {
        this.localBuilderConfig = getConfig("config", Map::class.java).let {
            LocalBuilderConfig.fromMap(it)
        }
        LogHelper.getLogger(this).info("localBuilderConfig: $localBuilderConfig")

        getConfig("dependencies", List::class.java).forEach {
            if (it is Map<*, *>) {
                val dependencyEntity = DependencyEntity.fromMap(it)
                LogHelper.getLogger(this).info("dependencyEntity: $dependencyEntity")

                dependencies.add(dependencyEntity)
            }
        }
    }

    override fun acceptedParameterNames(): List<String> {
        return listOf("configPath", "c", "")
    }

    override fun doExec(parameters: List<ParameterModel>) {
        /// Mark: Load App Config
        loadAppConfig()

        /// Mark: EXEC ///////////////////////

        val createdTempFolder = FolderBuilder.createFolders(
            Path.of(localBuilderConfig.tempRepoPath)
        )

        LogHelper.getLogger(this).info("createdTempFolder: $createdTempFolder")

        val installResult = ArrayList<ResultResponseEntity<DependencyEntity>>(dependencies.size)
        dependencies.forEach {
            try {
                LogHelper.getLogger(this).info(">>> Building dependency: ${it.name} <<<")

                if (it is GitDependencyEntity) {
                    GitDependencyBuilderFlow().buildDependency(it, localBuilderConfig)
                } else if (it is LocalDependencyEntity) {
                    LocalDependencyBuilderFlow().buildDependency(it, localBuilderConfig)
                } else {
                    throw UnsupportedDependencyException("Unsupported dependency type ${it.javaClass.name}")
                }

                LogHelper.getLogger(this).info(">>> Successfully build dependency: ${it.name} <<<")

                installResult.add(
                    ResultResponseEntity<DependencyEntity>()
                        .setSuccess(true)
                        .setData(it)
                )
            } catch (ex: Exception) {
                LogHelper.getLogger(this).error("!!! Failed to build dependency: ${it.name} : ${ex.message}")
                installResult.add(
                    ResultResponseEntity<DependencyEntity>()
                        .setSuccess(false)
                        .setMessage(ex.message)
                        .setData(it)
                )
            }
        }

        /// Mark: POST EXEC
        println("[DEPENDENCY INSTALL RESULT]")
        installResult.forEach {
            println(
                """
                |===================================|
                |dependency: ${it.data?.name}
                |success : ${it.success}
                |message : ${it.message}
                |===================================|
            """.trimMargin()
            )
        }
    }
}