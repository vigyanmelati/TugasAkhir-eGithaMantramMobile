package com.example.ekidungmantram.user.kidung

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
import com.example.ekidungmantram.adapter.BaitKidungAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitKidungModel
import com.example.ekidungmantram.model.DetailKidungModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioKidungAdminModel
import kotlinx.android.synthetic.main.activity_audio_kidung.*
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.play_btn
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.seekbar_audio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioKidungActivity : AppCompatActivity() {
    private var layoutManagerBait: LinearLayoutManager? = null
    private lateinit var baitKidungAdapter: BaitKidungAdapter
    lateinit var runnable :Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_kidung)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val postID = bundle.getInt("id_kidung_audio")
            val audio = bundle.getString("audio_kidung")
                val id_audio = bundle.getInt("id_audio_kidung")

            val id_kidung = bundle.getInt("id_kidung")
            val tag_kidung = bundle.getInt("tag_kidung")
            val nama_kidung = bundle.getString("nama_kidung")
            val nama_tag_kidung = bundle.getString("nama_tag_kidung")
            val gambar_kidung= bundle.getString("gambar_kidung")

            backToAudioKidung.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, DetailKidungActivity::class.java)
                bundle.putInt("id_kidung", id_kidung)
                bundle.putInt("tag_kidung", tag_kidung)
                bundle.putString("nama_kidung", nama_kidung)
                bundle.putString("nama_tag_kidung", nama_tag_kidung)
                bundle.putString("gambar_kidung", gambar_kidung)
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
        ApiService.endpoint.getDetailKidung(id).enqueue(object: Callback<DetailKidungModel> {
            override fun onResponse(
                call: Call<DetailKidungModel>,
                response: Response<DetailKidungModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiAudioKidung.text   = result.deskripsi
//                    detailNamaAudioKidung.text  = result.nama_post
                    detailJenisAudioKidung.text = result.nama_kategori
//                    if(result.gambar != null) {
//                        Glide.with(this@AudioKidungActivity)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageAudioKidung)
////                            .load(result.gambar).into(imageAudioKidung)
//                    }else{
                        imageAudioKidung.setImageResource(R.drawable.music)
//                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKidungModel>, t: Throwable) {
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
//                    deskripsiAudioKidung.text   = result.deskripsi
                    detailNamaAudioKidung.text  = result.judul_audio
//                    detailJenisAudioKidung.text = result.nama_kategori
                    if(result.gambar_audio != null) {
                        Glide.with(this@AudioKidungActivity)
                            .load(Constant.IMAGE_URL + result.gambar_audio).into(imageAudioKidung)
//                            .load(result.gambar).into(imageAudioKidung)
                    }else{
                        imageAudioKidung.setImageResource(R.drawable.sample_image_yadnya)
                    }
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
        ApiService.endpoint.getDetailBaitKidung(id).enqueue(object :
            Callback<DetailBaitKidungModel> {
            override fun onResponse(
                call: Call<DetailBaitKidungModel>,
                response: Response<DetailBaitKidungModel>
            ) {
                showBaitKidungData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitKidungModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("AudioKidungActivity", message)
    }

    private fun showBaitKidungData(body: DetailBaitKidungModel) {
        val results = body.data
        baitKidungAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKidungAdapter = BaitKidungAdapter(arrayListOf())
        baitAudioKidungList.apply {
            layoutManagerBait = LinearLayoutManager(this@AudioKidungActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun setShimmerToStop() {
        shimmerAudioKidung.stopShimmer()
        shimmerAudioKidung.visibility = View.GONE
        scrollAudioKidung.visibility = View.VISIBLE
    }
}