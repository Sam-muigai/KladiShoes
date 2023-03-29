package com.sam.kladishoes.presentation.accountScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sam.kladishoes.presentation.components.CustomButton
import com.sam.kladishoes.ui.theme.yusei

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    email: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = email,
                        style = MaterialTheme.typography.h6.copy(
                            fontFamily = yusei
                        )
                    )
                }

                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    text = "LOG OUT",
                    shape = MaterialTheme.shapes.medium,
                    onClick = onClick
                )
            }
        }
    }
}