package tech.nmhillusion.mvn_buflo_builder.model.dependency

import tech.nmhillusion.mvn_buflo_builder.validator.DependencyValidator
import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 *
 *
 * created date: 2024-07-04
 */
class LocalDependencyEntity(
    path: String,
    rootPath: String,
    val groupId: String,
    val artifactId: String,
    val version: String
) : DependencyEntity(path, rootPath) {

    companion object {
        fun fromMap(data: Map<*, *>): LocalDependencyEntity {
            val path_ = StringUtil.trimWithNull(data["path"])
            val rootPath = StringUtil.trimWithNull(data["rootPath"])
            val groupId = StringUtil.trimWithNull(data["groupId"])
            val artifactId = StringUtil.trimWithNull(data["artifactId"])
            val version_ = StringUtil.trimWithNull(data["version"])

            val dependencyEntity = LocalDependencyEntity(
                path = path_,
                rootPath = rootPath,
                groupId = groupId,
                artifactId = artifactId,
                version = version_
            )

            DependencyValidator.validateDependency(dependencyEntity)

            return dependencyEntity
        }
    }
}
