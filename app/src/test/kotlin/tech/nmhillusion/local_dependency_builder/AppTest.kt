/*
 * This source file was generated by the Gradle 'init' task
 */
package tech.nmhillusion.local_dependency_builder

import kotlin.test.Test
import kotlin.test.assertNotNull

class AppTest {
    @Test
    fun appHasAName() {
        val classUnderTest = App(listOf("config.yml").joinToString())
        assertNotNull(classUnderTest.name, "app should have a name")
    }
}