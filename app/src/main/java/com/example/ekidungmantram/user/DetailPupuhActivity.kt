package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.database.data.Dharmagita
import com.example.ekidungmantram.database.setup.DharmagitaDb
import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.user.pupuh.AllPupuhActivity
import kotlinx.android.synthetic.main.activity_detail_kakawin.*
import kotlinx.android.synthetic.main.activity_detail_pupuh.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPupuhActivity : AppCompatActivity() {
    private lateinit var db             : DharmagitaDb
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitPupuhAdapter : BaitPupuhAdapter
    private var layoutManagerArti          : LinearLayoutManager? = null
    private lateinit var artiPupuhAdapter : ArtiPupuhAdapter
    private lateinit var videoPupuhAdapter  : VideoPupuhAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioPupuhAdapter  : AudioPupuhAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaPupuhAdapter  : YadnyaPupuhAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_pupuh : Int = 0
    private var id_pupuh_kat : Int = 0
    private var tag_pupuh : Int = 0
    lateinit var nama_pupuh_kat: String
    lateinit var desc_pupuh_kat: String
    lateinit var nama_pupuh: String
    lateinit var nama_tag_pupuh: String
    lateinit var gambar_pupuh: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pupuh)
        db = Room.databaseBuilder(applicationContext, DharmagitaDb::class.java, "dharmagitabookmarked.db").build()
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh")
           id_pupuh_kat = bundle.getInt("id_pupuh_kat")
            nama_pupuh_kat = bundle.getString("nama_pupuh_kat").toString()
            desc_pupuh_kat = bundle.getString("desc_pupuh_kat").toString()

            backToPupuh.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AllPupuhActivity::class.java)
                bundle.putInt("id_pupuh", id_pupuh_kat)
                bundle.putString("nama_pupuh", nama_pupuh_kat)
                bundle.putString("desc_pupuh", desc_pupuh_kat)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_pupuh").toString()
            nama_tag_pupuh = bundle.getString("nama_tag_pupuh").toString()
            gambar_pupuh = bundle.getString("gambar_pupuh").toString()
            tag_pupuh = bundle.getInt("tag_pupuh")
            Log.d("id_pupuh",postID.toString())

            getDetailData(postID)
            getBaitData(postID)
            getListVideoPupuh(postID)
            getListAudioPupuh(postID)
            getListYadnyaPupuh(postID)
            setupRecyclerViewBait()
            setupRecyclerViewArti()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideopupuh.visibility = View.GONE
            lihatSemuaaudiopupuh.visibility = View.GONE
            lihatSemuayadnyapupuh.visibility = View.GONE

            bookmarked_pupuh.setOnClickListener {
                if(bookmarked_pupuh.isChecked){
                    Toast.makeText(this, "Bookmark Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bookmark Berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            bookmarked_pupuh.setOnCheckedChangeListener { _, isChecked->
                if (isChecked){
                    addBookmark(postID, tag_pupuh, nama_pupuh, nama_tag_pupuh, gambar_pupuh)
                }else{
                    deleteBookmark(postID)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val test = db.dharmagitaDao().fetch(postID)
                if(test != null){
                    bookmarked_pupuh.isChecked = true
                }else{
                    bookmarked_pupuh.isChecked = false
                }
            }
        }

    }

    private fun printLog(message: String) {
        Log.d("DetailPupuhAnakActivity", message)
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailPupuh(id).enqueue(object: Callback<DetailPupuhModel> {
            override fun onResponse(
                call: Call<DetailPupuhModel>,
                response: Response<DetailPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiPupuh.text   = result.deskripsi
                    detailNamaPupuh.text  = result.nama_post
                    detailPupuh.text = "Sekar Alit"
                    if(result.gambar != null) {
                        Glide.with(this@DetailPupuhActivity)
//                            .load(result.gambar).into(imageDetailPupuh)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailPupuh)
                    }else{
                        imageDetailPupuh.setImageResource(R.drawable.sample_image_yadnya)
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
                showBaitPupuhData(response.body()!!)
                showArtiPupuhData(response.body()!!)
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

    private fun showArtiPupuhData(body: DetailBaitPupuhModel) {
        val results = body.data
        artiPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitPupuhAdapter = BaitPupuhAdapter(arrayListOf())
        baitPupuhList.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailPupuhActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewArti() {
        artiPupuhAdapter = ArtiPupuhAdapter(arrayListOf())
        artiPupuhList.apply {
            layoutManagerArti = LinearLayoutManager(this@DetailPupuhActivity)
            layoutManager     = layoutManagerArti
            adapter           = artiPupuhAdapter
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
        shimmerDetailPupuh.stopShimmer()
        shimmerDetailPupuh.visibility = View.GONE
        scrollDetailPupuh.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoPupuhAdapter =  VideoPupuhAdapter(arrayListOf(), object : VideoPupuhAdapter.OnAdapterVideoPupuhListener{
            override fun onClick(result: VideoPupuhModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhActivity, VideoPupuhActivity::class.java)
                bundle.putString("video_pupuh", result.video)
                bundle.putInt("id_pupuh_video", id_pupuh)
                bundle.putInt("id_video_pupuh", result.id_video)
                bundle.putInt("id_pupuh", id_pupuh)
                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                bundle.putInt("tag_pupuh", tag_pupuh)
                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("nama_tag_pupuh", nama_pupuh)
                bundle.putString("gambar_pupuh", gambar_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_pupuh.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailPupuhActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioPupuhAdapter =  AudioPupuhAdapter(arrayListOf(), object : AudioPupuhAdapter.OnAdapterAudioPupuhListener{
            override fun onClick(result: AudioPupuhModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhActivity, AudioPupuhActivity::class.java)
                bundle.putString("audio_pupuh", result.audio)
                bundle.putInt("id_audio_pupuh", result.id_audio)
                bundle.putInt("id_pupuh_audio", id_pupuh)
                bundle.putInt("id_pupuh", id_pupuh)
                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                bundle.putInt("tag_pupuh", tag_pupuh)
                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("nama_tag_pupuh", nama_pupuh)
                bundle.putString("gambar_pupuh", gambar_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_pupuh.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailPupuhActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaPupuhAdapter =  YadnyaPupuhAdapter(arrayListOf(), object : YadnyaPupuhAdapter.OnAdapterYadnyaPupuhListener{
            override fun onClick(result: YadnyaPupuhModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailPupuhActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                bundle.putInt("id_pupuh", id_pupuh)
                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                bundle.putInt("tag_pupuh", tag_pupuh)
                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                bundle.putString("nama_pupuh", nama_pupuh)
                bundle.putString("nama_tag_pupuh", nama_pupuh)
                bundle.putString("nama_tag_dhar", nama_pupuh)
                bundle.putString("gambar_pupuh", gambar_pupuh)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_pupuh.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailPupuhActivity, 1, LinearLayoutManager.HORIZONTAL, false)
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
                        nodatavideopupuh.visibility  = View.VISIBLE
                        Log.d("video",response.body().toString())
                    }else{
                        nodatavideopupuh.visibility  = View.GONE
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
                        nodataaudiopupuh.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodataaudiopupuh.visibility  = View.GONE
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
                        nodatayadnyapupuh.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodatayadnyapupuh.visibility  = View.GONE
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

    private fun addBookmark(postID: Int , id_tag: Int, namapost: String?,namatag: String?, gambars: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            db.dharmagitaDao().addBookmarkedDharmagita(
                Dharmagita(0, postID ,id_tag,namapost.toString(),namatag.toString(), gambars.toString())
            )
            val test = db.dharmagitaDao().fetch(postID)
            printLog(test.toString())
        }
    }

    private fun deleteBookmark(postID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.dharmagitaDao().deleteById(postID)
        }
    }
}