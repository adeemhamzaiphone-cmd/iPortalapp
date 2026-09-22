package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.IpakRepository
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary
import com.example.ui.theme.IpakWhatsAppGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityChatSheet(
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val messages by IpakRepository.chatMessages.collectAsState()
  var inputMessage by remember { mutableStateOf("") }

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
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column {
          Text(
            text = "Community Chat",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = IpakNavy,
          )
          Text(
            text = "Real-time employee discussions & announcements",
            fontSize = 12.sp,
            color = IpakTextSecondary,
          )
        }
        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_chat_button"),
        ) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = IpakNavy)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Messages list
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        reverseLayout = false,
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        items(messages, key = { it.id }) { msg ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start,
          ) {
            Box(
              modifier = Modifier
                .clip(
                  RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (msg.isMe) 14.dp else 2.dp,
                    bottomEnd = if (msg.isMe) 2.dp else 14.dp,
                  )
                )
                .background(if (msg.isMe) IpakLime else IpakLightBg)
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Text(
                    text = msg.senderName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (msg.isMe) IpakDeepNavy else IpakNavy,
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = msg.time,
                    fontSize = 10.sp,
                    color = IpakTextSecondary,
                  )
                  if (msg.isMe && !msg.isDeleted) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                      imageVector = Icons.Default.DeleteOutline,
                      contentDescription = "Delete",
                      tint = IpakNavy.copy(alpha = 0.6f),
                      modifier = Modifier
                        .size(14.dp)
                        .clickable { IpakRepository.deleteChatMessage(msg.id) },
                    )
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = msg.message,
                  fontSize = 13.sp,
                  color = if (msg.isDeleted) IpakTextMuted else IpakTextPrimary,
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Input row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        OutlinedTextField(
          value = inputMessage,
          onValueChange = { inputMessage = it },
          placeholder = { Text("Write a message to colleagues...", fontSize = 13.sp) },
          modifier = Modifier
            .weight(1f)
            .testTag("chat_input"),
          singleLine = true,
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IpakNavy,
            unfocusedBorderColor = IpakBorder,
          ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
          onClick = {
            if (inputMessage.isNotBlank()) {
              IpakRepository.sendChatMessage(inputMessage.trim())
              inputMessage = ""
            }
          },
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(IpakLime)
            .testTag("chat_send_button"),
        ) {
          Icon(Icons.Default.Send, contentDescription = "Send", tint = IpakNavy)
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun ContactSupportDialog(
  onDismiss: () -> Unit,
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = IpakSurface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "Get in Touch",
          fontWeight = FontWeight.Bold,
          fontSize = 19.sp,
          color = IpakNavy,
        )
        Text(
          text = "Official Employee Services Support Desk",
          fontSize = 12.sp,
          color = IpakTextSecondary,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // WhatsApp Support Action
        SupportActionTile(
          icon = Icons.Default.Send,
          iconTint = IpakWhatsAppGreen,
          title = "WhatsApp HR Desk",
          subtitle = "+92 322 2818107",
          onClick = {
            openWhatsApp(context, "+923222818107", "Hello IPAK HR Desk, I need assistance with portal services.")
          },
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Phone Support
        SupportActionTile(
          icon = Icons.Default.Phone,
          iconTint = IpakNavy,
          title = "Call Corporate Desk",
          subtitle = "+92 42 111 000 472",
          onClick = {
            dialPhone(context, "+9242111000472")
          },
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Email Support
        SupportActionTile(
          icon = Icons.Default.Email,
          iconTint = IpakNavy,
          title = "Corporate HR Email",
          subtitle = "hamza.farooq@ipak.com.pk",
          onClick = {
            sendEmail(context, "hamza.farooq@ipak.com.pk", "Employee Portal Query")
          },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = IpakLightBg, contentColor = IpakNavy),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("close_support_button"),
        ) {
          Text("Close", fontWeight = FontWeight.SemiBold)
        }
      }
    }
  }
}

@Composable
fun SupportActionTile(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: androidx.compose.ui.graphics.Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = IpakLightBg,
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
          .size(40.dp)
          .clip(CircleShape)
          .background(iconTint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.width(14.dp))
      Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IpakNavy)
        Text(subtitle, fontSize = 12.sp, color = IpakTextSecondary)
      }
    }
  }
}

@Composable
fun ExternalServiceDialog(
  title: String,
  description: String,
  url: String,
  onDismiss: () -> Unit,
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = IpakSurface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
      ) {
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = IpakNavy,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = description,
          fontSize = 13.sp,
          color = IpakTextSecondary,
          lineHeight = 18.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = IpakLightBg,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = url,
            fontSize = 11.sp,
            color = IpakNavy,
            modifier = Modifier.padding(10.dp),
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = IpakLightBg, contentColor = IpakNavy),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f),
          ) {
            Text("Cancel")
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = {
              openUrl(context, url)
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = IpakLime, contentColor = IpakDeepNavy),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f).testTag("open_external_portal_btn"),
          ) {
            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Launch", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

fun openWhatsApp(context: Context, phone: String, message: String = "") {
  try {
    val cleanPhone = phone.replace("+", "").replace("-", "").replace(" ", "")
    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open WhatsApp: $phone", Toast.LENGTH_SHORT).show()
  }
}

fun dialPhone(context: Context, phone: String) {
  try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not dial number", Toast.LENGTH_SHORT).show()
  }
}

fun sendEmail(context: Context, email: String, subject: String) {
  try {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).apply {
      putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open email client", Toast.LENGTH_SHORT).show()
  }
}

fun openUrl(context: Context, url: String) {
  try {
    val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
      "https://$url"
    } else {
      url
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
  }
}
