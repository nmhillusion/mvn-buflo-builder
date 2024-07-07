package tech.nmhillusion.mvn_buflo_builder.model.dependency

import tech.nmhillusion.mvn_buflo_builder.validator.DependencyValidator.Companion.isGitDependency
import tech.nmhillusion.mvn_buflo_builder.validator.DependencyValidator.Companion.isLocalDependency
import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
abstract class DependencyEntity(
    val path: String,
) {

    abstract val name: String

    companion object {


        fun fromMap(data: Map<*, *>): DependencyEntity {
            if (!data.containsKey("path")) {
                throw IllegalArgumentException("Dependency path is empty")
            }

            val path_ = StringUtil.trimWithNull(data["path"])

            return if (isLocalDependency(path_)) {
                LocalDependencyEntity.fromMap(data)
            } else if (isGitDependency(path_)) {
                GitDependencyEntity.fromMap(data)
            } else {
                throw IllegalArgumentException("Unsupported dependency type: $path_")
            }
        }
    }
}