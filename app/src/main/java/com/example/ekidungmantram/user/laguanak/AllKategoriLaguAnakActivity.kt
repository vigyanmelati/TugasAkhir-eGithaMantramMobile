package com.example.ekidungmantram.user.laguanak

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
import com.example.ekidungmantram.adapter.KategoriLaguAnakAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriLaguAnakModel
import kotlinx.android.synthetic.main.activity_all_kategori_lagu_anak.*
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh.daftar_nama
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriLaguAnakActivity : AppCompatActivity() {
    private lateinit var kategoriLaguAnakAdapter : KategoriLaguAnakAdapter
    private lateinit var setAdapter    : KategoriLaguAnakAdapter
    private lateinit var sharedPreferences: SharedPreferences
    var id_lagu_anak_kat: Int = 0
    lateinit var nama_lagu_anak_kat: String
    lateinit var desc_lagu_anak_kat: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_lagu_anak)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarRare"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")
            val descPost = bundle.getString("desc_lagu_anak")
            Log.d("id_lagu_anak", postID.toString())

            id_lagu_anak_kat = postID
            if (namaPost != null) {
                nama_lagu_anak_kat = namaPost
            }
            if (descPost != null) {
                desc_lagu_anak_kat = descPost
            }

            daftar_nama.text = "Daftar " + namaPost
            desc_kategori_sekar_rare.text = descPost
            allKategoriLaguAnak1.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriLaguAnak2.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriLaguAnak(postID)

            swipeKategoriLaguAnak.setOnRefreshListener {
                getAllKategoriLaguAnak(postID)
                swipeKategoriLaguAnak.isRefreshing = false
            }
            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
            val id            = sharedPreferences.getString("ID_ADMIN", null)
            val mesage = sharedPreferences.getString("MESAGE", null)

            fabLaguAnak.setOnClickListener {
                if(id == null){
                    val bundle = Bundle()
                    val intent = Intent(this, LoginActivity::class.java)
                    bundle.putString("APP", "laguAnak")
                    bundle.putInt("id_lagu_anak", postID)
                    bundle.putString("nama_lagu_anak", namaPost)
                    bundle.putString("desc_lagu_anak", descPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }else{
                    val bundle = Bundle()
                    val intent = Intent(this, AllKategoriLaguAnakUserActivity::class.java)
                    bundle.putInt("id_lagu_anak", postID)
                    bundle.putString("nama_lagu_anak", namaPost)
                    bundle.putString("desc_lagu_anak", descPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        }

    }

    private fun printLog(message: String) {
        Log.d("KategoriLaguActivity", message)
    }

    private fun getAllKategoriLaguAnak(id: Int) {
        ApiService.endpoint.getKategoriLaguAnak(id)
            .enqueue(object: Callback<ArrayList<KategoriLaguAnakModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriLaguAnakModel>>,
                    response: Response<ArrayList<KategoriLaguAnakModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriLaguAnak.visibility   = View.VISIBLE
                        shimmerKategoriLaguAnak.visibility = View.GONE
                        noKategoriLaguAnak.visibility      = View.GONE
                    }else{
                        swipeKategoriLaguAnak.visibility   = View.GONE
                        shimmerKategoriLaguAnak.visibility = View.VISIBLE
                        noKategoriLaguAnak.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriLaguAnakAdapter(it,
                        object : KategoriLaguAnakAdapter.OnAdapterKategoriLaguAnakListener{
                            override fun onClick(result: KategoriLaguAnakModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriLaguAnakActivity, DetailLaguAnakActivity::class.java)
                                bundle.putInt("id_lagu", result.id_post)
                                bundle.putString("nama_lagu", result.nama_post)
                                bundle.putString("nama_tag_lagu", result.nama_tag)
                                bundle.putString("gambar_lagu", result.gambar)
                                bundle.putInt("tag_lagu", result.id_tag)
                                bundle.putInt("id_lagu_anak_kat", id_lagu_anak_kat)
                                bundle.putString("nama_lagu_anak_kat", nama_lagu_anak_kat)
                                bundle.putString("desc_lagu_anak_kat", desc_lagu_anak_kat)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriLaguAnak1.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriLaguAnak.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriLaguAnak.visibility   = View.GONE
                                    allKategoriLaguAnak1.visibility = View.VISIBLE
                                    allKategoriLaguAnak2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriLaguAnakAdapter = KategoriLaguAnakAdapter(filter as ArrayList<KategoriLaguAnakModel>,
                                        object : KategoriLaguAnakAdapter.OnAdapterKategoriLaguAnakListener{
                                            override fun onClick(result: KategoriLaguAnakModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriLaguAnakActivity, DetailLaguAnakActivity::class.java)
                                                bundle.putInt("id_lagu", result.id_post)
                                                bundle.putString("nama_lagu", result.nama_post)
                                                bundle.putString("nama_tag_lagu", result.nama_tag)
                                                bundle.putString("gambar_lagu", result.gambar)
                                                bundle.putInt("tag_lagu", result.id_tag)
                                                bundle.putInt("id_lagu_anak_kat", id_lagu_anak_kat)
                                                bundle.putString("nama_lagu_anak_kat", nama_lagu_anak_kat)
                                                bundle.putString("desc_lagu_anak_kat", desc_lagu_anak_kat)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriLaguAnak.visibility   = View.VISIBLE
                                        allKategoriLaguAnak1.visibility = View.GONE
                                        allKategoriLaguAnak2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriLaguAnak.visibility   = View.GONE
                                        allKategoriLaguAnak2.visibility = View.VISIBLE
                                        allKategoriLaguAnak2.adapter    = kategoriLaguAnakAdapter
                                        allKategoriLaguAnak1.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriLaguAnak1.visibility = View.VISIBLE
                                        allKategoriLaguAnak2.visibility = View.GONE
                                        noKategoriLaguAnak.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriLaguAnakModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriLaguAnak.stopShimmer()
        shimmerKategoriLaguAnak.visibility = View.GONE
        swipeKategoriLaguAnak.visibility   = View.VISIBLE
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
        val intent = Intent(this@AllKategoriLaguAnakActivity, AlllLaguAnakActivity::class.java)
        startActivity(intent)
    }
}