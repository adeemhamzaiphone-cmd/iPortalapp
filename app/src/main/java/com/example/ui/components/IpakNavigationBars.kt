package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakLimeDark
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary

enum class IpakDestination(val label: String, val testTag: String) {
  DASHBOARD("Home", "nav_dashboard"),
  BLOOD_BANK("Blood Bank", "nav_blood_bank"),
  ADMIN_SUPPORT("Admin", "nav_admin"),
  MARKETPLACE("Market", "nav_marketplace"),
  POLICIES("Policies", "nav_policies"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IpakTopAppBar(
  title: String,
  onOpenChat: () -> Unit,
  onOpenContact: () -> Unit,
  modifier: Modifier = Modifier,
) {
  TopAppBar(
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = IpakSurface,
      titleContentColor = IpakNavy,
    ),
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        // Authentic IPAK Corporate Logo
        Image(
          painter = painterResource(id = R.drawable.ipak_logo),
          contentDescription = "IPAK Logo",
          modifier = Modifier
            .height(30.dp)
            .width(120.dp),
          contentScale = ContentScale.Fit,
        )

        // Live connection indicator pill
        Surface(
          shape = CircleShape,
          color = IpakLime.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, IpakLime.copy(alpha = 0.5f)),
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(IpakLimeDark)
            )
            Text(
              text = "LIVE",
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold,
              color = IpakNavy,
              letterSpacing = 0.5.sp,
            )
          }
        }
      }
    },
    actions = {
      // Community Chat trigger
      IconButton(
        onClick = onOpenChat,
        modifier = Modifier.testTag("top_bar_chat_button"),
      ) {
        BadgedBox(
          badge = {
            Badge(
              containerColor = IpakLime,
              contentColor = IpakNavy,
            ) {
              Text("3", fontWeight = FontWeight.Bold)
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "Community Chat",
            tint = IpakNavy,
          )
        }
      }

      // HR & Admin Support Trigger
      IconButton(
        onClick = onOpenContact,
        modifier = Modifier.testTag("top_bar_support_button"),
      ) {
        Icon(
          imageVector = Icons.Default.HeadsetMic,
          contentDescription = "Get in Touch Support",
          tint = IpakNavy,
        )
      }

      // User Profile Avatar
      Box(
        modifier = Modifier
          .padding(end = 12.dp, start = 4.dp)
          .size(36.dp)
          .clip(CircleShape)
          .background(IpakNavy),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = "HA",
          color = IpakLime,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
        )
      }
    },
  )
}

@Composable
fun IpakBottomBar(
  currentDestination: IpakDestination,
  onNavigate: (IpakDestination) -> Unit,
  modifier: Modifier = Modifier,
) {
  NavigationBar(
    modifier = modifier,
    containerColor = IpakSurface,
    tonalElevation = 8.dp,
  ) {
    NavigationBarItem(
      selected = currentDestination == IpakDestination.DASHBOARD,
      onClick = { onNavigate(IpakDestination.DASHBOARD) },
      icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
      label = { Text(IpakDestination.DASHBOARD.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IpakNavy,
        selectedTextColor = IpakNavy,
        indicatorColor = IpakLime.copy(alpha = 0.45f),
        unselectedIconColor = IpakTextMuted,
        unselectedTextColor = IpakTextMuted,
      ),
      modifier = Modifier.testTag("bottom_nav_dashboard"),
    )

    NavigationBarItem(
      selected = currentDestination == IpakDestination.BLOOD_BANK,
      onClick = { onNavigate(IpakDestination.BLOOD_BANK) },
      icon = { Icon(Icons.Default.InvertColors, contentDescription = "Blood Bank") },
      label = { Text(IpakDestination.BLOOD_BANK.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IpakNavy,
        selectedTextColor = IpakNavy,
        indicatorColor = IpakLime.copy(alpha = 0.45f),
        unselectedIconColor = IpakTextMuted,
        unselectedTextColor = IpakTextMuted,
      ),
      modifier = Modifier.testTag("bottom_nav_blood_bank"),
    )

    NavigationBarItem(
      selected = currentDestination == IpakDestination.ADMIN_SUPPORT,
      onClick = { onNavigate(IpakDestination.ADMIN_SUPPORT) },
      icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Support") },
      label = { Text(IpakDestination.ADMIN_SUPPORT.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IpakNavy,
        selectedTextColor = IpakNavy,
        indicatorColor = IpakLime.copy(alpha = 0.45f),
        unselectedIconColor = IpakTextMuted,
        unselectedTextColor = IpakTextMuted,
      ),
      modifier = Modifier.testTag("bottom_nav_admin"),
    )

    NavigationBarItem(
      selected = currentDestination == IpakDestination.MARKETPLACE,
      onClick = { onNavigate(IpakDestination.MARKETPLACE) },
      icon = { Icon(Icons.Default.Storefront, contentDescription = "Marketplace") },
      label = { Text(IpakDestination.MARKETPLACE.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IpakNavy,
        selectedTextColor = IpakNavy,
        indicatorColor = IpakLime.copy(alpha = 0.45f),
        unselectedIconColor = IpakTextMuted,
        unselectedTextColor = IpakTextMuted,
      ),
      modifier = Modifier.testTag("bottom_nav_marketplace"),
    )

    NavigationBarItem(
      selected = currentDestination == IpakDestination.POLICIES,
      onClick = { onNavigate(IpakDestination.POLICIES) },
      icon = { Icon(Icons.Default.Gavel, contentDescription = "Policies") },
      label = { Text(IpakDestination.POLICIES.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IpakNavy,
        selectedTextColor = IpakNavy,
        indicatorColor = IpakLime.copy(alpha = 0.45f),
        unselectedIconColor = IpakTextMuted,
        unselectedTextColor = IpakTextMuted,
      ),
      modifier = Modifier.testTag("bottom_nav_policies"),
    )
  }
}
