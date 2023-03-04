package com.example.ekidungmantram.user.pupuh

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
import kotlinx.android.synthetic.main.activity_record_audio_edit_pupuh.*
import kotlinx.android.synthetic.main.activity_record_audio_pupuh.*
import java.util.*

class RecordAudioEditPupuhActivity : AppCompatActivity() {
    lateinit var mr : MediaRecorder
    var random: Random? = null
    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"
    private var id_pupuh: Int = 0
    private var id_audio: Int = 0
    private lateinit var nama_pupuh: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio_edit_pupuh)
        random = Random()
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh")
            id_audio = bundle.getInt("id_audio_pupuh")
        }
        if (bundle != null) {
            nama_pupuh = bundle.getString("nama_pupuh").toString()
        }

        cancelSubmitAddAudioKidungAdminEdit.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(this, EditAudioPupuhNewActivity::class.java)
            bundle.putInt("id_pupuh", id_pupuh)
            bundle.putInt("id_audio_pupuh", id_audio)
            bundle.putString("nama_pupuh", nama_pupuh)
            intent.putExtras(bundle)
            startActivity(intent)
        }

//        var path : String = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp" //store the data
        var path : String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp"
        mr = MediaRecorder()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        startAudioPupuhEdit.isEnabled = true

        //start recording
        startAudioPupuhEdit.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            stopAudioPupuhEdit.isEnabled = true
            startAudioPupuhEdit.isEnabled = false
        }

        //stop recording
        stopAudioPupuhEdit.setOnClickListener {
            mr.stop()
            startAudioPupuhEdit.isEnabled = true
            stopAudioPupuhEdit.isEnabled = false

//            Toast.makeText(this, "Recording Completed", Toast.LENGTH_LONG).show()
//            val returnIntent = Intent()
//            returnIntent.putExtra("result", path)
//            setResult(RESULT_OK, returnIntent)
//            finish()

        }

//        play recording
        playAudioPupuhEdit.setOnClickListener {
            var mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }

        submitAudioPupuhAdminEdit.setOnClickListener {
            Toast.makeText(this, "Audio Record Disimpan", Toast.LENGTH_LONG).show()
            val returnIntent = Intent()
//            val audioFile = File(path)
            returnIntent.putExtra("result", path)
//            returnIntent.putExtra("result", audioFile)
            setResult(RESULT_OK, returnIntent)
            finish()
        }


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            startAudioPupuhEdit.isEnabled = true
            stopAudioPupuhEdit.isEnabled = false
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startAudioPupuhEdit.isEnabled = true
            stopAudioPupuhEdit.isEnabled = false
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