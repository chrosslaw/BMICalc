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
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayRows(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InputBox(labelText: String, textState: MutableState<String>, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text(labelText) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    )
}

@Composable
fun GenderInputBox(labelText: String, textState: MutableState<String>, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text(labelText) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun Title() {
    Text(
        "BMI Calculator",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun InputDisplaySection(
    weightState: MutableState<String>,
    heightState: MutableState<String>,
    genderState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Title()
        InputBox("Enter weight in lbs", weightState)
        InputBox("Enter height in inches", heightState)
        GenderInputBox("Enter gender (Male/Female)", genderState)
    }
}

@Composable
fun ResultSection(resultText: MutableState<String>, resultColor: MutableState<Color>, gender:String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text( "Gender: " + gender,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp))

        Text(
            resultText.value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = resultColor.value,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CalcButton(
    weight: Int,
    height: Int,
    gender: String,
    resultText: MutableState<String>,
    resultColor: MutableState<Color>
) {
    Button(
        onClick = {
            // Determine the gender (defaulting to MALE if not recognized)
            val genderEnum = when (gender.lowercase()) {
                "male" -> Gender.MALE
                "female" -> Gender.FEMALE
                else -> Gender.MALE
            }
            // Create a Person object and calculate BMI
            val person = Person(weight, height, genderEnum)
            val bmi = person.calculateBMI()
            val category = person.bmiStatus()
            // Update the result message and color
            resultText.value = "Your BMI is %.2f. ${category.message}".format(bmi)
            resultColor.value = category.color
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Calculate", fontSize = 24.sp)
    }
}

@Composable
fun DisplayRows(modifier: Modifier = Modifier) {
    val weightState = remember { mutableStateOf("") }
    val heightState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val resultText = remember { mutableStateOf("Enter your details and tap Calculate") }
    val resultColor = remember { mutableStateOf(Color.Black) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Top section with input fields and button
        Column {
            InputDisplaySection(weightState, heightState, genderState)
            CalcButton(
                weight = weightState.value.toIntOrNull() ?: 0,
                height = heightState.value.toIntOrNull() ?: 0,
                gender = genderState.value,
                resultText = resultText,
                resultColor = resultColor
            )
        }
        // Bottom section to display the result
        ResultSection(resultText, resultColor, genderState.value)
    }
}


enum class Gender {
    MALE, FEMALE
}

enum class BMIStatus(val message: String, val color: Color) {
    UNDERWEIGHT("You are underweight", Color.Red),
    NORMAL("Your weight is normal", Color.Green),
    OVERWEIGHT("You are overweight", Color.Red)
}

// Person class that encapsulates the BMI calculation.
class Person(val weight: Int, val height: Int, val gender: Gender) {
    // Calculate BMI using lbs and inches: (weight / (height^2)) * 703
    fun calculateBMI(): Double {
        if (height == 0) return 0.0
        return (weight.toDouble() / (height.toDouble() * height.toDouble())) * 703.0
    }

    // Determine the BMI category based on standard thresholds.
    fun bmiStatus(): BMIStatus {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5 -> BMIStatus.UNDERWEIGHT
            bmi < 25.0 -> BMIStatus.NORMAL
            else -> BMIStatus.OVERWEIGHT
        }
    }
}