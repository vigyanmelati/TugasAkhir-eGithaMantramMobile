package com.example.ekidungmantram.admin.laguanak

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
import com.example.ekidungmantram.adapter.admin.*
import com.example.ekidungmantram.admin.pupuh.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.*
import com.example.ekidungmantram.user.*
import kotlinx.android.synthetic.main.activity_detail_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_detail_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_detail_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_detail_pupuh_admin.deletePupuh
import kotlinx.android.synthetic.main.activity_detail_pupuh_admin.tv_lirik
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLaguAnakAdminActivity : AppCompatActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitLaguAnakAdapter : BaitLaguAnakAdminAdapter
    private lateinit var videoLaguAnakAdapter  : VideoLaguAnakAdminAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioLaguAnakAdapter  : AudioLaguAnakAdminAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaLaguAnakAdapter  : YadnyaLaguAnakAdminAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_lagu_anak : Int = 0
    private var id_lagu_anak_admin : Int = 0
    private lateinit var nama_lagu_anak_admin :String
    private lateinit var desc_lagu_anak_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu_anak_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_lagu_anak = bundle.getInt("id_lagu_anak_admin")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak_admin")
            val nama_lagu_anak = bundle.getString("nama_lagu_anak_admin")
            val nama_tag_lagu_anak = bundle.getString("nama_tag_lagu_anak_admin")
            val gambar_lagu_anak = bundle.getString("gambar_lagu_anak_admin")
            val tag_lagu_anak = bundle.getInt("tag_lagu_anak_admin")
            Log.d("id_lagu_anak_admin",postID.toString())
            id_lagu_anak_admin = bundle.getInt("id_lagu_anak_admin_kat")
            nama_lagu_anak_admin = bundle.getString("nama_lagu_anak_admin_kat").toString()
            desc_lagu_anak_admin = bundle.getString("desc_lagu_anak_admin_kat").toString()

            getDetailData(postID)
            getBaitData(postID)
            getListVideoLaguAnak(postID)
            getListAudioLaguAnak(postID)
            getListYadnyaLaguAnak(postID)
            setupRecyclerViewBait()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideoLaguAnakAdmin.visibility = View.GONE
            lihatSemuaaudioLaguAnakAdmin.visibility = View.GONE
            lihatSemuayadnyaLaguAnakAdmin.visibility = View.GONE

            goToListLirikLaguAnak.setOnClickListener {
                val intent = Intent(this, AllLirikLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListVideoLaguAnak.setOnClickListener {
                val intent = Intent(this, AllVideoLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListYadnyaLaguAnak.setOnClickListener {
                val intent = Intent(this, AllYadnyaOnLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak_admin", postID)
                bundle.putString("nama_lagu_anak_admin", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListAudioLaguAnak.setOnClickListener {
                val intent = Intent(this, AllAudioLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahLirikLaguAnak.setOnClickListener {
                val intent = Intent(this, AddLirikLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahVideoLaguAnak.setOnClickListener {
                val intent = Intent(this, AddVideoPupuhAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahAudioLaguAnak.setOnClickListener {
                val intent = Intent(this, AddAudioLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahYadnyaLaguAnak.setOnClickListener {
                val intent = Intent(this, AddYadnyaToLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            toEditLaguAnak.setOnClickListener {
                val intent = Intent(this, EditLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deleteLaguAnak.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Sekar Rare")
                    .setMessage("Apakah anda yakin ingin menghapus Sekar Rare ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusLaguAnak(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

        }

        backToLaguAnakAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailLaguActivity", message)
    }

    private fun hapusLaguAnak(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteLaguAnakAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriLaguAnakAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_lagu_anak_admin", id_lagu_anak_admin)
        bundle.putString("nama_lagu_anak_admin", nama_lagu_anak_admin)
        bundle.putString("desc_lagu_anak__admin", desc_lagu_anak_admin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailLaguAnakAdmin(id).enqueue(object:
            Callback<DetailLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakAdminModel>,
                response: Response<DetailLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiLaguAnakAdmin.text   = result.deskripsi
                    detailNamaLaguAnakAdmin.text  = result.nama_post
                    detailLaguAnakAdmin.text = "Sekar Rare"
                    if(result.gambar != null) {
                        Glide.with(this@DetailLaguAnakAdminActivity)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailLaguAnakAdmin)
                            .load(result.gambar).into(imageDetailLaguAnakAdmin)
                    }else{
                        imageDetailLaguAnakAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailLaguAnakAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitLaguAnakAdmin(id).enqueue(object :
            Callback<DetailBaitLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailBaitLaguAnakAdminModel>,
                response: Response<DetailBaitLaguAnakAdminModel>
            ) {
                if(response.body()!!.data.toString() == "[]"){
                    tv_lirik_lagu.visibility = View.VISIBLE
                    baitLaguAnakListAdmin.visibility   = View.GONE
                    tambahLirikLaguAnak.visibility = View.VISIBLE
                    goToListLirikLaguAnak.visibility = View.GONE
                }else{
                    tv_lirik_lagu.visibility = View.GONE
                    baitLaguAnakListAdmin.visibility = View.VISIBLE
                    tambahLirikLaguAnak.visibility = View.GONE
                    goToListLirikLaguAnak.visibility = View.VISIBLE
                }
                showBaitLaguAnakData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitLaguAnakAdminModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitLaguAnakData(body: DetailBaitLaguAnakAdminModel) {
        val results = body.data
        baitLaguAnakAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitLaguAnakAdapter = BaitLaguAnakAdminAdapter(arrayListOf())
        baitLaguAnakListAdmin.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailLaguAnakAdminActivity)
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
        shimmerDetailLaguAnakAdmin.stopShimmer()
        shimmerDetailLaguAnakAdmin.visibility = View.GONE
        scrollDetailLaguAnakAdmin.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoLaguAnakAdapter =  VideoLaguAnakAdminAdapter(arrayListOf(), object : VideoLaguAnakAdminAdapter.OnAdapterVideoLaguAnakAdminListener{
            override fun onClick(result: VideoLaguAnakAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailLaguAnakAdminActivity, VideoLaguAnakActivity::class.java)
                bundle.putString("video_lagu_anak", result.video)
                bundle.putInt("id_lagu_anak_video", id_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_lagu_anakAdmin.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailLaguAnakAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioLaguAnakAdapter =  AudioLaguAnakAdminAdapter(arrayListOf(), object : AudioLaguAnakAdminAdapter.OnAdapterAudioLaguAnakAdminListener{
            override fun onClick(result: AudioLaguAnakAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailLaguAnakAdminActivity, AudioLaguAnakActivity::class.java)
                bundle.putString("audio_lagu_anak", result.audio)
                bundle.putInt("id_lagu_anak_audio", id_lagu_anak)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_lagu_anakAdmin.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailLaguAnakAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaLaguAnakAdapter =  YadnyaLaguAnakAdminAdapter(arrayListOf(), object : YadnyaLaguAnakAdminAdapter.OnAdapterYadnyaLaguAnakAdminListener{
            override fun onClick(result: YadnyaLaguAnakAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailLaguAnakAdminActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_lagu_anakAdmin.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailLaguAnakAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoLaguAnak(id_lagu_anak: Int) {
        ApiService.endpoint.getListVideoLaguAnakAdmin(id_lagu_anak)
            .enqueue(object: Callback<VideoLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<VideoLaguAnakAdminModel>,
                    response: Response<VideoLaguAnakAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideoLaguAnakAdmin.visibility  = View.GONE
                        rv_video_lagu_anakAdmin.visibility   = View.GONE
                        tambahVideoLaguAnak.visibility = View.VISIBLE
                        goToListVideoLaguAnak.visibility = View.GONE
                    }else{
                        nodatavideoLaguAnakAdmin.visibility  = View.GONE
                        rv_video_lagu_anakAdmin.visibility = View.VISIBLE
                        tambahVideoLaguAnak.visibility = View.GONE
                        goToListVideoLaguAnak.visibility = View.VISIBLE
                        Log.d("video",response.body().toString())
                        showVideoLaguAnakData(response.body()!!)
                    }
//                    if(response.body()!!.data.toString() == "[]") {
//                        layoutGamelan.visibility = View.GONE
//                    }else{
//                        layoutGamelan.visibility = View.VISIBLE
//                        nodatayadnyagamelan.visibility = View.GONE
//                        showGamelanYadnyaData(response.body()!!)
//                    }

                }

                override fun onFailure(call: Call<VideoLaguAnakAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioLaguAnak(id_post: Int) {
        ApiService.endpoint.getListAudioLaguAnakAdmin(id_post)
            .enqueue(object: Callback<AudioLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<AudioLaguAnakAdminModel>,
                    response: Response<AudioLaguAnakAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudioLaguAnakAdmin.visibility  = View.GONE
                        rv_audio_lagu_anakAdmin.visibility   = View.GONE
                        tambahAudioLaguAnak.visibility = View.VISIBLE
                        goToListAudioLaguAnak.visibility = View.GONE
                    }else{
                        nodataaudioLaguAnakAdmin.visibility  = View.GONE
                        rv_audio_lagu_anakAdmin.visibility = View.VISIBLE
                        tambahAudioLaguAnak.visibility = View.GONE
                        goToListAudioLaguAnak.visibility = View.VISIBLE
                        Log.d("audio",response.body().toString())
                        showAudioLaguAnakData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioLaguAnakAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaLaguAnak(id_lagu_anak: Int) {
        ApiService.endpoint.getYadnyaLaguAnakAdmin(id_lagu_anak)
            .enqueue(object: Callback<YadnyaLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaLaguAnakAdminModel>,
                    response: Response<YadnyaLaguAnakAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyaLaguAnakAdmin.visibility  = View.GONE
                        rv_yadnya_lagu_anakAdmin.visibility   = View.GONE
                        tambahYadnyaLaguAnak.visibility = View.VISIBLE
                        goToListYadnyaLaguAnak.visibility = View.GONE
                    }else{
                        nodatayadnyaLaguAnakAdmin.visibility  = View.GONE
                        rv_yadnya_lagu_anakAdmin.visibility = View.VISIBLE
                        tambahYadnyaLaguAnak.visibility = View.GONE
                        goToListYadnyaLaguAnak.visibility = View.VISIBLE
                        Log.d("yadnya",response.body().toString())
                        showYadnyaLaguAnakData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaLaguAnakAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoLaguAnakData(body: VideoLaguAnakAdminModel) {
        val results = body.data
        videoLaguAnakAdapter.setData(results)
    }
    private fun showAudioLaguAnakData(body: AudioLaguAnakAdminModel) {
        val results = body.data
        audioLaguAnakAdapter.setData(results)
    }
    private fun showYadnyaLaguAnakData(body: YadnyaLaguAnakAdminModel) {
        val results = body.data
        yadnyaLaguAnakAdapter.setData(results)
    }


}