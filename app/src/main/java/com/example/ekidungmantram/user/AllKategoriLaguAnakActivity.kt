package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.KategoriLaguAnakAdapter
import com.example.ekidungmantram.adapter.KategoriPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriLaguAnakModel
import com.example.ekidungmantram.model.KategoriPupuhModel
import kotlinx.android.synthetic.main.activity_all_kategori_lagu_anak.*
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh.*
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh.daftar_nama
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriLaguAnakActivity : AppCompatActivity() {
    private lateinit var kategoriLaguAnakAdapter : KategoriLaguAnakAdapter
    private lateinit var setAdapter    : KategoriLaguAnakAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_lagu_anak)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarRare"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")
            Log.d("id_lagu_anak", postID.toString())

            daftar_nama.text = "Daftar " + namaPost
            allKategoriLaguAnak1.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriLaguAnak2.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriLaguAnak(postID)

            swipeKategoriLaguAnak.setOnRefreshListener {
                getAllKategoriLaguAnak(postID)
                swipeKategoriLaguAnak.isRefreshing = false
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
}