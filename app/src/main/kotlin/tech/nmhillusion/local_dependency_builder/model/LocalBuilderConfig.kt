package tech.nmhillusion.local_dependency_builder.model

import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
data class LocalBuilderConfig(val tempRepoPath: String, val deleteAfterRun: Boolean) {
    companion object {
        fun fromMap(data: Map<*, *>): LocalBuilderConfig {
            return LocalBuilderConfig(
                StringUtil.trimWithNull(data["tempRepoPath"]),
                StringUtil.trimWithNull(data["deleteAfterRun"]).toBoolean()
            )
        }
    }
}
