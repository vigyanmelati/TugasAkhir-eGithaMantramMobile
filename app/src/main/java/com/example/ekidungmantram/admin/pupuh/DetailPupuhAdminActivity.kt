package com.example.ekidungmantram.admin.pupuh

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
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AudioPupuhAdapter
import com.example.ekidungmantram.adapter.BaitPupuhAdapter
import com.example.ekidungmantram.adapter.VideoPupuhAdapter
import com.example.ekidungmantram.adapter.YadnyaPupuhAdapter
import com.example.ekidungmantram.adapter.admin.AudioPupuhAdminAdapter
import com.example.ekidungmantram.adapter.admin.BaitPupuhAdminAdapter
import com.example.ekidungmantram.adapter.admin.VideoPupuhAdminAdapter
import com.example.ekidungmantram.adapter.admin.YadnyaPupuhAdminAdapter
import com.example.ekidungmantram.admin.kidung.AddLirikKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.AllLirikKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.EditKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.database.data.Dharmagita
import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.model.adminmodel.*
import com.example.ekidungmantram.user.AllPupuhActivity
import com.example.ekidungmantram.user.AudioPupuhActivity
import com.example.ekidungmantram.user.DetailYadnyaActivity
import com.example.ekidungmantram.user.VideoPupuhActivity
import kotlinx.android.synthetic.main.activity_detail_kidung_admin.*
import kotlinx.android.synthetic.main.activity_detail_pupuh.*
import kotlinx.android.synthetic.main.activity_detail_pupuh.lihatSemuayadnyapupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh.nodataaudiopupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh.nodatayadnyapupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh.rv_audio_pupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh.rv_yadnya_pupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh.shimmerDetailPupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh_admin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPupuhAdminActivity : AppCompatActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitPupuhAdapter : BaitPupuhAdminAdapter
    private lateinit var videoPupuhAdapter  : VideoPupuhAdminAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioPupuhAdapter  : AudioPupuhAdminAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaPupuhAdapter  : YadnyaPupuhAdminAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_pupuh : Int = 0
    private var id_pupuh_admin : Int = 0
    private lateinit var nama_pupuh_admin :String
    private lateinit var desc_pupuh_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pupuh_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh_admin")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_admin")
            val nama_pupuh = bundle.getString("nama_pupuh_admin")
            val nama_tag_pupuh = bundle.getString("nama_tag_pupuh_admin")
            val gambar_pupuh = bundle.getString("gambar_pupuh_admin")
            val tag_pupuh = bundle.getInt("tag_pupuh_admin")
            Log.d("id_pupuh_admin",postID.toString())
            id_pupuh_admin = bundle.getInt("id_pupuh_admin_kat")
            nama_pupuh_admin = bundle.getString("nama_pupuh_admin_kat").toString()
            desc_pupuh_admin = bundle.getString("desc_pupuh_admin_kat").toString()

            getDetailData(postID)
            getBaitData(postID)
            getListVideoPupuh(postID)
            getListAudioPupuh(postID)
            getListYadnyaPupuh(postID)
            setupRecyclerViewBait()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideopupuhAdmin.visibility = View.GONE
            lihatSemuaaudiopupuhAdmin.visibility = View.GONE
            lihatSemuayadnyapupuhAdmin.visibility = View.GONE

            goToListLirikPupuh.setOnClickListener {
                val intent = Intent(this, AllLirikPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListVideoPupuh.setOnClickListener {
                val intent = Intent(this, AllVideoPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListAudioPupuh.setOnClickListener {
                val intent = Intent(this, AllAudioPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahLirikPupuh.setOnClickListener {
                val intent = Intent(this, AddLirikPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("nama_pupuh", nama_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            toEditPupuh.setOnClickListener {
                val intent = Intent(this, EditPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deletePupuh.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Pupuh")
                    .setMessage("Apakah anda yakin ingin menghapus kidung ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusPupuh(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

        }

        backToPupuhAdmin.setOnClickListener {
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
                    Toast.makeText(this@DetailPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriPupuhAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh_admin", id_pupuh)
        bundle.putString("nama_pupuh_admin", nama_pupuh_admin)
        bundle.putString("desc_pupuh_admin", desc_pupuh_admin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailPupuhAdmin(id).enqueue(object: Callback<DetailPupuhAdminModel> {
            override fun onResponse(
                call: Call<DetailPupuhAdminModel>,
                response: Response<DetailPupuhAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiPupuhAdmin.text   = result.deskripsi
                    detailNamaPupuhAdmin.text  = result.nama_post
                    detailPupuhAdmin.text = "Sekar Alit"
                    if(result.gambar != null) {
                        Glide.with(this@DetailPupuhAdminActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailPupuhAdmin)
                    }else{
                        imageDetailPupuhAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailPupuhAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitPupuhAdmin(id).enqueue(object :
            Callback<DetailBaitPupuhAdminModel> {
            override fun onResponse(
                call: Call<DetailBaitPupuhAdminModel>,
                response: Response<DetailBaitPupuhAdminModel>
            ) {
                if(response.body()!!.data.toString() == "[]"){
                    tv_lirik.visibility = View.VISIBLE
                    baitPupuhListAdmin.visibility   = View.GONE
                    tambahLirikPupuh.visibility = View.VISIBLE
                    goToListLirikPupuh.visibility = View.GONE
                }else{
                    tv_lirik.visibility = View.GONE
                    baitPupuhListAdmin.visibility = View.VISIBLE
                    tambahLirikPupuh.visibility = View.GONE
                    goToListLirikPupuh.visibility = View.VISIBLE
                }
                showBaitPupuhData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitPupuhAdminModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitPupuhData(body: DetailBaitPupuhAdminModel) {
        val results = body.data
        baitPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitPupuhAdapter = BaitPupuhAdminAdapter(arrayListOf())
        baitPupuhListAdmin.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailPupuhAdminActivity)
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
        shimmerDetailPupuhAdmin.stopShimmer()
        shimmerDetailPupuhAdmin.visibility = View.GONE
        scrollDetailPupuhAdmin.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoPupuhAdapter =  VideoPupuhAdminAdapter(arrayListOf(), object : VideoPupuhAdminAdapter.OnAdapterVideoPupuhAdminListener{
            override fun onClick(result: VideoPupuhAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhAdminActivity, VideoPupuhActivity::class.java)
                bundle.putString("video_pupuh", result.video)
                bundle.putInt("id_pupuh_video", id_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_pupuhAdmin.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailPupuhAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioPupuhAdapter =  AudioPupuhAdminAdapter(arrayListOf(), object : AudioPupuhAdminAdapter.OnAdapterAudioPupuhAdminListener{
            override fun onClick(result: AudioPupuhAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhAdminActivity, AudioPupuhActivity::class.java)
                bundle.putString("audio_pupuh", result.audio)
                bundle.putInt("id_pupuh_audio", id_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_pupuhAdmin.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailPupuhAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaPupuhAdapter =  YadnyaPupuhAdminAdapter(arrayListOf(), object : YadnyaPupuhAdminAdapter.OnAdapterYadnyaPupuhAdminListener{
            override fun onClick(result: YadnyaPupuhAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhAdminActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_pupuhAdmin.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailPupuhAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoPupuh(id_pupuh: Int) {
        ApiService.endpoint.getListVideoPupuhAdmin(id_pupuh)
            .enqueue(object: Callback<VideoPupuhAdminModel> {
                override fun onResponse(
                    call: Call<VideoPupuhAdminModel>,
                    response: Response<VideoPupuhAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideopupuhAdmin.visibility  = View.GONE
                        rv_video_pupuhAdmin.visibility   = View.GONE
                        tambahVideoPupuh.visibility = View.VISIBLE
                        goToListVideoPupuh.visibility = View.GONE
                    }else{
                        nodatavideopupuhAdmin.visibility  = View.GONE
                        rv_video_pupuhAdmin.visibility = View.VISIBLE
                        tambahVideoPupuh.visibility = View.GONE
                        goToListVideoPupuh.visibility = View.VISIBLE
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

                override fun onFailure(call: Call<VideoPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioPupuh(id_post: Int) {
        ApiService.endpoint.getListAudioPupuhAdmin(id_post)
            .enqueue(object: Callback<AudioPupuhAdminModel> {
                override fun onResponse(
                    call: Call<AudioPupuhAdminModel>,
                    response: Response<AudioPupuhAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiopupuhAdmin.visibility  = View.GONE
                        rv_audio_pupuhAdmin.visibility   = View.GONE
                        tambahAudioPupuh.visibility = View.VISIBLE
                        goToListAudioPupuh.visibility = View.GONE
                    }else{
                        nodataaudiopupuhAdmin.visibility  = View.GONE
                        rv_audio_pupuhAdmin.visibility = View.VISIBLE
                        tambahAudioPupuh.visibility = View.GONE
                        goToListAudioPupuh.visibility = View.VISIBLE
                        Log.d("audio",response.body().toString())
                        showAudioPupuhData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaPupuh(id_pupuh: Int) {
        ApiService.endpoint.getYadnyaPupuhAdmin(id_pupuh)
            .enqueue(object: Callback<YadnyaPupuhAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaPupuhAdminModel>,
                    response: Response<YadnyaPupuhAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyapupuhAdmin.visibility  = View.GONE
                        rv_yadnya_pupuhAdmin.visibility   = View.GONE
                        tambahYadnyaPupuh.visibility = View.VISIBLE
                        goToListYadnyaPupuh.visibility = View.GONE
                    }else{
                        nodatayadnyapupuhAdmin.visibility  = View.GONE
                        rv_yadnya_pupuhAdmin.visibility = View.VISIBLE
                        tambahYadnyaPupuh.visibility = View.GONE
                        goToListYadnyaPupuh.visibility = View.VISIBLE
                        Log.d("yadnya",response.body().toString())
                        showYadnyaPupuhData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoPupuhData(body: VideoPupuhAdminModel) {
        val results = body.data
        videoPupuhAdapter.setData(results)
    }
    private fun showAudioPupuhData(body: AudioPupuhAdminModel) {
        val results = body.data
        audioPupuhAdapter.setData(results)
    }
    private fun showYadnyaPupuhData(body: YadnyaPupuhAdminModel) {
        val results = body.data
        yadnyaPupuhAdapter.setData(results)
    }


}