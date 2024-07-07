package tech.nmhillusion.mvn_buflo_builder.validator

import tech.nmhillusion.mvn_buflo_builder.model.dependency.DependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.LocalDependencyEntity
import tech.nmhillusion.n2mix.helper.log.LogHelper
import tech.nmhillusion.n2mix.util.StringUtil
import tech.nmhillusion.n2mix.validator.StringValidator


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class DependencyValidator {

    companion object {
        val GIT_DEPENDENCY_PATH_REGEXP = "^https?://.*?\\.git$".toRegex()
        val LOCAL_DEPENDENCY_PATH_REGEXP = "^(file://)?.*\\.jar$".toRegex()

        fun isLocalDependency(path: String): Boolean {
            return StringUtil.trimWithNull(path).endsWith(".jar")
        }

        fun isGitDependency(path: String): Boolean {
            return StringUtil.trimWithNull(path).matches(GIT_DEPENDENCY_PATH_REGEXP)
        }

        fun validateDependency(dependencyEntity: DependencyEntity) {
            LogHelper.getLogger(DependencyValidator::class.java)
                .debug("Validating dependency: ${dependencyEntity::class.java.simpleName} - ${dependencyEntity.name}")

            /// Mark: PATH
            if (dependencyEntity.path.isEmpty()) {
                throw Exception("Dependency path is empty")
            }

            if (dependencyEntity is GitDependencyEntity) {
                if (!dependencyEntity.path.matches(GIT_DEPENDENCY_PATH_REGEXP)) {
                    throw Exception("Dependency url is not valid, must be in pattern: $GIT_DEPENDENCY_PATH_REGEXP")
                }
            } else if (dependencyEntity is LocalDependencyEntity) {

                if (!dependencyEntity.path.matches(LOCAL_DEPENDENCY_PATH_REGEXP)) {
                    throw Exception("Dependency path is not valid, must be in pattern: $LOCAL_DEPENDENCY_PATH_REGEXP")
                }
            } else {
                throw Exception("Dependency type is not valid - must be GitDependencyEntity or LocalDependencyEntity, but was: ${dependencyEntity::class.java.simpleName}")
            }

            /// Mark: NAME
            if (dependencyEntity.name.isEmpty()) {
                throw Exception("Dependency name is empty")
            }

            /// Mark: FOR SPECIFIC DEPENDENCY TYPE
            if (dependencyEntity is GitDependencyEntity) {
                if (!StringValidator.isBlank(dependencyEntity.branch) && !StringValidator.isBlank(dependencyEntity.tag)) {
                    throw IllegalArgumentException("Dependency branch and tag cannot be set at the same time")
                }
            } else if (dependencyEntity is LocalDependencyEntity) {
                if (!StringValidator.isBlank(dependencyEntity.groupId)) {
                    throw IllegalArgumentException("Dependency groupId cannot be empty")
                }

                if (!StringValidator.isBlank(dependencyEntity.artifactId)) {
                    throw IllegalArgumentException("Dependency artifactId cannot be empty")
                }

                if (!StringValidator.isBlank(dependencyEntity.version)) {
                    throw IllegalArgumentException("Dependency version cannot be empty")
                }
            }
        }
    }

}