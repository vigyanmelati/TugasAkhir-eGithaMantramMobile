package com.example.ekidungmantram.user

import android.content.Intent
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
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_kakawin.*
import kotlinx.android.synthetic.main.activity_detail_kidung.*
import kotlinx.android.synthetic.main.activity_detail_yadnya.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKidungActivity : YouTubeBaseActivity() {
    private lateinit var db             : DharmagitaDb
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitKidungAdapter : BaitKidungAdapter
    private lateinit var videoKidungAdapter  : VideoKidungAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioKidungAdapter  : AudioKidungAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaKidungAdapter  : YadnyaKidungAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_kidung : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kidung)
        db = Room.databaseBuilder(applicationContext, DharmagitaDb::class.java, "dharmagitabookmarked.db").build()
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kidung = bundle.getInt("id_kidung")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            val nama_kidung = bundle.getString("nama_kidung")
            val nama_tag_kidung = bundle.getString("nama_tag_kidung")
            val gambar_kidung = bundle.getString("gambar_kidung")
            val tag_kidung = bundle.getInt("tag_kidung")
            Log.d("id_kidung",postID.toString())

            getDetailData(postID)
            getBaitData(postID)
            getListVideoKidung(postID)
            getListAudioKidung(postID)
            getListYadnyaKidung(postID)
            setupRecyclerViewBait()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideokidung.visibility = View.GONE
            lihatSemuaaudiokidung.visibility = View.GONE
            lihatSemuayadnyakidung.visibility = View.GONE

            bookmarked_kidung.setOnClickListener {
                if(bookmarked_kidung.isChecked){
                    Toast.makeText(this, "Bookmark Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bookmark Berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            bookmarked_kidung.setOnCheckedChangeListener { _, isChecked->
                if (isChecked){
                    addBookmark(postID, tag_kidung, nama_kidung, nama_tag_kidung, gambar_kidung)
                }else{
                    deleteBookmark(postID)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val test = db.dharmagitaDao().fetch(postID)
                if(test != null){
                    bookmarked_kidung.isChecked = true
                }else{
                    bookmarked_kidung.isChecked = false
                }
            }
        }

        backToKidung.setOnClickListener {
            val intent = Intent(this, AllKidungActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailKidungActivity", message)
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailKidung(id).enqueue(object: Callback<DetailKidungModel> {
            override fun onResponse(
                call: Call<DetailKidungModel>,
                response: Response<DetailKidungModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiKidung.text   = result.deskripsi
                    detailNamaKidung.text  = result.nama_post
                    detailJenisKidung.text = "Kidung " + result.nama_kategori
                    if(result.gambar != null) {
                        Glide.with(this@DetailKidungActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailKidung)
                    }else{
                        imageDetailKidung.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKidungModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitKidung(id).enqueue(object : Callback<DetailBaitKidungModel>{
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

    private fun showBaitKidungData(body: DetailBaitKidungModel) {
        val results = body.data
        baitKidungAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKidungAdapter = BaitKidungAdapter(arrayListOf())
        baitKidungList.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailKidungActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKidungAdapter
            setHasFixedSize(true)
        }
    }

//    private fun playYoutubeVideo(video: String) {
//        youtubePlayerKidung.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailKidung.stopShimmer()
        shimmerDetailKidung.visibility = View.GONE
        scrollDetailKidung.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoKidungAdapter =  VideoKidungAdapter(arrayListOf(), object : VideoKidungAdapter.OnAdapterVideoKidungListener{
            override fun onClick(result: VideoKidungModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKidungActivity, VideoKidungActivity::class.java)
                bundle.putString("video_kidung", result.video)
                bundle.putInt("id_kidung_video", id_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_kidung.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailKidungActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioKidungAdapter =  AudioKidungAdapter(arrayListOf(), object : AudioKidungAdapter.OnAdapterAudioKidungListener{
            override fun onClick(result: AudioKidungModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKidungActivity, AudioKidungActivity::class.java)
                bundle.putString("audio_kidung", result.audio)
                bundle.putInt("id_kidung_audio", id_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_kidung.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailKidungActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaKidungAdapter =  YadnyaKidungAdapter(arrayListOf(), object : YadnyaKidungAdapter.OnAdapterYadnyaKidungListener{
            override fun onClick(result: YadnyaKidungModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKidungActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_kidung.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailKidungActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoKidung(id_kidung: Int) {
        ApiService.endpoint.getListVideoKidung(id_kidung)
            .enqueue(object: Callback<VideoKidungModel> {
                override fun onResponse(
                    call: Call<VideoKidungModel>,
                    response: Response<VideoKidungModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideokidung.visibility  = View.VISIBLE
                        Log.d("video",response.body().toString())
                    }else{
                        nodatavideokidung.visibility  = View.GONE
                        showVideoKidungData(response.body()!!)
                    }
//                    if(response.body()!!.data.toString() == "[]") {
//                        layoutGamelan.visibility = View.GONE
//                    }else{
//                        layoutGamelan.visibility = View.VISIBLE
//                        nodatayadnyagamelan.visibility = View.GONE
//                        showGamelanYadnyaData(response.body()!!)
//                    }

                }

                override fun onFailure(call: Call<VideoKidungModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioKidung(id_post: Int) {
        ApiService.endpoint.getListAudioKidung(id_post)
            .enqueue(object: Callback<AudioKidungModel> {
                override fun onResponse(
                    call: Call<AudioKidungModel>,
                    response: Response<AudioKidungModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiokidung.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodataaudiokidung.visibility  = View.GONE
                        showAudioKidungData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioKidungModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaKidung(id_kidung: Int) {
        ApiService.endpoint.getYadnyaKidung(id_kidung)
            .enqueue(object: Callback<YadnyaKidungModel> {
                override fun onResponse(
                    call: Call<YadnyaKidungModel>,
                    response: Response<YadnyaKidungModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyakidungdetail.visibility  = View.VISIBLE
                        Log.d("audio",response.body().toString())
                    }else{
                        nodatayadnyakidungdetail.visibility  = View.GONE
                        showYadnyaKidungData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaKidungModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoKidungData(body: VideoKidungModel) {
        val results = body.data
        videoKidungAdapter.setData(results)
    }
    private fun showAudioKidungData(body: AudioKidungModel) {
        val results = body.data
        audioKidungAdapter.setData(results)
    }
    private fun showYadnyaKidungData(body: YadnyaKidungModel) {
        val results = body.data
        yadnyaKidungAdapter.setData(results)
        yadnyaKidungAdapter.setData(results)
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