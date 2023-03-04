package com.example.ekidungmantram.admin.kakawin

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ekidungmantram.R
import com.example.ekidungmantram.user.pupuh.AddAudioPupuhNewActivity
import kotlinx.android.synthetic.main.activity_record_audio_kakawin.*
import kotlinx.android.synthetic.main.activity_record_audio_pupuh.*
import java.util.*

class RecordAudioKakawinActivity : AppCompatActivity() {
    lateinit var mr : MediaRecorder
    var random: Random? = null
    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"
    private var id_kakawin: Int = 0
    private lateinit var nama_kakawin: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio_kakawin)
        random = Random()
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kakawin")
        }
        if (bundle != null) {
            nama_kakawin = bundle.getString("nama_kakawin").toString()
        }

        cancelSubmitAddAudioKakawinAdmin1.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(this, AddAudioKakawinNewActivity::class.java)
            bundle.putInt("id_kakawin", id_kakawin)
            bundle.putString("nama_kakawin", nama_kakawin)
            intent.putExtras(bundle)
            startActivity(intent)

        }

//        var path : String = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp" //store the data
        var path : String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp"
        mr = MediaRecorder()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        startAudioKakawin.isEnabled = true

        //start recording
        startAudioKakawin.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            stopAudioKakawin.isEnabled = true
            startAudioKakawin.isEnabled = false
        }

        //stop recording
        stopAudioKakawin.setOnClickListener {
            mr.stop()
            startAudioKakawin.isEnabled = true
            stopAudioKakawin.isEnabled = false

//            Toast.makeText(this, "Recording Completed", Toast.LENGTH_LONG).show()
//            val returnIntent = Intent()
//            returnIntent.putExtra("result", path)
//            setResult(RESULT_OK, returnIntent)
//            finish()

        }

//        play recording
        playAudioKakawin.setOnClickListener {
            var mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }

        submitAudioKakawinAdmin.setOnClickListener {
            Toast.makeText(this, "Audio Record Disimpan", Toast.LENGTH_LONG).show()
            val returnIntent = Intent()
//            val audioFile = File(path)
            returnIntent.putExtra("result", path)
//            returnIntent.putExtra("result", audioFile)
            setResult(RESULT_OK, returnIntent)
            finish()
        }


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            startAudioKakawin.isEnabled = true
            stopAudioKakawin.isEnabled = false
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startAudioKakawin.isEnabled = true
            stopAudioKakawin.isEnabled = false
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