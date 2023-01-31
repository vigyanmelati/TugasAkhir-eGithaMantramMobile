package com.example.ekidungmantram.admin.kidung

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.*
import com.example.ekidungmantram.admin.kidung.*
import com.example.ekidungmantram.admin.pupuh.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.*
import com.example.ekidungmantram.user.AudioKidungActivity
import com.example.ekidungmantram.user.DetailYadnyaActivity
import com.example.ekidungmantram.user.VideoKidungActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import kotlinx.android.synthetic.main.activity_detail_kidung_admin.*
import kotlinx.android.synthetic.main.activity_detail_kidung_admin.*
import kotlinx.android.synthetic.main.activity_detail_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKidungAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : BaitKidungListAdminAdapter
    private lateinit var videoKidungAdapter  : VideoKidungAdminAdapter
    private var gridLayoutManagerL      : GridLayoutManager? = null
    private lateinit var audioKidungAdapter  : AudioKidungAdminAdapter
    private var gridLayoutManagerA      : GridLayoutManager? = null
    private lateinit var yadnyaKidungAdapter  : YadnyaKidungAdminAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var id_kidung : Int = 0
    private var id_kidung_admin : Int = 0
    private lateinit var nama_kidung :String
    private lateinit var desc_kidung_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kidung_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            nama_kidung = bundle.getString("nama_kidung").toString()

            getDetailData(postID)
            baitKidungListAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getBaitData(postID)
            getListVideoKidung(postID)
            getListAudioKidung(postID)
            getListYadnyaKidung(postID)
            setupRecyclerViewK()
            setupRecyclerViewA()
            setupRecyclerViewY()
            lihatSemuavideokidungAdmin.visibility = View.GONE
            lihatSemuaaudiokidungAdmin.visibility = View.GONE
            lihatSemuayadnyakidungAdmin.visibility = View.GONE

            goToListLirik.setOnClickListener {
                val intent = Intent(this, AllLirikKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListVideoKidung.setOnClickListener {
                val intent = Intent(this, AllVideoKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListYadnyaKidung.setOnClickListener {
                val intent = Intent(this, AllYadnyaonKidungAdminActivity::class.java)
                bundle.putInt("id_kidung_admin", postID)
                bundle.putString("nama_kidung_admin", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToListAudioKidung.setOnClickListener {
                val intent = Intent(this, AllAudioKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahLirikKidung.setOnClickListener {
                val intent = Intent(this, AddLirikKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahVideoKidung.setOnClickListener {
                val intent = Intent(this, AddVideoKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahAudioKidung.setOnClickListener {
                val intent = Intent(this, AddAudioKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahYadnyaKidung.setOnClickListener {
                val intent = Intent(this, AddYadnyaToKidungAdminActivity::class.java)
                bundle.putInt("id_kidung_admin", postID)
                bundle.putString("nama_kidung_admin", nama_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            toEditKidung.setOnClickListener {
                val intent = Intent(this, EditKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deleteKidung.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Sekar Madya")
                    .setMessage("Apakah anda yakin ingin menghapus sekar madya ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusKidung(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToKidungAdmin.setOnClickListener {
            val intent = Intent(this, AllKidungAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getBaitData(postID: Int) {
        ApiService.endpoint.getDetailAllLirikKidungAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllLirikKidungAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllLirikKidungAdminModel>>,
                    response: Response<ArrayList<DetailAllLirikKidungAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        rv_layout_bait.visibility = View.VISIBLE
                        tambahLirikKidung.visibility = View.GONE
                        goToListLirik.visibility = View.VISIBLE
                    }else{
                        rv_layout_bait.visibility   = View.GONE
                        tambahLirikKidung.visibility = View.VISIBLE
                        goToListLirik.visibility = View.GONE
                    }
                    setAdapter = BaitKidungListAdminAdapter(datalist!!)
                    baitKidungListAdmin.adapter = setAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllLirikKidungAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailKidungAdmin(postID).enqueue(object: Callback<DetailKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailKidungAdminModel>,
                response: Response<DetailKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiKidungAdmin.text   = result.deskripsi
                    detailNamaKidungAdmin.text  = result.nama_post

                    if(result.nama_kategori != null){
                        detailJenisKidungAdmin.text = "Kidung "+ result.nama_kategori
                    }

                    if(result.gambar != null) {
                        Glide.with(this@DetailKidungAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailKidungAdmin)
//                            .load(result.gambar).into(imageDetailKidungAdmin)
                    }else{
                        imageDetailKidungAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKidungAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hapusKidung(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataKidungAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun playYoutubeVideo(video: String) {
//        youtubePlayerKidungAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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

    private fun goBack() {
        val intent = Intent(this, AllKidungAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailKidungAdmin.stopShimmer()
        shimmerDetailKidungAdmin.visibility = View.GONE
        scrollDetailKidungAdmin.visibility  = View.VISIBLE
    }

    private fun setupRecyclerViewK() {
        videoKidungAdapter =  VideoKidungAdminAdapter(arrayListOf(), object : VideoKidungAdminAdapter.OnAdapterVideoKidungAdminListener{
            override fun onClick(result: VideoKidungAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKidungAdminActivity, VideoKidungActivity::class.java)
                bundle.putString("video_kidung", result.video)
                bundle.putInt("id_kidung_video", id_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_video_kidungAdmin.apply {
            gridLayoutManagerL = GridLayoutManager(this@DetailKidungAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerL
            adapter            = videoKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewA() {
        audioKidungAdapter =  AudioKidungAdminAdapter(arrayListOf(), object : AudioKidungAdminAdapter.OnAdapterAudioKidungAdminListener{
            override fun onClick(result: AudioKidungAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKidungAdminActivity, AudioKidungActivity::class.java)
                bundle.putString("audio_kidung", result.audio)
                bundle.putInt("id_kidung_audio", id_kidung)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        rv_audio_kidungAdmin.apply {
            gridLayoutManagerA = GridLayoutManager(this@DetailKidungAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerA
            adapter            = audioKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewY() {
        yadnyaKidungAdapter =  YadnyaKidungAdminAdapter(arrayListOf(), object : YadnyaKidungAdminAdapter.OnAdapterYadnyaKidungAdminListener{
            override fun onClick(result: YadnyaKidungAdminModel.DataL) {
                val bundle = Bundle()
                val intent = Intent(this@DetailKidungAdminActivity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        rv_yadnya_kidungAdmin.apply {
            gridLayoutManagerY = GridLayoutManager(this@DetailKidungAdminActivity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun getListVideoKidung(id_kidung: Int) {
        ApiService.endpoint.getListVideoKidungAdmin(id_kidung)
            .enqueue(object: Callback<VideoKidungAdminModel> {
                override fun onResponse(
                    call: Call<VideoKidungAdminModel>,
                    response: Response<VideoKidungAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatavideokidungAdmin.visibility  = View.GONE
                        rv_video_kidungAdmin.visibility   = View.GONE
                        tambahVideoKidung.visibility = View.VISIBLE
                        goToListVideoKidung.visibility = View.GONE
                    }else{
                        nodatavideokidungAdmin.visibility  = View.GONE
                        rv_video_kidungAdmin.visibility = View.VISIBLE
                        tambahVideoKidung.visibility = View.GONE
                        goToListVideoKidung.visibility = View.VISIBLE
                        Log.d("video",response.body().toString())
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

                override fun onFailure(call: Call<VideoKidungAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListAudioKidung(id_post: Int) {
        ApiService.endpoint.getListAudioKidungAdmin(id_post)
            .enqueue(object: Callback<AudioKidungAdminModel> {
                override fun onResponse(
                    call: Call<AudioKidungAdminModel>,
                    response: Response<AudioKidungAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodataaudiokidungAdmin.visibility  = View.GONE
                        rv_audio_kidungAdmin.visibility   = View.GONE
                        tambahAudioKidung.visibility = View.VISIBLE
                        goToListAudioKidung.visibility = View.GONE
                    }else{
                        nodataaudiokidungAdmin.visibility  = View.GONE
                        rv_audio_kidungAdmin.visibility = View.VISIBLE
                        tambahAudioKidung.visibility = View.GONE
                        goToListAudioKidung.visibility = View.VISIBLE
                        Log.d("audio",response.body().toString())
                        showAudioKidungData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<AudioKidungAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getListYadnyaKidung(id_kidung: Int) {
        ApiService.endpoint.getYadnyaKidungAdmin(id_kidung)
            .enqueue(object: Callback<YadnyaKidungAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaKidungAdminModel>,
                    response: Response<YadnyaKidungAdminModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        nodatayadnyakidungAdmin.visibility  = View.GONE
                        rv_yadnya_kidungAdmin.visibility   = View.GONE
                        tambahYadnyaKidung.visibility = View.VISIBLE
                        goToListYadnyaKidung.visibility = View.GONE
                    }else{
                        nodatayadnyakidungAdmin.visibility  = View.GONE
                        rv_yadnya_kidungAdmin.visibility = View.VISIBLE
                        tambahYadnyaKidung.visibility = View.GONE
                        goToListYadnyaKidung.visibility = View.VISIBLE
                        Log.d("yadnya",response.body().toString())
                        showYadnyaKidungData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<YadnyaKidungAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showVideoKidungData(body: VideoKidungAdminModel) {
        val results = body.data
        videoKidungAdapter.setData(results)
    }
    private fun showAudioKidungData(body: AudioKidungAdminModel) {
        val results = body.data
        audioKidungAdapter.setData(results)
    }
    private fun showYadnyaKidungData(body: YadnyaKidungAdminModel) {
        val results = body.data
        yadnyaKidungAdapter.setData(results)
    }
}