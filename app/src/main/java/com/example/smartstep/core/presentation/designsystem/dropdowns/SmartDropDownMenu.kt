package com.example.smartstep.core.presentation.designsystem.dropdowns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun <T> PopUpDropDownMenu(
    items: List<T>,
    itemDisplayText: (T) -> String,
    selected: T,
    onDismissRequest: () -> Unit,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    dropDownOffset: IntOffset = IntOffset.Zero
) {
    Popup(
        onDismissRequest = onDismissRequest,
        offset = dropDownOffset
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .dropShadow(
                    shape = RoundedCornerShape(8.dp),
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.tertiary,
                        spread = (-4).dp,
                        offset = DpOffset(x = 0.dp, y = 12.dp),
                        radius = 16.dp
                    )
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ) {
                items.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelected(item) }
                            .then(
                                if (selected == item)
                                    Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                else
                                    Modifier
                            )
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = itemDisplayText(item),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        if (selected == item)
                            Icon(
                                painter = painterResource(id = R.drawable.outline_check_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PopUpDropDownMenuPreview() {
    SmartStepTheme {
        PopUpDropDownMenu(
            items = listOf("Female", "Male"),
            itemDisplayText = { it },
            selected = "Female",
            onDismissRequest = {},
            onSelected = {},

            )
    }
}