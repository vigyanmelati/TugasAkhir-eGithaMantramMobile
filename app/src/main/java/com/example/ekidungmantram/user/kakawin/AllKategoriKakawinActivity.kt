package com.example.ekidungmantram.user.kakawin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.LoginActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.KategoriKakawinAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriKakawinModel
import kotlinx.android.synthetic.main.activity_all_kategori_kakawin.*
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh.daftar_nama
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriKakawinActivity : AppCompatActivity() {
    private lateinit var kategoriKakawinAdapter : KategoriKakawinAdapter
    private lateinit var setAdapter    : KategoriKakawinAdapter
    private lateinit var sharedPreferences: SharedPreferences
    var id_kakawin_kat: Int = 0
    lateinit var nama_kakawin_kat: String
    lateinit var desc_kakawin_kat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_kakawin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAgung"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")
            val descPost = bundle.getString("desc_kakawin")
            Log.d("id_kakawin", postID.toString())
            id_kakawin_kat =postID
            if (namaPost != null) {
                nama_kakawin_kat = namaPost
            }
            if (descPost != null) {
                desc_kakawin_kat = descPost
            }


            daftar_nama.text = "Daftar " + namaPost
            desc_kategori_sekar_agung.text = descPost
            allKategoriKakawin1.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriKakawin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriKakawin(postID)

            swipeKategoriKakawin.setOnRefreshListener {
                getAllKategoriKakawin(postID)
                swipeKategoriKakawin.isRefreshing = false
            }
            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
            val id            = sharedPreferences.getString("ID_ADMIN", null)
            val mesage = sharedPreferences.getString("MESAGE", null)

            fabKakawin.setOnClickListener {
                if(id == null){
                    val bundle = Bundle()
                    val intent = Intent(this, LoginActivity::class.java)
                    bundle.putString("APP", "kakawin")
                    bundle.putInt("id_kakawin", postID)
                    bundle.putString("nama_kakawin", namaPost)
                    bundle.putString("desc_kakawin", descPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }else{
                    val bundle = Bundle()
                    val intent = Intent(this, AllKategoriKakawinUserActivity::class.java)
                    bundle.putInt("id_kakawin", postID)
                    bundle.putString("nama_kakawin", namaPost)
                    bundle.putString("desc_kakawin", descPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        }


    }

    private fun printLog(message: String) {
        Log.d("KategoriKakawinActivity", message)
    }

    private fun getAllKategoriKakawin(id: Int) {
        ApiService.endpoint.getKategoriKakawin(id)
            .enqueue(object: Callback<ArrayList<KategoriKakawinModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriKakawinModel>>,
                    response: Response<ArrayList<KategoriKakawinModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriKakawin.visibility   = View.VISIBLE
                        shimmerKategoriKakawin.visibility = View.GONE
                        noKategoriKakawin.visibility      = View.GONE
                    }else{
                        swipeKategoriKakawin.visibility   = View.GONE
                        shimmerKategoriKakawin.visibility = View.VISIBLE
                        noKategoriKakawin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriKakawinAdapter(it,
                        object : KategoriKakawinAdapter.OnAdapterKategoriKakawinListener{
                            override fun onClick(result: KategoriKakawinModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriKakawinActivity, DetailKakawinActivity::class.java)
                                bundle.putInt("id_kakawin", result.id_post)
                                bundle.putInt("tag_kakawin", result.id_tag)
                                Log.d("kawid", result.id_tag.toString())
                                bundle.putString("nama_kakawin", result.nama_post)
                                bundle.putString("gambar_kakawin", result.gambar)
                                bundle.putString("nama_tag_kakawin", result.nama_tag)
                                Log.d("kawidnam", result.nama_tag)
                                bundle.putInt("id_kakawin_kat", id_kakawin_kat)
                                bundle.putString("nama_kakawin_kat", nama_kakawin_kat)
                                bundle.putString("desc_kakawin_kat", desc_kakawin_kat)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriKakawin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriKakawin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriKakawin.visibility   = View.GONE
                                    allKategoriKakawin1.visibility = View.VISIBLE
                                    allKategoriKakawin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriKakawinAdapter = KategoriKakawinAdapter(filter as ArrayList<KategoriKakawinModel>,
                                        object : KategoriKakawinAdapter.OnAdapterKategoriKakawinListener{
                                            override fun onClick(result: KategoriKakawinModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriKakawinActivity, DetailKakawinActivity::class.java)
                                                bundle.putInt("id_kakawin", result.id_post)
                                                bundle.putInt("tag_kakawin", result.id_tag)
                                                bundle.putString("nama_kakawin", result.nama_post)
                                                bundle.putString("gambar_kakawin", result.gambar)
                                                bundle.putString("nama_tag_kakawin", result.nama_tag)
                                                bundle.putInt("id_kakawin_kat", id_kakawin_kat)
                                                bundle.putString("nama_kakawin_kat", nama_kakawin_kat)
                                                bundle.putString("desc_kakawin_kat", desc_kakawin_kat)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriKakawin.visibility   = View.VISIBLE
                                        allKategoriKakawin1.visibility = View.GONE
                                        allKategoriKakawin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriKakawin.visibility   = View.GONE
                                        allKategoriKakawin2.visibility = View.VISIBLE
                                        allKategoriKakawin2.adapter    = kategoriKakawinAdapter
                                        allKategoriKakawin1.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriKakawin1.visibility = View.VISIBLE
                                        allKategoriKakawin2.visibility = View.GONE
                                        noKategoriKakawin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriKakawinModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriKakawin.stopShimmer()
        shimmerKategoriKakawin.visibility = View.GONE
        swipeKategoriKakawin.visibility   = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AllKategoriKakawinActivity, AllKakawinActivity::class.java)
        startActivity(intent)
    }
}