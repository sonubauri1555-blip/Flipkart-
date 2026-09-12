package com.example.toonstudio.model

data class CartoonCharacterProfile(
    val type: CharacterType,
    val name: String,
    val bio: String,
    val pitch: Float,
    val speed: Float,
    val accentColor: Long
)

data class SceneActor(
    val characterType: CharacterType,
    val position: CharacterPosition,
    val facing: CharacterFacing = CharacterFacing.FACING_RIGHT,
    val expression: CharacterExpression = CharacterExpression.HAPPY,
    val isSpeaking: Boolean = false
)

data class CartoonScene(
    val id: String,
    val index: Int,
    val title: String,
    val environment: CartoonEnvironment = CartoonEnvironment.VILLAGE_FARM,
    val weather: WeatherEffect = WeatherEffect.NONE,
    val camera: CameraMotion = CameraMotion.STATIC,
    val bgm: BgmStyle = BgmStyle.CHEERFUL_VILLAGE,
    val sfx: ActionSoundEffect = ActionSoundEffect.NONE,
    val leftActor: SceneActor? = null,
    val centerActor: SceneActor? = null,
    val rightActor: SceneActor? = null,
    val speakerPosition: CharacterPosition? = CharacterPosition.LEFT,
    val dialogueText: String = "",
    val narrationText: String = "",
    val durationSeconds: Int = 8
) {
    val activeSpeakerType: CharacterType?
        get() = when (speakerPosition) {
            CharacterPosition.LEFT -> leftActor?.characterType
            CharacterPosition.CENTER -> centerActor?.characterType
            CharacterPosition.RIGHT -> rightActor?.characterType
            else -> null
        }
}

data class CartoonProject(
    val id: Long,
    val title: String,
    val description: String,
    val scenes: List<CartoonScene> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val totalDurationSeconds: Int
        get() = scenes.sumOf { it.durationSeconds }

    val formattedDuration: String
        get() {
            val totalSec = totalDurationSeconds
            val hours = totalSec / 3600
            val minutes = (totalSec % 3600) / 60
            val seconds = totalSec % 60
            return if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
}
