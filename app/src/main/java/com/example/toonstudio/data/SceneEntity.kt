package com.example.toonstudio.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scenes",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId"), Index("sceneIndex")]
)
data class SceneEntity(
    @PrimaryKey
    val id: String,
    val projectId: Long,
    val sceneIndex: Int,
    val title: String,
    val environment: String,
    val weather: String,
    val camera: String,
    val bgm: String,
    val sfx: String,
    val leftActorType: String?,
    val leftActorExpression: String?,
    val leftActorFacing: String?,
    val centerActorType: String?,
    val centerActorExpression: String?,
    val centerActorFacing: String?,
    val rightActorType: String?,
    val rightActorExpression: String?,
    val rightActorFacing: String?,
    val speakerPosition: String?,
    val dialogueText: String,
    val narrationText: String,
    val durationSeconds: Int
)
