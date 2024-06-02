/*
 * This source file was generated by the Gradle 'init' task
 */
package tech.nmhillusion.local_dependency_builder

import tech.nmhillusion.local_dependency_builder.builder.FolderBuilder
import tech.nmhillusion.local_dependency_builder.model.DependencyEntity
import tech.nmhillusion.local_dependency_builder.model.LocalBuilderConfig
import tech.nmhillusion.n2mix.helper.YamlReader
import tech.nmhillusion.n2mix.helper.log.LogHelper
import java.nio.file.Files
import java.nio.file.Path

class App(private val configPath: String) {
    private val dependencies = ArrayList<DependencyEntity>()
    private lateinit var localBuilderConfig: LocalBuilderConfig

    val name: String
        get() {
            return "Local Dependency Builder"
        }


    fun <T> getConfig(configKey: String, class2Cast: Class<T>): T {
        Files.newInputStream(Path.of(configPath)).use {
            return YamlReader(it).getProperty(configKey, class2Cast)
        }
    }

    fun loadAppConfig() {
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

    fun exec() {
        /// Mark: Load App Config
        loadAppConfig()

        /// Mark: EXEC ///////////////////////

        val createdTempFolder = FolderBuilder.createFolders(
            Path.of(localBuilderConfig.tempRepoPath)
        )

        LogHelper.getLogger(this).info("createdTempFolder: $createdTempFolder")
    }
}
