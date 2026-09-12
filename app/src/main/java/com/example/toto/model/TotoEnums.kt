package com.example.toto.model

enum class GearMode(val label: String, val shortCode: String, val bengaliLabel: String) {
    PARK("Park", "P", "পার্কিং লক"),
    REVERSE("Reverse", "R", "রিভার্স (পিছনে)"),
    NEUTRAL("Neutral", "N", "নিউট্রাল (ফ্রি)"),
    DRIVE("Drive", "D", "ফরওয়ার্ড (সামনে)")
}

enum class DriveProfile(
    val title: String,
    val bengaliTitle: String,
    val speedLimitKmH: Int,
    val description: String,
    val bengaliDesc: String
) {
    ECO(
        title = "Eco Saver",
        bengaliTitle = "ইকো সেভার",
        speedLimitKmH = 18,
        description = "Max battery conservation, smooth acceleration for market crowds",
        bengaliDesc = "সর্বাধিক ব্যাটারি ব্যাকআপ ও জ্যামের রাস্তায় মসৃণ ড্রাইভিং"
    ),
    CITY(
        title = "City Standard",
        bengaliTitle = "সিটি রেগুলার",
        speedLimitKmH = 26,
        description = "Balanced torque and speed for standard passenger runs",
        bengaliDesc = "যাত্রী চলাচলের জন্য আদর্শ ব্যালান্সড স্পিড ও পাওয়ার"
    ),
    TURBO(
        title = "Turbo Boost",
        bengaliTitle = "টার্বো / হাই স্পিড",
        speedLimitKmH = 40,
        description = "Full 1200W/1500W BLDC motor output for highway stretches",
        bengaliDesc = "ফাঁকা রাস্তায় দ্রুত যাতায়াতের জন্য মোটর ফুল পাওয়ার"
    ),
    HEAVY_LOAD(
        title = "Heavy Load / Hill",
        bengaliTitle = "হাই টর্ক / চড়াই",
        speedLimitKmH = 20,
        description = "Max starting torque for 6 passengers or steep bridge climb",
        bengaliDesc = "৬ জন পূর্ণ যাত্রী বা ব্রিজ/উঁচু রাস্তায় ওঠার অতিরিক্ত টর্ক"
    )
}

enum class RegenLevel(val label: String, val bengaliLabel: String, val percent: Int) {
    OFF("Off", "বন্ধ", 0),
    LOW("Low (5%)", "কম (৫%)", 5),
    MEDIUM("Medium (12%)", "মাঝারি (১২%)", 12),
    HIGH("High (20%)", "বেশি (২০%)", 20)
}

enum class HeadlightMode(val label: String, val bengaliLabel: String) {
    OFF("Off", "বন্ধ"),
    PARKING("Park Light", "পার্কিং লাইট"),
    LOW_BEAM("Low Beam", "লো বিম"),
    HIGH_BEAM("High Beam", "হাই বিম")
}

enum class WiperMode(val label: String, val bengaliLabel: String) {
    OFF("Off", "বন্ধ"),
    SLOW("Slow", "ধীর"),
    FAST("Fast", "দ্রুত")
}

enum class BatteryType(val label: String, val bengaliLabel: String) {
    LIFEPO4_LITHIUM("Lithium LiFePO4", "লিথিয়াম LiFePO4 স্মার্ট প্যাক"),
    LEAD_ACID_TUBULAR("Lead-Acid Tubular", "লেড-অ্যাসিড (৪/৫ ব্যাটারি সেট)")
}

enum class BatteryPackVoltage(val title: String, val nominalVolts: Int, val maxVolts: Float, val cutoffVolts: Float) {
    VOLT_48("48V (4-Pack)", 48, 54.6f, 42.0f),
    VOLT_60("60V (5-Pack)", 60, 68.2f, 52.5f)
}

enum class ConnectionStatus(val label: String, val bengaliLabel: String) {
    CONNECTED("ECU Connected (BLE)", "ইসিইউ সংযুক্ত (Bluetooth)"),
    CONNECTING("Searching Controller...", "ইসিইউ সন্ধান চলছে..."),
    DISCONNECTED("Offline / Disconnected", "ইসিইউ বিচ্ছিন্ন")
}

enum class ChargeStatus(val label: String, val bengaliLabel: String) {
    NOT_CHARGING("Discharging", "ব্যাটারি ড্রাইভ মোড"),
    AC_NORMAL_CHARGING("AC Home Charger (15A)", "হোম চার্জার যুক্ত (১৫A)"),
    FAST_DC_CHARGING("Fast DC Station (30A)", "ফাস্ট চার্জিং চালু (৩০A)")
}

enum class TotoTab(val label: String, val bengaliLabel: String) {
    COCKPIT("Cockpit", "ককপিট"),
    SWITCHES("Switches", "সুইচবোর্ড"),
    BATTERY("BMS & Battery", "ব্যাটারি ও BMS"),
    FARE_METER("Fare Meter", "ভাড়া মিটার"),
    SETTINGS("Settings & ECU", "সেটিংস ও ডায়াগনস্টিক")
}
