package tech.nmhillusion.local_dependency_builder.model

import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
data class DependencyEntity(val url: String, val rootPath: String?) {
    companion object {
        fun fromMap(data: Map<*, *>): DependencyEntity {
            return DependencyEntity(
                StringUtil.trimWithNull(data["url"]),
                StringUtil.trimWithNull(data["rootPath"])
            )
        }
    }
}