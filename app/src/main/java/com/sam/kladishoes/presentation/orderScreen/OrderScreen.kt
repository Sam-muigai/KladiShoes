package com.sam.kladishoes.presentation.orderScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.kladishoes.presentation.components.TitleSection
import com.sam.kladishoes.ui.theme.Orange

@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val orders = viewModel.orders.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TitleSection(
                leadingIcon = Icons.Default.ArrowBack,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Orders"
                    )
                },
                text = "Your Orders",
                onLeadingIconClicked = {
                    onBackClicked()
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            orders.value.forEachIndexed { index, orders ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "orders :#${index + 1}")
                            Text(text = "Ksh ${orders.total_price}")
                        }
                        Text(
                            text = orders.delivered,
                            color = if (orders.delivered == "Pending") Orange else Color.Green
                        )
                    }
                    Divider()
                }
            }
        }
    }
}