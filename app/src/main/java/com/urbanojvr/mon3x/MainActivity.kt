package com.urbanojvr.mon3x

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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

    fun resetValues() {
        expenseDate = LocalDate.now()
        expenseConcept = ""
        expenseAmount = ""
    }

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
            expenseAmount,
            onSaved = {resetValues()}
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
fun ExpenseDatePicker(date: LocalDate, onDateChanged: (LocalDate) -> Unit) {
    val dateDialogState = rememberMaterialDialogState()
//    var selectedDate by remember { mutableStateOf(date) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { dateDialogState.show() }) {
            Text(text = stringResource(id = R.string.pickDate))
        }
        Text(text = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
        Spacer(modifier = Modifier.height(16.dp))
    }

    ExpenseDateDialog(
        dateDialogState = dateDialogState,
        selectedDate = date,
        onDateChanged = {
            onDateChanged(it)
        }
    )
}

@Composable
private fun ExpenseDateDialog(dateDialogState: MaterialDialogState, selectedDate: LocalDate, onDateChanged: (LocalDate) -> Unit) {
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.OK))
            negativeButton(text = stringResource(R.string.cancel))
        }
    ) {
        datepicker(
            initialDate = selectedDate,
            title = stringResource(id = R.string.pickADate),
            onDateChange = onDateChanged
        )
    }
}

@Composable
private fun SaveButton(expenseService: ExpenseService, expenseDate: LocalDate, expenseConcept: String, expenseAmount: String, onSaved: () -> Unit) {
    val context = LocalContext.current
    val okText = stringResource(R.string.savedUppercase)
    val errText = stringResource(R.string.errorUppercase)

    Button(
        onClick = {
            try {
                expenseService.saveExpense(Expense(expenseAmount, expenseConcept, expenseDate))
                Toast.makeText(
                    context,
                    okText,
                    Toast.LENGTH_LONG
                ).show()
                onSaved()
            } catch (err: Exception) {
                Toast.makeText(
                    context,
                    errText,
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Icon(
            Icons.Filled.Done,
            contentDescription = stringResource(R.string.save),
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.save))
    }
}

@Preview()
@Composable
fun DefaultPreview() {
    val expenseService = ExpenseService()
    NewExpense(expenseService)
}