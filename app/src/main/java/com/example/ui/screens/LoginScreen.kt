package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakEmergencyRed
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakLimeDark
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary

@Composable
fun LoginScreen(
  onLoginSuccess: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var email by remember { mutableStateOf("employee@ipak.com.pk") }
  var password by remember { mutableStateOf("••••••••") }
  var passwordVisible by remember { mutableStateOf(false) }
  var rememberMe by remember { mutableStateOf(true) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(IpakNavy, IpakDeepNavy)
        )
      )
      .testTag("login_screen"),
  ) {
    // Glowing backlight aura effect matching the portal screensaver
    Box(
      modifier = Modifier
        .size(260.dp)
        .align(Alignment.TopCenter)
        .padding(top = 40.dp)
        .clip(CircleShape)
        .background(IpakLime.copy(alpha = 0.15f))
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      // Authentic Corporate Logo
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier.padding(bottom = 16.dp),
        shadowElevation = 8.dp,
      ) {
        Image(
          painter = painterResource(id = R.drawable.ipak_logo),
          contentDescription = "IPAK Corporate Logo",
          modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(36.dp)
            .width(180.dp),
          contentScale = ContentScale.Fit,
        )
      }

      Text(
        text = "Empowering Employees.",
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
      )
      Text(
        text = "Simplifying Services.",
        color = IpakLime,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Login Card
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IpakSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.padding(24.dp)) {
          Text(
            text = "Service Portal",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = IpakNavy,
          )
          Text(
            text = "Secure Employee Mobile Access",
            fontSize = 12.sp,
            color = IpakTextSecondary,
          )

          Spacer(modifier = Modifier.height(18.dp))

          // Corporate Email Field
          OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Corporate Email") },
            placeholder = { Text("name@ipak.com.pk") },
            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, tint = IpakNavy) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = IpakNavy,
              unfocusedBorderColor = IpakBorder,
            ),
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Password Field
          OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = IpakNavy) },
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                  imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                  contentDescription = null,
                  tint = IpakTextSecondary,
                )
              }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = IpakNavy,
              unfocusedBorderColor = IpakBorder,
            ),
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Remember Me Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Checkbox(
              checked = rememberMe,
              onCheckedChange = { rememberMe = it },
              colors = CheckboxDefaults.colors(
                checkedColor = IpakNavy,
                checkmarkColor = IpakLime,
              ),
              modifier = Modifier.testTag("login_remember_checkbox"),
            )
            Text(
              text = "Keep me logged in",
              fontSize = 12.sp,
              color = IpakTextSecondary,
              modifier = Modifier.clickable { rememberMe = !rememberMe },
            )
          }

          errorMessage?.let { err ->
            Text(
              text = err,
              color = IpakEmergencyRed,
              fontSize = 12.sp,
              modifier = Modifier.padding(vertical = 4.dp),
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Main Submit Button
          Button(
            onClick = {
              if (email.isBlank()) {
                errorMessage = "Please enter corporate email"
              } else {
                onLoginSuccess(email)
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = IpakLime, contentColor = IpakDeepNavy),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("login_submit_button"),
          ) {
            Text("Login to Portal", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Quick Guest Access
          OutlinedButton(
            onClick = { onLoginSuccess("guest@ipak.com.pk") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("login_guest_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = IpakNavy),
          ) {
            Text("Explore as Guest Staff", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "International Packaging Films Limited • ISO 9001 Certified",
        color = Color.White.copy(alpha = 0.6f),
        fontSize = 11.sp,
      )
    }
  }
}
