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
import kotlinx.android.synthetic.main.activity_detail_kakawin.*
import kotlinx.android.synthetic.main.activity_detail_pupuh.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKakawinActivity : AppCompatActivity() {
    private lateinit var db             : DharmagitaDb
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitKakawinAdapter : BaitKakawinAdapter
    private lateinit var videoKakawinAdapter  : VideoKakawinAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioKakawinAdapter  : AudioKakawinAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaKakawinAdapter  : YadnyaKakawinAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_kakawin : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kakawin)
        db = Room.databaseBuilder(applicationContext, DharmagitaDb::class.java, "dharmagitabookmarked.db").build()
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kakawin")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val nama_kakawin = bundle.getString("nama_kakawin")
            val nama_tag_kakawin = bundle.getString("nama_tag_kakawin")
            val gambar_kakawin = bundle.getString("gambar_kakawin")
            val tag_kakawin = bundle.getInt("tag_kakawin")
            Log.d("id_kakawin",postID.toString())

            getDetailData(postID)
            getBaitData(postID)
            getListVideoKakawin(postID)
            getListAudioKakawin(postID)
            getListYadnyaKakawin(postID)
            setupRecyclerViewBait()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideokakawin.visibility = View.GONE
            lihatSemuaaudiokakawin.visibility = View.GONE
            lihatSemuayadnyakakawin.visibility = View.GONE

            bookmarked_kakawin.setOnClickListener {
                if(bookmarked_kakawin.isChecked){
                    Toast.makeText(this, "Bookmark Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bookmark Berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            bookmarked_kakawin.setOnCheckedChangeListener { _, isChecked->
                if (isChecked){
                    addBookmark(postID, tag_kakawin, nama_kakawin, nama_tag_kakawin, gambar_kakawin)
                }else{
                    deleteBookmark(postID)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val test = db.dharmagitaDao().fetch(postID)
                if(test != null){
                    bookmarked_kakawin.isChecked = true
                }else{
                    bookmarked_kakawin.isChecked = false
                }
            }
        }

        backToKakawin.setOnClickListener {
            val intent = Intent(this, AllKakawinActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailKakawinActivity", message)
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailKakawin(id).enqueue(object: Callback<DetailKakawinModel> {
            override fun onResponse(
                call: Call<DetailKakawinModel>,
                response: Response<DetailKakawinModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiKakawin.text   = result.deskripsi
                    detailNamaKakawin.text  = result.nama_post
                    detailKakawin.text = "Sekar Agung "
                    if(result.gambar != null) {
                        Glide.with(this@DetailKakawinActivity)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailKakawin)
                            .load(result.gambar).into(imageDetailKakawin)
                    }else{
                        imageDetailKakawin.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKakawinModel>, t: Throwable) {
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

    private fun showBaitKakawinData(body: DetailBaitKakawinModel) {
        val results = body.data
        baitKakawinAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKakawinAdapter = BaitKakawinAdapter(arrayListOf())
        baitKakawinList.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailKakawinActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKakawinAdapter
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
        shimmerDetailKakawin.stopShimmer()
        shimmerDetailKakawin.visibility = View.GONE
        scrollDetailKakawin.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoKakawinAdapter =  VideoKakawinAdapter(arrayListOf(), object : VideoKakawinAdapter.OnAdapterVideoKakawinListener{
            override fun onClick(result: VideoKakawinModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKakawinActivity, VideoKakawinActivity::class.java)
                bundle.putString("video_kakawin", result.video)
                bundle.putInt("id_kakawin_video", id_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_kakawin.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailKakawinActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioKakawinAdapter =  AudioKakawinAdapter(arrayListOf(), object : AudioKakawinAdapter.OnAdapterAudioKakawinListener{
            override fun onClick(result: AudioKakawinModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKakawinActivity, AudioKakawinActivity::class.java)
                bundle.putString("audio_kakawin", result.audio)
                bundle.putInt("id_kakawin_audio", id_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_kakawin.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailKakawinActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaKakawinAdapter =  YadnyaKakawinAdapter(arrayListOf(), object : YadnyaKakawinAdapter.OnAdapterYadnyaKakawinListener{
            override fun onClick(result: YadnyaKakawinModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKakawinActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_kakawin.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailKakawinActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoKakawin(id_kakawin: Int) {
        ApiService.endpoint.getListVideoKakawin(id_kakawin)
            .enqueue(object: Callback<VideoKakawinModel> {
                override fun onResponse(
                    call: Call<VideoKakawinModel>,
                    response: Response<VideoKakawinModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideokakawin.visibility  = View.VISIBLE
                        Log.d("video",response.body().toString())
                    }else{
                        nodatavideokakawin.visibility  = View.GONE
                        showVideoKakawinData(response.body()!!)
                    }
//                    if(response.body()!!.data.toString() == "[]") {
//                        layoutGamelan.visibility = View.GONE
//                    }else{
//                        layoutGamelan.visibility = View.VISIBLE
//                        nodatayadnyagamelan.visibility = View.GONE
//                        showGamelanYadnyaData(response.body()!!)
//                    }

                }

                override fun onFailure(call: Call<VideoKakawinModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioKakawin(id_post: Int) {
        ApiService.endpoint.getListAudioKakawin(id_post)
            .enqueue(object: Callback<AudioKakawinModel> {
                override fun onResponse(
                    call: Call<AudioKakawinModel>,
                    response: Response<AudioKakawinModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiokakawin.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodataaudiokakawin.visibility  = View.GONE
                        showAudioKakawinData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioKakawinModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaKakawin(id_kakawin: Int) {
        ApiService.endpoint.getYadnyaKakawin(id_kakawin)
            .enqueue(object: Callback<YadnyaKakawinModel> {
                override fun onResponse(
                    call: Call<YadnyaKakawinModel>,
                    response: Response<YadnyaKakawinModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyakakawin.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodatayadnyakakawin.visibility  = View.GONE
                        showYadnyaKakawinData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaKakawinModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoKakawinData(body: VideoKakawinModel) {
        val results = body.data
        videoKakawinAdapter.setData(results)
    }
    private fun showAudioKakawinData(body: AudioKakawinModel) {
        val results = body.data
        audioKakawinAdapter.setData(results)
    }
    private fun showYadnyaKakawinData(body: YadnyaKakawinModel) {
        val results = body.data
        yadnyaKakawinAdapter.setData(results)
        yadnyaKakawinAdapter.setData(results)
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