package com.example.ekidungmantram.admin.kajidharmagita

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AudioDharmagitaModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_detail_audio_dharmagita_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAudioDharmagitaNeedApprovalActivity : AppCompatActivity() {
    lateinit var runnable :Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_audio_dharmagita_need_approval)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val postID = bundle.getInt("id_audio_dharmagita")
            getDetailData(postID)
            acceptAudioDharmagitaNA.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Terima Audio Dharmagita")
                    .setMessage("Apakah anda yakin ingin terima audio Dharmagita ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "yes")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            rejectAudioDharmagitaNA.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Tolak Audio Dharmagita")
                    .setMessage("Apakah anda yakin ingin menolak audio Dharmagita ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "no")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }
        backToAudioDharmagitaNA.setOnClickListener {
            val intent = Intent(this,ListAudioDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun accMantram(postID: Int, s: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengupdate Data")
        progressDialog.show()
        ApiService.endpoint.approveAudioDharmagita(postID, s).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailAudioDharmagitaNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailAudioDharmagitaNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailAudioDharmagitaNeedApprovalActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, ListAudioDharmagitaNeedApprovalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailAudioDharmagita(id).enqueue(object: Callback<AudioDharmagitaModel> {
            override fun onResponse(
                call: Call<AudioDharmagitaModel>,
                response: Response<AudioDharmagitaModel>
            ) {
                val result = response.body()!!
                result.let {
                    detailNamaAudioDharmagitaNA.text  = result.judul_audio
                    detailJenisAudioDharmagitaNA.text = result.nama_post
                    val audio_constant = Constant.AUDIO_URL + result.audio
                    val audio_uri = audio_constant.toUri()
//                    val audio_uri = result.audio.toUri()
                    Log.d("audio_uri",audio_uri.toString())
                    val mediaplayer = MediaPlayer.create(this@DetailAudioDharmagitaNeedApprovalActivity,audio_uri)
                    seekbar_audio_NA.progress = 0
                    seekbar_audio_NA.max = mediaplayer.duration
                    play_btn_NA.setOnClickListener {
                        if(!mediaplayer.isPlaying){
                            mediaplayer.start()
                            play_btn_NA.setImageResource(R.drawable.ic_pause)
                        }else{
                            mediaplayer.pause()
                            play_btn_NA.setImageResource(R.drawable.ic_play)
                        }
                    }
                    seekbar_audio_NA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                            if(p2){
                                mediaplayer.seekTo(p1)
                            }
                        }

                        override fun onStartTrackingTouch(p0: SeekBar?) {
                            TODO("Not yet implemented")
                        }

                        override fun onStopTrackingTouch(p0: SeekBar?) {
                            TODO("Not yet implemented")
                        }

                    })

                    runnable = Runnable {
                        seekbar_audio_NA.progress = mediaplayer.currentPosition
                        handler.postDelayed(runnable,1000)
                    }
                    handler.postDelayed(runnable,1000)
                    mediaplayer.setOnCompletionListener {
                        play_btn_NA.setImageResource(R.drawable.ic_play)
                        seekbar_audio_NA.progress = 0
                    }
//                    if(result.gambar_audio != null) {
//                        Glide.with(this@DetailAudioDharmagitaNeedApprovalActivity)
//                            .load(Constant.IMAGE_URL + result.gambar_audio).into(imageAudioDharmagitaNA)
////                            .load(result.gambar_audio).into(imageAudioDharmagitaNA)
//                    }else{
                        imageAudioDharmagitaNA.setImageResource(R.drawable.music)
//                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<AudioDharmagitaModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("AudioKakawinActivity", message)
    }

    private fun setShimmerToStop() {
        shimmerAudioDharmagitaNA.stopShimmer()
        shimmerAudioDharmagitaNA.visibility = View.GONE
        scrollAudioDharmagitaNA.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ListAudioDharmagitaNeedApprovalActivity::class.java)
        startActivity(intent)
    }
}