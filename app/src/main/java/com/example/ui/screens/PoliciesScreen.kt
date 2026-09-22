package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IpakRepository
import com.example.model.CompanyPolicy
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakBorderLight
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary

@Composable
fun PoliciesScreen(
  modifier: Modifier = Modifier,
) {
  val policies = remember { IpakRepository.getPolicies() }
  var searchQuery by remember { mutableStateOf("") }

  val filteredPolicies = policies.filter { policy ->
    searchQuery.isBlank() ||
      policy.title.contains(searchQuery, ignoreCase = true) ||
      policy.subtitle.contains(searchQuery, ignoreCase = true) ||
      policy.sections.any { s -> s.heading.contains(searchQuery, ignoreCase = true) || s.points.any { p -> p.contains(searchQuery, ignoreCase = true) } }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(IpakLightBg)
      .testTag("policies_screen"),
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
          text = "Company HR Policies",
          color = Color.White,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 22.sp,
        )
        Text(
          text = "Official corporate rules, career grading & code of conduct",
          color = IpakLime,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
        )
      }
    }

    // Search bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(IpakSurface)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search policies, dress code, grades...", fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IpakTextSecondary) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Clear", tint = IpakTextSecondary)
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("policy_search_input"),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = IpakNavy,
          unfocusedBorderColor = IpakBorder,
        ),
      )
    }

    // Policies list
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      items(filteredPolicies, key = { it.id }) { policy ->
        PolicyCard(policy = policy)
      }
    }
  }
}

@Composable
fun PolicyCard(
  policy: CompanyPolicy,
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = IpakSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
    modifier = Modifier.fillMaxWidth().testTag("policy_card_${policy.id}"),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(IpakNavy.copy(alpha = 0.08f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(Icons.Default.Gavel, contentDescription = null, tint = IpakNavy, modifier = Modifier.size(22.dp))
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = IpakLightBg,
        ) {
          Text(
            text = "Effective: ${policy.effectiveDate}",
            fontSize = 10.sp,
            color = IpakTextSecondary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = policy.title,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = IpakNavy,
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = policy.subtitle,
        fontSize = 12.sp,
        color = IpakTextSecondary,
      )

      Spacer(modifier = Modifier.height(10.dp))

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = IpakLightBg,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text("Objective:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = IpakNavy)
          Text(policy.objective, fontSize = 11.sp, color = IpakTextSecondary, lineHeight = 16.sp)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Expand/Collapse toggle
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded }
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = if (isExpanded) "Collapse Policy Details" else "Read Full Clauses & Guidelines",
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
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          policy.sections.forEach { section ->
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(IpakLightBg)
                .padding(12.dp)
            ) {
              Text(
                text = section.heading,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = IpakNavy,
              )
              Spacer(modifier = Modifier.height(6.dp))
              section.points.forEach { pt ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                  verticalAlignment = Alignment.Top,
                ) {
                  Text("• ", fontWeight = FontWeight.Bold, color = IpakNavy)
                  Text(
                    text = pt,
                    fontSize = 11.sp,
                    color = IpakTextSecondary,
                    lineHeight = 16.sp,
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
