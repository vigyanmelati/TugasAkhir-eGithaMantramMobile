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
    private var id_pupuh: Int = 0
    private lateinit var nama_pupuh: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio_pupuh)
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh")
        }
        if (bundle != null) {
            nama_pupuh = bundle.getString("nama_pupuh").toString()
        }

        cancelSubmitAddAudioKidungAdmin.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(this, AddAudioPupuhNewActivity::class.java)
            bundle.putInt("id_pupuh", id_pupuh)
            bundle.putString("nama_kat_pupuh_user", nama_pupuh)
            intent.putExtras(bundle)
            startActivity(intent)

        }

        random = Random()

//        var path : String = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp" //store the data
        var path : String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp"
        mr = MediaRecorder()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        startAudioPupuh.isEnabled = true

        //start recording
        startAudioPupuh.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            // set the maximum duration to 5 menit
            mr.setMaxDuration(10000)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            stopAudioPupuh.isEnabled = true
            startAudioPupuh.isEnabled = false

            mr.setOnInfoListener { mr, what, extra ->
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    // stop the recording when the maximum duration is reached
                    Toast.makeText(this,"Rekam audio dihentikan karena sudah melebihi durasi maksimum perekaman",Toast.LENGTH_LONG).show()
                    mr.stop()
                    mr.release()
                    startAudioPupuh.isEnabled = true
                    stopAudioPupuh.isEnabled = false
                    // perform any other actions you need to do when the recording stops
                }
            }
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
            stopAudioPupuh.isEnabled = false
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