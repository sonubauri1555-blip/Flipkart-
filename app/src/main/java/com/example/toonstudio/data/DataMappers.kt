package com.example.toonstudio.data

import com.example.toonstudio.model.ActionSoundEffect
import com.example.toonstudio.model.BgmStyle
import com.example.toonstudio.model.CameraMotion
import com.example.toonstudio.model.CartoonEnvironment
import com.example.toonstudio.model.CartoonProject
import com.example.toonstudio.model.CartoonScene
import com.example.toonstudio.model.CharacterExpression
import com.example.toonstudio.model.CharacterFacing
import com.example.toonstudio.model.CharacterPosition
import com.example.toonstudio.model.CharacterType
import com.example.toonstudio.model.SceneActor
import com.example.toonstudio.model.WeatherEffect

fun SceneEntity.toDomain(): CartoonScene {
    val leftActor = leftActorType?.let { typeStr ->
        try {
            SceneActor(
                characterType = CharacterType.valueOf(typeStr),
                position = CharacterPosition.LEFT,
                facing = leftActorFacing?.let { CharacterFacing.valueOf(it) } ?: CharacterFacing.FACING_RIGHT,
                expression = leftActorExpression?.let { CharacterExpression.valueOf(it) } ?: CharacterExpression.HAPPY
            )
        } catch (e: Exception) { null }
    }

    val centerActor = centerActorType?.let { typeStr ->
        try {
            SceneActor(
                characterType = CharacterType.valueOf(typeStr),
                position = CharacterPosition.CENTER,
                facing = centerActorFacing?.let { CharacterFacing.valueOf(it) } ?: CharacterFacing.FACING_RIGHT,
                expression = centerActorExpression?.let { CharacterExpression.valueOf(it) } ?: CharacterExpression.HAPPY
            )
        } catch (e: Exception) { null }
    }

    val rightActor = rightActorType?.let { typeStr ->
        try {
            SceneActor(
                characterType = CharacterType.valueOf(typeStr),
                position = CharacterPosition.RIGHT,
                facing = rightActorFacing?.let { CharacterFacing.valueOf(it) } ?: CharacterFacing.FACING_LEFT,
                expression = rightActorExpression?.let { CharacterExpression.valueOf(it) } ?: CharacterExpression.HAPPY
            )
        } catch (e: Exception) { null }
    }

    val spkPos = speakerPosition?.let {
        try { CharacterPosition.valueOf(it) } catch (e: Exception) { null }
    }

    val env = try { CartoonEnvironment.valueOf(environment) } catch (e: Exception) { CartoonEnvironment.VILLAGE_FARM }
    val wth = try { WeatherEffect.valueOf(weather) } catch (e: Exception) { WeatherEffect.NONE }
    val cam = try { CameraMotion.valueOf(camera) } catch (e: Exception) { CameraMotion.STATIC }
    val bgmStyle = try { BgmStyle.valueOf(bgm) } catch (e: Exception) { BgmStyle.CHEERFUL_VILLAGE }
    val sfxEffect = try { ActionSoundEffect.valueOf(sfx) } catch (e: Exception) { ActionSoundEffect.NONE }

    return CartoonScene(
        id = id,
        index = sceneIndex,
        title = title,
        environment = env,
        weather = wth,
        camera = cam,
        bgm = bgmStyle,
        sfx = sfxEffect,
        leftActor = leftActor,
        centerActor = centerActor,
        rightActor = rightActor,
        speakerPosition = spkPos,
        dialogueText = dialogueText,
        narrationText = narrationText,
        durationSeconds = durationSeconds
    )
}

fun CartoonScene.toEntity(projectId: Long): SceneEntity {
    return SceneEntity(
        id = id,
        projectId = projectId,
        sceneIndex = index,
        title = title,
        environment = environment.name,
        weather = weather.name,
        camera = camera.name,
        bgm = bgm.name,
        sfx = sfx.name,
        leftActorType = leftActor?.characterType?.name,
        leftActorExpression = leftActor?.expression?.name,
        leftActorFacing = leftActor?.facing?.name,
        centerActorType = centerActor?.characterType?.name,
        centerActorExpression = centerActor?.expression?.name,
        centerActorFacing = centerActor?.facing?.name,
        rightActorType = rightActor?.characterType?.name,
        rightActorExpression = rightActor?.expression?.name,
        rightActorFacing = rightActor?.facing?.name,
        speakerPosition = speakerPosition?.name,
        dialogueText = dialogueText,
        narrationText = narrationText,
        durationSeconds = durationSeconds
    )
}

fun ProjectEntity.toDomain(scenes: List<CartoonScene> = emptyList()): CartoonProject {
    return CartoonProject(
        id = id,
        title = title,
        description = description,
        scenes = scenes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
