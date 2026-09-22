package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakNavy

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun IpakWebPortalScreen(
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  var webViewInstance by remember { mutableStateOf<WebView?>(null) }
  var canGoBack by remember { mutableStateOf(false) }
  var progress by remember { mutableFloatStateOf(0f) }
  var isLoading by remember { mutableStateOf(true) }

  val defaultUrl = "file:///android_asset/web/dashboard.html"

  BackHandler(enabled = canGoBack) {
    webViewInstance?.let { webView ->
      if (webView.canGoBack()) {
        webView.goBack()
      }
    }
  }

  Box(modifier = modifier.fillMaxSize().testTag("ipak_web_portal_screen")) {
    Column(modifier = Modifier.fillMaxSize()) {
      if (isLoading && progress < 1f) {
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp),
          color = IpakLime,
          trackColor = IpakNavy,
        )
      }

      AndroidView(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        factory = { ctx ->
          WebView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT,
            )

            // Use SOFTWARE layer type to avoid Mesa DRM rendernode device errors in emulator/container environments
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            setBackgroundColor(android.graphics.Color.WHITE)

            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              databaseEnabled = true
              allowFileAccess = true
              allowContentAccess = true
              loadWithOverviewMode = true
              useWideViewPort = true
              setSupportZoom(true)
              builtInZoomControls = true
              displayZoomControls = false
              mediaPlaybackRequiresUserGesture = false
              mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
              cacheMode = WebSettings.LOAD_DEFAULT
            }

            webViewClient = object : WebViewClient() {
              override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false

                if (url.startsWith("tel:")) {
                  handleDial(ctx, url)
                  return true
                }
                if (url.startsWith("mailto:")) {
                  handleEmail(ctx, url)
                  return true
                }
                if (url.contains("api.whatsapp.com") || url.contains("wa.me")) {
                  handleExternal(ctx, url)
                  return true
                }

                // If loading external HTTP/HTTPS portal (like ESS SmartHCM or external links), handle gracefully
                if (url.startsWith("http://") || url.startsWith("https://")) {
                  // If it's a font or CDN script, let it load
                  if (url.contains("cdnjs.cloudflare.com") || url.contains("fonts.googleapis.com") || url.contains("gstatic.com") || url.contains("firebaseio.com")) {
                    return false
                  }
                  handleExternal(ctx, url)
                  return true
                }

                return false
              }

              override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isLoading = true
                canGoBack = view?.canGoBack() == true
              }

              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
                canGoBack = view?.canGoBack() == true
              }

              override fun onRenderProcessGone(view: WebView?, detail: android.webkit.RenderProcessGoneDetail?): Boolean {
                // Return true so the app does not crash if render process is terminated
                return true
              }
            }

            webChromeClient = object : WebChromeClient() {
              override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress = newProgress / 100f
                if (newProgress >= 100) {
                  isLoading = false
                }
              }

              override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                return true
              }
            }

            loadUrl(defaultUrl)
            webViewInstance = this
          }
        },
        update = { webView ->
          webViewInstance = webView
        },
      )
    }

    // Floating navigation toolbar for mobile convenience
    Surface(
      shape = RoundedCornerShape(28.dp),
      color = IpakDeepNavy.copy(alpha = 0.95f),
      shadowElevation = 8.dp,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 16.dp)
        .testTag("web_portal_floating_toolbar"),
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (canGoBack) {
          IconButton(
            onClick = { webViewInstance?.goBack() },
            modifier = Modifier.size(36.dp),
          ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Spacer(modifier = Modifier.width(6.dp))
        }

        IconButton(
          onClick = { webViewInstance?.loadUrl(defaultUrl) },
          modifier = Modifier.size(36.dp),
        ) {
          Icon(Icons.Default.Home, contentDescription = "Home Dashboard", tint = IpakLime)
        }

        Spacer(modifier = Modifier.width(6.dp))

        IconButton(
          onClick = { webViewInstance?.reload() },
          modifier = Modifier.size(36.dp),
        ) {
          Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
        }
      }
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      webViewInstance?.stopLoading()
    }
  }
}

private fun handleDial(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not launch phone dialer", Toast.LENGTH_SHORT).show()
  }
}

private fun handleEmail(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not launch email app", Toast.LENGTH_SHORT).show()
  }
}

private fun handleExternal(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
  }
}
