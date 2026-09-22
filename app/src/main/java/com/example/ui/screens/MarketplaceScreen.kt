package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.IpakRepository
import com.example.model.MarketItem
import com.example.ui.components.dialPhone
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.IpakBorder
import com.example.ui.theme.IpakBorderLight
import com.example.ui.theme.IpakDeepNavy
import com.example.ui.theme.IpakLightBg
import com.example.ui.theme.IpakLime
import com.example.ui.theme.IpakNavy
import com.example.ui.theme.IpakSurface
import com.example.ui.theme.IpakTextMuted
import com.example.ui.theme.IpakTextPrimary
import com.example.ui.theme.IpakTextSecondary
import com.example.ui.theme.IpakWhatsAppGreen

@Composable
fun MarketplaceScreen(
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val items by IpakRepository.marketplaceItems.collectAsState()

  var selectedCategory by remember { mutableStateOf("ALL") }
  var searchQuery by remember { mutableStateOf("") }
  var showPostDialog by remember { mutableStateOf(false) }

  val categories = listOf("ALL", "Electronics", "Vehicles", "Furniture", "Fashion", "Others")

  val filteredItems = items.filter { item ->
    val matchesCategory = selectedCategory == "ALL" || item.category.equals(selectedCategory, ignoreCase = true)
    val matchesSearch = searchQuery.isBlank() ||
      item.title.contains(searchQuery, ignoreCase = true) ||
      item.sellerName.contains(searchQuery, ignoreCase = true) ||
      item.price.contains(searchQuery, ignoreCase = true)
    matchesCategory && matchesSearch
  }

  Scaffold(
    modifier = modifier.testTag("marketplace_screen"),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showPostDialog = true },
        containerColor = IpakNavy,
        contentColor = IpakLime,
        modifier = Modifier.testTag("fab_post_ad"),
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Default.Add, contentDescription = "Post an Ad")
          Spacer(modifier = Modifier.width(6.dp))
          Text("Sell an Item", fontWeight = FontWeight.Bold)
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
          Text(
            text = "IPAK Marketplace",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
          )
          Text(
            text = "Exclusive staff classifieds • Buy & sell directly with trusted colleagues",
            color = IpakLime,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
          )
        }
      }

      // Search & Category Chips
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(IpakSurface)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search items, cars, laptops...", fontSize = 13.sp) },
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
            .testTag("market_search_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IpakNavy,
            unfocusedBorderColor = IpakBorder,
          ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Categories Row
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          contentPadding = PaddingValues(vertical = 4.dp),
        ) {
          items(categories) { category ->
            val isSelected = selectedCategory == category
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) IpakNavy else IpakLightBg,
              border = if (isSelected) null else CardDefaults.outlinedCardBorder(),
              modifier = Modifier
                .clickable { selectedCategory = category }
                .testTag("market_cat_$category"),
            ) {
              Text(
                text = category,
                color = if (isSelected) IpakLime else IpakNavy,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
              )
            }
          }
        }
      }

      // Items List
      if (filteredItems.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          contentAlignment = Alignment.Center,
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = IpakTextMuted, modifier = Modifier.size(54.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No listings found in \"$selectedCategory\"",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = IpakNavy,
            )
            Text(
              text = "Be the first colleague to post an item for sale!",
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
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        ) {
          items(filteredItems, key = { it.id }) { item ->
            MarketItemCard(
              item = item,
              onCall = { dialPhone(context, item.phone) },
              onWhatsApp = {
                openWhatsApp(
                  context,
                  item.phone,
                  "Salam ${item.sellerName}, I saw your listing for \"${item.title}\" (${item.price}) on IPAK Marketplace. Is it still available?"
                )
              },
            )
          }
        }
      }
    }
  }

  // Post Ad Dialog
  if (showPostDialog) {
    PostMarketItemDialog(
      onDismiss = { showPostDialog = false },
      onSave = { newItem ->
        IpakRepository.addMarketItem(newItem)
        showPostDialog = false
        Toast.makeText(context, "Listing published to staff marketplace!", Toast.LENGTH_SHORT).show()
      }
    )
  }
}

@Composable
fun MarketItemCard(
  item: MarketItem,
  onCall: () -> Unit,
  onWhatsApp: () -> Unit,
) {
  val categoryIcon: ImageVector = when (item.category.lowercase()) {
    "electronics" -> Icons.Default.Laptop
    "vehicles" -> Icons.Default.ElectricCar
    "furniture" -> Icons.Default.Weekend
    else -> Icons.Default.ShoppingBag
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = IpakSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(IpakBorder, IpakBorderLight))),
    modifier = Modifier.fillMaxWidth().testTag("market_item_${item.id}"),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
      ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(IpakNavy.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(categoryIcon, contentDescription = null, tint = IpakNavy, modifier = Modifier.size(22.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = IpakLightBg,
            ) {
              Text(
                text = item.category.uppercase(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = IpakTextSecondary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = item.title,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = IpakNavy,
              maxLines = 2,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Price & Seller details
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(IpakLightBg)
          .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text("Asking Price", fontSize = 10.sp, color = IpakTextSecondary)
          Text(
            text = item.price,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = IpakNavy,
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text("Seller", fontSize = 10.sp, color = IpakTextSecondary)
          Text(
            text = item.sellerName,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = IpakNavy,
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Contact Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Button(
          onClick = onWhatsApp,
          colors = ButtonDefaults.buttonColors(containerColor = IpakWhatsAppGreen, contentColor = Color.White),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f).testTag("market_whatsapp_btn_${item.id}"),
        ) {
          Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Button(
          onClick = onCall,
          colors = ButtonDefaults.buttonColors(containerColor = IpakNavy, contentColor = Color.White),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f).testTag("market_call_btn_${item.id}"),
        ) {
          Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Call Seller", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostMarketItemDialog(
  onDismiss: () -> Unit,
  onSave: (MarketItem) -> Unit,
) {
  var title by remember { mutableStateOf("") }
  var price by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Electronics") }
  var sellerName by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var expandedCat by remember { mutableStateOf(false) }

  val catOptions = listOf("Electronics", "Vehicles", "Furniture", "Fashion", "Others")

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
          text = "Post an Ad on Marketplace",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = IpakNavy,
        )
        Text(
          text = "Visible to all IPAK group employees.",
          fontSize = 11.sp,
          color = IpakTextSecondary,
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Item Title / Description *") },
          placeholder = { Text("e.g. Honda City 2020 or iPhone 13") },
          modifier = Modifier.fillMaxWidth().testTag("post_title_input"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price (PKR) *") },
            placeholder = { Text("e.g. 85,000") },
            modifier = Modifier.weight(1f).testTag("post_price_input"),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
          )

          ExposedDropdownMenuBox(
            expanded = expandedCat,
            onExpandedChange = { expandedCat = !expandedCat },
            modifier = Modifier.weight(1f),
          ) {
            OutlinedTextField(
              value = category,
              onValueChange = {},
              readOnly = true,
              label = { Text("Category") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
              modifier = Modifier.menuAnchor(),
              shape = RoundedCornerShape(10.dp),
            )
            ExposedDropdownMenu(
              expanded = expandedCat,
              onDismissRequest = { expandedCat = false },
            ) {
              catOptions.forEach { opt ->
                DropdownMenuItem(
                  text = { Text(opt) },
                  onClick = {
                    category = opt
                    expandedCat = false
                  },
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = sellerName,
          onValueChange = { sellerName = it },
          label = { Text("Your Name & Department *") },
          placeholder = { Text("e.g. Ali Khan (SCM)") },
          modifier = Modifier.fillMaxWidth().testTag("post_seller_input"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Phone / WhatsApp *") },
          placeholder = { Text("0300-1234567") },
          modifier = Modifier.fillMaxWidth().testTag("post_phone_input"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
        )

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
              if (title.isNotBlank() && price.isNotBlank() && sellerName.isNotBlank() && phone.isNotBlank()) {
                val formattedPrice = if (!price.contains("PKR", ignoreCase = true)) "PKR $price" else price
                onSave(
                  MarketItem(
                    id = System.currentTimeMillis().toString(),
                    title = title.trim(),
                    price = formattedPrice,
                    category = category,
                    sellerName = sellerName.trim(),
                    phone = phone.trim(),
                    isMine = true,
                  )
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = IpakNavy, contentColor = IpakLime),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1.2f).testTag("post_submit_button"),
            enabled = title.isNotBlank() && price.isNotBlank() && sellerName.isNotBlank() && phone.isNotBlank(),
          ) {
            Text("Publish Ad", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
