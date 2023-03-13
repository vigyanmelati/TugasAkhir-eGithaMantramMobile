package com.example.ekidungmantram.admin.laguanak

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
import com.example.ekidungmantram.admin.kidung.AddAudioKidungNewActivity
import kotlinx.android.synthetic.main.activity_record_audio_kakawin.*
import kotlinx.android.synthetic.main.activity_record_audio_kidung.*
import kotlinx.android.synthetic.main.activity_record_audio_lagu_anak.*
import java.util.*

class RecordAudioLaguAnakActivity : AppCompatActivity() {
    lateinit var mr : MediaRecorder
    var random: Random? = null
    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"
    private var id_lagu_anak: Int = 0
    private lateinit var nama_lagu_anak: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio_lagu_anak)
        random = Random()
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_lagu_anak = bundle.getInt("id_lagu_anak")
        }
        if (bundle != null) {
            nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()
        }

        cancelSubmitAddAudioLaguAnakAdmin1.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(this, AddAudioLaguAnakNewActivity::class.java)
            bundle.putInt("id_lagu_anak", id_lagu_anak)
            bundle.putString("nama_lagu_anak", nama_lagu_anak)
            intent.putExtras(bundle)
            startActivity(intent)
        }

//        var path : String = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp" //store the data
        var path : String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp"
        mr = MediaRecorder()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        startAudioLaguAnak.isEnabled = true

        //start recording
        startAudioLaguAnak.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr.setMaxDuration(300000)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            stopAudioLaguAnak.isEnabled = true
            startAudioLaguAnak.isEnabled = false
            mr.setOnInfoListener { mr, what, extra ->
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    // stop the recording when the maximum duration is reached
                    Toast.makeText(
                        this,
                        "Rekam audio dihentikan karena sudah melebihi durasi maksimum perekaman",
                        Toast.LENGTH_LONG
                    ).show()
                    mr.stop()
                    mr.release()
                    startAudioLaguAnak.isEnabled = true
                    stopAudioLaguAnak.isEnabled = false
                    // perform any other actions you need to do when the recording stops
                }
            }
        }

        //stop recording
        stopAudioLaguAnak.setOnClickListener {
            mr.stop()
            startAudioLaguAnak.isEnabled = true
            stopAudioLaguAnak.isEnabled = false

//            Toast.makeText(this, "Recording Completed", Toast.LENGTH_LONG).show()
//            val returnIntent = Intent()
//            returnIntent.putExtra("result", path)
//            setResult(RESULT_OK, returnIntent)
//            finish()

        }

//        play recording
        playAudioLaguAnak.setOnClickListener {
            var mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }

        submitAudioLaguAnakAdmin.setOnClickListener {
            Toast.makeText(this, "Audio Record Disimpan", Toast.LENGTH_LONG).show()
            val returnIntent = Intent()
//            val audioFile = File(path)
            returnIntent.putExtra("result", path)
//            returnIntent.putExtra("result", audioFile)
            setResult(RESULT_OK, returnIntent)
            finish()
        }


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            startAudioLaguAnak.isEnabled = true
            stopAudioLaguAnak.isEnabled = false
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startAudioLaguAnak.isEnabled = true
            stopAudioLaguAnak.isEnabled = false
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