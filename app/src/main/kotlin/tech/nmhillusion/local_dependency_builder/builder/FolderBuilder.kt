package tech.nmhillusion.local_dependency_builder.builder

import java.nio.file.Path

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-06-01
 */
abstract class FolderBuilder {
    companion object {
        fun createFolders(path: Path): Boolean {
            if (!path.toFile().exists()) {
                return path.toFile().mkdirs()
            }
            return true
        }
    }
}