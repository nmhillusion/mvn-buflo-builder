package tech.nmhillusion.local_dependency_builder.flow

import tech.nmhillusion.n2mix.model.cli.ParameterModel
import tech.nmhillusion.n2mix.util.StringUtil

/**
 * created by: nmhillusion
 * created date: 2024-06-09
 */
abstract class BaseFlow {
    protected abstract fun acceptedParameterNames(): List<String>

    fun isSupported(parameters: List<ParameterModel>): Boolean {
        return acceptedParameterNames().any {
            parameters.any { p ->
                StringUtil.trimWithNull(p.name).equals(
                    StringUtil.trimWithNull(it), true
                )
            }
        }
    }

    fun exec(parameters: List<ParameterModel>) {
        /// Mark: PRE EXEC
        preExec(parameters)

        /// Mark: DO EXEC
        doExec(parameters)

        /// Mark: POST EXEC
        postExec(parameters)
    }

    protected open fun preExec(parameters: List<ParameterModel>) {}

    protected abstract fun doExec(parameters: List<ParameterModel>)

    protected fun postExec(parameters: List<ParameterModel>) {}
}
