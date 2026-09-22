package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.components.CommunityChatSheet
import com.example.ui.components.ContactSupportDialog
import com.example.ui.components.IpakBottomBar
import com.example.ui.components.IpakDestination
import com.example.ui.components.IpakTopAppBar
import com.example.ui.screens.AdminSupportScreen
import com.example.ui.screens.AudioBooksSheet
import com.example.ui.screens.BloodBankScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GallerySheet
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.NewsletterSheet
import com.example.ui.screens.PodcastSheet
import com.example.ui.screens.PoliciesScreen
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        IpakApp()
      }
    }
  }
}

@Composable
fun IpakApp() {
  var currentDestination by remember { mutableStateOf(IpakDestination.DASHBOARD) }

  var showChatSheet by remember { mutableStateOf(false) }
  var showSupportDialog by remember { mutableStateOf(false) }

  var showPodcastSheet by remember { mutableStateOf(false) }
  var showAudioBooksSheet by remember { mutableStateOf(false) }
  var showGallerySheet by remember { mutableStateOf(false) }
  var showNewsletterSheet by remember { mutableStateOf(false) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = IpakLightBg,
    topBar = {
      IpakTopAppBar(
        title = currentDestination.label,
        onOpenChat = { showChatSheet = true },
        onOpenContact = { showSupportDialog = true },
      )
    },
    bottomBar = {
      IpakBottomBar(
        currentDestination = currentDestination,
        onNavigate = { currentDestination = it },
      )
    },
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(IpakLightBg)
    ) {
      AnimatedContent(
        targetState = currentDestination,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "destination_transition",
      ) { target ->
        when (target) {
          IpakDestination.DASHBOARD -> DashboardScreen(
            onNavigate = { currentDestination = it },
            onOpenPodcast = { showPodcastSheet = true },
            onOpenAudioBooks = { showAudioBooksSheet = true },
            onOpenGallery = { showGallerySheet = true },
            onOpenNewsletter = { showNewsletterSheet = true },
          )
          IpakDestination.BLOOD_BANK -> BloodBankScreen()
          IpakDestination.ADMIN_SUPPORT -> AdminSupportScreen()
          IpakDestination.MARKETPLACE -> MarketplaceScreen()
          IpakDestination.POLICIES -> PoliciesScreen()
        }
      }
    }
  }

  // Real-time Community Chat Bottom Sheet
  if (showChatSheet) {
    CommunityChatSheet(
      onDismiss = { showChatSheet = false },
    )
  }

  // Contact Support & Emergency Helpdesk Dialog
  if (showSupportDialog) {
    ContactSupportDialog(
      onDismiss = { showSupportDialog = false },
    )
  }

  // Media & Learning Hub Modals
  if (showPodcastSheet) {
    PodcastSheet(onDismiss = { showPodcastSheet = false })
  }
  if (showAudioBooksSheet) {
    AudioBooksSheet(onDismiss = { showAudioBooksSheet = false })
  }
  if (showGallerySheet) {
    GallerySheet(onDismiss = { showGallerySheet = false })
  }
  if (showNewsletterSheet) {
    NewsletterSheet(onDismiss = { showNewsletterSheet = false })
  }
}

// Retained for screenshot & Robolectric test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}
