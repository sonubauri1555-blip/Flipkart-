package com.example.toonstudio.model

enum class CharacterType(
    val defaultNameBn: String,
    val defaultNameEn: String,
    val roleDescBn: String,
    val voicePitch: Float,
    val voiceSpeed: Float
) {
    BOY("বাবলু (স্কুল ছাত্র)", "Bablu (Boy)", "চঞ্চল ও বুদ্ধিমান বালক", 1.35f, 1.05f),
    GIRL("টুনি (ছোট্ট মেয়ে)", "Tuni (Girl)", "মিষ্টি ও দয়ালু বালিকা", 1.45f, 1.0f),
    HERO("বিক্রম (নায়ক)", "Vikram (Hero)", "সাহসী ও উদ্যমী যুবক", 1.05f, 1.0f),
    HEROINE("প্রিয়া (নায়িকা)", "Priya (Heroine)", "বুদ্ধিমতি ও সাহসী তরুণী", 1.25f, 1.0f),
    KING("মহারাজা বিক্রমাদিত্য", "Maharaja (King)", "প্রতাপশালী ও ন্যায়পরায়ণ রাজা", 0.85f, 0.88f),
    MONSTER("ভয়ঙ্কর রাক্ষস", "Monster (Rakshas)", "দানবাকৃতি ও গর্জনশীল শত্রু", 0.6f, 0.8f),
    THIEF("কালাচোর (চালাক চোর)", "Kalachor (Thief)", "ধূর্ত ও পালানো চোর", 1.15f, 1.15f),
    POLICE("ইনস্পেক্টর রায়", "Inspector Roy (Police)", "কর্তব্যনিষ্ঠ পুলিশ অফিসার", 0.95f, 0.95f),
    GRANDFATHER("দাদু (জ্ঞানবৃদ্ধ)", "Dadu (Grandfather)", "অভিজ্ঞ ও নীতিবান বৃদ্ধ", 0.75f, 0.82f),
    FAIRY("মায়াপরি (পরী)", "Maya Pari (Fairy)", "জাদুকরী ডানাওয়ালা পরী", 1.5f, 0.95f),
    ROBOT("সাইবার রোবট Z-9", "Robot Z-9", "ভবিষ্যত প্রযুক্তির কৃত্রিম বুদ্ধিমত্তা", 1.1f, 1.1f),
    FOX("ধূর্ত শিয়াল", "Clever Fox", "বনের চালাক প্রাণী", 1.25f, 1.1f),
    TIGER("রয়েল বেঙ্গল টাইগার", "Royal Tiger", "বনের গর্জনকারী রাজা", 0.65f, 0.85f)
}

enum class CharacterExpression(val labelBn: String, val labelEn: String) {
    HAPPY("হাসিখুশি", "Happy"),
    ANGRY("রাগান্বিত", "Angry"),
    SURPRISED("বিস্মিত", "Surprised"),
    SAD("দুঃখিত", "Sad"),
    LAUGHING("অট্টহাসি", "Laughing"),
    SHOUTING("চিৎকার", "Shouting"),
    THINKING("চিন্তিত", "Thinking"),
    NEUTRAL("স্বাভাবিক", "Neutral")
}

enum class CharacterPosition(val fractionX: Float) {
    LEFT(0.22f),
    CENTER(0.50f),
    RIGHT(0.78f),
    OFF_SCREEN(-0.5f)
}

enum class CharacterFacing {
    FACING_RIGHT,
    FACING_LEFT
}

enum class CartoonEnvironment(val titleBn: String, val titleEn: String, val category: String) {
    VILLAGE_FARM("সবুজ গ্রাম ও মাটির বাড়ি", "Village & Farm", "Rural"),
    DEEP_JUNGLE("রহস্যময় ঘন জঙ্গল", "Mystic Deep Jungle", "Nature"),
    ROYAL_PALACE("স্বর্ণালী রাজদরবার", "Royal Golden Palace", "Historical"),
    CITY_STREET("ব্যস্ত আধুনিক শহর", "Busy City Street", "Urban"),
    SCHOOL_CLASSROOM("স্কুল ক্লাসরুম", "School Classroom", "Indoor"),
    NIGHT_FOREST("ভয়াল রাতের বনভূমি", "Spooky Night Forest", "Mystery"),
    RIVER_SUNSET("নদী ও রক্তিম সূর্যাস্ত", "River & Sunset", "Nature"),
    OUTER_SPACE("মহাকাশ ও গ্যালাক্সি", "Outer Space & Galaxy", "Sci-Fi"),
    CLOUD_KINGDOM("জাদুকরী মেঘের রাজ্য", "Magical Cloud Kingdom", "Fantasy")
}

enum class WeatherEffect(val labelBn: String, val labelEn: String) {
    NONE("স্বাভাবিক আবহাওয়া", "Clear Day"),
    RAIN("মুষলধারে বৃষ্টি", "Rain Storm"),
    SNOW("তুষারপাত", "Snow Fall"),
    NIGHT_STARS("উজ্জ্বল তারা ও চাঁদ", "Stars & Moon"),
    SUNSET_GLOW("গোধূলির সোনালী আলো", "Sunset Glow"),
    MAGIC_SPARKLES("জাদুর ঝলকানি", "Magic Sparkles"),
    THUNDER_FLASH("বজ্রপাত ও বিদ্যুৎ", "Thunder Flash")
}

enum class CameraMotion(val labelBn: String, val labelEn: String) {
    STATIC("স্থির শট (Static)", "Static Shot"),
    ZOOM_IN("স্পিকার জুম-ইন (Close-up)", "Zoom In Focus"),
    PAN_LEFT_TO_RIGHT("প্যান বাম থেকে ডানে (Pan R)", "Pan Left to Right"),
    PAN_RIGHT_TO_LEFT("প্যান ডান থেকে বামে (Pan L)", "Pan Right to Left"),
    SHAKE("ক্যামেরা কাঁপন (Action Shake)", "Action Shake")
}

enum class BgmStyle(val titleBn: String, val mood: String) {
    NONE("কোনো সুর নেই (Silent)", "None"),
    CHEERFUL_VILLAGE("আনন্দময় গ্রামীণ বাঁশি ও সুর", "Happy"),
    SUSPENSE_DRAMA("রহস্য ও সাসপেন্স মিউজিক", "Suspense"),
    ACTION_CHASE("উত্তেজনাপূর্ণ তাড়া করার অ্যাকশন", "Action"),
    EMOTIONAL("শান্ত ও আবেগঘন সুর", "Emotional"),
    SPOOKY_MYSTERY("ভুতুড়ে ও অন্ধকার শিহরণ", "Spooky"),
    COMEDY_FUNNY("মজার ও হাস্যরসাত্মক সুর", "Comedy")
}

enum class ActionSoundEffect(val titleBn: String, val soundType: String) {
    NONE("কোনো সাউন্ড এফেক্ট নেই", "None"),
    THUNDER_CRACK("মেঘের গর্জন ও বজ্রপাত", "Thunder"),
    PUNCH_HIT("ঘুষি বা আঘাতের শব্দ (Punch)", "Hit"),
    FOOTSTEPS("দৌড়ানো বা পায়ের শব্দ", "Steps"),
    MAGIC_SPELL("জাদুর সুর ও ঘণ্টার ঝংকার", "Magic"),
    LAUGHTER("জোরে হাসির রোল (Laughter)", "Laugh"),
    APPLAUSE("করতালি ও হাততালি (Clap)", "Applause"),
    GASP("ভয়ে বা বিস্ময়ে আঁতকে ওঠা", "Gasp")
}

enum class StudioNavTab(val titleBn: String, val titleEn: String) {
    SCRIPT_DIRECTOR("স্ক্রিপ্ট স্টুডিও", "Director"),
    CINEMA_PLAYER("সিনেমা প্লেয়ার", "Cinema Play"),
    CHARACTERS("ক্যারেক্টারস", "Characters"),
    STORY_LIBRARY("মুভি লাইব্রেরি", "Library")
}
