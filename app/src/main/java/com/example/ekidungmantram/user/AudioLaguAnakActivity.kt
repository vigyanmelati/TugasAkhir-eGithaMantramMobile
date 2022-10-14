package com.example.ekidungmantram.user

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BaitLaguAnakAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitLaguAnakModel
import com.example.ekidungmantram.model.DetailLaguAnakModel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.*
import kotlinx.android.synthetic.main.activity_video_lagu_anak.*
import kotlinx.android.synthetic.main.activity_video_lagu_anak.youtubePlayerLaguAnak
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioLaguAnakActivity : AppCompatActivity() {
    private var layoutManagerBait: LinearLayoutManager? = null
    private lateinit var baitLaguAnakAdapter: BaitLaguAnakAdapter
    lateinit var runnable :Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_lagu_anak)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val postID = bundle.getInt("id_lagu_audio")
            val audio = bundle.getString("audio")
            if (audio != null) {
                val audio_uri = audio.toUri()
                Log.d("audio_uri",audio_uri.toString())
                val mediaplayer = MediaPlayer.create(this,audio_uri)
                seekbar_audio.progress = 0
                seekbar_audio.max = mediaplayer.duration
                play_btn.setOnClickListener {
                    if(!mediaplayer.isPlaying){
                        mediaplayer.start()
                        play_btn.setImageResource(R.drawable.ic_pause)
                    }else{
                        mediaplayer.pause()
                        play_btn.setImageResource(R.drawable.ic_play)
                    }
                }
                seekbar_audio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
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
                    seekbar_audio.progress = mediaplayer.currentPosition
                    handler.postDelayed(runnable,1000)
                }
                handler.postDelayed(runnable,1000)
                mediaplayer.setOnCompletionListener {
                    play_btn.setImageResource(R.drawable.ic_play)
                    seekbar_audio.progress = 0
                }
            }
            getDetailData(postID)
            getBaitData(postID)
            setupRecyclerViewBait()
        }
        backToAudioLaguAnak.setOnClickListener {
            val intent = Intent(this, DetailLaguAnakActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailLaguAnak(id).enqueue(object : Callback<DetailLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakModel>,
                response: Response<DetailLaguAnakModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiAudioLaguAnak.text = result.deskripsi
                    detailNamaAudioLaguAnak.text = result.nama_post
                    detailJenisAudioLaguAnak.text = "Lagu Anak "
                    if (result.gambar != null) {
                        Glide.with(this@AudioLaguAnakActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageAudioLaguAnak)
                    } else {
                        imageAudioLaguAnak.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailLaguAnakModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitLaguAnak(id).enqueue(object :
            Callback<DetailBaitLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailBaitLaguAnakModel>,
                response: Response<DetailBaitLaguAnakModel>
            ) {
                showBaitLaguAnakData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitLaguAnakModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("AudioLaguAnakActivity", message)
    }

    private fun showBaitLaguAnakData(body: DetailBaitLaguAnakModel) {
        val results = body.data
        baitLaguAnakAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitLaguAnakAdapter = BaitLaguAnakAdapter(arrayListOf())
        baitAudioLaguAnakList.apply {
            layoutManagerBait = LinearLayoutManager(this@AudioLaguAnakActivity)
            layoutManager = layoutManagerBait
            adapter = baitLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun setShimmerToStop() {
        shimmerAudioLaguAnak.stopShimmer()
        shimmerAudioLaguAnak.visibility = View.GONE
        scrollAudioLaguAnak.visibility = View.VISIBLE
    }
}