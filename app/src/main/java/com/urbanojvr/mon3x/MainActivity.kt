package com.urbanojvr.mon3x

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urbanojvr.mon3x.ui.theme.Mon3xTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        DatePicker()
        ConceptInput()
        AmountInput()
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
private fun AmountInput() {
    OutlinedTextField(
        value = "Hola",
        onValueChange = { var text = it },
        label = { Text(stringResource(R.string.amount)) }
    )
}

@Composable
private fun ConceptInput() {
    OutlinedTextField(
        value = "Hola",
        onValueChange = { var text = it },
        label = { Text(stringResource(R.string.concept)) }
    )
}

@Composable
private fun DatePicker() {
    val context = LocalContext.current

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd MMM yyyy").format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            dateDialogState.show()
        }) {
            Text(text = stringResource(R.string.pickDate))
        }
        Text(text = formattedDate)
        Spacer(modifier = Modifier.height(16.dp))
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
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
            title = stringResource(R.string.pickADate),
        ) {
            pickedDate = it
        }
    }
}