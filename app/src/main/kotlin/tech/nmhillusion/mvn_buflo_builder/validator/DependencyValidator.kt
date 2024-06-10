package tech.nmhillusion.mvn_buflo_builder.validator

import tech.nmhillusion.mvn_buflo_builder.model.DependencyEntity
import tech.nmhillusion.n2mix.validator.StringValidator


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

            if (!StringValidator.isBlank(dependencyEntity.branch) && !StringValidator.isBlank(dependencyEntity.tag)) {
                throw IllegalArgumentException("Dependency branch and tag cannot be set at the same time")
            }
        }
    }

}