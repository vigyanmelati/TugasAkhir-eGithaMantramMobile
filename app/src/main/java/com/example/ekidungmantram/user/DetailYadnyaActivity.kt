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
import com.example.ekidungmantram.database.data.Yadnya
import com.example.ekidungmantram.database.setup.YadnyaDb
import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.user.kakawin.DetailKakawinActivity
import com.example.ekidungmantram.user.kidung.DetailKidungActivity
import com.example.ekidungmantram.user.laguanak.DetailLaguAnakActivity
import com.example.ekidungmantram.user.pupuh.DetailPupuhActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_yadnya.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailYadnyaActivity : YouTubeBaseActivity() {
    private lateinit var db             : YadnyaDb
    private var LayoutManagerAwal       : LinearLayoutManager? = null
    private lateinit var awalAdapter    : ProsesiAwalAdapter
    private var LayoutManagerPuncak     : LinearLayoutManager? = null
    private lateinit var puncakAdapter  : ProsesiPuncakAdapter
    private var LayoutManagerAkhir      : LinearLayoutManager? = null
    private lateinit var akhirAdapter   : ProsesiAkhirAdapter
    private var LayoutManagerGamelan    : LinearLayoutManager? = null
    private lateinit var gamelanAdapter : GamelanYadnyaAdapter
    private var LayoutManagerTari       : LinearLayoutManager? = null
    private lateinit var tariAdapter    : TariYadnyaAdapter
    private var LayoutManagerKidung     : LinearLayoutManager? = null
    private lateinit var kidungAdapter  : KidungYadnyaAdapter
    private var yadID                   : Int? = null
    lateinit var nama_tag_dhar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_yadnya)
        db = Room.databaseBuilder(applicationContext, YadnyaDb::class.java, "yadnyabookmarked.db").build()
        val bundle :Bundle ?= intent.extras
        if (bundle!=null){
            yadID            = bundle.getInt("id_yadnya")
            val postID       = bundle.getInt("id_yadnya")
            val categoryID   = bundle.getInt("id_kategori")
            val namayadnya   = bundle.getString("nama_yadnya")
            val kategoris    = bundle.getString("kategori")
            val gambars      = bundle.getString("gambar")
            val url : String = Constant.URL + "kategori_pengguna/detil/" + postID.toString() + "/" + categoryID.toString()
//            shareURL.setOnClickListener {
//                shareYadnya(url)
//            }

            nama_tag_dhar = bundle.getString("nama_tag_dhar").toString()


            if(nama_tag_dhar == "Sekar Madya"){
                val id_kidung = bundle.getInt("id_kidung")
                val tag_kidung = bundle.getInt("tag_kidung")
                val nama_kidung = bundle.getString("nama_kidung")
                val nama_tag_kidung = bundle.getString("nama_tag_kidung")
                val gambar_kidung= bundle.getString("gambar_kidung")
                backToHome.setOnClickListener {
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
            }else if(nama_tag_dhar == "Sekar Agung"){
                val id_kakawin = bundle.getInt("id_kakawin")
                val id_kakawin_kat = bundle.getInt("id_kakawin_kat")
                val tag_kakawin = bundle.getInt("tag_kakawin")
                val nama_kakawin_kat = bundle.getString("nama_kakawin_kat")
                val desc_kakawin_kat= bundle.getString("desc_kakawin_kat")
                val nama_kakawin = bundle.getString("nama_kakawin")
                val nama_tag_kakawin = bundle.getString("nama_tag_kakawin")
                val gambar_kakawin= bundle.getString("gambar_kakawin")

                backToHome.setOnClickListener {
                    val bundle = Bundle()
                    val intent = Intent(this, DetailKakawinActivity::class.java)
                    bundle.putInt("id_kakawin", id_kakawin)
                    bundle.putInt("id_kakawin_kat", id_kakawin_kat)
                    bundle.putInt("tag_kakawin", tag_kakawin)
                    bundle.putString("nama_kakawin_kat", nama_kakawin_kat)
                    bundle.putString("desc_kakawin_kat", desc_kakawin_kat)
                    bundle.putString("nama_kakawin", nama_kakawin)
                    bundle.putString("nama_tag_kakawin", nama_tag_kakawin)
                    bundle.putString("gambar_kakawin", gambar_kakawin)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
            }else if(nama_tag_dhar == "Sekar Rare"){
                val id_lagu = bundle.getInt("id_lagu")
                val id_lagu_anak_kat = bundle.getInt("id_lagu_anak_kat")
                val tag_lagu = bundle.getInt("tag_lagu")
                val nama_lagu_anak_kat = bundle.getString("nama_lagu_anak_kat")
                val desc_lagu_anak_kat= bundle.getString("desc_lagu_anak_kat")
                val nama_lagu = bundle.getString("nama_lagu")
                val nama_tag_lagu = bundle.getString("nama_tag_lagu")
                val gambar_lagu= bundle.getString("gambar_lagu")

                backToHome.setOnClickListener {
                    val bundle = Bundle()
                    val intent = Intent(this, DetailLaguAnakActivity::class.java)
                    bundle.putInt("id_lagu", id_lagu)
                    bundle.putInt("id_lagu_anak_kat", id_lagu_anak_kat)
                    bundle.putInt("tag_lagu", tag_lagu)
                    bundle.putString("nama_lagu_anak_kat", nama_lagu_anak_kat)
                    bundle.putString("desc_lagu_anak_kat", desc_lagu_anak_kat)
                    bundle.putString("nama_lagu", nama_lagu)
                    bundle.putString("nama_tag_lagu", nama_tag_lagu)
                    bundle.putString("gambar_lagu", gambar_lagu)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
            }else if(nama_tag_dhar == "Sekar Alit"){
                val id_pupuh = bundle.getInt("id_pupuh")
                val id_pupuh_kat = bundle.getInt("id_pupuh_kat")
                val tag_pupuh = bundle.getInt("tag_pupuh")
                val nama_pupuh_kat = bundle.getString("nama_pupuh_kat")
                val desc_pupuh_kat= bundle.getString("desc_pupuh_kat")
                val nama_pupuh = bundle.getString("nama_pupuh")
                val nama_tag_pupuh = bundle.getString("nama_tag_pupuh")
                val gambar_pupuh= bundle.getString("gambar_pupuh")

                backToHome.setOnClickListener {
                    val bundle = Bundle()
                    val intent = Intent(this, DetailPupuhActivity::class.java)
                    bundle.putInt("id_pupuh", id_pupuh)
                    bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                    bundle.putInt("tag_pupuh", tag_pupuh)
                    bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                    bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                    bundle.putString("nama_pupuh", nama_pupuh)
                    bundle.putString("nama_tag_pupuh", nama_tag_pupuh)
                    bundle.putString("gambar_pupuh", gambar_pupuh)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
            }

//            backToHome.setOnClickListener {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
            bookmarked.setOnClickListener {
                if(bookmarked.isChecked){
                    Toast.makeText(this, "Bookmark Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bookmark Berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            bookmarked.setOnCheckedChangeListener { _, isChecked->
                if (isChecked){
                    addBookmark(postID, categoryID , namayadnya, kategoris, gambars)
                }else{
                    deleteBookmark(postID)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val test = db.yadnyaDao().fetch(postID)
                if(test != null){
                    bookmarked.isChecked = true
                }else{
                    bookmarked.isChecked = false
                }
            }

            getDetailData(postID)

            setupRecyclerViewAwal()
            getProsesiAwalData(postID)

            setupRecyclerViewPuncak()
            getProsesiPuncakData(postID)

            setupRecyclerViewAkhir()
            getProsesiAkhirData(postID)

            setupRecyclerViewGamelan()
            getGamelanYadnyaData(postID)

            setupRecyclerViewTari()
            getTariYadnyaData(postID)

            setupRecyclerViewKidung()
            getKidungYadnyaData(postID)
        }
    }


    private fun addBookmark(postID: Int, categoryID: Int , namayadnya: String?, kategoris: String?, gambars: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            db.yadnyaDao().addBookmarkedYadnya(
                Yadnya(0, postID, categoryID , kategoris.toString() ,namayadnya.toString(), gambars.toString())
            )
            val test = db.yadnyaDao().fetch(postID)
            printLog(test.toString())
        }
    }

    private fun deleteBookmark(postID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.yadnyaDao().deleteById(postID)
        }
    }

//    private fun shareYadnya(url: String) {
//        val link    = url
//        val intent  = Intent(Intent.ACTION_SEND)
//        intent.type = "type/plain"
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Silahkan dilihat yadnya yang saya bagikan!")
//        intent.putExtra(Intent.EXTRA_TEXT, link)
//        val chooser = Intent.createChooser(intent, "Bagikan lewat")
//        startActivity(chooser)
//    }

    private fun printLog(message: String) {
        Log.d("DetailYadnyaActivity", message)
    }

    private fun getProsesiAwalData(id: Int) {
        ApiService.endpoint.getDetailAwal(id).enqueue(object : Callback<ProsesiAwalModel>{
            override fun onResponse(
                call: Call<ProsesiAwalModel>,
                response: Response<ProsesiAwalModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutAwal.visibility = View.GONE
                }else {
                    layoutAwal.visibility = View.VISIBLE
                    showAwalData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ProsesiAwalModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showAwalData(data: ProsesiAwalModel) {
        val results = data.data
        awalAdapter.setData(results)
    }

    private fun setupRecyclerViewAwal() {
        awalAdapter = ProsesiAwalAdapter(arrayListOf(), object : ProsesiAwalAdapter.OnAdapterAwalListener{
            override fun onClick(result: ProsesiAwalModel.Data) {
                Toast.makeText(this@DetailYadnyaActivity, result.nama_post+" dipilih", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", yadID!!)
                bundle.putInt("id_prosesi", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        awal.apply {
            LayoutManagerAwal = LinearLayoutManager(this@DetailYadnyaActivity)
            layoutManager     = LayoutManagerAwal
            adapter           = awalAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiPuncakData(id: Int) {
        ApiService.endpoint.getDetailPuncak(id).enqueue(object : Callback<ProsesiPuncakModel>{
            override fun onResponse(
                call: Call<ProsesiPuncakModel>,
                response: Response<ProsesiPuncakModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutPuncak.visibility = View.GONE
                }else {
                    layoutPuncak.visibility = View.VISIBLE
                    showPuncakData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ProsesiPuncakModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showPuncakData(data: ProsesiPuncakModel) {
        val results = data.data
        puncakAdapter.setData(results)
    }

    private fun setupRecyclerViewPuncak() {
        puncakAdapter = ProsesiPuncakAdapter(arrayListOf(), object : ProsesiPuncakAdapter.OnAdapterPuncakListener{
            override fun onClick(result: ProsesiPuncakModel.Data) {
                Toast.makeText(this@DetailYadnyaActivity, result.nama_post+" dipilih", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", yadID!!)
                bundle.putInt("id_prosesi", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        puncak.apply {
            LayoutManagerPuncak = LinearLayoutManager(this@DetailYadnyaActivity)
            layoutManager       = LayoutManagerPuncak
            adapter             = puncakAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiAkhirData(id: Int) {
        ApiService.endpoint.getDetailAkhir(id).enqueue(object : Callback<ProsesiAkhirModel>{
            override fun onResponse(
                call: Call<ProsesiAkhirModel>,
                response: Response<ProsesiAkhirModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutAkhir.visibility = View.GONE
                }else {
                    layoutAkhir.visibility = View.VISIBLE
                    showAkhirData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ProsesiAkhirModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showAkhirData(data: ProsesiAkhirModel) {
        val results = data.data
        akhirAdapter.setData(results)
    }

    private fun setupRecyclerViewAkhir() {
        akhirAdapter = ProsesiAkhirAdapter(arrayListOf(), object : ProsesiAkhirAdapter.OnAdapterAkhirListener{
            override fun onClick(result: ProsesiAkhirModel.Data) {
                Toast.makeText(this@DetailYadnyaActivity, result.nama_post+" dipilih", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", yadID!!)
                bundle.putInt("id_prosesi", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        akhir.apply {
            LayoutManagerAkhir  = LinearLayoutManager(this@DetailYadnyaActivity)
            layoutManager       = LayoutManagerAkhir
            adapter             = akhirAdapter
            setHasFixedSize(true)
        }
    }

    private fun getGamelanYadnyaData(id: Int) {
        ApiService.endpoint.getDetailGamelanYadnya(id).enqueue(object : Callback<GamelanYadnyaModel>{
            override fun onResponse(
                call: Call<GamelanYadnyaModel>,
                response: Response<GamelanYadnyaModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutGamelan.visibility = View.GONE
                }else{
                    layoutGamelan.visibility = View.VISIBLE
                    nodatayadnyagamelan.visibility = View.GONE
                    showGamelanYadnyaData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<GamelanYadnyaModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showGamelanYadnyaData(data: GamelanYadnyaModel) {
        val results = data.data
        gamelanAdapter.setData(results)
    }

    private fun setupRecyclerViewGamelan() {
        gamelanAdapter = GamelanYadnyaAdapter(arrayListOf(), object : GamelanYadnyaAdapter.OnAdapterGamelanYadnyaListener{
            override fun onClick(result: GamelanYadnyaModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailGamelanActivity::class.java)
                bundle.putInt("id_gamelan", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        gamelanYadnyaList.apply {
            LayoutManagerGamelan = GridLayoutManager(this@DetailYadnyaActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerGamelan
            adapter              = gamelanAdapter
            setHasFixedSize(true)
        }
    }

    private fun getTariYadnyaData(id: Int) {
        ApiService.endpoint.getDetailTariYadnya(id).enqueue(object : Callback<TariYadnyaModel>{
            override fun onResponse(
                call: Call<TariYadnyaModel>,
                response: Response<TariYadnyaModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutTari.visibility = View.GONE
                }else{
                    layoutTari.visibility = View.VISIBLE
                    nodatayadnyatari.visibility = View.GONE
                    showTariYadnyaData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<TariYadnyaModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showTariYadnyaData(data: TariYadnyaModel) {
        val results = data.data
        tariAdapter.setData(results)
    }

    private fun setupRecyclerViewTari() {
        tariAdapter = TariYadnyaAdapter(arrayListOf(), object : TariYadnyaAdapter.OnAdapterTariYadnyaListener{
            override fun onClick(result: TariYadnyaModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailTariActivity::class.java)
                bundle.putInt("id_tari", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        tariYadnyaList.apply {
            LayoutManagerTari = GridLayoutManager(this@DetailYadnyaActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerTari
            adapter              = tariAdapter
            setHasFixedSize(true)
        }
    }

    private fun getKidungYadnyaData(id: Int) {
        ApiService.endpoint.getDetailKidungYadnya(id).enqueue(object : Callback<KidungYadnyaModel>{
            override fun onResponse(
                call: Call<KidungYadnyaModel>,
                response: Response<KidungYadnyaModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutKidung.visibility = View.GONE
                }else{
                    layoutKidung.visibility = View.VISIBLE
                    nodatayadnyakidung.visibility = View.GONE
                    showKidungYadnyaData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<KidungYadnyaModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showKidungYadnyaData(data: KidungYadnyaModel) {
        val results = data.data
        kidungAdapter.setData(results)
    }

    private fun setupRecyclerViewKidung() {
        kidungAdapter = KidungYadnyaAdapter(arrayListOf(), object : KidungYadnyaAdapter.OnAdapterKidungYadnyaListener{
            override fun onClick(result: KidungYadnyaModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailKidungActivity::class.java)
                bundle.putInt("id_kidung", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        kidungYadnyaList.apply {
            LayoutManagerKidung  = GridLayoutManager(this@DetailYadnyaActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerKidung
            adapter              = kidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailYadnya(id).enqueue(object: Callback<DetailYadnyaModel>{
            override fun onResponse(
                call: Call<DetailYadnyaModel>,
                response: Response<DetailYadnyaModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskription.text = result.deskripsi
                    detailNamaYadnya.text = result.nama_post
                    detailJenisYadnya.text = result.nama_kategori
                    Glide.with(this@DetailYadnyaActivity).load(Constant.IMAGE_URL +result.gambar).into(imageDetailYadnya)
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailYadnyaModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayer.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetail.stopShimmer()
        shimmerDetail.visibility = View.GONE
        scrollDetail.visibility  = View.VISIBLE
    }

}