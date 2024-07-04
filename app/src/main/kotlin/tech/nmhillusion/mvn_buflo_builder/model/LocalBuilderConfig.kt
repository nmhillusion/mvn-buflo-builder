package tech.nmhillusion.mvn_buflo_builder.model

import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
data class LocalBuilderConfig(
    val tempRepoPath: String,
    val deleteAfterRun: Boolean,
    val accessToken: AccessTokenConfig? = null
) {
    companion object {
        fun fromMap(data: Map<*, *>): LocalBuilderConfig {
            val accessToken = if (data.containsKey("accessToken")) {
                AccessTokenConfig.fromMap(data["accessToken"] as Map<*, *>)
            } else {
                null
            }

            return LocalBuilderConfig(
                StringUtil.trimWithNull(data["tempRepoPath"]),
                StringUtil.trimWithNull(data["deleteAfterRun"]).toBoolean(),
                accessToken
            )
        }
    }
}

data class AccessTokenConfig(val tokenName: String, val tokenValue: String) {
    companion object {
        fun fromMap(data: Map<*, *>): AccessTokenConfig {
            return AccessTokenConfig(
                StringUtil.trimWithNull(data["tokenName"]),
                StringUtil.trimWithNull(data["tokenValue"])
            )
        }
    }
}