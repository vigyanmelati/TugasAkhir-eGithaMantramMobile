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
import com.example.ekidungmantram.adapter.BaitKidungAdapter
import com.example.ekidungmantram.adapter.BaitPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitKidungModel
import com.example.ekidungmantram.model.DetailBaitPupuhModel
import com.example.ekidungmantram.model.DetailKidungModel
import com.example.ekidungmantram.model.DetailPupuhModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoKidungAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_audio_kakawin.*
import kotlinx.android.synthetic.main.activity_video_kidung.*
import kotlinx.android.synthetic.main.activity_video_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoKidungActivity : YouTubeBaseActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitKidungAdapter : BaitKidungAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_kidung)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung_video")
            val video = bundle.getString("video_kidung")
            val id_video = bundle.getInt("id_video_kidung")

            val id_kidung = bundle.getInt("id_kidung")
            val tag_kidung = bundle.getInt("tag_kidung")
            val nama_kidung = bundle.getString("nama_kidung")
            val nama_tag_kidung = bundle.getString("nama_tag_kidung")
            val gambar_kidung= bundle.getString("gambar_kidung")

            backToVideoKidung.setOnClickListener {
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

            Log.d("id_kidung_video",postID.toString())
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
        ApiService.endpoint.getDetailKidung(id).enqueue(object: Callback<DetailKidungModel> {
            override fun onResponse(
                call: Call<DetailKidungModel>,
                response: Response<DetailKidungModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiVideoKidung.text   = result.deskripsi
//                    detailNamaVideoKidung.text  = result.nama_post
                    detailJenisVideoKidung.text = result.nama_kategori
//                    if(result.gambar != null) {
//                        Glide.with(this@VideoKidungActivity)
////                            .load(result.gambar).into(imageVideoKidung)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageVideoKidung)
//                    }else{
//                        imageVideoKidung.setImageResource(R.drawable.sample_image_yadnya)
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

    private fun getDetailDataVideo(id: Int) {
        ApiService.endpoint.getShowVideoKidungAdmin(id).enqueue(object: Callback<DetailVideoKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoKidungAdminModel>,
                response: Response<DetailVideoKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
//                    deskripsiVideoKidung.text   = result.deskripsi
                    detailNamaVideoKidung.text  = result.judul_video
//                    detailJenisVideoKidung.text = result.nama_kategori
                    if(result.gambar_video != null) {
                        Glide.with(this@VideoKidungActivity)
//                            .load(result.gambar).into(imageVideoKidung)
                            .load(Constant.IMAGE_URL + result.gambar_video).into(imageVideoKidung)
                    }else{
                        imageVideoKidung.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailVideoKidungAdminModel>, t: Throwable) {
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
        Log.d("VideoKidungActivity", message)
    }

    private fun showBaitKidungData(body: DetailBaitKidungModel) {
        val results = body.data
        baitKidungAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKidungAdapter = BaitKidungAdapter(arrayListOf())
        baitVideoKidungList.apply {
            layoutManagerBait = LinearLayoutManager(this@VideoKidungActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerKidung.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerVideoKidung.stopShimmer()
        shimmerVideoKidung.visibility = View.GONE
        scrollVideoKidung.visibility  = View.VISIBLE
    }
}