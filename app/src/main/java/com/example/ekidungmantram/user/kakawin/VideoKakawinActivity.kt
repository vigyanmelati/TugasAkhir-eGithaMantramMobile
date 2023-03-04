package com.example.ekidungmantram.user.kakawin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BaitKakawinAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitKakawinModel
import com.example.ekidungmantram.model.DetailKakawinModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoKakawinAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_video_kakawin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoKakawinActivity : YouTubeBaseActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitKakawinAdapter : BaitKakawinAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_kakawin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin_video")
            val video = bundle.getString("video_kakawin")
            val id_video = bundle.getInt("id_video_kakawin")

            val id_kakawin = bundle.getInt("id_kakawin")
            val id_kakawin_kat = bundle.getInt("id_kakawin_kat")
            val tag_kakawin = bundle.getInt("tag_kakawin")
            val nama_kakawin_kat = bundle.getString("nama_kakawin_kat")
            val desc_kakawin_kat= bundle.getString("desc_kakawin_kat")
            val nama_kakawin = bundle.getString("nama_kakawin")
            val nama_tag_kakawin = bundle.getString("nama_tag_kakawin")
            val gambar_kakawin= bundle.getString("gambar_kakawin")

            backToVideoKakawin.setOnClickListener {
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

            Log.d("id_kakawin_video",postID.toString())
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
        ApiService.endpoint.getDetailKakawin(id).enqueue(object: Callback<DetailKakawinModel> {
            override fun onResponse(
                call: Call<DetailKakawinModel>,
                response: Response<DetailKakawinModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiVideoKakawin.text   = result.deskripsi
//                    detailNamaVideoKakawin.text  = result.nama_post
//                    detailJenisVideoKakawin.text = "Sekar Agung "
//                    if(result.gambar != null) {
//                        Glide.with(this@VideoKakawinActivity)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageVideoKakawin)
////                            .load(result.gambar).into(imageVideoKakawin)
//                    }else{
//                        imageVideoKakawin.setImageResource(R.drawable.sample_image_yadnya)
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

    private fun getDetailDataVideo(id: Int) {
        ApiService.endpoint.getShowVideoKakawinAdmin(id).enqueue(object: Callback<DetailVideoKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoKakawinAdminModel>,
                response: Response<DetailVideoKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
//                    deskripsiVideoKakawin.text   = result.deskripsi
                    detailNamaVideoKakawin.text  = result.judul_video
                    detailJenisVideoKakawin.text = "Sekar Agung "
                    if(result.gambar_video != null) {
                        Glide.with(this@VideoKakawinActivity)
                            .load(Constant.IMAGE_URL + result.gambar_video).into(imageVideoKakawin)
//                            .load(result.gambar).into(imageVideoKakawin)
                    }else{
                        imageVideoKakawin.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailVideoKakawinAdminModel>, t: Throwable) {
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
        Log.d("VideoKakawinActivity", message)
    }

    private fun showBaitKakawinData(body: DetailBaitKakawinModel) {
        val results = body.data
        baitKakawinAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKakawinAdapter = BaitKakawinAdapter(arrayListOf())
        baitVideoKakawinList.apply {
            layoutManagerBait = LinearLayoutManager(this@VideoKakawinActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerKakawin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerVideoKakawin.stopShimmer()
        shimmerVideoKakawin.visibility = View.GONE
        scrollVideoKakawin.visibility  = View.VISIBLE
    }
}