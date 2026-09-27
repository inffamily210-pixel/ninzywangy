package com.ninzywangy.editor.project

import com.ninzywangy.editor.timeline.TimelineComposition

/**
 * Project metadata and save/load structure.
 */
data class Project(
    val id: String = "",
    val name: String = "Untitled Project",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val composition: TimelineComposition = TimelineComposition(),
    val aspectRatio: String = "9:16", // portrait
    val fps: Int = 30
)

/**
 * Project manager for save/load operations.
 */
class ProjectManager {
    private var currentProject: Project? = null

    fun createNewProject(name: String = "Untitled Project"): Project {
        return Project(
            id = java.util.UUID.randomUUID().toString(),
            name = name,
            composition = TimelineComposition()
        ).also { currentProject = it }
    }

    fun saveProject(project: Project): Boolean {
        // TODO: Implement JSON serialization to file
        currentProject = project
        return true
    }

    fun loadProject(projectId: String): Project? {
        // TODO: Implement JSON deserialization from file
        return currentProject
    }

    fun getCurrentProject(): Project? = currentProject

    fun closeProject() {
        currentProject = null
    }

    fun getRecentProjects(): List<Project> {
        // TODO: Implement reading from project database
        return emptyList()
    }
}
