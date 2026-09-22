package com.example.model

data class BloodDonor(
  val id: String,
  val name: String,
  val designation: String,
  val department: String,
  val phone: String,
  val bloodGroup: String,
  val empCode: String = "",
  val timestamp: String = "",
)

data class RouteStop(
  val name: String,
  val time: String = "",
  val morning: String = "",
  val evening: String = "",
  val night: String = "",
)

data class TransportRoute(
  val id: Int,
  val title: String,
  val type: String,
  val driver: String,
  val contact: String,
  val vehicle: String,
  val shiftOut: String = "",
  val isMultiShift: Boolean = false,
  val stops: List<RouteStop> = emptyList(),
)

data class MessMenuItem(
  val date: String,
  val day: String,
  val main: String,
  val side: String,
  val sweet: String = "",
)

data class MarketItem(
  val id: String,
  val title: String,
  val price: String,
  val category: String,
  val sellerName: String,
  val phone: String,
  val imageUrl: String = "",
  val timestamp: Long = System.currentTimeMillis(),
  val isMine: Boolean = false,
)

data class ChatMessage(
  val id: String,
  val senderName: String,
  val message: String,
  val time: String,
  val isMe: Boolean,
  val isDeleted: Boolean = false,
)

data class PodcastItem(
  val id: String,
  val title: String,
  val speaker: String,
  val channelCode: String,
  val duration: String,
  val youtubeId: String,
)

data class EBookItem(
  val id: String,
  val title: String,
  val category: String,
  val previewUrl: String,
)

data class AudioTrackItem(
  val id: String,
  val title: String,
  val author: String,
  val duration: String,
  val spotifyUrl: String,
)

data class MediaEvent(
  val id: String,
  val title: String,
  val tag: String,
  val desc: String,
  val photoCount: Int,
)

data class PolicySection(
  val heading: String,
  val points: List<String>,
)

data class CompanyPolicy(
  val id: String,
  val title: String,
  val subtitle: String,
  val effectiveDate: String,
  val objective: String,
  val scope: String,
  val sections: List<PolicySection>,
)
