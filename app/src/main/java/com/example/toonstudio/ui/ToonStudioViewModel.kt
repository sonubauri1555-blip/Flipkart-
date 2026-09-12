package com.example.toonstudio.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.toonstudio.data.DefaultProjects
import com.example.toonstudio.data.ToonStudioDatabase
import com.example.toonstudio.data.toDomain
import com.example.toonstudio.data.toEntity
import com.example.toonstudio.engine.AudioSfxEngine
import com.example.toonstudio.engine.VoiceSpeechEngine
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
import com.example.toonstudio.model.StudioNavTab
import com.example.toonstudio.model.WeatherEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class ToonStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ToonStudioDatabase.getDatabase(application)
    private val dao = database.toonStudioDao()

    val speechEngine = VoiceSpeechEngine(application)
    val sfxEngine = AudioSfxEngine()

    // Navigation Tab
    private val _currentTab = MutableStateFlow(StudioNavTab.SCRIPT_DIRECTOR)
    val currentTab: StateFlow<StudioNavTab> = _currentTab.asStateFlow()

    // Current Active Project
    private val _currentProject = MutableStateFlow(DefaultProjects.createGopalBharProject())
    val currentProject: StateFlow<CartoonProject> = _currentProject.asStateFlow()

    // Selected Scene in Director
    private val _selectedSceneIndex = MutableStateFlow(0)
    val selectedSceneIndex: StateFlow<Int> = _selectedSceneIndex.asStateFlow()

    // Cinema Playback Engine State
    private val _isPlayingMovie = MutableStateFlow(false)
    val isPlayingMovie: StateFlow<Boolean> = _isPlayingMovie.asStateFlow()

    private val _movieCurrentSceneIndex = MutableStateFlow(0)
    val movieCurrentSceneIndex: StateFlow<Int> = _movieCurrentSceneIndex.asStateFlow()

    private val _moviePlaybackProgress = MutableStateFlow(0f)
    val moviePlaybackProgress: StateFlow<Float> = _moviePlaybackProgress.asStateFlow()

    private val _allProjects = MutableStateFlow<List<CartoonProject>>(emptyList())
    val allProjects: StateFlow<List<CartoonProject>> = _allProjects.asStateFlow()

    private var cinemaPlaybackJob: Job? = null

    val isSpeaking = speechEngine.isSpeaking
    val currentSpeaker = speechEngine.currentSpeaker

    init {
        initDatabaseProjects()
    }

    private fun initDatabaseProjects() {
        viewModelScope.launch {
            val projectEntities = dao.getAllProjects().first()
            if (projectEntities.isEmpty()) {
                // Populate default projects
                val p1 = DefaultProjects.createGopalBharProject()
                val p2 = DefaultProjects.createFantasyAdventureProject()
                saveProjectToDb(p1)
                saveProjectToDb(p2)
                _currentProject.value = p1
            } else {
                val firstProject = projectEntities.first()
                val scenes = dao.getScenesForProjectSync(firstProject.id).map { it.toDomain() }
                _currentProject.value = firstProject.toDomain(scenes)
            }
            refreshAllProjectsList()
        }
    }

    private suspend fun refreshAllProjectsList() {
        val list = dao.getAllProjects().first().map { projEntity ->
            val sc = dao.getScenesForProjectSync(projEntity.id).map { it.toDomain() }
            projEntity.toDomain(sc)
        }
        _allProjects.value = list
    }

    fun setTab(tab: StudioNavTab) {
        if (_isPlayingMovie.value && tab != StudioNavTab.CINEMA_PLAYER) {
            pauseMovie()
        }
        _currentTab.value = tab
    }

    fun selectScene(index: Int) {
        val maxIdx = (_currentProject.value.scenes.size - 1).coerceAtLeast(0)
        _selectedSceneIndex.value = index.coerceIn(0, maxIdx)
    }

    fun getSelectedScene(): CartoonScene? {
        val scenes = _currentProject.value.scenes
        val idx = _selectedSceneIndex.value
        return if (idx in scenes.indices) scenes[idx] else scenes.firstOrNull()
    }

    fun updateCurrentScene(updated: CartoonScene) {
        val currentScenes = _currentProject.value.scenes.toMutableList()
        val idx = _selectedSceneIndex.value
        if (idx in currentScenes.indices) {
            currentScenes[idx] = updated
            val newProj = _currentProject.value.copy(
                scenes = currentScenes,
                updatedAt = System.currentTimeMillis()
            )
            _currentProject.value = newProj
            viewModelScope.launch {
                dao.insertScene(updated.toEntity(_currentProject.value.id))
            }
        }
    }

    fun addNewScene() {
        val currentScenes = _currentProject.value.scenes.toMutableList()
        val newIndex = currentScenes.size + 1
        val lastScene = currentScenes.lastOrNull()

        val newScene = CartoonScene(
            id = UUID.randomUUID().toString(),
            index = newIndex,
            title = "দৃশ্য $newIndex: নতুন কাহিনী বিস্তার",
            environment = lastScene?.environment ?: CartoonEnvironment.VILLAGE_FARM,
            weather = WeatherEffect.NONE,
            camera = CameraMotion.STATIC,
            bgm = BgmStyle.CHEERFUL_VILLAGE,
            sfx = ActionSoundEffect.NONE,
            leftActor = lastScene?.leftActor ?: SceneActor(CharacterType.BOY, CharacterPosition.LEFT),
            rightActor = lastScene?.rightActor ?: SceneActor(CharacterType.GIRL, CharacterPosition.RIGHT, CharacterFacing.FACING_LEFT),
            speakerPosition = CharacterPosition.LEFT,
            dialogueText = "বলুন, তারপর কী হলো?",
            narrationText = "গল্পের পরবর্তী দৃশ্যে উত্তেজনা বৃদ্ধি পেল।",
            durationSeconds = 8
        )

        currentScenes.add(newScene)
        val newProj = _currentProject.value.copy(scenes = currentScenes)
        _currentProject.value = newProj
        _selectedSceneIndex.value = currentScenes.size - 1

        viewModelScope.launch {
            dao.insertScene(newScene.toEntity(_currentProject.value.id))
        }
    }

    fun deleteScene(sceneId: String) {
        val currentScenes = _currentProject.value.scenes.toMutableList()
        if (currentScenes.size <= 1) return // Keep at least one scene

        currentScenes.removeAll { it.id == sceneId }
        // Re-index
        val reindexed = currentScenes.mapIndexed { index, scene ->
            scene.copy(index = index + 1)
        }

        val newProj = _currentProject.value.copy(scenes = reindexed)
        _currentProject.value = newProj
        _selectedSceneIndex.value = _selectedSceneIndex.value.coerceIn(0, reindexed.size - 1)

        viewModelScope.launch {
            dao.deleteScene(sceneId)
            dao.replaceScenesForProject(_currentProject.value.id, reindexed.map { it.toEntity(_currentProject.value.id) })
        }
    }

    fun testCurrentSceneVoice() {
        val scene = getSelectedScene() ?: return
        val speaker = scene.activeSpeakerType ?: CharacterType.BOY
        val textToSpeak = if (scene.dialogueText.isNotBlank()) scene.dialogueText else scene.narrationText

        // Play SFX if any
        sfxEngine.playSfx(scene.sfx)
        speechEngine.speak(textToSpeak, speaker)
    }

    fun testCharacterVoice(type: CharacterType) {
        val sampleLines = mapOf(
            CharacterType.BOY to "আমি বাবলু! আজ স্কুলে অনেক মজা হবে!",
            CharacterType.GIRL to "আমি টুনি! চলো সবাই মিলে বাগানে যাই!",
            CharacterType.KING to "আমি কৃষ্ণনগরের মহারাজ! রাজকোষের চুরি সহ্য করা হবে না!",
            CharacterType.MONSTER to "হা হা হা! আমি এই জঙ্গলের রাজা! তোদের সবাইকে খেয়ে ফেলব!",
            CharacterType.THIEF to "চুপ! কেউ যেন টের না পায়! সোনাদানা নিয়ে পালাতে হবে!",
            CharacterType.POLICE to "আইন নিজের হাতে তুলে নেবেন না! চোর পালাবে কোথায়!",
            CharacterType.GRANDFATHER to "সবসময় সত্যি কথা বলবে দাদু ভাই, ধর্মে মতি রাখবে!",
            CharacterType.FAIRY to "আমি পরীর দেশের আলো নিয়ে এসেছি! তোমার স্বপ্ন পূরণ হবে!",
            CharacterType.ROBOT to "সিস্টেম অনলাইন! প্রোটোকল 9 সক্রিয় করা হয়েছে!",
            CharacterType.HERO to "অন্যায় সহ্য করব না! আমি শেষ পর্যন্ত লড়ব!",
            CharacterType.HEROINE to "বুদ্ধি আর সাহস থাকলে যে কোনো বিপদ জয় করা সম্ভব!",
            CharacterType.FOX to "হা হা, বোকাসোকা বাঘটাকে এবার দারুণ শিক্ষা দেব!",
            CharacterType.TIGER to "হালুম! আমার এলাকায় কার এত সাহস?"
        )
        val text = sampleLines[type] ?: "নমস্কার! আমি কার্টুনের চরিত্র!"
        speechEngine.speak(text, type)
    }

    // Cinema Playback Engine (Handles continuous 1-2 hour episodes)
    fun toggleMoviePlayback() {
        if (_isPlayingMovie.value) {
            pauseMovie()
        } else {
            startMoviePlayback(_selectedSceneIndex.value)
        }
    }

    fun startMoviePlayback(fromSceneIndex: Int = 0) {
        val scenes = _currentProject.value.scenes
        if (scenes.isEmpty()) return

        cinemaPlaybackJob?.cancel()
        _isPlayingMovie.value = true
        _movieCurrentSceneIndex.value = fromSceneIndex.coerceIn(0, scenes.size - 1)

        cinemaPlaybackJob = viewModelScope.launch {
            var currentIdx = _movieCurrentSceneIndex.value

            while (isActive && currentIdx < scenes.size) {
                val scene = scenes[currentIdx]
                _selectedSceneIndex.value = currentIdx
                _movieCurrentSceneIndex.value = currentIdx

                // Start scene BGM and SFX
                sfxEngine.playBgm(scene.bgm)
                sfxEngine.playSfx(scene.sfx)

                // Speak dialogue/narration
                val textToSpeak = if (scene.dialogueText.isNotBlank()) scene.dialogueText else scene.narrationText
                if (textToSpeak.isNotBlank()) {
                    speechEngine.speak(textToSpeak, scene.activeSpeakerType)
                }

                // Animate progress smoothly through the scene duration
                val durationMs = scene.durationSeconds * 1000L
                val stepMs = 50L
                var elapsed = 0L

                while (isActive && elapsed < durationMs) {
                    delay(stepMs)
                    elapsed += stepMs
                    _moviePlaybackProgress.value = (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)
                }

                speechEngine.stop()
                currentIdx++
            }

            // Finished playback
            _isPlayingMovie.value = false
            _moviePlaybackProgress.value = 0f
            sfxEngine.stopBgm()
        }
    }

    fun pauseMovie() {
        cinemaPlaybackJob?.cancel()
        _isPlayingMovie.value = false
        speechEngine.stop()
        sfxEngine.stopBgm()
    }

    fun nextMovieScene() {
        val nextIdx = _movieCurrentSceneIndex.value + 1
        if (nextIdx < _currentProject.value.scenes.size) {
            startMoviePlayback(nextIdx)
        }
    }

    fun prevMovieScene() {
        val prevIdx = (_movieCurrentSceneIndex.value - 1).coerceAtLeast(0)
        startMoviePlayback(prevIdx)
    }

    // Projects Management
    fun createNewProject(title: String, desc: String) {
        viewModelScope.launch {
            val proj = CartoonProject(
                id = System.currentTimeMillis(),
                title = title.ifBlank { "নতুন কার্টুন সিনেমা" },
                description = desc.ifBlank { "স্ক্রিপ্ট দিয়ে তৈরি পূর্ণাঙ্গ কার্টুন ভিডিও" },
                scenes = listOf(
                    CartoonScene(
                        id = UUID.randomUUID().toString(),
                        index = 1,
                        title = "দৃশ্য ১: কাহিনীর সূচনা",
                        environment = CartoonEnvironment.VILLAGE_FARM,
                        leftActor = SceneActor(CharacterType.BOY, CharacterPosition.LEFT),
                        rightActor = SceneActor(CharacterType.GIRL, CharacterPosition.RIGHT, CharacterFacing.FACING_LEFT),
                        dialogueText = "স্বাগতম আমাদের নতুন কার্টুন জগতে!",
                        narrationText = "একটি সুন্দর সকালের মনোরম দৃশ্য।"
                    )
                )
            )
            saveProjectToDb(proj)
            _currentProject.value = proj
            _selectedSceneIndex.value = 0
            refreshAllProjectsList()
            _currentTab.value = StudioNavTab.SCRIPT_DIRECTOR
        }
    }

    fun switchProject(project: CartoonProject) {
        pauseMovie()
        _currentProject.value = project
        _selectedSceneIndex.value = 0
        _currentTab.value = StudioNavTab.SCRIPT_DIRECTOR
    }

    private suspend fun saveProjectToDb(project: CartoonProject) {
        val entity = project.toEntity()
        val generatedId = dao.insertProject(entity)
        val finalId = if (project.id == 0L) generatedId else project.id
        dao.replaceScenesForProject(finalId, project.scenes.map { it.toEntity(finalId) })
    }

    private fun CartoonProject.toEntity() = com.example.toonstudio.data.ProjectEntity(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun exportScriptText(): String {
        val proj = _currentProject.value
        val sb = StringBuilder()
        sb.append("🎬 সিনেমা শিরোনাম: ${proj.title}\n")
        sb.append("📝 বিবরণ: ${proj.description}\n")
        sb.append("⏱️ মোট দৃশ্য: ${proj.scenes.size} টি | মোট সময়: ${proj.formattedDuration}\n\n")
        sb.append("========================================\n\n")

        proj.scenes.forEach { sc ->
            sb.append("【 দৃশ্য ${sc.index}: ${sc.title} 】\n")
            sb.append("🏞️ পরিবেশ: ${sc.environment.titleBn} | আবহাওয়া: ${sc.weather.labelBn}\n")
            sb.append("🎵 BGM: ${sc.bgm.titleBn} | SFX: ${sc.sfx.titleBn}\n")
            if (sc.narrationText.isNotBlank()) {
                sb.append("📜 বর্ণনা: ${sc.narrationText}\n")
            }
            if (sc.dialogueText.isNotBlank()) {
                val spk = sc.activeSpeakerType?.defaultNameBn ?: "চরিত্র"
                sb.append("💬 $spk: \"${sc.dialogueText}\"\n")
            }
            sb.append("\n----------------------------------------\n\n")
        }
        return sb.toString()
    }

    override fun onCleared() {
        super.onCleared()
        speechEngine.shutdown()
        sfxEngine.release()
    }
}
