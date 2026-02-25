package com.example.smartstep.smart.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeIcon(
    @DrawableRes id: Int,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(38.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.2f))
    ) {
        Icon(
            painter = painterResource(id),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    }
}