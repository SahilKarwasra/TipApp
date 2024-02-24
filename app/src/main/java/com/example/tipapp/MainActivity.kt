package com.example.tipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tipapp.components.InputText
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.util.calculateTotalBillAmount
import com.example.tipapp.util.calculateTotalTip
import com.example.tipapp.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                TipCalculator()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Composable
fun TipCalculator() {
    Surface(modifier = Modifier.padding(12.dp)) {
        Column() {
            MainContent()
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp)
            .clip(
                shape = CircleShape.copy(
                    all = CornerSize(16.dp)
                )
            ), color = Color.Cyan
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Per Person", style = MaterialTheme.typography.titleLarge
            )
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "$$total",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun MainContent() {
    BillForm() {
        Log.d("Bill", it)
    }
}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()

    val splitByState = remember {
        mutableIntStateOf(2)
    }

    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    TopHeader(totalPerPerson = totalPerPersonState.doubleValue)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clip(
                CircleShape.copy(CornerSize(12.dp))
            )
            .fillMaxWidth(),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            InputText(valueState = totalBillState,
                labelId = "Enter Bill",
                enable = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                })
            if (validState) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier
                            .align(
                                alignment = Alignment.CenterVertically
                            )
                    )
                    Spacer(modifier = modifier.width(120.dp))
                    Row(
                        modifier = modifier.padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundedIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                splitByState.intValue =
                                    if (splitByState.intValue > 1) splitByState.intValue - 1 else 1
                                totalPerPersonState.doubleValue = calculateTotalBillAmount(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.intValue,
                                    tipPercentage = tipPercentage
                                )
                            }
                        )
                        Text(
                            text = "${splitByState.intValue}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 12.dp, end = 12.dp)
                        )
                        RoundedIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                splitByState.intValue++
                                totalPerPersonState.doubleValue = calculateTotalBillAmount(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.intValue,
                                    tipPercentage = tipPercentage
                                )
                            }
                        )

                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Tip",
                        modifier = modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(
                        text = "$ ${tipAmountState.doubleValue}",
                        modifier = modifier.align(Alignment.CenterVertically)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercentage %")
                    Spacer(modifier = Modifier.height(12.dp))
                    Slider(
                        value = sliderPositionState.floatValue,
                        onValueChange = { newVal ->
                            sliderPositionState.floatValue = newVal
                            tipAmountState.doubleValue =
                                calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentage
                                )

                            totalPerPersonState.doubleValue = calculateTotalBillAmount(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.intValue,
                                tipPercentage = tipPercentage
                            )
                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
//                        steps = 5
                    )
                }

            }
        }
    }
}

