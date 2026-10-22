package com.example.ca2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ca2.ui.theme.Ca2Theme
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelScreen()
        }
    }
}

@Composable
fun TravelScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        if (isLoading) {
            CircularProgressIndicator()
        }

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    result = ""

                    try {
                        coroutineScope {
                            supervisorScope {

                                val flights = async {
                                    delay(2000)
                                    "Flights: Available"
                                }

                                val hotels = async {
                                    delay(3000)
                                    throw Exception("Hotel API failed")
                                }

                                val cars = async {
                                    delay(1500)
                                    "Cars: Available"
                                }

                                val flightResult = try {
                                    flights.await()
                                } catch (e: Exception) {
                                    "Flights: Failed"
                                }

                                val hotelResult = try {
                                    hotels.await()
                                } catch (e: Exception) {
                                    "Hotels: Failed"
                                }

                                val carResult = try {
                                    cars.await()
                                } catch (e: Exception) {
                                    "Cars: Failed"
                                }

                                result = "$flightResult\n$hotelResult\n$carResult"
                            }
                        }
                    } catch (e: Exception) {
                        result = "Search failed"
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading
        ) {
            Text("Search Packages")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(result)
    }
}