package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import com.example.data.IpakRepository
import com.example.model.BloodDonor
import com.example.ui.components.dialPhone
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakBorderLight
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakEmergencyRed
import com.example.ui.theme.IpakEmergencyRedBg
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary
import com.example.ui.theme.IpakWhatsAppGreen
import kotlinx.coroutines.launch

@Composable
fun BloodBankScreen(
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val donors by IpakRepository.bloodDonors.collectAsState()
  val isLoading by IpakRepository.isBloodLoading.collectAsState()

  var selectedGroup by remember { mutableStateOf("ALL") }
  var searchQuery by remember { mutableStateOf("") }
  var showRegisterDialog by remember { mutableStateOf(false) }

  val bloodGroups = listOf("ALL", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

  LaunchedEffect(Unit) {
    IpakRepository.refreshBloodDonorsFromSheet()
  }

  val filteredDonors = donors.filter { donor ->
    val matchesGroup = selectedGroup == "ALL" || donor.bloodGroup.equals(selectedGroup, ignoreCase = true)
    val matchesSearch = searchQuery.isBlank() ||
      donor.name.contains(searchQuery, ignoreCase = true) ||
      donor.department.contains(searchQuery, ignoreCase = true) ||
      donor.designation.contains(searchQuery, ignoreCase = true) ||
      donor.empCode.contains(searchQuery, ignoreCase = true)
    matchesGroup && matchesSearch
  }

  Scaffold(
    modifier = modifier.testTag("blood_bank_screen"),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showRegisterDialog = true },
        containerColor = IpakEmergencyRed,
        contentColor = Color.White,
        modifier = Modifier.testTag("fab_register_donor"),
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Default.Add, contentDescription = "Register Donor")
          Spacer(modifier = Modifier.width(6.dp))
          Text("Register as Donor", fontWeight = FontWeight.Bold)
        }
      }
    },
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(IpakLightBg)
        .padding(paddingValues),
    ) {
      // Header Banner
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
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column {
              Text(
                text = "IPAK Blood Bank",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
              )
              Text(
                text = "Every drop counts • Save a colleague's life",
                color = IpakLime,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
              )
            }

            IconButton(
              onClick = {
                coroutineScope.launch {
                  IpakRepository.refreshBloodDonorsFromSheet()
                  Toast.makeText(context, "Refreshed live blood bank data", Toast.LENGTH_SHORT).show()
                }
              },
              modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .testTag("refresh_blood_donors"),
            ) {
              if (isLoading) {
                CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  color = IpakLime,
                  strokeWidth = 2.dp,
                )
              } else {
                Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Color.White)
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Statistics pill row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            StatsPill(label = "Total Donors", value = "${donors.size}", modifier = Modifier.weight(1f))
            StatsPill(label = "Universal (O-)", value = "${donors.count { it.bloodGroup.equals("O-", true) }}", modifier = Modifier.weight(1f))
            StatsPill(label = "Filtered", value = "${filteredDonors.size}", modifier = Modifier.weight(1f))
          }
        }
      }

      // Search & Filters
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(IpakSurface)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search by name, department, code...", fontSize = 13.sp) },
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
            .testTag("blood_search_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IpakEmergencyRed,
            unfocusedBorderColor = IpakBorder,
          ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Blood Group Filter Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          contentPadding = PaddingValues(vertical = 4.dp),
        ) {
          items(bloodGroups) { group ->
            val isSelected = selectedGroup == group
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) IpakEmergencyRed else IpakLightBg,
              border = if (isSelected) null else CardDefaults.outlinedCardBorder(),
              modifier = Modifier
                .clickable { selectedGroup = group }
                .testTag("filter_chip_$group"),
            ) {
              Text(
                text = group,
                color = if (isSelected) Color.White else IpakNavy,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
              )
            }
          }
        }
      }

      // Donors List
      if (filteredDonors.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          contentAlignment = Alignment.Center,
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.InvertColors,
              contentDescription = null,
              tint = IpakTextMuted,
              modifier = Modifier.size(54.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No donors found for \"$selectedGroup\"",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = IpakNavy,
            )
            Text(
              text = "Try clearing the search or select 'ALL' blood groups.",
              fontSize = 12.sp,
              color = IpakTextSecondary,
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        ) {
          items(filteredDonors, key = { it.id }) { donor ->
            DonorCard(
              donor = donor,
              onCall = { dialPhone(context, donor.phone) },
              onWhatsApp = {
                openWhatsApp(
                  context,
                  donor.phone,
                  "Salam ${donor.name}, reaching out via IPAK Blood Bank portal regarding urgent ${donor.bloodGroup} blood requirement."
                )
              },
            )
          }
        }
      }
    }
  }

  // Register Dialog
  if (showRegisterDialog) {
    RegisterDonorDialog(
      onDismiss = { showRegisterDialog = false },
      onSave = { newDonor ->
        IpakRepository.registerBloodDonor(newDonor)
        showRegisterDialog = false
        Toast.makeText(context, "Registered successfully! Thank you for donating.", Toast.LENGTH_SHORT).show()
      }
    )
  }
}

@Composable
fun StatsPill(label: String, value: String, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color.White.copy(alpha = 0.1f),
    modifier = modifier,
  ) {
    Column(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      Text(label, color = IpakLime, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
  }
}

@Composable
fun DonorCard(
  donor: BloodDonor,
  onCall: () -> Unit,
  onWhatsApp: () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = IpakSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
    modifier = Modifier.fillMaxWidth().testTag("donor_card_${donor.id}"),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      // Blood Group Emblem
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(IpakEmergencyRedBg)
          .border(1.5.dp, IpakEmergencyRed.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center,
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.InvertColors, contentDescription = null, tint = IpakEmergencyRed, modifier = Modifier.size(16.dp))
          Text(
            text = donor.bloodGroup,
            color = IpakEmergencyRed,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
          )
        }
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Donor Information
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = donor.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = IpakNavy,
          )
          if (donor.empCode.isNotBlank()) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = IpakLightBg,
            ) {
              Text(
                text = donor.empCode,
                color = IpakTextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${donor.designation} • ${donor.department}",
          fontSize = 11.sp,
          color = IpakTextSecondary,
          maxLines = 1,
        )
        Text(
          text = donor.phone,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = IpakNavy,
        )
      }

      // Actions: Call & WhatsApp
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        IconButton(
          onClick = onWhatsApp,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(IpakWhatsAppGreen.copy(alpha = 0.12f))
            .testTag("donor_whatsapp_button_${donor.id}"),
        ) {
          Icon(Icons.Default.Send, contentDescription = "WhatsApp", tint = IpakWhatsAppGreen, modifier = Modifier.size(18.dp))
        }

        IconButton(
          onClick = onCall,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(IpakNavy.copy(alpha = 0.08f))
            .testTag("donor_call_button_${donor.id}"),
        ) {
          Icon(Icons.Default.Phone, contentDescription = "Call", tint = IpakNavy, modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDonorDialog(
  onDismiss: () -> Unit,
  onSave: (BloodDonor) -> Unit,
) {
  var name by remember { mutableStateOf("") }
  var designation by remember { mutableStateOf("") }
  var department by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var bloodGroup by remember { mutableStateOf("O+") }
  var empCode by remember { mutableStateOf("") }
  var expandedGroup by remember { mutableStateOf(false) }

  val groups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

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
          text = "Register as Blood Donor",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = IpakNavy,
        )
        Text(
          text = "Your info will be accessible to colleagues during medical emergencies.",
          fontSize = 11.sp,
          color = IpakTextSecondary,
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Full Name *") },
          modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = designation,
            onValueChange = { designation = it },
            label = { Text("Designation") },
            modifier = Modifier.weight(1f).testTag("reg_designation_input"),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
          )
          OutlinedTextField(
            value = empCode,
            onValueChange = { empCode = it },
            label = { Text("Emp Code") },
            modifier = Modifier.weight(0.8f).testTag("reg_empcode_input"),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = department,
          onValueChange = { department = it },
          label = { Text("Department *") },
          modifier = Modifier.fillMaxWidth().testTag("reg_department_input"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Mobile Phone / WhatsApp *") },
          modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Blood Group Selector
        ExposedDropdownMenuBox(
          expanded = expandedGroup,
          onExpandedChange = { expandedGroup = !expandedGroup },
        ) {
          OutlinedTextField(
            value = bloodGroup,
            onValueChange = {},
            readOnly = true,
            label = { Text("Blood Group *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor()
              .testTag("reg_bloodgroup_selector"),
            shape = RoundedCornerShape(10.dp),
          )
          ExposedDropdownMenu(
            expanded = expandedGroup,
            onDismissRequest = { expandedGroup = false },
          ) {
            groups.forEach { g ->
              DropdownMenuItem(
                text = { Text(g, fontWeight = FontWeight.Bold) },
                onClick = {
                  bloodGroup = g
                  expandedGroup = false
                },
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

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
              if (name.isNotBlank() && phone.isNotBlank() && department.isNotBlank()) {
                onSave(
                  BloodDonor(
                    id = System.currentTimeMillis().toString(),
                    name = name.trim(),
                    designation = if (designation.isNotBlank()) designation.trim() else "Staff",
                    department = department.trim(),
                    phone = phone.trim(),
                    bloodGroup = bloodGroup,
                    empCode = empCode.trim(),
                    timestamp = "Just now",
                  )
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = IpakEmergencyRed, contentColor = Color.White),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1.2f).testTag("reg_submit_button"),
            enabled = name.isNotBlank() && phone.isNotBlank() && department.isNotBlank(),
          ) {
            Text("Register", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
