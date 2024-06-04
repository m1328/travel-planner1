@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.example.travelplanner.ui.theme
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import com.example.travelplanner.data.PackingListItem
import com.example.travelplanner.viewmodel.PackingListViewModel

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PackingListScreen(viewModel: PackingListViewModel) {
    val items by viewModel.items.observeAsState(initial = emptyList())
    var newItem by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Travel Planner") }) },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Lista pakowania",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Tutaj dodaj wszystkie rzeczy, o których nie możesz zapomnieć przed wyjazdem.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = newItem,
                    onValueChange = { newItem = it },
                    label = { Text("Nowy element") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.addItem(newItem)
                            newItem = ""
                            keyboardController?.hide()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.addItem(newItem)
                    newItem = ""
                    keyboardController?.hide()
                }) {
                    Text("Dodaj")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items.forEachIndexed { index, item ->
                        item {
                            PackingListItemView(
                                item = item,
                                onCheckedChange = { viewModel.toggleItemChecked(index) }
                            ) { viewModel.removeItem(index) }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PackingListItemView(item: PackingListItem, onCheckedChange: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text = item.text, modifier = Modifier.weight(1f))
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onCheckedChange() }
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}