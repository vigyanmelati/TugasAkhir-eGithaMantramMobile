package com.example.ekidungmantram.admin.kajidharmagita

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoDharmagitaModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_video_dharmagita_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVideoDharmagitaNeedApprovalActivity :  YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video_dharmagita_need_approval)

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_video_dharmagita")
            getDetailData(postID)

            acceptVideoDharmagitaNA.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Terima Video Dharmagita")
                    .setMessage("Apakah anda yakin ingin terima video Dharmagita ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "yes")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            rejectVideoDharmagitaNA.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Tolak Video Dharmagita")
                    .setMessage("Apakah anda yakin ingin menolak video Dharmagita ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "no")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }
        backToVideoDharmagitaNA.setOnClickListener {
            val intent = Intent(this, ListVideoDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun accMantram(postID: Int, s: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengupdate Data")
        progressDialog.show()
        ApiService.endpoint.approveVideoDharmagita(postID, s).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailVideoDharmagitaNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailVideoDharmagitaNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailVideoDharmagitaNeedApprovalActivity , t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, ListVideoDharmagitaNeedApprovalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailVideoDharmagita(id).enqueue(object: Callback<VideoDharmagitaModel> {
            override fun onResponse(
                call: Call<VideoDharmagitaModel>,
                response: Response<VideoDharmagitaModel>
            ) {
                val result = response.body()!!
                result.let {
                    detailNamaVideoDharmagitaNA.text  = result.judul_video
                    detailJenisVideoDharmagitaNA.text = result.nama_post
                    if(result.gambar_video != null) {
                        Glide.with(this@DetailVideoDharmagitaNeedApprovalActivity)
                            .load(Constant.IMAGE_URL + result.gambar_video).into(imageVideoDharmagitaNA)
//                            .load(result.gambar_video).into(imageVideoDharmagitaNA)
                    }else{
                        imageVideoDharmagitaNA.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<VideoDharmagitaModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("VideoKakawinActivity", message)
    }


    private fun playYoutubeVideo(video: String) {
        youtubePlayerDharmagitaNA.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerVideoDharmagitaNA.stopShimmer()
        shimmerVideoDharmagitaNA.visibility = View.GONE
        scrollVideoDharmagitaNA.visibility  = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ListVideoDharmagitaNeedApprovalActivity::class.java)
        startActivity(intent)
    }
}