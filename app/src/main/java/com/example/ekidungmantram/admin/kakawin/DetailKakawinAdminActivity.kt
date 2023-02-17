package com.example.ekidungmantram.admin.kakawin

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
import com.example.ekidungmantram.admin.kakawin.*
import com.example.ekidungmantram.admin.pupuh.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.*
import com.example.ekidungmantram.user.AudioKakawinActivity
import com.example.ekidungmantram.user.DetailYadnyaActivity
import com.example.ekidungmantram.user.VideoKakawinActivity
import kotlinx.android.synthetic.main.activity_detail_kakawin.*
import kotlinx.android.synthetic.main.activity_detail_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKakawinAdminActivity : AppCompatActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitKakawinAdapter : BaitKakawinAdminAdapter
    private var layoutManagerArti         : LinearLayoutManager? = null
    private lateinit var artiKakawinAdapter : ArtiKakawinAdminAdapter
    private lateinit var videoKakawinAdapter  : VideoKakawinAdminAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioKakawinAdapter  : AudioKakawinAdminAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaKakawinAdapter  : YadnyaKakawinAdminAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_kakawin : Int = 0
    private var id_kakawin_admin : Int = 0
    private lateinit var nama_kakawin_admin :String
    private lateinit var desc_kakawin_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kakawin_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kakawin_admin")
        }
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin_admin")
            val nama_kakawin = bundle.getString("nama_kakawin_admin")
            val nama_tag_kakawin = bundle.getString("nama_tag_kakawin_admin")
            val gambar_kakawin = bundle.getString("gambar_kakawin_admin")
            val tag_kakawin = bundle.getInt("tag_kakawin_admin")
            Log.d("id_kakawin_admin",postID.toString())
            id_kakawin_admin = bundle.getInt("id_kakawin_admin_kat")
            nama_kakawin_admin = bundle.getString("nama_kakawin_admin_kat").toString()
            desc_kakawin_admin = bundle.getString("desc_kakawin_admin_kat").toString()

            getDetailData(postID)
            getBaitData(postID)
            getListVideoKakawin(postID)
            getListAudioKakawin(postID)
            getListYadnyaKakawin(postID)
            setupRecyclerViewBait()
            setupRecyclerViewArti()
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideokakawinAdmin.visibility = View.GONE
            lihatSemuaaudiokakawinAdmin.visibility = View.GONE
            lihatSemuayadnyakakawinAdmin.visibility = View.GONE

            goToListLirikKakawin.setOnClickListener {
                val intent = Intent(this, AllLirikKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListVideoKakawin.setOnClickListener {
                val intent = Intent(this, AllVideoKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListYadnyaKakawin.setOnClickListener {
                val intent = Intent(this, AllYadnyaonKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin_admin", postID)
                bundle.putString("nama_kakawin_admin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListAudioKakawin.setOnClickListener {
                val intent = Intent(this, AllAudioKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahLirikKakawin.setOnClickListener {
                val intent = Intent(this, AddLirikKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahVideoKakawin.setOnClickListener {
                val intent = Intent(this, AddVideoKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahAudioKakawin.setOnClickListener {
                val intent = Intent(this, AddAudioKakawinNewActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahYadnyaKakawin.setOnClickListener {
                val intent = Intent(this, AddYadnyaToKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                bundle.putString("nama_kakawin", nama_kakawin)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            toEditKakawin.setOnClickListener {
                val intent = Intent(this, EditKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deleteKakawin.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Sekar Agung")
                    .setMessage("Apakah anda yakin ingin menghapus sekar agung ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusKakawin(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

        }

        backToKakawinAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailKakawinActivity", message)
    }

    private fun hapusKakawin(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteKakawinAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin_admin", id_kakawin_admin)
        bundle.putString("nama_kakawin_admin", nama_kakawin_admin)
        bundle.putString("desc_kakawin_admin", desc_kakawin_admin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailKakawinAdmin(id).enqueue(object:
            Callback<DetailKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailKakawinAdminModel>,
                response: Response<DetailKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiKakawinAdmin.text   = result.deskripsi
                    detailNamaKakawinAdmin.text  = result.nama_post
                    detailKakawinAdmin.text = "Sekar Alit"
                    if(result.gambar != null) {
                        Glide.with(this@DetailKakawinAdminActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailKakawinAdmin)
//                            .load(result.gambar).into(imageDetailKakawinAdmin)
                    }else{
                        imageDetailKakawinAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKakawinAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitKakawinAdmin(id).enqueue(object :
            Callback<DetailBaitKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailBaitKakawinAdminModel>,
                response: Response<DetailBaitKakawinAdminModel>
            ) {
                if(response.body()!!.data.toString() == "[]"){
                    tv_lirik.visibility = View.VISIBLE
                    baitKakawinListAdmin.visibility   = View.GONE
                    tambahLirikKakawin.visibility = View.VISIBLE
                    goToListLirikKakawin.visibility = View.GONE
                    bait_sekar_agung.visibility = View.GONE
                    arti_sekar_agung.visibility = View.GONE
                }else{
                    tv_lirik.visibility = View.GONE
                    baitKakawinListAdmin.visibility = View.VISIBLE
                    tambahLirikKakawin.visibility = View.GONE
                    goToListLirikKakawin.visibility = View.VISIBLE
                    bait_sekar_agung.visibility = View.VISIBLE
                    arti_sekar_agung.visibility = View.VISIBLE
                }
                showBaitKakawinData(response.body()!!)
                showArtiKakawinData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitKakawinAdminModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitKakawinData(body: DetailBaitKakawinAdminModel) {
        val results = body.data
        baitKakawinAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKakawinAdapter = BaitKakawinAdminAdapter(arrayListOf())
        baitKakawinListAdmin.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailKakawinAdminActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun showArtiKakawinData(body: DetailBaitKakawinAdminModel) {
        val results = body.data
        artiKakawinAdapter.setData(results)
    }

    private fun setupRecyclerViewArti() {
        artiKakawinAdapter = ArtiKakawinAdminAdapter(arrayListOf())
        artiKakawinListAdmin.apply {
            layoutManagerArti = LinearLayoutManager(this@DetailKakawinAdminActivity)
            layoutManager     = layoutManagerArti
            adapter           = artiKakawinAdapter
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
        shimmerDetailKakawinAdmin.stopShimmer()
        shimmerDetailKakawinAdmin.visibility = View.GONE
        scrollDetailKakawinAdmin.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoKakawinAdapter =  VideoKakawinAdminAdapter(arrayListOf(), object : VideoKakawinAdminAdapter.OnAdapterVideoKakawinAdminListener{
            override fun onClick(result: VideoKakawinAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKakawinAdminActivity, VideoKakawinActivity::class.java)
                bundle.putString("video_kakawin", result.video)
                bundle.putInt("id_kakawin_video", id_kakawin)
                bundle.putInt("id_video_kakawin", result.id_video)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_kakawinAdmin.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailKakawinAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioKakawinAdapter =  AudioKakawinAdminAdapter(arrayListOf(), object : AudioKakawinAdminAdapter.OnAdapterAudioKakawinAdminListener{
            override fun onClick(result: AudioKakawinAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKakawinAdminActivity, AudioKakawinActivity::class.java)
                bundle.putString("audio_kakawin", result.audio)
                bundle.putInt("id_kakawin_audio", id_kakawin)
                bundle.putInt("id_audio_kakawin", result.id_audio)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_kakawinAdmin.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailKakawinAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaKakawinAdapter =  YadnyaKakawinAdminAdapter(arrayListOf(), object : YadnyaKakawinAdminAdapter.OnAdapterYadnyaKakawinAdminListener{
            override fun onClick(result: YadnyaKakawinAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKakawinAdminActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_kakawinAdmin.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailKakawinAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoKakawin(id_kakawin: Int) {
        ApiService.endpoint.getListVideoKakawinAdmin(id_kakawin)
            .enqueue(object: Callback<VideoKakawinAdminModel> {
                override fun onResponse(
                    call: Call<VideoKakawinAdminModel>,
                    response: Response<VideoKakawinAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideokakawinAdmin.visibility  = View.GONE
                        rv_video_kakawinAdmin.visibility   = View.GONE
                        tambahVideoKakawin.visibility = View.VISIBLE
                        goToListVideoKakawin.visibility = View.GONE
                    }else{
                        nodatavideokakawinAdmin.visibility  = View.GONE
                        rv_video_kakawinAdmin.visibility = View.VISIBLE
                        tambahVideoKakawin.visibility = View.GONE
                        goToListVideoKakawin.visibility = View.VISIBLE
                        Log.d("video",response.body().toString())
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

                override fun onFailure(call: Call<VideoKakawinAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioKakawin(id_post: Int) {
        ApiService.endpoint.getListAudioKakawinAdmin(id_post)
            .enqueue(object: Callback<AudioKakawinAdminModel> {
                override fun onResponse(
                    call: Call<AudioKakawinAdminModel>,
                    response: Response<AudioKakawinAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiokakawinAdmin.visibility  = View.GONE
                        rv_audio_kakawinAdmin.visibility   = View.GONE
                        tambahAudioKakawin.visibility = View.VISIBLE
                        goToListAudioKakawin.visibility = View.GONE
                    }else{
                        nodataaudiokakawinAdmin.visibility  = View.GONE
                        rv_audio_kakawinAdmin.visibility = View.VISIBLE
                        tambahAudioKakawin.visibility = View.GONE
                        goToListAudioKakawin.visibility = View.VISIBLE
                        Log.d("audio",response.body().toString())
                        showAudioKakawinData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioKakawinAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaKakawin(id_kakawin: Int) {
        ApiService.endpoint.getYadnyaKakawinAdmin(id_kakawin)
            .enqueue(object: Callback<YadnyaKakawinAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaKakawinAdminModel>,
                    response: Response<YadnyaKakawinAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyakakawinAdmin.visibility  = View.GONE
                        rv_yadnya_kakawinAdmin.visibility   = View.GONE
                        tambahYadnyaKakawin.visibility = View.VISIBLE
                        goToListYadnyaKakawin.visibility = View.GONE
                    }else{
                        nodatayadnyakakawinAdmin.visibility  = View.GONE
                        rv_yadnya_kakawinAdmin.visibility = View.VISIBLE
                        tambahYadnyaKakawin.visibility = View.GONE
                        goToListYadnyaKakawin.visibility = View.VISIBLE
                        Log.d("yadnya",response.body().toString())
                        showYadnyaKakawinData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaKakawinAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoKakawinData(body: VideoKakawinAdminModel) {
        val results = body.data
        videoKakawinAdapter.setData(results)
    }
    private fun showAudioKakawinData(body: AudioKakawinAdminModel) {
        val results = body.data
        audioKakawinAdapter.setData(results)
    }
    private fun showYadnyaKakawinData(body: YadnyaKakawinAdminModel) {
        val results = body.data
        yadnyaKakawinAdapter.setData(results)
    }


}