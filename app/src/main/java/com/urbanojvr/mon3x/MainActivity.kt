package com.urbanojvr.mon3x

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
import com.urbanojvr.mon3x.model.Expense
import com.urbanojvr.mon3x.service.ExpenseService
import com.urbanojvr.mon3x.ui.theme.Mon3xTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mon3xTheme {
                val expenseService = ExpenseService()
                NewExpense(expenseService)
            }
        }
    }
}


@Composable
fun NewExpense(expenseService: ExpenseService) {
    var expenseDate by remember { mutableStateOf(LocalDate.now()) }
    var expenseConcept by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

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
        AmountInput(expenseAmount, onAmountChanged = {expenseAmount = it})
        SaveButton(
            expenseService,
            expenseDate,
            expenseConcept,
            expenseAmount
        )
    }
}

@Composable
private fun AmountInput(amount: String, onAmountChanged: (String) -> Unit) {
    var isValid by remember { mutableStateOf(true) }

    OutlinedTextField(
        value = amount,
        onValueChange = { newAmount ->
            if (validAmount(newAmount)) {
                onAmountChanged(newAmount)
                isValid = true
            } else {
                isValid = false
            }
        },
        label = { Text(stringResource(R.string.amount)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

private fun validAmount(amount: String): Boolean {
    val regex = Regex("^(\\d+(,\\d{0,2})?)?\$")

    return regex.matches(amount)
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
    val dateDialogState = rememberMaterialDialogState()
    var selectedDate by remember { mutableStateOf(initialDate) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { dateDialogState.show() }) {
            Text(text = stringResource(id = R.string.pickDate))
        }
        Text(text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
        Spacer(modifier = Modifier.height(16.dp))
    }

    ExpenseDateDialog(
        dateDialogState = dateDialogState,
        onDateChanged = {
            selectedDate = it
            onDateChanged(it)
        }
    )
}

@Composable
private fun ExpenseDateDialog(dateDialogState: MaterialDialogState, onDateChanged: (LocalDate) -> Unit) {
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.OK))
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

@Composable
private fun SaveButton(expenseService: ExpenseService, expenseDate: LocalDate, expenseConcept: String, expenseAmount: String) {
    val context = LocalContext.current
    val okText = stringResource(R.string.savedUppercase)

    Button(
        onClick = {
            expenseService.saveExpense(Expense(expenseAmount, expenseConcept, expenseDate))
            Toast.makeText(
                context,
                okText,
                Toast.LENGTH_LONG
            ).show()
        },
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Text(stringResource(R.string.save))
    }
}