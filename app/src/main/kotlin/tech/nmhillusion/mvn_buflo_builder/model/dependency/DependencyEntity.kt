package tech.nmhillusion.mvn_buflo_builder.model.dependency

import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
abstract class DependencyEntity(
    val path: String,
    val rootPath: String?,
) {

    val name: String
        get() = path.split("/").last()
            .replace(".git", "")
            .replace(".jar", "")

    companion object {
        fun isLocalDependency(path: String): Boolean {
            return StringUtil.trimWithNull(path).endsWith(".jar")
        }

        fun isGitDependency(path: String): Boolean {
            return StringUtil.trimWithNull(path).matches("https?://.*(?:\\.git)?".toRegex())
        }

        fun fromMap(data: Map<*, *>): DependencyEntity {
            val path_ = StringUtil.trimWithNull(data["path"])

            return if (isLocalDependency(path_)) {
                LocalDependencyEntity.fromMap(data)
            } else if (isGitDependency(path_)) {
                GitDependencyEntity.fromMap(data)
            } else {
                throw IllegalArgumentException("Unsupported dependency type")
            }
        }
    }
}