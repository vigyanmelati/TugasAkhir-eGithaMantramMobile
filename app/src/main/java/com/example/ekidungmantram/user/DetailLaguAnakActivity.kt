package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_kidung.*
import kotlinx.android.synthetic.main.activity_detail_lagu_anak.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLaguAnakActivity : YouTubeBaseActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitLaguAnakAdapter : BaitLaguAnakAdapter
    private lateinit var videoLaguAnakAdapter  : VideoLaguAnakAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioLaguAnakAdapter  : AudioLaguAnakAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private var id_lagu : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu_anak)
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_lagu = bundle.getInt("id_lagu")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu")
            Log.d("id_lagu",postID.toString())

            getDetailData(postID)
            getBaitData(postID)
            getListVideoLaguAnak(postID)
            getListAudioLaguAnak(postID)
            setupRecyclerViewBait()
            setupRecyclerViewK()
            setupRecyclerViewA()
            lihatSemuavideoanak.visibility = View.GONE
            lihatSemuaaudioanak.visibility = View.GONE
        }

        backToLaguAnak.setOnClickListener {
            val intent = Intent(this, AlllLaguAnakActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailLaguAnakActivity", message)
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailLaguAnak(id).enqueue(object: Callback<DetailLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakModel>,
                response: Response<DetailLaguAnakModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiLaguAnak.text   = result.deskripsi
                    detailNamaLaguAnak.text  = result.nama_post
                    detailJenisLaguAnak.text = "Lagu Anak "
                    if(result.gambar != null) {
                        Glide.with(this@DetailLaguAnakActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailLaguAnak)
                    }else{
                        imageDetailLaguAnak.setImageResource(R.drawable.sample_image_yadnya)
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

    private fun showBaitLaguAnakData(body: DetailBaitLaguAnakModel) {
        val results = body.data
        baitLaguAnakAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitLaguAnakAdapter = BaitLaguAnakAdapter(arrayListOf())
        baitLaguAnakList.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailLaguAnakActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

//    private fun playYoutubeVideo(video: String) {
//        youtubePlayerLaguAnak.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
//            override fun onInitializationSuccess(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubePlayer?,
//                p2: Boolean
//            ) {
//                p1!!.loadVideo(video)
//                p1.play()
//            }
//
//            override fun onInitializationFailure(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubeInitializationResult?
//            ) {
//                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }

    private fun setShimmerToStop() {
        shimmerDetailLaguAnak.stopShimmer()
        shimmerDetailLaguAnak.visibility = View.GONE
        scrollDetailLaguAnak.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoLaguAnakAdapter =  VideoLaguAnakAdapter(arrayListOf(), object : VideoLaguAnakAdapter.OnAdapterVideoLaguAnakListener{
            override fun onClick(result: VideoLaguAnakModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailLaguAnakActivity, VideoLaguAnakActivity::class.java)
                bundle.putString("video", result.video)
                bundle.putInt("id_lagu_video", id_lagu)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_lagu_anak.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailLaguAnakActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioLaguAnakAdapter =  AudioLaguAnakAdapter(arrayListOf(), object : AudioLaguAnakAdapter.OnAdapterAudioLaguAnakListener{
            override fun onClick(result: AudioLaguAnakModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailLaguAnakActivity, AudioLaguAnakActivity::class.java)
                bundle.putString("audio", result.audio)
                bundle.putInt("id_lagu_audio", id_lagu)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_lagu_anak.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailLaguAnakActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoLaguAnak(id_lagu_anak: Int) {
        ApiService.endpoint.getListVideoLaguAnak(id_lagu_anak)
            .enqueue(object: Callback<VideoLaguAnakModel>{
                override fun onResponse(
                    call: Call<VideoLaguAnakModel>,
                    response: Response<VideoLaguAnakModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideolagu.visibility  = View.VISIBLE
                        Log.d("video",response.body().toString())
                    }else{
                        nodatavideolagu.visibility  = View.GONE
                        showVideoLaguAnakData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<VideoLaguAnakModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioLaguAnak(id_post: Int) {
        ApiService.endpoint.getListAudioLaguAnak(id_post)
            .enqueue(object: Callback<AudioLaguAnakModel>{
                override fun onResponse(
                    call: Call<AudioLaguAnakModel>,
                    response: Response<AudioLaguAnakModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiolagu.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodataaudiolagu.visibility  = View.GONE
                        showAudioLaguAnakData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioLaguAnakModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoLaguAnakData(body: VideoLaguAnakModel) {
        val results = body.data
        videoLaguAnakAdapter.setData(results)
    }
    private fun showAudioLaguAnakData(body: AudioLaguAnakModel) {
        val results = body.data
        audioLaguAnakAdapter.setData(results)
    }
}