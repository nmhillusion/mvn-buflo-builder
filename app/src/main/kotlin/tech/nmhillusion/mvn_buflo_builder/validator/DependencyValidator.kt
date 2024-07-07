package tech.nmhillusion.mvn_buflo_builder.validator

import tech.nmhillusion.mvn_buflo_builder.model.dependency.DependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.GitDependencyEntity
import tech.nmhillusion.mvn_buflo_builder.model.dependency.LocalDependencyEntity
import tech.nmhillusion.n2mix.validator.StringValidator


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class DependencyValidator {

    companion object {
        fun validateDependency(dependencyEntity: DependencyEntity) {
            if (dependencyEntity.path.isEmpty()) {
                throw Exception("Dependency path is empty")
            }

            if (dependencyEntity is GitDependencyEntity) {
                if (!dependencyEntity.path.matches("^https?://.*?\\.git$".toRegex())) {
                    throw Exception("Dependency url is not valid, must be in pattern: https?://.*?\\.git$")
                }
            } else if (dependencyEntity is LocalDependencyEntity) {
                if (!dependencyEntity.path.matches("^(file://)?.*\\.jar$".toRegex())) {
                    throw Exception("Dependency path is not valid, must be in pattern: (file://)?.*\\.jar$")
                }
            } else {
                throw Exception("Dependency type is not valid - must be GitDependencyEntity or LocalDependencyEntity, but was: ${dependencyEntity::class.java.simpleName}")
            }

            if (dependencyEntity.name.isEmpty()) {
                throw Exception("Dependency name is empty")
            }

            if (dependencyEntity is GitDependencyEntity) {
                if (!StringValidator.isBlank(dependencyEntity.branch) && !StringValidator.isBlank(dependencyEntity.tag)) {
                    throw IllegalArgumentException("Dependency branch and tag cannot be set at the same time")
                }
            }
        }
    }

}