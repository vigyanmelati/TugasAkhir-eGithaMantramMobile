package com.example.ekidungmantram.user

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AudioPupuhAdapter
import com.example.ekidungmantram.adapter.BaitPupuhAdapter
import com.example.ekidungmantram.adapter.VideoPupuhAdapter
import com.example.ekidungmantram.adapter.YadnyaPupuhAdapter
import com.example.ekidungmantram.admin.pupuh.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.model.adminmodel.*
import com.example.ekidungmantram.user.pupuh.AllKategoriPupuhUserActivity
import kotlinx.android.synthetic.main.activity_detail_pupuh_admin.tv_lirik
import kotlinx.android.synthetic.main.activity_detail_pupuh_user.*
import kotlinx.android.synthetic.main.activity_detail_pupuh_user.baitPupuhList
import kotlinx.android.synthetic.main.activity_detail_pupuh_user.rv_audio_pupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh_user.rv_video_pupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh_user.rv_yadnya_pupuh
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPupuhUserActivity : AppCompatActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitPupuhAdapter : BaitPupuhAdapter
    private lateinit var videoPupuhAdapter  : VideoPupuhAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioPupuhAdapter  : AudioPupuhAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaPupuhAdapter  : YadnyaPupuhAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_pupuh : Int = 0
    private var id_pupuh_user : Int = 0
    private lateinit var nama_pupuh_user :String
    private lateinit var desc_pupuh_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pupuh_user)
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh_user")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_user")
            val nama_pupuh = bundle.getString("nama_pupuh_user")
            val nama_tag_pupuh = bundle.getString("nama_tag_pupuh_user")
            val gambar_pupuh = bundle.getString("gambar_pupuh_user")
            val tag_pupuh = bundle.getInt("tag_pupuh_user")
            Log.d("id_pupuh_admin",postID.toString())
            id_pupuh_user = bundle.getInt("id_pupuh_user_kat")
            nama_pupuh_user = bundle.getString("nama_pupuh_user_kat").toString()
            desc_pupuh_user = bundle.getString("desc_pupuh_user_kat").toString()

            getDetailData(postID)
            getBaitData(postID)
            getListVideoPupuh(postID)
            getListAudioPupuh(postID)
            getListYadnyaPupuh(postID)
            setupRecyclerViewBait()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()

            goToListLirikPupuhUser.setOnClickListener {
                val intent = Intent(this, AllLirikPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("padalingsa", desc_pupuh_user)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListVideoPupuhUser.setOnClickListener {
                val intent = Intent(this, AllVideoPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListYadnyaPupuhUser.setOnClickListener {
                val intent = Intent(this, AllYadnyaOnPupuhActivity::class.java)
                bundle.putInt("id_pupuh_user", postID)
                bundle.putString("nama_pupuh_user", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListAudioPupuhUser.setOnClickListener {
                val intent = Intent(this, AllAudioPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahLirikPupuhUser.setOnClickListener {
                val intent = Intent(this, AddLirikPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("padalingsa", desc_pupuh_user)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahVideoPupuhUser.setOnClickListener {
                val intent = Intent(this, AddVideoPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("nama_kat_pupuh_user", nama_pupuh_user)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahAudioPupuhUser.setOnClickListener {
                val intent = Intent(this, AddAudioPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahYadnyaPupuhUser.setOnClickListener {
                val intent = Intent(this, AddYadnyaToPupuhActivity::class.java)
                bundle.putInt("id_pupuh_user", postID)
                bundle.putString("nama_pupuh_user", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            toEditPupuhUser.setOnClickListener {
                val intent = Intent(this, EditPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deletePupuhUser.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Pupuh")
                    .setMessage("Apakah anda yakin ingin menghapus pupuh ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusPupuh(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

        }

        backToPupuhUser.setOnClickListener {
            goBack()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailPupuhActivity", message)
    }

    private fun hapusPupuh(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deletePupuhAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailPupuhUserActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailPupuhUserActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailPupuhUserActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriPupuhUserActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh_user", id_pupuh)
        bundle.putString("nama_pupuh_user", nama_pupuh_user)
        bundle.putString("desc_pupuh_user", desc_pupuh_user)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailPupuh(id).enqueue(object:
            Callback<DetailPupuhModel> {
            override fun onResponse(
                call: Call<DetailPupuhModel>,
                response: Response<DetailPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiPupuhUser.text   = result.deskripsi
                    detailNamaPupuhUser.text  = result.nama_post
                    detailPupuhUser.text = "Sekar Alit"
                    if(result.gambar != null) {
                        Glide.with(this@DetailPupuhUserActivity)
                            .load(result.gambar).into(imageDetailPupuhUser)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailPupuhUser)
                    }else{
                        imageDetailPupuhUser.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailPupuhModel>, t: Throwable) {
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
                if(response.body()!!.data.toString() == "[]"){
                    tv_lirik.visibility = View.VISIBLE
                    baitPupuhList.visibility   = View.GONE
                    tambahLirikPupuhUser.visibility = View.VISIBLE
                    goToListLirikPupuhUser.visibility = View.GONE
                }else{
                    tv_lirik.visibility = View.GONE
                    baitPupuhList.visibility = View.VISIBLE
                    tambahLirikPupuhUser.visibility = View.GONE
                    goToListLirikPupuhUser.visibility = View.VISIBLE
                }
                showBaitPupuhData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitPupuhModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitPupuhData(body: DetailBaitPupuhModel) {
        val results = body.data
        baitPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitPupuhAdapter = BaitPupuhAdapter(arrayListOf())
        baitPupuhList.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailPupuhUserActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitPupuhAdapter
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
        shimmerDetailPupuhUser.stopShimmer()
        shimmerDetailPupuhUser.visibility = View.GONE
        scrollDetailPupuhUser.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoPupuhAdapter =  VideoPupuhAdapter(arrayListOf(), object : VideoPupuhAdapter.OnAdapterVideoPupuhListener{
            override fun onClick(result: VideoPupuhModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhUserActivity, VideoPupuhActivity::class.java)
                bundle.putString("video_pupuh", result.video)
                bundle.putInt("id_pupuh_video", id_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_pupuh.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailPupuhUserActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioPupuhAdapter =  AudioPupuhAdapter(arrayListOf(), object : AudioPupuhAdapter.OnAdapterAudioPupuhListener{
            override fun onClick(result: AudioPupuhModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhUserActivity, AudioPupuhActivity::class.java)
                bundle.putString("audio_pupuh", result.audio)
                bundle.putInt("id_pupuh_audio", id_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_pupuh.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailPupuhUserActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaPupuhAdapter =  YadnyaPupuhAdapter(arrayListOf(), object : YadnyaPupuhAdapter.OnAdapterYadnyaPupuhListener{
            override fun onClick(result: YadnyaPupuhModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhUserActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_pupuh.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailPupuhUserActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoPupuh(id_pupuh: Int) {
        ApiService.endpoint.getListVideoPupuh(id_pupuh)
            .enqueue(object: Callback<VideoPupuhModel> {
                override fun onResponse(
                    call: Call<VideoPupuhModel>,
                    response: Response<VideoPupuhModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideopupuhUser.visibility  = View.GONE
                        rv_video_pupuh.visibility   = View.GONE
                        tambahVideoPupuhUser.visibility = View.VISIBLE
                        goToListVideoPupuhUser.visibility = View.GONE
                    }else{
                        nodatavideopupuhUser.visibility  = View.GONE
                        rv_video_pupuh.visibility = View.VISIBLE
                        tambahVideoPupuhUser.visibility = View.GONE
                        goToListVideoPupuhUser.visibility = View.VISIBLE
                        Log.d("video",response.body().toString())
                        showVideoPupuhData(response.body()!!)
                    }
//                    if(response.body()!!.data.toString() == "[]") {
//                        layoutGamelan.visibility = View.GONE
//                    }else{
//                        layoutGamelan.visibility = View.VISIBLE
//                        nodatayadnyagamelan.visibility = View.GONE
//                        showGamelanYadnyaData(response.body()!!)
//                    }

                }

                override fun onFailure(call: Call<VideoPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioPupuh(id_post: Int) {
        ApiService.endpoint.getListAudioPupuh(id_post)
            .enqueue(object: Callback<AudioPupuhModel> {
                override fun onResponse(
                    call: Call<AudioPupuhModel>,
                    response: Response<AudioPupuhModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiopupuhUser.visibility  = View.GONE
                        rv_audio_pupuh.visibility   = View.GONE
                        tambahAudioPupuhUser.visibility = View.VISIBLE
                        goToListAudioPupuhUser.visibility = View.GONE
                    }else{
                        nodataaudiopupuhUser.visibility  = View.GONE
                        rv_audio_pupuh.visibility = View.VISIBLE
                        tambahAudioPupuhUser.visibility = View.GONE
                        goToListAudioPupuhUser.visibility = View.VISIBLE
                        Log.d("audio",response.body().toString())
                        showAudioPupuhData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaPupuh(id_pupuh: Int) {
        ApiService.endpoint.getYadnyaPupuh(id_pupuh)
            .enqueue(object: Callback<YadnyaPupuhModel> {
                override fun onResponse(
                    call: Call<YadnyaPupuhModel>,
                    response: Response<YadnyaPupuhModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyapupuhUser.visibility  = View.GONE
                        rv_yadnya_pupuh.visibility   = View.GONE
                        tambahYadnyaPupuhUser.visibility = View.VISIBLE
                        goToListYadnyaPupuhUser.visibility = View.GONE
                    }else{
                        nodatayadnyapupuhUser.visibility  = View.GONE
                        rv_yadnya_pupuh.visibility = View.VISIBLE
                        tambahYadnyaPupuhUser.visibility = View.GONE
                        goToListYadnyaPupuhUser.visibility = View.VISIBLE
                        Log.d("yadnya",response.body().toString())
                        showYadnyaPupuhData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoPupuhData(body: VideoPupuhModel) {
        val results = body.data
        videoPupuhAdapter.setData(results)
    }
    private fun showAudioPupuhData(body: AudioPupuhModel) {
        val results = body.data
        audioPupuhAdapter.setData(results)
    }
    private fun showYadnyaPupuhData(body: YadnyaPupuhModel) {
        val results = body.data
        yadnyaPupuhAdapter.setData(results)
    }


}