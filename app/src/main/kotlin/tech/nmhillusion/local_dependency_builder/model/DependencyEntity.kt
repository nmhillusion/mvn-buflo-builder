package tech.nmhillusion.local_dependency_builder.model

import tech.nmhillusion.local_dependency_builder.validator.DependencyValidator
import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
data class DependencyEntity(val url: String, val rootPath: String?) {
    val name: String
        get() = url.split("/").last().replace(".git", "")

    companion object {
        fun fromMap(data: Map<*, *>): DependencyEntity {
            val url = StringUtil.trimWithNull(data["url"])
            val dependencyEntity = DependencyEntity(
                url,
                StringUtil.trimWithNull(data["rootPath"])
            )

            DependencyValidator.validateDependency(dependencyEntity)

            return dependencyEntity
        }
    }
}