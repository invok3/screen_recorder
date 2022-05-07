package invoke.freeapps.screenrecorder

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var startBtn: Button? = findViewById<Button>(R.id.start_recording_button)
        startBtn?.setOnClickListener{
            startScreenRecordingService()
        }
    }

    private fun startScreenRecordingService() {
        println("Starting Service..")
        startService(Intent(this, RecordingService::class.java))
    }


}