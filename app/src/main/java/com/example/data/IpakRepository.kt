package com.example.data

import com.example.model.AudioTrackItem
import com.example.model.BloodDonor
import com.example.model.ChatMessage
import com.example.model.CompanyPolicy
import com.example.model.EBookItem
import com.example.model.MarketItem
import com.example.model.MediaEvent
import com.example.model.MessMenuItem
import com.example.model.PodcastItem
import com.example.model.PolicySection
import com.example.model.RouteStop
import com.example.model.TransportRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object IpakRepository {

  private const val BLOOD_SHEET_CSV_URL =
    "https://docs.google.com/spreadsheets/d/e/2PACX-1vTrNQpq641epv1fwbFpW21_1MyJdfrnJS6NUk0yA_VyKeNSPtE7IZ0NMbNAL8JJ02oOzvSbZxatqc5o/pub?gid=15022141&single=true&output=csv"

  private val _bloodDonors = MutableStateFlow<List<BloodDonor>>(getInitialBloodDonors())
  val bloodDonors: StateFlow<List<BloodDonor>> = _bloodDonors.asStateFlow()

  private val _isBloodLoading = MutableStateFlow(false)
  val isBloodLoading: StateFlow<Boolean> = _isBloodLoading.asStateFlow()

  private val _marketplaceItems = MutableStateFlow<List<MarketItem>>(getInitialMarketItems())
  val marketplaceItems: StateFlow<List<MarketItem>> = _marketplaceItems.asStateFlow()

  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(getInitialChatMessages())
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  suspend fun refreshBloodDonorsFromSheet() {
    withContext(Dispatchers.IO) {
      try {
        _isBloodLoading.value = true
        val url = URL(BLOOD_SHEET_CSV_URL)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 8000
        conn.readTimeout = 8000
        conn.requestMethod = "GET"

        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
          val reader = BufferedReader(InputStreamReader(conn.inputStream))
          val lines = reader.readLines()
          reader.close()

          if (lines.size > 1) {
            val parsedList = mutableListOf<BloodDonor>()
            // Skip header line
            for (i in 1 until lines.size) {
              val line = lines[i].trim()
              if (line.isEmpty()) continue
              val tokens = parseCsvLine(line)
              if (tokens.size >= 5) {
                val timestamp = tokens.getOrNull(0) ?: ""
                val name = tokens.getOrNull(1) ?: ""
                val designation = tokens.getOrNull(2) ?: ""
                val department = tokens.getOrNull(3) ?: ""
                val phone = tokens.getOrNull(4) ?: ""
                val bloodGroup = tokens.getOrNull(5) ?: ""
                val empCode = tokens.getOrNull(6) ?: ""

                if (name.isNotBlank() && bloodGroup.isNotBlank()) {
                  parsedList.add(
                    BloodDonor(
                      id = "sheet_$i",
                      name = name.trim(),
                      designation = designation.trim(),
                      department = department.trim(),
                      phone = phone.trim(),
                      bloodGroup = bloodGroup.trim().uppercase(),
                      empCode = empCode.trim(),
                      timestamp = timestamp.trim(),
                    )
                  )
                }
              }
            }
            if (parsedList.isNotEmpty()) {
              _bloodDonors.value = parsedList
            }
          }
        }
      } catch (e: Exception) {
        // Fallback to pre-loaded official directory
      } finally {
        _isBloodLoading.value = false
      }
    }
  }

  private fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val sb = java.lang.StringBuilder()
    var inQuotes = false
    for (char in line) {
      if (char == '\"') {
        inQuotes = !inQuotes
      } else if (char == ',' && !inQuotes) {
        result.add(sb.toString())
        sb.setLength(0)
      } else {
        sb.append(char)
      }
    }
    result.add(sb.toString())
    return result
  }

  fun registerBloodDonor(donor: BloodDonor) {
    val current = _bloodDonors.value.toMutableList()
    current.add(0, donor)
    _bloodDonors.value = current
  }

  fun addMarketItem(item: MarketItem) {
    val current = _marketplaceItems.value.toMutableList()
    current.add(0, item)
    _marketplaceItems.value = current
  }

  fun sendChatMessage(text: String, sender: String = "You") {
    if (text.isBlank()) return
    val current = _chatMessages.value.toMutableList()
    val now = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
    current.add(
      ChatMessage(
        id = System.currentTimeMillis().toString(),
        senderName = sender,
        message = text,
        time = now,
        isMe = true,
      )
    )
    _chatMessages.value = current
  }

  fun deleteChatMessage(id: String) {
    val current = _chatMessages.value.map {
      if (it.id == id) it.copy(isDeleted = true, message = "This message was deleted") else it
    }
    _chatMessages.value = current
  }

  // Official Transport Routes from IPAK Portal
  fun getTransportRoutes(): List<TransportRoute> {
    return listOf(
      TransportRoute(
        id = 1,
        title = "Route 1 - Raiwind",
        type = "Bus (Coaster)",
        driver = "Amjad Khan",
        contact = "0300-8451203",
        vehicle = "LEG-1428",
        shiftOut = "05:15 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("Raiwind Railway Station", "06:40 AM"),
          RouteStop("Paji Morr", "06:55 AM"),
          RouteStop("Kahna Nau", "07:15 AM"),
          RouteStop("Kot Lakhpat", "07:35 AM"),
          RouteStop("IPAK Plant 1 & 2", "07:55 AM"),
        ),
      ),
      TransportRoute(
        id = 2,
        title = "Route 2 - Gajjumata / Ferozepur",
        type = "Coaster",
        driver = "Muhammad Tariq",
        contact = "0321-4478129",
        vehicle = "LEB-9011",
        shiftOut = "05:15 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("Gajjumata Metro Station", "06:45 AM"),
          RouteStop("Chungi Amar Sidhu", "07:05 AM"),
          RouteStop("Model Town Link Road", "07:25 AM"),
          RouteStop("Qayyum Park", "07:40 AM"),
          RouteStop("IPAK Complex", "07:55 AM"),
        ),
      ),
      TransportRoute(
        id = 3,
        title = "Route 3 - Sky Land / Canal Road",
        type = "Hiace Van",
        driver = "Rashid Mehmood",
        contact = "0333-4921045",
        vehicle = "LES-3420",
        shiftOut = "05:15 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("Sky Land Water Park", "06:30 AM"),
          RouteStop("Thokar Niaz Baig", "06:50 AM"),
          RouteStop("Canal View", "07:10 AM"),
          RouteStop("Bahria Town Gate", "07:30 AM"),
          RouteStop("IPAK Main Gate", "07:55 AM"),
        ),
      ),
      TransportRoute(
        id = 4,
        title = "Route 4 - Sheikhupura (SKP) General",
        type = "Coaster (General Shift)",
        driver = "Ghulam Abbas",
        contact = "0302-7714209",
        vehicle = "LER-7745",
        shiftOut = "05:15 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("SKP By-Pass Chowk", "06:15 AM"),
          RouteStop("Bhikhi Road", "06:35 AM"),
          RouteStop("Kot Abdul Malik", "07:10 AM"),
          RouteStop("Shahdara Morr", "07:30 AM"),
          RouteStop("IPAK Factory", "07:55 AM"),
        ),
      ),
      TransportRoute(
        id = 5,
        title = "Route 5 - SP Chowk Multi-Shift",
        type = "Bus (3-Shift Operations)",
        driver = "Zulfiqar Ali",
        contact = "0345-6612098",
        vehicle = "LEA-5510",
        shiftOut = "Continuous",
        isMultiShift = true,
        stops = listOf(
          RouteStop("SP Chowk Sheikhupura", morning = "06:30 AM", evening = "02:30 PM", night = "10:30 PM"),
          RouteStop("Chowk Pir Bahar Shah", morning = "06:45 AM", evening = "02:45 PM", night = "10:45 PM"),
          RouteStop("Batti Chowk", morning = "07:05 AM", evening = "03:05 PM", night = "11:05 PM"),
          RouteStop("IPAK Plant Sites", morning = "07:45 AM", evening = "03:45 PM", night = "11:45 PM"),
        ),
      ),
      TransportRoute(
        id = 6,
        title = "Route 6 - Batti Chowk Shift Route",
        type = "Coaster (Rotational)",
        driver = "Nadeem Ashraf",
        contact = "0301-4431980",
        vehicle = "LEC-8822",
        shiftOut = "Continuous",
        isMultiShift = true,
        stops = listOf(
          RouteStop("Batti Chowk Terminal", morning = "06:40 AM", evening = "02:40 PM", night = "10:40 PM"),
          RouteStop("Ravi Bridge", morning = "07:00 AM", evening = "03:00 PM", night = "11:00 PM"),
          RouteStop("IPAK Gates", morning = "07:45 AM", evening = "03:45 PM", night = "11:45 PM"),
        ),
      ),
      TransportRoute(
        id = 7,
        title = "Route 7 - Ali Town (Orange Line)",
        type = "Van",
        driver = "Kashif Butt",
        contact = "0322-9014522",
        vehicle = "LEN-1209",
        shiftOut = "05:15 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("Ali Town Station", "06:50 AM"),
          RouteStop("Chung Multan Road", "07:10 AM"),
          RouteStop("Maraka Quaid-e-Azam", "07:30 AM"),
          RouteStop("IPAK HQ", "07:55 AM"),
        ),
      ),
      TransportRoute(
        id = 8,
        title = "Route 8 - Harbanspura / Ring Road",
        type = "Hiace Van",
        driver = "Sajid Hussain",
        contact = "0306-7821034",
        vehicle = "LEF-4431",
        shiftOut = "05:15 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("Harbanspura Interchange", "06:30 AM"),
          RouteStop("DHA Phase 5 Ring Road", "06:50 AM"),
          RouteStop("Sui Gas Society", "07:15 AM"),
          RouteStop("IPAK Main Campus", "07:55 AM"),
        ),
      ),
      TransportRoute(
        id = 9,
        title = "Route 9 - HRSG Staff Express",
        type = "Contract Carrier",
        driver = "Babar Saeed",
        contact = "0312-3321908",
        vehicle = "LEG-9912",
        shiftOut = "05:30 PM",
        isMultiShift = false,
        stops = listOf(
          RouteStop("Kalma Chowk", "06:45 AM"),
          RouteStop("Barkat Market", "07:00 AM"),
          RouteStop("Peco Road", "07:15 AM"),
          RouteStop("IPAK Facilities", "07:55 AM"),
        ),
      ),
    )
  }

  // Daily Mess Menu from IPAK Admin
  fun getMessMenu(): List<MessMenuItem> {
    return listOf(
      MessMenuItem("Apr 01", "Tuesday", "Chicken Karahi with Tandoori Naan", "Fresh Mint Raita & Green Salad", "Kheer"),
      MessMenuItem("Apr 02", "Wednesday", "Daal Chawal (Special Tarka) & Shami Kabab", "Mixed Pickles & Onion Salad", "Fruit Custard"),
      MessMenuItem("Apr 03", "Thursday", "Aloo Gosht Gravy with Roghani Roti", "Kachumber Salad", "Gulab Jamun"),
      MessMenuItem("Apr 04", "Friday", "Special Chicken Biryani with Boiled Egg", "Zeera Raita & Chutney", "Zarda"),
      MessMenuItem("Apr 05", "Saturday", "Sabzi Mix (Seasonal) with Chapati", "Dahi Bhalla & Chutney", ""),
      MessMenuItem("Apr 07", "Monday", "Chicken Qorma with Fresh Roti", "Fresh Salad Platter", "Halwa"),
      MessMenuItem("Apr 08", "Tuesday", "Daal Mash Fried with Mint Dip", "Green Chili Onion Salad", "Kheer"),
      MessMenuItem("Apr 09", "Wednesday", "Chicken Nihari with Ginger Garnish", "Lemon & Green Chilies", "Fruit Trifle"),
      MessMenuItem("Apr 10", "Thursday", "Kadhi Pakora with Steamed White Rice", "Achar & Green Salad", "Sweet Jalebi"),
      MessMenuItem("Apr 11", "Friday", "Sindhi Beef Biryani Special", "Raita & Fresh Mint Dip", "Firni"),
      MessMenuItem("Apr 12", "Saturday", "Aloo Palak / Mixed Veg with Roti", "Cucumber Raita", ""),
      MessMenuItem("Apr 14", "Monday", "Chicken Pulao with Shami Tikki", "Mint Raita & Onion Salad", "Custard"),
      MessMenuItem("Apr 15", "Tuesday", "Daal Chana with Special Zeera Tadka", "Spicy Pickles & Salad", "Suji Halwa"),
      MessMenuItem("Apr 16", "Wednesday", "Chicken Manchurian with Egg Fried Rice", "Kole Slaw Salad", "Ice Cream Cup"),
      MessMenuItem("Apr 17", "Thursday", "Mutton Mix / Chicken Salan with Roti", "Fresh Green Salad", "Gulab Jamun"),
      MessMenuItem("Apr 18", "Friday", "Special Hyderabadi Chicken Biryani", "Podina Raita", "Zarda Special"),
    )
  }

  // Initial Preloaded Blood Donors
  private fun getInitialBloodDonors(): List<BloodDonor> {
    return listOf(
      BloodDonor("1", "Muhammad Rizwan", "Assistant Manager", "Supply Chain", "0300-8451290", "A+", "EMP-1042"),
      BloodDonor("2", "Hamza Farooq", "HR Officer", "Human Resources", "0322-2818107", "O+", "EMP-1108"),
      BloodDonor("3", "Ali Raza", "Quality Assurance Lead", "QA/QC", "0333-4129856", "B+", "EMP-0982"),
      BloodDonor("4", "Usman Tariq", "Shift Engineer", "Production Line 1", "0321-7789012", "AB+", "EMP-1405"),
      BloodDonor("5", "Zainab Bibi", "Accounts Executive", "Finance", "0345-9923187", "O-", "EMP-1220"),
      BloodDonor("6", "Bilal Ahmed", "Electrical Engineer", "Maintenance", "0301-6654312", "A-", "EMP-1311"),
      BloodDonor("7", "Kashif Mehmood", "Safety Officer", "HSE", "0312-8871234", "B-", "EMP-1087"),
      BloodDonor("8", "Farhan Saeed", "Senior Chemist", "R&D Lab", "0302-5544123", "AB-", "EMP-0876"),
      BloodDonor("9", "Shahid Nadeem", "Logistics Coordinator", "Warehouse", "0323-4411902", "O+", "EMP-1199"),
      BloodDonor("10", "Asad Ullah", "Machine Operator", "Extrusion Plant", "0304-7766554", "A+", "EMP-1502"),
      BloodDonor("11", "Waqas Akram", "IT Specialist", "Information Technology", "0334-1234567", "B+", "EMP-1014"),
      BloodDonor("12", "Taimoor Hassan", "Packaging Specialist", "Converting", "0313-9876543", "O+", "EMP-1380"),
    )
  }

  // Initial Marketplace Items
  private fun getInitialMarketItems(): List<MarketItem> {
    return listOf(
      MarketItem("1", "Honda Civic Oriel 2018 - Spotless Condition", "PKR 5,450,000", "Vehicles", "Imran Butt (Finance)", "0300-8412980"),
      MarketItem("2", "Dell Latitude i7 16GB RAM Laptop", "PKR 85,000", "Electronics", "Fahad Khan (IT)", "0321-4478120"),
      MarketItem("3", "Solid Sheesham Wood Office / Study Desk", "PKR 22,000", "Furniture", "Sarmad Ali (SCM)", "0333-6671245"),
      MarketItem("4", "Apple iPhone 13 128GB Factory Unlocked", "PKR 145,000", "Electronics", "Hamza (HR)", "0322-2818107"),
      MarketItem("5", "Yamaha YBR 128G Black 2022", "PKR 310,000", "Vehicles", "Zahid (QA)", "0345-8812903"),
      MarketItem("6", "Executive Leather Swivel Chair", "PKR 16,500", "Furniture", "Naveed (Admin)", "0302-9988112"),
    )
  }

  // Initial Chat Messages
  private fun getInitialChatMessages(): List<ChatMessage> {
    return listOf(
      ChatMessage("1", "Hamza Farooq", "Welcome to the new mobile IPAK Portal! All employee services are now at your fingertips.", "09:00 AM", false),
      ChatMessage("2", "Rizwan Ahmed", "The Blood Bank directory is super fast. Thanks to the HR and IT team!", "09:15 AM", false),
      ChatMessage("3", "Admin Desk", "Reminder: Mess menu for April 2026 is updated under Admin Support. Transport route timings are active.", "10:30 AM", false),
    )
  }

  // Podcasts and Video Talks
  fun getPodcasts(): List<PodcastItem> {
    return listOf(
      PodcastItem("1", "IPAK Leadership Insights: Vision & Growth", "CEO & Leadership Panel", "CH-IPAK", "24:18", "dQw4w9WgXcQ"),
      PodcastItem("2", "Building a Safety First Culture in Packaging", "Head of HSE", "CH-IPAK", "18:45", "dQw4w9WgXcQ"),
      PodcastItem("3", "Lean Manufacturing & Operational Excellence", "VP Operations", "CH-IPAK", "31:10", "dQw4w9WgXcQ"),
      PodcastItem("4", "Mental Wellness & Work Life Harmony", "Dr. Sehat Kahani Panel", "CH-MED", "22:05", "dQw4w9WgXcQ"),
    )
  }

  // Audio Tracks and Audiobooks
  fun getAudioTracks(): List<AudioTrackItem> {
    return listOf(
      AudioTrackItem("1", "Think and Grow Rich", "Napoleon Hill", "45:00", "https://open.spotify.com"),
      AudioTrackItem("2", "Rich Dad Poor Dad", "Robert Kiyosaki", "52:14", "https://open.spotify.com"),
      AudioTrackItem("3", "The Power of Your Subconscious Mind", "Dr. Joseph Murphy", "38:40", "https://open.spotify.com"),
      AudioTrackItem("4", "Deep Focus & Ambient Workplace Sounds", "IPAK Focus Soundscapes", "60:00", "https://open.spotify.com"),
    )
  }

  // E-Books
  fun getEBooks(): List<EBookItem> {
    return listOf(
      EBookItem("1", "The 7 Habits of Highly Effective People", "Leadership & Self Development", "https://example.com"),
      EBookItem("2", "Good to Great: Why Some Companies Make the Leap", "Corporate Strategy", "https://example.com"),
      EBookItem("3", "Zero to One: Notes on Startups", "Innovation", "https://example.com"),
      EBookItem("4", "Lean Production System Field Guide", "Operations & Engineering", "https://example.com"),
      EBookItem("5", "IPAK Employee Handbook 2025-2026", "Corporate Policies", "https://example.com"),
    )
  }

  // Media Gallery Events
  fun getMediaEvents(): List<MediaEvent> {
    return listOf(
      MediaEvent("1", "Annual Ramadan Iftaar Dinner 2025", "Corporate", "Annual company gathering celebrating the holy month with employee recognition.", 28),
      MediaEvent("2", "Independence Day Celebration 14th August", "National", "Flag hoisting ceremony, cultural presentations, and green tribute tree planting.", 34),
      MediaEvent("3", "Smash Champs Inter-Departmental Sports Gala", "Sports", "Cricket tournament, badminton championship, and table tennis league at IPAK sports ground.", 52),
      MediaEvent("4", "Annual Excellence Awards & Gala", "Recognition", "Honoring top performers, safety champions, and 10+ year service milestones.", 41),
    )
  }

  // Company Policies
  fun getPolicies(): List<CompanyPolicy> {
    return listOf(
      CompanyPolicy(
        id = "grading",
        title = "Grading Structure & Career Bands",
        subtitle = "Organizational framework & career progression bands",
        effectiveDate = "January 2025",
        objective = "To provide a transparent, merit-oriented hierarchy that defines operational, technical, managerial, and executive responsibilities across all business units.",
        scope = "Applies to all permanent, contractual, and technical employees across IPAK, CPAK, PETPAK, and GPAK facilities.",
        sections = listOf(
          PolicySection(
            heading = "Executive & Senior Leadership (E1 - E3)",
            points = listOf(
              "E3: Executive Director / Group Chief Officer",
              "E2: General Manager / Head of Department",
              "E1: Deputy General Manager / Lead Functional Strategist",
            ),
          ),
          PolicySection(
            heading = "Management Cadre (M1 - M4)",
            points = listOf(
              "M4: Senior Manager - Strategic operations, capital budget ownership",
              "M3: Manager - Departmental execution, cross-functional alignment",
              "M2: Deputy Manager - Team leadership, sprint and KPI delivery",
              "M1: Assistant Manager - Unit supervision, quality standards control",
            ),
          ),
          PolicySection(
            heading = "Officer & Professional Band (O1 - O3)",
            points = listOf(
              "O3: Senior Officer / Senior Engineer - Advanced technical execution",
              "O2: Officer / Engineer - Day-to-day functional responsibility",
              "O1: Junior Officer / Trainee Engineer - Onboarding & associate support",
            ),
          ),
          PolicySection(
            heading = "Technical & Operational Cadre (T1 - T4)",
            points = listOf(
              "T4: Master Technician / Senior Shift Supervisor",
              "T3: Plant Technician / Line Operator (Extrusion / Slitting / Metallizing)",
              "T2: Associate Operator / Maintenance Specialist",
              "T1: Plant Apprentice / Operational Trainee",
            ),
          ),
        ),
      ),
      CompanyPolicy(
        id = "drug",
        title = "Drug & Substance Abuse Policy",
        subtitle = "Zero-tolerance for narcotics, prohibited substances & workplace impairment",
        effectiveDate = "Strict & Ongoing",
        objective = "To ensure a safe, healthy, and hazard-free environment for all employees, visitors, contractors, and machinery operators.",
        scope = "Applies 24/7 across all corporate offices, factory premises, company transport vehicles, and affiliated events.",
        sections = listOf(
          PolicySection(
            heading = "Prohibited Conduct",
            points = listOf(
              "Possession, consumption, purchase, sale, distribution, or manufacturing of any illegal narcotics or unauthorized prescription drugs.",
              "Reporting to duty or operating any vehicle, heavy machinery, or production equipment under the influence of any intoxicant.",
              "Smoking or vaping in non-designated restricted production, warehouse, and solvent storage zones.",
            ),
          ),
          PolicySection(
            heading = "Compliance & Testing",
            points = listOf(
              "Periodic random screening as well as post-incident safety investigations.",
              "Mandatory pre-employment medical checks for high-hazard technical and machinery roles.",
              "Confidential Employee Assistance Program (EAP) available for voluntary rehabilitation before violations occur.",
            ),
          ),
          PolicySection(
            heading = "Disciplinary Consequences",
            points = listOf(
              "Violation is categorized as Gross Misconduct, resulting in immediate suspension pending inquiry, followed by termination of employment without notice.",
            ),
          ),
        ),
      ),
      CompanyPolicy(
        id = "conduct",
        title = "Code of Conduct & Disciplinary Action",
        subtitle = "Ethics, integrity, respectful workplace, and disciplinary procedure",
        effectiveDate = "Updated 2025",
        objective = "To establish ethical standards, protect intellectual property, prevent harassment, and provide a fair disciplinary inquiry process.",
        scope = "All employees, vendors, and representatives across all locations.",
        sections = listOf(
          PolicySection(
            heading = "Core Principles",
            points = listOf(
              "Mutual Respect: Zero tolerance for harassment, discrimination, or abusive behavior based on gender, religion, ethnicity, or background.",
              "Integrity & Anti-Bribery: No acceptance of gifts, kickbacks, or personal favors from suppliers, distributors, or logistics contractors.",
              "Data & Trade Secrets: Stringent protection of polymer formulations, customer lists, pricing matrices, and technical specifications.",
            ),
          ),
          PolicySection(
            heading = "Disciplinary Process",
            points = listOf(
              "Step 1: Verbal counseling & documented reminder.",
              "Step 2: Formal Written Warning issued by HR.",
              "Step 3: Show-cause notice with 48 hours for employee response.",
              "Step 4: Independent Inquiry Committee hearing adhering to legal due process.",
            ),
          ),
        ),
      ),
      CompanyPolicy(
        id = "dress",
        title = "Dress Code & Personal Protective Equipment",
        subtitle = "Professional office attire and mandatory plant floor PPE",
        effectiveDate = "Active",
        objective = "To uphold corporate professionalism in offices while guaranteeing strict safety and contamination prevention on plant floors.",
        scope = "All corporate, administrative, and plant personnel.",
        sections = listOf(
          PolicySection(
            heading = "Corporate & Administrative Offices",
            points = listOf(
              "Monday to Thursday: Business casual or formal corporate attire (Collared shirts, formal trousers, Shalwar Kameez with Waistcoat).",
              "Friday: Smart cultural or casual attire in accordance with cultural decorum.",
              "Formal leather or clean closed-toe footwear required at all times.",
            ),
          ),
          PolicySection(
            heading = "Factory, Warehouse & Technical Areas",
            points = listOf(
              "Mandatory PPE: Steel-toe safety shoes, high-visibility reflective vests, hairnets in converting areas, and ear protection in high-decibel zones.",
              "Strictly prohibited: Loose ties, flowing dupattas near rotating shafts/extruders, open sandals, or jewelry in clean rooms.",
            ),
          ),
        ),
      ),
    )
  }
}
