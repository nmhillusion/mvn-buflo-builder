package tech.nmhillusion.mvn_buflo_builder.flow.dependency_flow

import tech.nmhillusion.mvn_buflo_builder.model.LocalBuilderConfig
import tech.nmhillusion.mvn_buflo_builder.model.dependency.DependencyEntity

/**
 * created by: nmhillusion
 *
 *
 * created date: 2024-07-07
 */
interface DependencyBuilderFlow<T : DependencyEntity> {
    fun buildDependency(dependency_: T, localBuilderConfig: LocalBuilderConfig)
}
