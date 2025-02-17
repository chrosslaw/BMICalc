package com.example.bmicalc

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Instead of setContentView, use setContent for Compose
        setContent {
            MaterialTheme{
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayRows(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun CalcButton(height: Int, weight: Int) {
    // Use a Box to position the button at the bottom center.
    Box(
        modifier = Modifier.padding(top = 50.dp)
    ) {
        Button(
            onClick = { /* Handle the click here */ },
            modifier = Modifier.padding(20.dp)
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            Text("Calculate", fontSize = 24.sp, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun InputBox(labelText: String, textState: MutableState<String>, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text(labelText) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}
@Composable
fun Title() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp),
    ) {
        Text("BMI Calculator",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
}


@Composable
fun InputDisplayRow(weightState: MutableState<String>,heightState: MutableState<String>,modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Title()
        InputBox("Add your height", heightState)
        InputBox("Add your weight", weightState)
    }
}

@Composable
fun TextDisplayRow(displayText: String, weightState: MutableState<String>,heightState: MutableState<String>, modifier: Modifier = Modifier){

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row( modifier = Modifier.fillMaxWidth()){
            Text(displayText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
        }
        Row( modifier = Modifier.fillMaxWidth()) {
            CalcButton(heightState.value.toIntOrNull() ?: 0, weightState.value.toIntOrNull() ?: 0)
        }
    }
}

@Composable
fun DisplayRows(modifier: Modifier = Modifier){
    val weightState = remember { mutableStateOf("") }
    val heightState = remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        InputDisplayRow(weightState,heightState)
        TextDisplayRow("DISPLAY TEXT",weightState,heightState)
    }
}


