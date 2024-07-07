package tech.nmhillusion.mvn_buflo_builder.exception

/**
 * created by: nmhillusion
 *
 *
 * created date: 2024-07-07
 */
class UnsupportedDependencyException : Exception {
    constructor()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}
