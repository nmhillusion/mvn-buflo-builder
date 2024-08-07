package tech.nmhillusion.mvn_buflo_builder.model.dependency

import tech.nmhillusion.mvn_buflo_builder.model.AccessTokenConfig
import tech.nmhillusion.mvn_buflo_builder.validator.DependencyValidator
import tech.nmhillusion.n2mix.util.StringUtil
import tech.nmhillusion.n2mix.validator.StringValidator
import java.util.regex.Pattern

/**
 * created by: nmhillusion
 * created date: 2024-07-04
 */
class GitDependencyEntity(
    path: String,
    val rootPath: String?,
    val branch: String?,
    val tag: String?,
    val ignoredTest: Boolean,
    val useAccessToken: Boolean
) : DependencyEntity(path) {

    override val name: String
        get() = path.split("/").last()
            .replace(".git", "")

    val isNeedCheckout: Boolean
        get() = !StringValidator.isBlank(branch) || !StringValidator.isBlank(tag)

    fun getUrl(accessToken: AccessTokenConfig?): String = if (useAccessToken && null != accessToken) {
        Pattern.compile("(https?://)(.*)")
            .matcher(path)
            .replaceFirst("$1${accessToken.tokenName}:${accessToken.tokenValue}@$2")
    } else {
        path
    }

    companion object {
        fun fromMap(data: Map<*, *>): GitDependencyEntity {
            val baseUrl = StringUtil.trimWithNull(data["path"])
            val branch = StringUtil.trimWithNull(data["branch"])
            val tag = StringUtil.trimWithNull(data["tag"])
            val rootPath = StringUtil.trimWithNull(data["rootPath"])
            val ignoredTest = "false" != StringUtil.trimWithNull(data["ignoredTest"]).lowercase()
            val useAccessToken = "true" == StringUtil.trimWithNull(data["useAccessToken"]).lowercase()

            val dependencyEntity = GitDependencyEntity(
                path = baseUrl,
                branch = branch,
                tag = tag,
                rootPath = rootPath,
                ignoredTest = ignoredTest,
                useAccessToken = useAccessToken
            )

            DependencyValidator.validateDependency(dependencyEntity)

            return dependencyEntity
        }
    }
}
