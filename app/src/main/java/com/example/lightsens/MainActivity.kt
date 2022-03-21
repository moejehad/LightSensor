package com.example.lightsens

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity() , SensorEventListener {

    private lateinit var senserManager : SensorManager
    private var brightness : Sensor? = null
    private lateinit var text : TextView
    private lateinit var pb : CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        text = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)

        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        senserManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = senserManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    private fun brightness(brighness : Float) : String {
        return when(brighness.toInt()){
            0 -> "Pitch black"
            in 1..10 -> "Dark"
            in 11..50 -> "Gray"
            in 51..5000 -> "Normal"
            in 5001..25000 -> "Incredbly bright"
            else -> "This light will blind you"
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT){
            val light = event.values[0]

            text.text = " Sensor : $light\n${brightness(light)}"
            pb.setProgressWithAnimation(light)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        senserManager.registerListener(this,brightness,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        senserManager.unregisterListener(this)
    }
}