package com.example.smartstep.smart.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.buttons.PrimaryButton
import com.example.smartstep.core.presentation.designsystem.dropdowns.PopUpDropDownMenu
import com.example.smartstep.core.presentation.designsystem.fields.SmartTextField
import com.example.smartstep.core.presentation.designsystem.pickerdialogs.PickerDialog
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.util.ObserveAsEvents
import com.example.smartstep.smart.presentation.profile.components.ProfileTopAppBar
import com.example.smartstep.smart.presentation.profile.models.Gender
import com.example.smartstep.smart.presentation.profile.models.UnitType
import com.example.smartstep.smart.presentation.profile.models.Units
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ProfileScreenRoot(
    onSkip: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProfileEvent.OnStart -> onSkip()
        }
    }

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var textFieldHeight by remember { mutableIntStateOf(0) }
    var textFieldWidth by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                isInitialScreen = state.isInitialScreen,
                onSkip = {
                    onAction(ProfileAction.OnSkip)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentWindowInsets = WindowInsets.systemBars,
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 394.dp)
                    .padding(16.dp)
            ) {
                if (state.isInitialScreen)
                    Text(
                        text = stringResource(R.string.profile_info),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                else
                    Text(
                        text = "Personal Settings",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SmartTextField(
                            label = stringResource(R.string.gender),
                            value = state.gender.value.asString(),
                            onClick = { onAction(ProfileAction.OnGenderClick) },
                            modifier = Modifier
                                .onSizeChanged {
                                    textFieldHeight = it.height
                                    textFieldWidth = it.width
                                }
                        )
                        if (state.isGenderDropdownExpanded)
                            PopUpDropDownMenu(
                                items = Gender.entries,
                                itemDisplayText = { it.value.asString(context) },
                                selected = state.gender,
                                onDismissRequest = {},
                                dropDownOffset = IntOffset(
                                    x = 0,
                                    y = textFieldHeight
                                ),
                                onSelected = { onAction(ProfileAction.OnGenderSelected(it)) },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) {
                                        textFieldWidth.toDp()
                                    })
                            )
                    }
                    SmartTextField(
                        label = stringResource(R.string.height),
//                        value = stringResource(R.string.number_cm, state.height),
                        value = state.formattedHeight.asString(),
                        onClick = { onAction(ProfileAction.OnHeightClick) }
                    )
                    SmartTextField(
                        label = stringResource(R.string.weight),
//                        value = stringResource(R.string.number_kg, state.weight),
                        value = state.formattedWeight.asString(),
                        onClick = { onAction(ProfileAction.OnWeightClick) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = if (state.isInitialScreen) stringResource(R.string.start) else stringResource(
                    R.string.save
                ),
                onClick = { onAction(ProfileAction.OnStarButtonClick) },
                modifier = Modifier
                    .widthIn(max = 394.dp)
                    .padding(horizontal = 16.dp)

            )
        }
    }

    if (state.isHeightDialogVisible)
        PickerDialog(
            titleResId = R.string.height,
            descriptionResId = R.string.used_to_calculate_distance,
            units = Units.entries.filter { it.unitType == UnitType.HEIGHT },
            selectedUnit = state.heightUnit,
            unitDisplayText = { it.value.asString(context) },
            onSelectedUnits = { onAction(ProfileAction.OnUnitSelected(it)) },
            options1 = state.heightUnit.range1,
            selectedOption1 = state.heightPicker,
            onSelected1 = { onAction(ProfileAction.OnHeightSelected(it)) },
            onCanceled = { onAction(ProfileAction.OnCancel(state.heightUnit)) },
            onConfirmed = { onAction(ProfileAction.OnConfirm(state.heightUnit)) },
            options2 = state.heightUnit.range2,
            selectedOption2 = state.heightPickerInches,
            onSelected2 = {
                if (state.heightUnit == Units.FT)
                    onAction(ProfileAction.OnHeightInchesSelected(it))
            },
            unit1ResId = if (state.heightUnit == Units.FT) R.string.ft else null,
            unit2ResId = if (state.heightUnit == Units.FT) R.string.inches else null
        )
    if (state.isWeightDialogVisible)
        PickerDialog(
            titleResId = R.string.weight,
            descriptionResId = R.string.used_to_calculate_calories,
            units = Units.entries.filter { it.unitType == UnitType.WEIGHT },
            selectedUnit = state.weightUnit,
            unitDisplayText = { it.value.asString(context) },
            onSelectedUnits = { onAction(ProfileAction.OnUnitSelected(it)) },
            options1 = state.weightUnit.range1.toList(),
            selectedOption1 = state.weightPicker,
            onSelected1 = { onAction(ProfileAction.OnWeightSelected(it)) },
            onCanceled = { onAction(ProfileAction.OnCancel(state.weightUnit)) },
            onConfirmed = { onAction(ProfileAction.OnConfirm(state.weightUnit)) },
        )
}

@Preview(widthDp = 800)
@Composable
private fun ProfileScreenPreview() {
    SmartStepTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}