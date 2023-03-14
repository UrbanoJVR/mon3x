package com.urbanojvr.mon3x

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.urbanojvr.mon3x.ui.theme.Mon3xTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mon3xTheme {
                NewExpense()
            }
        }
    }
}


@Composable
fun NewExpense() {
//    val context = LocalContext.current
    var expenseDate by remember { mutableStateOf(LocalDate.now()) }
    var expenseConcept by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf(BigDecimal.ZERO) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        ExpenseDatePicker(expenseDate, onDateChanged = { expenseDate = it })
        ConceptInput(expenseConcept, onConceptChanged = {expenseConcept = it})
        AmountInput(expenseAmount, onAmountChange = {expenseAmount = it})
        SaveButton()
    }
}

@Composable
private fun SaveButton() {
    Button(
        onClick = { /* ... */ },
        // Uses ButtonDefaults.ContentPadding by default
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Text("Like")
    }
}

@Composable
private fun AmountInput(amount: BigDecimal, onAmountChange: (BigDecimal) -> Unit) {
    OutlinedTextField(
        value = amount.toPlainString(),
        onValueChange = { onAmountChange(it.toBigDecimalOrNull() ?: BigDecimal.ZERO)},
        label = { Text(stringResource(R.string.amount)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun ConceptInput(concept: String, onConceptChanged: (String) -> Unit) {
    OutlinedTextField(
        value = concept,
        onValueChange = onConceptChanged,
        label = { Text(stringResource(R.string.concept)) }
    )
}

@Composable
fun ExpenseDatePicker(initialDate: LocalDate, onDateChanged: (LocalDate) -> Unit) {
    val formattedDate = remember { initialDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) }
    val dateDialogState = rememberMaterialDialogState()

    ExpenseDatePickerContent(formattedDate = formattedDate, dateDialogState = dateDialogState, onDateChanged = onDateChanged)
}

@Composable
private fun ExpenseDatePickerContent(formattedDate: String, dateDialogState: MaterialDialogState, onDateChanged: (LocalDate) -> Unit) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { dateDialogState.show() }) {
            Text(text = stringResource(id = R.string.pickDate))
        }
        Text(text = formattedDate)
        Spacer(modifier = Modifier.height(16.dp))
    }

    ExpenseDateDialog(
        dateDialogState = dateDialogState,
        context = context,
        onDateChanged = onDateChanged
    )
}

@Composable
private fun ExpenseDateDialog(dateDialogState: MaterialDialogState, context: Context, onDateChanged: (LocalDate) -> Unit) {
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "OK") {
                Toast.makeText(
                    context,
                    "Clicked Ok",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = stringResource(id = R.string.pickADate),
            onDateChange = onDateChanged
        )
    }
}