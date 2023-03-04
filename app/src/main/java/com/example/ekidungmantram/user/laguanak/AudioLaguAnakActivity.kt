package com.example.ekidungmantram.user.laguanak

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
import com.example.ekidungmantram.model.adminmodel.DetailAudioLaguAnakAdminModel
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.*
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.play_btn
import kotlinx.android.synthetic.main.activity_audio_lagu_anak.seekbar_audio
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
            val id_audio = bundle.getInt("id_audio_lagu")

            val id_lagu = bundle.getInt("id_lagu")
            val id_lagu_anak_kat = bundle.getInt("id_lagu_anak_kat")
            val tag_lagu = bundle.getInt("tag_lagu")
            val nama_lagu_anak_kat = bundle.getString("nama_lagu_anak_kat")
            val desc_lagu_anak_kat= bundle.getString("desc_lagu_anak_kat")
            val nama_lagu = bundle.getString("nama_lagu")
            val nama_tag_lagu = bundle.getString("nama_tag_lagu")
            val gambar_lagu= bundle.getString("gambar_lagu")

            backToAudioLaguAnak.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, DetailLaguAnakActivity::class.java)
                bundle.putInt("id_lagu", id_lagu)
                bundle.putInt("id_lagu_anak_kat", id_lagu_anak_kat)
                bundle.putInt("tag_lagu", tag_lagu)
                bundle.putString("nama_lagu_anak_kat", nama_lagu_anak_kat)
                bundle.putString("desc_lagu_anak_kat", desc_lagu_anak_kat)
                bundle.putString("nama_lagu", nama_lagu)
                bundle.putString("nama_tag_lagu", nama_tag_lagu)
                bundle.putString("gambar_lagu", gambar_lagu)
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
        ApiService.endpoint.getDetailLaguAnak(id).enqueue(object : Callback<DetailLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakModel>,
                response: Response<DetailLaguAnakModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiAudioLaguAnak.text = result.deskripsi
//                    detailNamaAudioLaguAnak.text = result.nama_post
//                    detailJenisAudioLaguAnak.text = "Lagu Anak "
//                    if (result.gambar != null) {
//                        Glide.with(this@AudioLaguAnakActivity)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageAudioLaguAnak)
////                            .load(result.gambar).into(imageAudioLaguAnak)
//                    } else {
//                        imageAudioLaguAnak.setImageResource(R.drawable.sample_image_yadnya)
//                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailLaguAnakModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDetailDataAudio(id: Int) {
        ApiService.endpoint.getShowAudioLaguAnakAdmin(id).enqueue(object : Callback<DetailAudioLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailAudioLaguAnakAdminModel>,
                response: Response<DetailAudioLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
//                    deskripsiAudioLaguAnak.text = result.deskripsi
                    detailNamaAudioLaguAnak.text = result.judul_audio
                    detailJenisAudioLaguAnak.text = "Sekar Rare"
                    if (result.gambar_audio != null) {
                        Glide.with(this@AudioLaguAnakActivity)
                            .load(Constant.IMAGE_URL + result.gambar_audio).into(imageAudioLaguAnak)
//                            .load(result.gambar).into(imageAudioLaguAnak)
                    } else {
                        imageAudioLaguAnak.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailAudioLaguAnakAdminModel>, t: Throwable) {
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