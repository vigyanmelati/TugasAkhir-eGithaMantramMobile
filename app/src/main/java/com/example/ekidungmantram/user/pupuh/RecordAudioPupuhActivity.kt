package com.example.ekidungmantram.user.pupuh

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ekidungmantram.R
import kotlinx.android.synthetic.main.activity_record_audio_pupuh.*
import java.io.File
import java.util.*


class RecordAudioPupuhActivity : AppCompatActivity() {
    lateinit var mr : MediaRecorder
    var random: Random? = null
    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio_pupuh)

        random = Random()

//        var path : String = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp" //store the data
        var path : String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.mp4"
        mr = MediaRecorder()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        startAudioPupuh.isEnabled = true

        //start recording
        startAudioPupuh.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
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

//            Toast.makeText(this, "Recording Completed", Toast.LENGTH_LONG).show()
//            val returnIntent = Intent()
//            returnIntent.putExtra("result", path)
//            setResult(RESULT_OK, returnIntent)
//            finish()

        }

//        play recording
        playAudioPupuh.setOnClickListener {
            var mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }

        submitAudioPupuhAdmin.setOnClickListener {
            Toast.makeText(this, "Audio Record Disimpan", Toast.LENGTH_LONG).show()
            val returnIntent = Intent()
//            val audioFile = File(path)
            returnIntent.putExtra("result", path)
//            returnIntent.putExtra("result", audioFile)
            setResult(RESULT_OK, returnIntent)
            finish()
        }


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            startAudioPupuh.isEnabled = true
            stopAudioPupuh.isEnabled = false
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

    fun CreateRandomAudioFileName(string: Int): String? {
        val stringBuilder = StringBuilder(string)
        var i = 0
        while (i < string) {
            stringBuilder.append(RandomAudioFileName[random!!.nextInt(RandomAudioFileName.length)])
            i++
        }
        return stringBuilder.toString()
    }

}