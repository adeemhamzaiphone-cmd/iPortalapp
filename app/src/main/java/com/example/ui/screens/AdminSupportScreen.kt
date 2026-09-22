package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IpakRepository
import com.example.model.MessMenuItem
import com.example.model.TransportRoute
import com.example.ui.components.ExternalServiceDialog
import com.example.ui.components.dialPhone
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakBorderLight
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakLimeDark
import com.example.ui.theme.IpakLimeLight
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary

@Composable
fun AdminSupportScreen(
  modifier: Modifier = Modifier,
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Transport", "Mess Menu", "Feedback")

  val routes = remember { IpakRepository.getTransportRoutes() }
  val messMenu = remember { IpakRepository.getMessMenu() }
  var feedbackDialogInfo by remember { mutableStateOf<Pair<String, Pair<String, String>>?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(IpakLightBg)
      .testTag("admin_support_screen"),
  ) {
    // Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(IpakNavy, IpakDeepNavy)
          )
        )
        .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
      Column {
        Text(
          text = "Admin Support Services",
          color = Color.White,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 22.sp,
        )
        Text(
          text = "Official transport network, cafeteria menu & facility desks",
          color = IpakLime,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
        )
      }
    }

    // Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = IpakSurface,
      contentColor = IpakNavy,
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.sp,
              color = if (selectedTab == index) IpakNavy else IpakTextSecondary,
            )
          },
          modifier = Modifier.testTag("admin_tab_$index"),
        )
      }
    }

    when (selectedTab) {
      0 -> TransportTabContent(routes = routes)
      1 -> MessMenuTabContent(menu = messMenu)
      2 -> FeedbackTabContent(
        onOpenFeedback = { title, desc, url ->
          feedbackDialogInfo = Pair(title, Pair(desc, url))
        }
      )
    }
  }

  feedbackDialogInfo?.let { (title, pair) ->
    ExternalServiceDialog(
      title = title,
      description = pair.first,
      url = pair.second,
      onDismiss = { feedbackDialogInfo = null },
    )
  }
}

@Composable
fun TransportTabContent(routes: List<TransportRoute>) {
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = IpakNavy.copy(alpha = 0.05f),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = IpakNavy)
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "IPAK Daily Transport Routes",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = IpakNavy,
            )
            Text(
              text = "General shift arrival 07:55 AM • Departure 05:15 PM",
              fontSize = 11.sp,
              color = IpakTextSecondary,
            )
          }
        }
      }
    }

    items(routes, key = { it.id }) { route ->
      TransportRouteCard(
        route = route,
        onCallDriver = { dialPhone(context, route.contact) },
      )
    }
  }
}

@Composable
fun TransportRouteCard(
  route: TransportRoute,
  onCallDriver: () -> Unit,
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = IpakSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("route_card_${route.id}"),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = route.title,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = IpakNavy,
            )
            if (route.isMultiShift) {
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = IpakLime.copy(alpha = 0.25f),
              ) {
                Text(
                  text = "3-SHIFTS",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = IpakNavy,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Vehicle: ${route.vehicle} • Type: ${route.type}",
            fontSize = 11.sp,
            color = IpakTextSecondary,
          )
        }

        IconButton(
          onClick = onCallDriver,
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(IpakNavy.copy(alpha = 0.08f)),
        ) {
          Icon(Icons.Default.Call, contentDescription = "Call Driver", tint = IpakNavy, modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Driver info row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(IpakLightBg)
          .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Person, contentDescription = null, tint = IpakTextSecondary, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Driver: ${route.driver}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = IpakNavy,
          )
        }
        Text(
          text = route.contact,
          fontSize = 11.sp,
          color = IpakTextSecondary,
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Toggle stops timeline
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded }
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = if (isExpanded) "Hide Route Stops (${route.stops.size})" else "View Route Stops (${route.stops.size})",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = IpakNavy,
        )
        Icon(
          imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null,
          tint = IpakNavy,
        )
      }

      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          route.stops.forEachIndexed { index, stop ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Box(
                modifier = Modifier
                  .size(20.dp)
                  .clip(CircleShape)
                  .background(if (index == route.stops.lastIndex) IpakLime else IpakNavy.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
              ) {
                Text(
                  text = "${index + 1}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (index == route.stops.lastIndex) IpakNavy else IpakNavy,
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(stop.name, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = IpakNavy)
                if (stop.time.isNotBlank()) {
                  Text(stop.time, fontSize = 10.sp, color = IpakTextSecondary)
                } else if (stop.morning.isNotBlank()) {
                  Text("M: ${stop.morning} | E: ${stop.evening} | N: ${stop.night}", fontSize = 10.sp, color = IpakTextSecondary)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun MessMenuTabContent(menu: List<MessMenuItem>) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFEF3C7),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color(0xFFD97706))
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Official Mess Menu - April 2026",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF92400E),
            )
            Text(
              text = "Hygiene, fresh ingredients & balanced corporate diet",
              fontSize = 11.sp,
              color = Color(0xFFB45309),
            )
          }
        }
      }
    }

    items(menu) { item ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = IpakSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          // Date badge
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(IpakLightBg),
            contentAlignment = Alignment.Center,
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = item.date.split(" ").getOrNull(1) ?: "",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = IpakNavy,
              )
              Text(
                text = item.date.split(" ").getOrNull(0) ?: "Apr",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = IpakTextSecondary,
              )
            }
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = item.day,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = IpakTextSecondary,
            )
            Text(
              text = item.main,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = IpakNavy,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = item.side,
              fontSize = 11.sp,
              color = IpakTextSecondary,
            )
            if (item.sweet.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = IpakLimeLight.copy(alpha = 0.4f),
              ) {
                Text(
                  text = "Sweet: ${item.sweet}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = IpakNavy,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun FeedbackTabContent(
  onOpenFeedback: (String, String, String) -> Unit,
) {
  val forms = listOf(
    Triple("IPAK | CPAK Employee Feedback", "Administrative and facility feedback form for IPAK & CPAK units.", "https://forms.gle/ipak-cpak-feedback"),
    Triple("PETPAK | GPAK Employee Feedback", "Operations and working environment feedback for PETPAK & GPAK.", "https://forms.gle/petpak-gpak-feedback"),
    Triple("PPAK Facility Suggestions", "Suggestions and cafeteria review for PPAK campus.", "https://forms.gle/ppak-facility-feedback"),
    Triple("Karachi Plant Desk", "Administrative support and feedback desk for Karachi operations.", "https://forms.gle/karachi-plant-feedback"),
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE0F2FE),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Default.Feedback, contentDescription = null, tint = Color(0xFF0284C7))
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Anonymous & Confidential Feedback",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF075985),
            )
            Text(
              text = "Share constructive feedback directly with administration",
              fontSize = 11.sp,
              color = Color(0xFF0369A1),
            )
          }
        }
      }
    }

    items(forms) { (title, desc, url) ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = IpakSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenFeedback(title, desc, url) },
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(IpakNavy.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(Icons.Default.Feedback, contentDescription = null, tint = IpakNavy)
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IpakNavy)
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 11.sp, color = IpakTextSecondary)
          }
          Icon(Icons.Default.OpenInNew, contentDescription = "Open", tint = IpakNavy, modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}
