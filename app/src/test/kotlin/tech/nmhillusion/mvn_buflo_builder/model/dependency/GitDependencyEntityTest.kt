package tech.nmhillusion.mvn_buflo_builder.model.dependency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.nmhillusion.mvn_buflo_builder.model.AccessTokenConfig

/**
 * created by: nmhillusion
 *
 *
 * created date: 2024-07-07
 */
class GitDependencyEntityTest {

    @Test
    fun getUrl1() {
        /// MARK: TEST CASE #1
        val accessToken = AccessTokenConfig.fromMap(mapOf("tokenName" to "nmh", "tokenValue" to "1234"))
        val gitDependencyEntity = GitDependencyEntity(
            path = "https://github.com/nmhillusion/n2mix-java.git",
            rootPath = null,
            branch = null,
            tag = null,
            ignoredTest = false,
            useAccessToken = true
        )

        assertEquals(
            "https://nmh:1234@github.com/nmhillusion/n2mix-java.git",
            gitDependencyEntity.getUrl(accessToken)
        )
    }

    @Test
    fun getUrl2() {
        /// MARK: TEST CASE #2
        val accessToken = AccessTokenConfig.fromMap(mapOf("tokenName" to "nmh", "tokenValue" to "1234"))
        val gitDependencyEntity = GitDependencyEntity(
            path = "https://github.com/nmhillusion/n2mix-java.git",
            rootPath = null,
            branch = null,
            tag = null,
            ignoredTest = false,
            useAccessToken = false
        )

        assertEquals(
            "https://github.com/nmhillusion/n2mix-java.git",
            gitDependencyEntity.getUrl(accessToken)
        )
    }

    @Test
    fun getUrl3() {
        /// MARK: TEST CASE #3
        val accessToken = null
        val gitDependencyEntity = GitDependencyEntity(
            path = "https://github.com/nmhillusion/n2mix-java.git",
            rootPath = null,
            branch = null,
            tag = null,
            ignoredTest = false,
            useAccessToken = true
        )

        assertEquals(
            "https://github.com/nmhillusion/n2mix-java.git",
            gitDependencyEntity.getUrl(accessToken)
        )
    }

    @Test
    fun getUrl4() {
        /// MARK: TEST CASE #4
        val accessToken = null
        val gitDependencyEntity = GitDependencyEntity(
            path = "https://github.com/nmhillusion/n2mix-java.git",
            rootPath = null,
            branch = null,
            tag = null,
            ignoredTest = false,
            useAccessToken = false
        )

        assertEquals(
            "https://github.com/nmhillusion/n2mix-java.git",
            gitDependencyEntity.getUrl(accessToken)
        )
    }
}