package com.example.localization

object TotoLocale {

    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "টোটো কন্ট্রোলার"
        AppLanguage.ENGLISH -> "Toto Commander"
        AppLanguage.HINDI -> "टोटो कंट्रोलर"
        AppLanguage.URDU -> "ٹوٹو کنٹرولر"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "৩ চাকা ই-রিকশা সম্পূর্ণ মোবাইল অপারেটিং সিস্টেম"
        AppLanguage.ENGLISH -> "Full Commercial 3-Wheeler E-Rickshaw Control OS"
        AppLanguage.HINDI -> "३ पहिया ई-रिक्शा फुल मोबाइल ऑपरेटिंग सिस्टम"
        AppLanguage.URDU -> "تین پہیہ ای رکشہ مکمل موبائل کنٹرول"
    }

    // Tabs
    fun tabCockpit(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ককপিট"
        AppLanguage.ENGLISH -> "Cockpit"
        AppLanguage.HINDI -> "कॉकपिट"
        AppLanguage.URDU -> "کاکپٹ"
    }

    fun tabSwitches(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "সুইচবোর্ড"
        AppLanguage.ENGLISH -> "Switches"
        AppLanguage.HINDI -> "स्विचबोर्ड"
        AppLanguage.URDU -> "سوئچ بورڈ"
    }

    fun tabBattery(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ব্যাটারি ও BMS"
        AppLanguage.ENGLISH -> "Battery BMS"
        AppLanguage.HINDI -> "बैटरी व BMS"
        AppLanguage.URDU -> "بیٹری بی ایم ایس"
    }

    fun tabFareMeter(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ভাড়া মিটার"
        AppLanguage.ENGLISH -> "Fare Meter"
        AppLanguage.HINDI -> "किराया मीटर"
        AppLanguage.URDU -> "کرایہ میٹر"
    }

    fun tabSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "সেটিংস ও ECU"
        AppLanguage.ENGLISH -> "Settings & ECU"
        AppLanguage.HINDI -> "सेटिंग्स व ECU"
        AppLanguage.URDU -> "سیٹنگز"
    }

    // Ignition & Power
    fun ignitionOn(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "পাওয়ার চালু"
        AppLanguage.ENGLISH -> "POWER ON"
        AppLanguage.HINDI -> "पावर ऑन"
        AppLanguage.URDU -> "پاور آن"
    }

    fun ignitionOff(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "পাওয়ার বন্ধ"
        AppLanguage.ENGLISH -> "POWER OFF"
        AppLanguage.HINDI -> "पावर ऑफ"
        AppLanguage.URDU -> "پاور آف"
    }

    fun motorLocked(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "মোটর লকড (চুরি প্রতিরোধ)"
        AppLanguage.ENGLISH -> "Motor Anti-Theft Lock Active"
        AppLanguage.HINDI -> "मोटर लॉक (एंटी थेफ्ट ऑन)"
        AppLanguage.URDU -> "موٹر لاک ہے"
    }

    fun motorUnlocked(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "মোটর আনলকড (রেডি টু ড্রাইভ)"
        AppLanguage.ENGLISH -> "Motor Unlocked (Ready to Drive)"
        AppLanguage.HINDI -> "मोटर अनलॉक (ड्राइव हेतु तैयार)"
        AppLanguage.URDU -> "موٹر تیار ہے"
    }

    // Horn & Alarm
    fun hornButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ইলেকট্রিক হর্ন"
        AppLanguage.ENGLISH -> "ELECTRIC HORN"
        AppLanguage.HINDI -> "इलेक्ट्रिक हॉर्न"
        AppLanguage.URDU -> "الیکٹرک ہارن"
    }

    fun alarmSiren(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "সাইরেন অ্যালার্ম"
        AppLanguage.ENGLISH -> "Siren Alarm"
        AppLanguage.HINDI -> "सायरन अलार्म"
        AppLanguage.URDU -> "سائرن الارم"
    }

    fun emergencySos(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "জরুরি SOS"
        AppLanguage.ENGLISH -> "EMERGENCY SOS"
        AppLanguage.HINDI -> "इमरजेंसी SOS"
        AppLanguage.URDU -> "ہنگامی مدد"
    }

    // Inching crawler
    fun crawlerForward(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "স্লো ক্রল (সামনে)"
        AppLanguage.ENGLISH -> "Crawl Forward"
        AppLanguage.HINDI -> "धीमे आगे (क्रॉल)"
        AppLanguage.URDU -> "آہستہ آگے"
    }

    fun crawlerReverse(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "স্লো ক্রল (পিছনে)"
        AppLanguage.ENGLISH -> "Crawl Reverse"
        AppLanguage.HINDI -> "धीमे पीछे (क्रॉल)"
        AppLanguage.URDU -> "آہستہ پیچھے"
    }

    // Lighting
    fun headlight(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "সামনের হেডলাইট"
        AppLanguage.ENGLISH -> "Headlight"
        AppLanguage.HINDI -> "हेडलाइट"
        AppLanguage.URDU -> "ہیڈ لائٹ"
    }

    fun cabinLight(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "কেবিন যাত্রী লাইট"
        AppLanguage.ENGLISH -> "Passenger Cabin Light"
        AppLanguage.HINDI -> "केबिन पैसेंजर लाइट"
        AppLanguage.URDU -> "مسافر لائٹ"
    }

    fun roofAmbient(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ছাদ ডেকোরেশন LED"
        AppLanguage.ENGLISH -> "Roof Ambient LED"
        AppLanguage.HINDI -> "रूफ एम्बिएंट स्ट्रिप"
        AppLanguage.URDU -> "روف ایل ای ڈی"
    }

    fun fogLamps(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "কুয়াশা ফগ লাইট"
        AppLanguage.ENGLISH -> "Fog Lamps"
        AppLanguage.HINDI -> "फॉग लैम्प"
        AppLanguage.URDU -> "فوگ لائٹ"
    }

    fun hazardFlasher(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "হ্যাজার্ড ৪-ওয়ে ফ্লাশার"
        AppLanguage.ENGLISH -> "4-Way Hazard Flasher"
        AppLanguage.HINDI -> "हैज़र्ड 4-वे लाइट"
        AppLanguage.URDU -> "ہیزرڈ لائٹس"
    }

    fun wiper(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "উইন্ডশিল্ড ওয়াইপার"
        AppLanguage.ENGLISH -> "Windshield Wiper"
        AppLanguage.HINDI -> "वाइपर"
        AppLanguage.URDU -> "وائپر"
    }

    fun usbCharger(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "যাত্রী মোবাইল চার্জার (USB)"
        AppLanguage.ENGLISH -> "Passenger USB Charger Relay"
        AppLanguage.HINDI -> "पैसेंजर मोबाइल चार्जर"
        AppLanguage.URDU -> "موبائل چارجر"
    }

    fun locateVehicle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "টোটো খুঁজুন (বীপ ও ফ্লাশ)"
        AppLanguage.ENGLISH -> "Locate Toto (Beep & Flash)"
        AppLanguage.HINDI -> "टोटो खोजें (बीप व लाइट)"
        AppLanguage.URDU -> "ٹوٹو تلاش کریں"
    }

    // Battery & BMS
    fun batteryPackTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "বিএমএস ব্যাটারি স্বাস্থ্য ও ভোল্টেজ"
        AppLanguage.ENGLISH -> "BMS Telemetry & Battery Pack"
        AppLanguage.HINDI -> "बैटरी मैनेजमेंट सिस्टम (BMS)"
        AppLanguage.URDU -> "بیٹری مینجمنٹ سسٹم"
    }

    fun remainingRange(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "অবশিষ্ট রেঞ্জ"
        AppLanguage.ENGLISH -> "Estimated Range"
        AppLanguage.HINDI -> "अनुमानित रेंज"
        AppLanguage.URDU -> "باقی فاصلہ"
    }

    fun healthStatus(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ব্যাটারি স্বাস্থ্য (SoH)"
        AppLanguage.ENGLISH -> "Battery Health (SoH)"
        AppLanguage.HINDI -> "बैटरी हेल्थ"
        AppLanguage.URDU -> "بیٹری صحت"
    }

    fun cellBalancing(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ব্যালান্সড সেল ভোল্টেজ (১৬S)"
        AppLanguage.ENGLISH -> "16S Cell Balancing Voltage"
        AppLanguage.HINDI -> "16S सेल संतुलन वोल्ट"
        AppLanguage.URDU -> "سیل بیلنسنگ"
    }

    // Commercial Fare Meter
    fun startRide(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ট্রিপ শুরু করুন"
        AppLanguage.ENGLISH -> "Start Passenger Trip"
        AppLanguage.HINDI -> "ट्रिप शुरू करें"
        AppLanguage.URDU -> "ٹرپ شروع کریں"
    }

    fun stopRide(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "ট্রিপ সমাপ্ত ও ভাড়া নিন"
        AppLanguage.ENGLISH -> "End Trip & Collect Fare"
        AppLanguage.HINDI -> "ट्रिप समाप्त करें"
        AppLanguage.URDU -> "ٹرپ ختم کریں"
    }

    fun todaysEarnings(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "আজকের মোট ইনকাম"
        AppLanguage.ENGLISH -> "Today's Total Earnings"
        AppLanguage.HINDI -> "आज की कुल कमाई"
        AppLanguage.URDU -> "آج کی کمائی"
    }

    fun completedTrips(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "সম্পন্ন ট্রিপ"
        AppLanguage.ENGLISH -> "Completed Trips"
        AppLanguage.HINDI -> "पूरे हुए ट्रिप"
        AppLanguage.URDU -> "مکمل ٹرپ"
    }

    fun passengerCountLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.BENGALI -> "যাত্রী সংখ্যা (১-৬ জন)"
        AppLanguage.ENGLISH -> "Passenger Count (1-6)"
        AppLanguage.HINDI -> "सवारी संख्या (1-6)"
        AppLanguage.URDU -> "سواریوں کی تعداد"
    }

    val speedUnit: String = "km/h"
    val rupeeSymbol: String = "₹"
}
