package tech.nmhillusion.local_dependency_builder.model

import tech.nmhillusion.local_dependency_builder.validator.DependencyValidator
import tech.nmhillusion.n2mix.util.StringUtil
import tech.nmhillusion.n2mix.validator.StringValidator

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
data class DependencyEntity(
    val url: String,
    val branch: String?,
    val tag: String?,
    val rootPath: String?,
    val ignoredTest: Boolean = true
) {
    val name: String
        get() = url.split("/").last().replace(".git", "")

    val isNeedCheckout: Boolean
        get() = !StringValidator.isBlank(branch) || !StringValidator.isBlank(tag)

    companion object {
        fun fromMap(data: Map<*, *>): DependencyEntity {
            val baseUrl = StringUtil.trimWithNull(data["url"])
            val branch = StringUtil.trimWithNull(data["branch"])
            val tag = StringUtil.trimWithNull(data["tag"])
            val rootPath = StringUtil.trimWithNull(data["rootPath"])
            val ignoredTest = "false" != StringUtil.trimWithNull(data["ignoredTest"]).lowercase()

            val dependencyEntity = DependencyEntity(
                url = baseUrl,
                branch = branch,
                tag = tag,
                rootPath = rootPath,
                ignoredTest = ignoredTest
            )

            DependencyValidator.validateDependency(dependencyEntity)

            return dependencyEntity
        }
    }
}