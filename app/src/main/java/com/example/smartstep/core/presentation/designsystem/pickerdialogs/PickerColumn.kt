package com.example.smartstep.core.presentation.designsystem.pickerdialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun PickerColumn(
    items: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes unitResId: Int? = null
) {
    val lazyListState = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current

    val snappingLayout = remember(lazyListState) {
        SnapLayoutInfoProvider(lazyListState, SnapPosition.Start)
    }
    val snapFlingBehavior = rememberSnapFlingBehavior(snappingLayout)

    val selectedColor = MaterialTheme.colorScheme.surface

    val selectedItem by remember(lazyListState) {
        derivedStateOf {
            val visible = lazyListState.layoutInfo.visibleItemsInfo
            if (visible.size == 4)
                visible[2].key as Int
            else
                null
        }
    }

    LaunchedEffect(selected) {
        val index = items.indexOf(selected)
        if (index - 2 > -1) {
            lazyListState.scrollToItem(index - 2)
        }
    }

    LaunchedEffect(lazyListState) {
        launch {
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
                .filter { it.size == 4 }
                .collectLatest { items ->
                    val targetKey = items.getOrNull(2)?.key as? Int
                    targetKey?.let { onSelected(it) }
                }
        }

        launch {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .filter { lazyListState.isScrollInProgress }
                .collectLatest {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                }
        }
    }


    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = snapFlingBehavior,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .drawBehind {
                drawRect(
                    color = selectedColor,
                    topLeft = Offset(x = 0f, y = size.height / 4 * 2),
                    size = Size(size.width, size.height / 4)
                )
            }
    ) {
        items(items = items, key = { it }) { item ->
            val selected = item == selectedItem
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = item.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSecondary,
                )
                unitResId?.let { unit ->
                    Text(
                        text = stringResource(unit),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PickerColumnPreview() {
    SmartStepTheme {
        PickerColumn(
            items = (100..220).toList(),
            selected = 103,
            onSelected = {},
            unitResId = R.string.ft
        )
    }
}