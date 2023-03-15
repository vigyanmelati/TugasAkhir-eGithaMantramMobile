package com.example.ekidungmantram.user.kakawin

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
import com.example.ekidungmantram.adapter.BaitKakawinAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitKakawinModel
import com.example.ekidungmantram.model.DetailKakawinModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioKidungAdminModel
import kotlinx.android.synthetic.main.activity_audio_kakawin.*
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.play_btn
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.seekbar_audio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioKakawinActivity : AppCompatActivity() {
    private var layoutManagerBait: LinearLayoutManager? = null
    private lateinit var baitKakawinAdapter: BaitKakawinAdapter
    lateinit var runnable :Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_kakawin)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val postID = bundle.getInt("id_kakawin_audio")
            val audio = bundle.getString("audio_kakawin")
            val id_audio = bundle.getInt("id_audio_kakawin")

            val id_kakawin = bundle.getInt("id_kakawin")
            val id_kakawin_kat = bundle.getInt("id_kakawin_kat")
            val tag_kakawin = bundle.getInt("tag_kakawin")
            val nama_kakawin_kat = bundle.getString("nama_kakawin_kat")
            val desc_kakawin_kat= bundle.getString("desc_kakawin_kat")
            val nama_kakawin = bundle.getString("nama_kakawin")
            val nama_tag_kakawin = bundle.getString("nama_tag_kakawin")
            val gambar_kakawin= bundle.getString("gambar_kakawin")

            backToAudioKakawin.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, DetailKakawinActivity::class.java)
                bundle.putInt("id_kakawin", id_kakawin)
                bundle.putInt("id_kakawin_kat", id_kakawin_kat)
                bundle.putInt("tag_kakawin", tag_kakawin)
                bundle.putString("nama_kakawin_kat", nama_kakawin_kat)
                bundle.putString("desc_kakawin_kat", desc_kakawin_kat)
                bundle.putString("nama_kakawin", nama_kakawin)
                bundle.putString("nama_tag_kakawin", nama_tag_kakawin)
                bundle.putString("gambar_kakawin", gambar_kakawin)
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
        ApiService.endpoint.getDetailKakawin(id).enqueue(object: Callback<DetailKakawinModel> {
            override fun onResponse(
                call: Call<DetailKakawinModel>,
                response: Response<DetailKakawinModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiAudioKakawin.text   = result.deskripsi
//                    detailNamaAudioKakawin.text  = result.nama_post
//                    detailJenisAudioKakawin.text = "Sekar Agung "
//                    if(result.gambar != null) {
//                        Glide.with(this@AudioKakawinActivity)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageAudioKakawin)
////                            .load(result.gambar).into(imageAudioKakawin)
//                    }else{
//                        imageAudioKakawin.setImageResource(R.drawable.sample_image_yadnya)
//                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKakawinModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDetailDataAudio(id: Int) {
        ApiService.endpoint.getShowAudioKidungAdmin(id).enqueue(object: Callback<DetailAudioKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailAudioKidungAdminModel>,
                response: Response<DetailAudioKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
//                    deskripsiAudioKakawin.text   = result.deskripsi
                    detailNamaAudioKakawin.text  = result.judul_audio
                    detailJenisAudioKakawin.text = "Sekar Agung "
//                    if(result.gambar_audio != null) {
//                        Glide.with(this@AudioKakawinActivity)
//                            .load(Constant.IMAGE_URL + result.gambar_audio).into(imageAudioKakawin)
////                            .load(result.gambar).into(imageAudioKakawin)
//                    }else{
                        imageAudioKakawin.setImageResource(R.drawable.music)
//                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailAudioKidungAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitKakawin(id).enqueue(object :
            Callback<DetailBaitKakawinModel> {
            override fun onResponse(
                call: Call<DetailBaitKakawinModel>,
                response: Response<DetailBaitKakawinModel>
            ) {
                showBaitKakawinData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitKakawinModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("AudioKakawinActivity", message)
    }

    private fun showBaitKakawinData(body: DetailBaitKakawinModel) {
        val results = body.data
        baitKakawinAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKakawinAdapter = BaitKakawinAdapter(arrayListOf())
        baitAudioKakawinList.apply {
            layoutManagerBait = LinearLayoutManager(this@AudioKakawinActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun setShimmerToStop() {
        shimmerAudioKakawin.stopShimmer()
        shimmerAudioKakawin.visibility = View.GONE
        scrollAudioKakawin.visibility = View.VISIBLE
    }
}