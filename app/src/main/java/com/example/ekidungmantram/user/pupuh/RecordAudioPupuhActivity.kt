package com.example.ekidungmantram.user.pupuh

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.example.ekidungmantram.R
import kotlinx.android.synthetic.main.activity_record_audio_pupuh.*
import java.util.jar.Manifest

class RecordAudioPupuhActivity : AppCompatActivity() {
    lateinit var mr : MediaRecorder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio_pupuh)

        var path : String = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp" //store the data
        mr = MediaRecorder()
        startAudioPupuh.isEnabled = false
        stopAudioPupuh.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
            startAudioPupuh.isEnabled = true

            //start recording
            startAudioPupuh.setOnClickListener {
                mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mr.setOutputFile(path)
                mr.prepare()
                mr.start()
                stopAudioPupuh.isEnabled = true
                startAudioPupuh.isEnabled = false
            }

            //stop recording
            stopAudioPupuh.setOnClickListener {
                mr.stop()
                startAudioPupuh.isEnabled = true
                stopAudioPupuh.isEnabled = false

            }

            //play recording
            playAudioPupuh.setOnClickListener {
                var mp = MediaPlayer()
                mp.setDataSource(path)
                mp.prepare()
                mp.start()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startAudioPupuh.isEnabled = true
        }
    }
}