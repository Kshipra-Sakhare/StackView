package com.example.stackview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.stackview.stackview.model.StackItem
import com.example.stackview.stackview.model.StackItemState
import com.example.stackview.stackview.viewmodel.StackViewModel
import com.example.stackview.ui.theme.StackviewTheme

class MainActivity : ComponentActivity() {
    private val viewModel: StackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StackviewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Observe the stackItems and loading state
                    val items by viewModel.stackItems.observeAsState(emptyList())
                    val isLoading by viewModel.isLoading.observeAsState(initial = true)

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        when {
                            isLoading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            items.isEmpty() -> {
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No items found",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Button(
                                        onClick = { viewModel.fetchItems() },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                            else -> {
                                StackViewCompose(
                                    items = items,
                                    onItemClick = { itemId ->
                                        viewModel.toggleItemState(itemId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StackviewTheme {
        Greeting("Android")
    }
}

@Composable
fun StackViewCompose(
    items: List<StackItem>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items.forEachIndexed { index, item ->
            StackItemCard(
                item = item,
                index = index,
                totalItems = items.size,
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

@Composable
private fun StackItemCard(
    item: StackItem,
    index: Int,
    totalItems: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            )
            .zIndex(totalItems - index.toFloat()),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (item.state == StackItemState.EXPANDED) 8.dp else 4.dp
        )
    ) {
        when (item.state) {
            StackItemState.EXPANDED -> ExpandedContent(item)
            StackItemState.COLLAPSED -> CollapsedContent(item)
        }
    }
}

@Composable
private fun CollapsedContent(item: StackItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ExpandedContent(item: StackItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = item.description,
            fontSize = 14.sp
        )
    }
}
