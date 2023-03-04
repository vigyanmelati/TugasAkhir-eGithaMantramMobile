package com.example.ekidungmantram.user.pupuh

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
import com.example.ekidungmantram.adapter.BaitPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.play_btn
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.seekbar_audio
import kotlinx.android.synthetic.main.activity_audio_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioPupuhActivity : AppCompatActivity() {
    private var layoutManagerBait: LinearLayoutManager? = null
    private lateinit var baitPupuhAdapter: BaitPupuhAdapter
    lateinit var runnable :Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_pupuh)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val postID = bundle.getInt("id_pupuh_audio")
            val audio = bundle.getString("audio_pupuh")
            val id_audio = bundle.getInt("id_audio_pupuh")

            val id_pupuh = bundle.getInt("id_pupuh")
            val id_pupuh_kat = bundle.getInt("id_pupuh_kat")
            val tag_pupuh = bundle.getInt("tag_pupuh")
            val nama_pupuh_kat = bundle.getString("nama_pupuh_kat")
            val desc_pupuh_kat= bundle.getString("desc_pupuh_kat")
            val nama_pupuh = bundle.getString("nama_pupuh")
            val nama_tag_pupuh = bundle.getString("nama_tag_pupuh")
            val gambar_pupuh= bundle.getString("gambar_pupuh")

            backToAudioPupuh.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, DetailPupuhActivity::class.java)
                bundle.putInt("id_pupuh", id_pupuh)
                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                bundle.putInt("tag_pupuh", tag_pupuh)
                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("nama_tag_pupuh", nama_tag_pupuh)
                bundle.putString("gambar_pupuh", gambar_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }

            val audio_constant = Constant.AUDIO_URL + audio
            if (audio != null) {
                val audio_uri = audio_constant.toUri()
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
            getDetailDataAudio(id_audio)
            setupRecyclerViewBait()
        }
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailPupuh(id).enqueue(object: Callback<DetailPupuhModel> {
            override fun onResponse(
                call: Call<DetailPupuhModel>,
                response: Response<DetailPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiAudioPupuh.text   = result.deskripsi
//                    detailNamaAudioPupuh.text  = result.nama_post
//                    detailJenisAudioPupuh.text = "Pupuh "
//                    if(result.gambar != null) {
//                        Glide.with(this@AudioPupuhActivity)
////                            .load(result.gambar).into(imageAudioPupuh)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageAudioPupuh)
//                    }else{
//                        imageAudioPupuh.setImageResource(R.drawable.sample_image_yadnya)
//                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailPupuhModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDetailDataAudio(id: Int) {
        ApiService.endpoint.getShowAudioPupuh(id).enqueue(object: Callback<DetailAudioPupuhModel> {
            override fun onResponse(
                call: Call<DetailAudioPupuhModel>,
                response: Response<DetailAudioPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    detailNamaAudioPupuh.text  = result.judul_audio
                    detailJenisAudioPupuh.text = "Sekar Alit "
                    if(result.gambar_audio != null) {
                        Glide.with(this@AudioPupuhActivity)
//                            .load(result.gambar).into(imageAudioPupuh)
                            .load(Constant.IMAGE_URL + result.gambar_audio).into(imageAudioPupuh)
                    }else{
                        imageAudioPupuh.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailAudioPupuhModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitPupuh(id).enqueue(object :
            Callback<DetailBaitPupuhModel> {
            override fun onResponse(
                call: Call<DetailBaitPupuhModel>,
                response: Response<DetailBaitPupuhModel>
            ) {
                showBaitPupuhData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitPupuhModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("AudioPupuhActivity", message)
    }

    private fun showBaitPupuhData(body: DetailBaitPupuhModel) {
        val results = body.data
        baitPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitPupuhAdapter = BaitPupuhAdapter(arrayListOf())
        baitAudioPupuhList.apply {
            layoutManagerBait = LinearLayoutManager(this@AudioPupuhActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setShimmerToStop() {
        shimmerAudioPupuh.stopShimmer()
        shimmerAudioPupuh.visibility = View.GONE
        scrollAudioPupuh.visibility = View.VISIBLE
    }
}