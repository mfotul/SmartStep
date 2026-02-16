package com.example.smartstep.core.presentation.designsystem.pickerdialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun <T>UnitSwitch(
    items: List<T>,
    selected: T,
    itemDisplayText: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.surface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .drawBehind{
                drawRect(
                    color = borderColor,
                    topLeft = Offset(x = size.width / 2, y = 0f),
                    size = Size(width = 1.dp.toPx(), height = size.height)
                )
            }
    ) {
        items.forEach { item ->
            val selected = item == selected
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable{
                        onSelected(item)
                    }
                    .background(if (selected) MaterialTheme.colorScheme.secondary else Color.Transparent)
                    .padding(vertical = 12.dp)
                    .weight(1f)

            ) {
                if (selected)
                    Icon(
                        painter = painterResource(R.drawable.outline_check_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                    )
                Text(
                    text = itemDisplayText(item),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun UnitSwitchPreview() {
    SmartStepTheme {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.White)
        ) {
            UnitSwitch(
                items = listOf("cm", "ft/in"),
                selected = "cm",
                itemDisplayText = { it },
                onSelected = {},
                modifier = Modifier
            )
        }
    }
}