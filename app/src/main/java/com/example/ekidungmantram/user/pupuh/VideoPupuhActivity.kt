package com.example.ekidungmantram.user.pupuh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BaitPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_video_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoPupuhActivity : YouTubeBaseActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitPupuhAdapter : BaitPupuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_pupuh)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_video")
            val video = bundle.getString("video_pupuh")
            val id_video = bundle.getInt("id_video_pupuh")

            val id_pupuh = bundle.getInt("id_pupuh")
            val id_pupuh_kat = bundle.getInt("id_pupuh_kat")
            val tag_pupuh = bundle.getInt("tag_pupuh")
            val nama_pupuh_kat = bundle.getString("nama_pupuh_kat")
            val desc_pupuh_kat= bundle.getString("desc_pupuh_kat")
            val nama_pupuh = bundle.getString("nama_pupuh")
            val nama_tag_pupuh = bundle.getString("nama_tag_pupuh")
            val gambar_pupuh= bundle.getString("gambar_pupuh")

            backToVideoPupuh.setOnClickListener {
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
            Log.d("id_pupuh_video",postID.toString())
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
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailPupuh(id).enqueue(object: Callback<DetailPupuhModel> {
            override fun onResponse(
                call: Call<DetailPupuhModel>,
                response: Response<DetailPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiVideoPupuh.text   = result.deskripsi
//                    detailNamaVideoPupuh.text  = result.nama_post
//                    detailJenisVideoPupuh.text = "Pupuh "
//                    if(result.gambar != null) {
//                        Glide.with(this@VideoPupuhActivity)
////                            .load(result.gambar).into(imageVideoPupuh)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageVideoPupuh)
//                    }else{
//                        imageVideoPupuh.setImageResource(R.drawable.sample_image_yadnya)
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

    private fun getDetailDataVideo(id: Int) {
        ApiService.endpoint.getShowVideoPupuh(id).enqueue(object: Callback<DetailVideoPupuhModel> {
            override fun onResponse(
                call: Call<DetailVideoPupuhModel>,
                response: Response<DetailVideoPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
//                    deskripsiVideoPupuh.text   = result.deskripsi
                    detailNamaVideoPupuh.text  = result.judul_video
                    detailJenisVideoPupuh.text = "Sekar Alit"
                    if(result.gambar_video != null) {
                        Glide.with(this@VideoPupuhActivity)
//                            .load(result.gambar).into(imageVideoPupuh)
                            .load(Constant.IMAGE_URL + result.gambar_video).into(imageVideoPupuh)
                    }else{
                        imageVideoPupuh.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailVideoPupuhModel>, t: Throwable) {
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
        Log.d("VideoPupuhAnakActivity", message)
    }

    private fun showBaitPupuhData(body: DetailBaitPupuhModel) {
        val results = body.data
        baitPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitPupuhAdapter = BaitPupuhAdapter(arrayListOf())
        baitVideoPupuhList.apply {
            layoutManagerBait = LinearLayoutManager(this@VideoPupuhActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerPupuh.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerVideoPupuh.stopShimmer()
        shimmerVideoPupuh.visibility = View.GONE
        scrollVideoPupuh.visibility  = View.VISIBLE
    }

}