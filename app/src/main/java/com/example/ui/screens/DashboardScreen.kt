package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ExternalServiceDialog
import com.example.ui.components.IpakDestination
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakBorderLight
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakEmergencyRed
import com.example.ui.theme.IpakEmergencyRedBg
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakLimeDark
import com.example.ui.theme.IpakLimeLight
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
  onNavigate: (IpakDestination) -> Unit,
  onOpenPodcast: () -> Unit,
  onOpenAudioBooks: () -> Unit,
  onOpenGallery: () -> Unit,
  onOpenNewsletter: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var searchQuery by remember { mutableStateOf("") }
  var externalDialogInfo by remember { mutableStateOf<Pair<String, Pair<String, String>>?>(null) }

  // Banner carousel state
  val banners = listOf(
    Triple(R.drawable.banner_1, "Empowering Innovation & Excellence", "Your complete employee services hub"),
    Triple(R.drawable.banner_2, "Collaborative Culture & Success", "Working together to achieve greatness"),
    Triple(R.drawable.banner_3, "Jeet Hum Say Hai", "Winning together in the packaging industry"),
  )
  var currentBannerIndex by remember { mutableIntStateOf(0) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(4000)
      currentBannerIndex = (currentBannerIndex + 1) % banners.size
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(IpakLightBg)
      .testTag("dashboard_screen"),
    contentPadding = PaddingValues(bottom = 24.dp),
  ) {
    // Top Greeting & Welcome
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(IpakNavy, IpakDeepNavy)
            )
          )
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column {
              Text(
                text = "Welcome to IPAK",
                color = IpakLime,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
              )
              Text(
                text = "Service Portal",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
              )
            }
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.White.copy(alpha = 0.12f),
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(IpakLime)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Portal Online",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Corporate Hero Banner Carousel
          Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(160.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
          ) {
            Box(modifier = Modifier.fillMaxSize()) {
              Image(
                painter = painterResource(id = banners[currentBannerIndex].first),
                contentDescription = "Corporate Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
              )
              // Gradient overlay for crisp typography
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(
                    Brush.verticalGradient(
                      colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                      startY = 60f,
                    )
                  )
              )
              Column(
                modifier = Modifier
                  .align(Alignment.BottomStart)
                  .padding(14.dp)
              ) {
                Text(
                  text = banners[currentBannerIndex].second,
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                )
                Text(
                  text = banners[currentBannerIndex].third,
                  color = Color.White.copy(alpha = 0.85f),
                  fontSize = 12.sp,
                )
              }

              // Indicators
              Row(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
              ) {
                banners.indices.forEach { index ->
                  Box(
                    modifier = Modifier
                      .size(if (index == currentBannerIndex) 16.dp else 6.dp, 6.dp)
                      .clip(CircleShape)
                      .background(if (index == currentBannerIndex) IpakLime else Color.White.copy(alpha = 0.5f))
                  )
                }
              }
            }
          }
        }
      }
    }

    // Search Bar
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search services, routes, policies...", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IpakTextSecondary) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_search_input"),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = IpakSurface,
            unfocusedContainerColor = IpakSurface,
            focusedBorderColor = IpakNavy,
            unfocusedBorderColor = IpakBorder,
          ),
        )
      }
    }

    // Quick Alert: Blood Donor Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .clickable { onNavigate(IpakDestination.BLOOD_BANK) }
          .testTag("dashboard_blood_bank_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = IpakEmergencyRedBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(IpakEmergencyRed.copy(alpha = 0.4f), IpakEmergencyRed.copy(alpha = 0.2f)))),
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(IpakEmergencyRed),
            contentAlignment = Alignment.Center,
          ) {
            Icon(Icons.Default.InvertColors, contentDescription = "Blood Bank", tint = Color.White, modifier = Modifier.size(24.dp))
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "IPAK Blood Bank",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = IpakEmergencyRed,
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = IpakEmergencyRed,
              ) {
                Text(
                  text = "EMERGENCY",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = "Live employee donor database & emergency contact",
              fontSize = 12.sp,
              color = IpakTextSecondary,
            )
          }
          Icon(Icons.Default.ChevronRight, contentDescription = null, tint = IpakEmergencyRed)
        }
      }
    }

    // Bento Grid Section Title
    item {
      PaddingTitle(title = "Core Employee Services", subtitle = "Essential portals and administrative desks")
    }

    // Bento Grid Row 1: Admin Support & Marketplace
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        // Transport & Mess Hub (Admin)
        BentoCard(
          title = "Admin Support",
          subtitle = "9 Transport Routes & April Mess Menu",
          tag = "Daily Ops",
          tagColor = IpakLimeDark,
          icon = Icons.Default.AdminPanelSettings,
          iconBg = IpakNavy,
          iconTint = IpakLime,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigate(IpakDestination.ADMIN_SUPPORT) }
            .testTag("card_admin_support"),
        )

        // Internal Marketplace
        BentoCard(
          title = "Marketplace",
          subtitle = "Internal buy & sell classifieds for staff",
          tag = "Active Deals",
          tagColor = IpakNavy,
          icon = Icons.Default.Storefront,
          iconBg = IpakLimeLight,
          iconTint = IpakNavy,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigate(IpakDestination.MARKETPLACE) }
            .testTag("card_marketplace"),
        )
      }
    }

    // Bento Grid Row 2: Policies & ESS Portal
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        // HR Policies
        BentoCard(
          title = "Company Policies",
          subtitle = "Grading, Code of Conduct & Dress Code",
          tag = "HR Guide",
          tagColor = IpakNavy,
          icon = Icons.Default.Gavel,
          iconBg = IpakBorderLight,
          iconTint = IpakNavy,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigate(IpakDestination.POLICIES) }
            .testTag("card_policies"),
        )

        // ESS SmartHCM
        BentoCard(
          title = "ESS Portal",
          subtitle = "Employee Self-Service (SmartHCM)",
          tag = "SmartHCM",
          tagColor = Color(0xFF0284C7),
          icon = Icons.Default.PermIdentity,
          iconBg = Color(0xFFE0F2FE),
          iconTint = Color(0xFF0284C7),
          modifier = Modifier
            .weight(1f)
            .clickable {
              externalDialogInfo = Pair(
                "ESS Employee Self-Service",
                Pair(
                  "Access SmartHCM employee self-service for leave requests, pay slips, attendance, and official tax certificates.",
                  "http://ess.ipak.com.pk"
                )
              )
            }
            .testTag("card_ess_portal"),
        )
      }
    }

    // Bento Grid Row 3: Healthcare (Sehat Kahani) & Succession Planning
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        // Sehat Kahani
        BentoCard(
          title = "Sehat Kahani",
          subtitle = "Online doctor consultations & telehealth",
          tag = "Healthcare",
          tagColor = Color(0xFF059669),
          icon = Icons.Default.LocalHospital,
          iconBg = Color(0xFFD1FAE5),
          iconTint = Color(0xFF059669),
          modifier = Modifier
            .weight(1f)
            .clickable {
              externalDialogInfo = Pair(
                "Sehat Kahani Telemedicine",
                Pair(
                  "Access online certified physician consultations, specialist bookings, and digital prescriptions through IPAK corporate health benefits.",
                  "https://sehatkahani.com"
                )
              )
            }
            .testTag("card_sehat_kahani"),
        )

        // Succession Planning
        BentoCard(
          title = "Succession Insights",
          subtitle = "Talent mapping & leadership readiness",
          tag = "HR Strategy",
          tagColor = Color(0xFF7C3AED),
          icon = Icons.Default.TrendingUp,
          iconBg = Color(0xFFEDE9FE),
          iconTint = Color(0xFF7C3AED),
          modifier = Modifier
            .weight(1f)
            .clickable {
              externalDialogInfo = Pair(
                "Succession Insights",
                Pair(
                  "Talent mapping and leadership readiness evaluation framework for IPAK high-potential personnel.",
                  "https://ipak.com.pk/talent-readiness"
                )
              )
            }
            .testTag("card_planning"),
        )
      }
    }

    // Learning & Culture Section Title
    item {
      PaddingTitle(title = "Media, Culture & Learning", subtitle = "Podcasts, corporate events, and library resources")
    }

    // Media & Learning Cards
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        // Podcast Hub Tile
        MediaHorizontalTile(
          icon = Icons.Default.Podcasts,
          iconBg = Color(0xFFFFEDD5),
          iconTint = Color(0xFFEA580C),
          title = "Leadership Podcast Series",
          subtitle = "Executive talks, safety culture & industrial innovations",
          badge = "Watch & Listen",
          onClick = onOpenPodcast,
        )

        // Audio & E-Books Library Tile
        MediaHorizontalTile(
          icon = Icons.Default.Headphones,
          iconBg = Color(0xFFFEF3C7),
          iconTint = Color(0xFFD97706),
          title = "Audio Library & Books",
          subtitle = "Napoleon Hill, Robert Kiyosaki, Dr. Joseph Murphy & more",
          badge = "E-Library",
          onClick = onOpenAudioBooks,
        )

        // Media Gallery
        MediaHorizontalTile(
          icon = Icons.Default.PhotoLibrary,
          iconBg = Color(0xFFE0E7FF),
          iconTint = Color(0xFF4F46E5),
          title = "Corporate Event Gallery",
          subtitle = "Ramadan Iftaar, 14th August, Smash Champs Sports Gala",
          badge = "4 Albums",
          onClick = onOpenGallery,
        )

        // Company Newsletter
        MediaHorizontalTile(
          icon = Icons.Default.Newspaper,
          iconBg = Color(0xFFCFFAFE),
          iconTint = Color(0xFF0891B2),
          title = "Corporate Newsletter",
          subtitle = "Quarterly business highlights, milestones & celebrations",
          badge = "Edition 2025",
          onClick = onOpenNewsletter,
        )
      }
    }
  }

  // External Portal Dialog
  externalDialogInfo?.let { (title, content) ->
    ExternalServiceDialog(
      title = title,
      description = content.first,
      url = content.second,
      onDismiss = { externalDialogInfo = null },
    )
  }
}

@Composable
fun PaddingTitle(title: String, subtitle: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
  ) {
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = IpakNavy,
    )
    Text(
      text = subtitle,
      fontSize = 12.sp,
      color = IpakTextSecondary,
    )
  }
}

@Composable
fun BentoCard(
  title: String,
  subtitle: String,
  tag: String,
  tagColor: Color,
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  modifier: Modifier = Modifier,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = IpakSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
    modifier = modifier,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(iconBg),
          contentAlignment = Alignment.Center,
        ) {
          Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = tagColor.copy(alpha = 0.1f),
        ) {
          Text(
            text = tag,
            color = tagColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = IpakNavy,
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = IpakTextSecondary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 15.sp,
      )
    }
  }
}

@Composable
fun MediaHorizontalTile(
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  title: String,
  subtitle: String,
  badge: String,
  onClick: () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = IpakSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center,
      ) {
        Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(22.dp))
      }
      Spacer(modifier = Modifier.width(14.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IpakNavy)
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = IpakLightBg,
          ) {
            Text(
              text = badge,
              fontSize = 9.sp,
              color = IpakTextSecondary,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(subtitle, fontSize = 11.sp, color = IpakTextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
      }
      Icon(Icons.Default.ChevronRight, contentDescription = null, tint = IpakTextMuted)
    }
  }
}
