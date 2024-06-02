package tech.nmhillusion.local_dependency_builder.validator

import tech.nmhillusion.local_dependency_builder.model.DependencyEntity


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-02
 */
abstract class DependencyValidator {

    companion object {
        fun validateDependency(dependencyEntity: DependencyEntity) {
            if (dependencyEntity.url.isEmpty()) {
                throw Exception("Dependency url is empty")
            }

            if (!dependencyEntity.url.matches("^https?://.*?\\.git$".toRegex())) {
                throw Exception("Dependency url is not valid, must be in pattern: https?://.*?\\.git$")
            }

            if (dependencyEntity.name.isEmpty()) {
                throw Exception("Dependency name is empty")
            }
        }
    }

}