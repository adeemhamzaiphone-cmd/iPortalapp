package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.IpakRepository
import com.example.ui.components.openUrl
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakBorderLight
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastSheet(onDismiss: () -> Unit) {
  val context = LocalContext.current
  val podcasts = remember { IpakRepository.getPodcasts() }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = IpakSurface,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("podcast_sheet"),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text("IPAK Podcast & Talks", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = IpakNavy)
          Text("Executive series, HSE discussions & leadership panels", fontSize = 12.sp, color = IpakTextSecondary)
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = IpakNavy)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        items(podcasts) { pod ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = IpakSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { openUrl(context, "https://www.youtube.com/watch?v=${pod.youtubeId}") },
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(IpakNavy),
                contentAlignment = Alignment.Center,
              ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = IpakLime, modifier = Modifier.size(28.dp))
              }
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(pod.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IpakNavy)
                Spacer(modifier = Modifier.height(2.dp))
                Text("${pod.speaker} • ${pod.duration}", fontSize = 11.sp, color = IpakTextSecondary)
              }
              Icon(Icons.Default.OpenInNew, contentDescription = null, tint = IpakNavy, modifier = Modifier.size(18.dp))
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioBooksSheet(onDismiss: () -> Unit) {
  val context = LocalContext.current
  val audioTracks = remember { IpakRepository.getAudioTracks() }
  val books = remember { IpakRepository.getEBooks() }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = IpakSurface,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("audio_books_sheet"),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text("Audio Library & E-Books", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = IpakNavy)
          Text("Self-development, leadership and focus soundtracks", fontSize = 12.sp, color = IpakTextSecondary)
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = IpakNavy)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        item {
          Text("Audiobooks & Focus Playlists", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IpakNavy)
        }
        items(audioTracks) { track ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = IpakLightBg),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { openUrl(context, track.spotifyUrl) },
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(Icons.Default.Headphones, contentDescription = null, tint = IpakNavy)
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(track.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IpakNavy)
                Text("${track.author} • ${track.duration}", fontSize = 11.sp, color = IpakTextSecondary)
              }
              Icon(Icons.Default.OpenInNew, contentDescription = null, tint = IpakTextSecondary, modifier = Modifier.size(16.dp))
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(10.dp))
          Text("Recommended E-Books & Handbooks", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IpakNavy)
        }

        items(books) { book ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = IpakSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { openUrl(context, book.previewUrl) },
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(Icons.Default.MenuBook, contentDescription = null, tint = IpakNavy)
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(book.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IpakNavy)
                Text(book.category, fontSize = 10.sp, color = IpakTextSecondary)
              }
              Icon(Icons.Default.OpenInNew, contentDescription = null, tint = IpakTextSecondary, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GallerySheet(onDismiss: () -> Unit) {
  val events = remember { IpakRepository.getMediaEvents() }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = IpakSurface,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("gallery_sheet"),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text("Corporate Events Gallery", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = IpakNavy)
          Text("Moments and memories across IPAK campuses", fontSize = 12.sp, color = IpakTextSecondary)
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = IpakNavy)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        items(events) { ev ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = IpakSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = IpakNavy.copy(alpha = 0.08f),
                ) {
                  Text(ev.tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = IpakNavy, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Text("${ev.photoCount} Photos", fontSize = 11.sp, color = IpakTextSecondary)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(ev.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = IpakNavy)
              Spacer(modifier = Modifier.height(4.dp))
              Text(ev.desc, fontSize = 12.sp, color = IpakTextSecondary)
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsletterSheet(onDismiss: () -> Unit) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = IpakSurface,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("newsletter_sheet"),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text("IPAK Quarterly Newsletter", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = IpakNavy)
          Text("Edition 2025 - Company Vision & Expansions", fontSize = 12.sp, color = IpakTextSecondary)
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = IpakNavy)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = IpakLightBg),
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "Message from the CEO",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = IpakNavy,
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "\"Our relentless pursuit of quality, sustainable packaging materials, and employee welfare remains at the heart of International Packaging Films Limited. Together, we continue to set industrial benchmarks across Pakistan and global markets.\"",
                fontSize = 12.sp,
                color = IpakTextSecondary,
                lineHeight = 18.sp,
              )
            }
          }
        }

        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = IpakSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text("Key Highlights of the Quarter", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = IpakNavy)
              Spacer(modifier = Modifier.height(8.dp))
              Text("• Expansion of high-barrier BOPP line commissioning.", fontSize = 12.sp, color = IpakTextSecondary)
              Text("• Zero-incident safety milestone celebrated across plants.", fontSize = 12.sp, color = IpakTextSecondary)
              Text("• Rollout of digital employee self-service apps and blood bank network.", fontSize = 12.sp, color = IpakTextSecondary)
            }
          }
        }
      }
    }
  }
}
