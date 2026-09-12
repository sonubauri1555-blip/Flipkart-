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
import java.util.UUID

object DefaultProjects {

    fun createGopalBharProject(): CartoonProject {
        val scenes = listOf(
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 1,
                title = "দৃশ্য ১: মহারাজ ও দরবারের রহস্য",
                environment = CartoonEnvironment.ROYAL_PALACE,
                weather = WeatherEffect.NONE,
                camera = CameraMotion.ZOOM_IN,
                bgm = BgmStyle.COMEDY_FUNNY,
                sfx = ActionSoundEffect.APPLAUSE,
                leftActor = SceneActor(
                    characterType = CharacterType.KING,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.THINKING
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.BOY,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.LAUGHING
                ),
                speakerPosition = CharacterPosition.LEFT,
                dialogueText = "শুনলাম রাজ্যের কোষাগার থেকে নাকি চোর স্বর্ণমুদ্রা চুরি করেছে! গোপাল, তুমি কি এর কোনো সমাধান করতে পারবে?",
                narrationText = "কৃষ্ণনগরের স্বর্ণালী রাজদরবার। মহারাজ বিক্রমাদিত্য চিন্তিত মুখে সিংহাসনে বসে আছেন।",
                durationSeconds = 9
            ),
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 2,
                title = "দৃশ্য ২: গোপাল ভাঁড়ের রসিক জবাব",
                environment = CartoonEnvironment.ROYAL_PALACE,
                weather = WeatherEffect.NONE,
                camera = CameraMotion.STATIC,
                bgm = BgmStyle.CHEERFUL_VILLAGE,
                sfx = ActionSoundEffect.LAUGHTER,
                leftActor = SceneActor(
                    characterType = CharacterType.KING,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.HAPPY
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.BOY,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.HAPPY
                ),
                speakerPosition = CharacterPosition.RIGHT,
                dialogueText = "জাহাঁপনা, আপনি একদম চিন্তা করবেন না! চোর যতই চালাক হোক, আমার বুদ্ধির ফাঁদ থেকে তার নিস্তার নেই!",
                narrationText = "গোপাল একগাল হেসে মহারাজকে আশ্বস্ত করলেন।",
                durationSeconds = 8
            ),
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 3,
                title = "দৃশ্য ৩: ভয়াল রাতের আঁধারে চোর",
                environment = CartoonEnvironment.NIGHT_FOREST,
                weather = WeatherEffect.NIGHT_STARS,
                camera = CameraMotion.PAN_LEFT_TO_RIGHT,
                bgm = BgmStyle.SPOOKY_MYSTERY,
                sfx = ActionSoundEffect.FOOTSTEPS,
                centerActor = SceneActor(
                    characterType = CharacterType.THIEF,
                    position = CharacterPosition.CENTER,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.THINKING
                ),
                speakerPosition = CharacterPosition.CENTER,
                dialogueText = "হাহা! রাজপ্রাসাদের সমস্ত সোনা এখন আমার ঝুলিতে! আজ রাতেই নদী পার হয়ে দূর দেশে পালিয়ে যাব!",
                narrationText = "ঘন অন্ধকার রাতের বনভূমি। কালো মুখোশ পরা চোর দ্রুত পায়ে এগিয়ে চলেছে।",
                durationSeconds = 9
            ),
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 4,
                title = "দৃশ্য ৪: পুলিশ অফিসারের অতর্কিত আক্রমণ",
                environment = CartoonEnvironment.NIGHT_FOREST,
                weather = WeatherEffect.THUNDER_FLASH,
                camera = CameraMotion.SHAKE,
                bgm = BgmStyle.ACTION_CHASE,
                sfx = ActionSoundEffect.PUNCH_HIT,
                leftActor = SceneActor(
                    characterType = CharacterType.POLICE,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.ANGRY
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.THIEF,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.SURPRISED
                ),
                speakerPosition = CharacterPosition.LEFT,
                dialogueText = "থামো চোর! হাত উপরে তোলো! আইনকে ফাঁকি দিয়ে পালানো অত সহজ নয়!",
                narrationText = "হঠাৎ বনের ঝোপের আড়াল থেকে ইনস্পেক্টর রায় গর্জে উঠলেন।",
                durationSeconds = 8
            ),
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 5,
                title = "দৃশ্য ৫: গ্রামে আনন্দ ও উৎসব",
                environment = CartoonEnvironment.VILLAGE_FARM,
                weather = WeatherEffect.SUNSET_GLOW,
                camera = CameraMotion.STATIC,
                bgm = BgmStyle.CHEERFUL_VILLAGE,
                sfx = ActionSoundEffect.APPLAUSE,
                leftActor = SceneActor(
                    characterType = CharacterType.GRANDFATHER,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.HAPPY
                ),
                centerActor = SceneActor(
                    characterType = CharacterType.GIRL,
                    position = CharacterPosition.CENTER,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.HAPPY
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.BOY,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.HAPPY
                ),
                speakerPosition = CharacterPosition.LEFT,
                dialogueText = "সবাই দেখে রাখো, অসৎ পথে কখনো জয় হয় না। বুদ্ধির জোরে আজ রাজ্য রক্ষা পেল!",
                narrationText = "গ্রামের মেঠোপথে আনন্দ আর কোলাহলে সবাই গোপাল আর পুলিশের প্রশংসায় মাতল।",
                durationSeconds = 9
            )
        )

        return CartoonProject(
            id = 1L,
            title = "গোপাল ভাঁড় ও চালাক চোর",
            description = "কৃষ্ণনগরের রাজদরবার থেকে চোরের অভিযান ও রোমাঞ্চকর কমেডি কার্টুন সিনেমা।",
            scenes = scenes
        )
    }

    fun createFantasyAdventureProject(): CartoonProject {
        val scenes = listOf(
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 1,
                title = "দৃশ্য ১: মায়াপরির আগমন",
                environment = CartoonEnvironment.CLOUD_KINGDOM,
                weather = WeatherEffect.MAGIC_SPARKLES,
                camera = CameraMotion.STATIC,
                bgm = BgmStyle.EMOTIONAL,
                sfx = ActionSoundEffect.MAGIC_SPELL,
                leftActor = SceneActor(
                    characterType = CharacterType.FAIRY,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.HAPPY
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.HERO,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.SURPRISED
                ),
                speakerPosition = CharacterPosition.LEFT,
                dialogueText = "সাহসী বিক্রম, অন্ধকারের রাক্ষস বনে আক্রমণ করেছে! তোমাকে এখনই জাদুকরী তলোয়ার নিয়ে যেতে হবে!",
                narrationText = "মেঘের দেশে অলৌকিক আলো ছড়িয়ে মায়াপরি আবির্ভূত হলো।",
                durationSeconds = 9
            ),
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 2,
                title = "দৃশ্য ২: বনের দানবের গর্জন",
                environment = CartoonEnvironment.DEEP_JUNGLE,
                weather = WeatherEffect.RAIN,
                camera = CameraMotion.SHAKE,
                bgm = BgmStyle.ACTION_CHASE,
                sfx = ActionSoundEffect.THUNDER_CRACK,
                leftActor = SceneActor(
                    characterType = CharacterType.HERO,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.ANGRY
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.MONSTER,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.SHOUTING
                ),
                speakerPosition = CharacterPosition.RIGHT,
                dialogueText = "হা হা হা! ক্ষুদ্র মানব, তুই আমার সামনে দাঁড়াতে সাহস করিস? আজ তোকে আমি ধ্বংস করে দেব!",
                narrationText = "ঘন জঙ্গলে বৃষ্টির মধ্যে সুবিশাল রাক্ষস পথ আটকে দাঁড়াল।",
                durationSeconds = 10
            ),
            CartoonScene(
                id = UUID.randomUUID().toString(),
                index = 3,
                title = "দৃশ্য ৩: বিজয়ের আলো",
                environment = CartoonEnvironment.RIVER_SUNSET,
                weather = WeatherEffect.SUNSET_GLOW,
                camera = CameraMotion.ZOOM_IN,
                bgm = BgmStyle.CHEERFUL_VILLAGE,
                sfx = ActionSoundEffect.APPLAUSE,
                leftActor = SceneActor(
                    characterType = CharacterType.HERO,
                    position = CharacterPosition.LEFT,
                    facing = CharacterFacing.FACING_RIGHT,
                    expression = CharacterExpression.HAPPY
                ),
                rightActor = SceneActor(
                    characterType = CharacterType.HEROINE,
                    position = CharacterPosition.RIGHT,
                    facing = CharacterFacing.FACING_LEFT,
                    expression = CharacterExpression.HAPPY
                ),
                speakerPosition = CharacterPosition.RIGHT,
                dialogueText = "তুমি পেরেছ বিক্রম! তোমার সাহসিকতায় আজ পুরো জঙ্গল বিপদমুক্ত হলো!",
                narrationText = "নদীর তীরে রক্তিম সূর্যাস্তের আলোয় শান্তি ফিরে এল।",
                durationSeconds = 8
            )
        )

        return CartoonProject(
            id = 2L,
            title = "জাদুকরী ড্রাগন ও রাজপুত্র",
            description = "মেঘের দেশ, মায়াপরি ও দানবের বিরুদ্ধে এক বীরত্বের রোমাঞ্চকর রূপকথা।",
            scenes = scenes
        )
    }
}
