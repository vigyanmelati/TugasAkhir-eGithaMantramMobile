package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BaitLaguAnakAdapter
import com.example.ekidungmantram.adapter.VideoLaguAnakAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitLaguAnakModel
import com.example.ekidungmantram.model.DetailLaguAnakModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoLaguAnakAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_lagu_anak.*
import kotlinx.android.synthetic.main.activity_video_kidung.*
import kotlinx.android.synthetic.main.activity_video_lagu_anak.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoLaguAnakActivity : YouTubeBaseActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitLaguAnakAdapter : BaitLaguAnakAdapter
    private lateinit var videoLaguAnakAdapter  : VideoLaguAnakAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_lagu_anak)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_video")
            val video = bundle.getString("video")
            val id_video = bundle.getInt("id_video_lagu")
            Log.d("id_lagu_video",postID.toString())
            if (video != null) {
                Log.d("id_video",video)
            }

            if (video != null) {
                playYoutubeVideo(video)
            }
            getDetailData(postID)
            getBaitData(postID)
            getDetailDataVideo(id_video)
            setupRecyclerViewBait()
        }
        backToVideoLaguAnak.setOnClickListener {
            val intent = Intent(this, DetailLaguAnakActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailLaguAnak(id).enqueue(object: Callback<DetailLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakModel>,
                response: Response<DetailLaguAnakModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiVideoLaguAnak.text   = result.deskripsi
//                    detailNamaVideoLaguAnak.text  = result.nama_post
//                    detailJenisVideoLaguAnak.text = "Lagu Anak "
//                    if(result.gambar != null) {
//                        Glide.with(this@VideoLaguAnakActivity)
////                            .load(result.gambar).into(imageVideoLaguAnak)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageVideoLaguAnak)
//                    }else{
//                        imageVideoLaguAnak.setImageResource(R.drawable.sample_image_yadnya)
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

    private fun getDetailDataVideo(id: Int) {
        ApiService.endpoint.getShowVideoLaguAnakAdmin(id).enqueue(object: Callback<DetailVideoLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoLaguAnakAdminModel>,
                response: Response<DetailVideoLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
//                    deskripsiVideoLaguAnak.text   = result.deskripsi
                    detailNamaVideoLaguAnak.text  = result.judul_video
                    detailJenisVideoLaguAnak.text = "Sekar Rare"
                    if(result.gambar_video != null) {
                        Glide.with(this@VideoLaguAnakActivity)
//                            .load(result.gambar).into(imageVideoLaguAnak)
                            .load(Constant.IMAGE_URL + result.gambar_video).into(imageVideoLaguAnak)
                    }else{
                        imageVideoLaguAnak.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailVideoLaguAnakAdminModel>, t: Throwable) {
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
        Log.d("VideoLaguAnakActivity", message)
    }

    private fun showBaitLaguAnakData(body: DetailBaitLaguAnakModel) {
        val results = body.data
        baitLaguAnakAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitLaguAnakAdapter = BaitLaguAnakAdapter(arrayListOf())
        baitVideoLaguAnakList.apply {
            layoutManagerBait = LinearLayoutManager(this@VideoLaguAnakActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerLaguAnak.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1!!.loadVideo(video)
                p1.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setShimmerToStop() {
        shimmerVideoLaguAnak.stopShimmer()
        shimmerVideoLaguAnak.visibility = View.GONE
        scrollVideoLaguAnak.visibility  = View.VISIBLE
    }

}