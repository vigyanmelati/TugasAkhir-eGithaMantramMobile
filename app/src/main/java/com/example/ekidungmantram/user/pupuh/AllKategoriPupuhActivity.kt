package com.example.ekidungmantram.user.pupuh

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
import com.example.ekidungmantram.adapter.KategoriPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriPupuhModel
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriPupuhActivity : AppCompatActivity() {
    private lateinit var kategoriPupuhAdapter : KategoriPupuhAdapter
    private lateinit var setAdapter    : KategoriPupuhAdapter
    private lateinit var sharedPreferences: SharedPreferences
    var id_pupuh_kat: Int = 0
    lateinit var nama_pupuh_kat: String
    lateinit var desc_pupuh_kat: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_pupuh)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAlit"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            val descPost = bundle.getString("desc_pupuh")
            Log.d("id_pupuh", postID.toString())

            id_pupuh_kat = postID
            if (namaPost != null) {
                nama_pupuh_kat = namaPost
            }
            if (descPost != null) {
                desc_pupuh_kat = descPost
            }else {
                desc_pupuh_kat ="-"
            }

            daftar_nama.text = "Daftar " + namaPost
            desc_kategori_pupuh.text = descPost
            allKategoriPupuh1.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriPupuh2.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriPupuh(postID)

            swipeKategoriPupuh.setOnRefreshListener {
                getAllKategoriPupuh(postID)
                swipeKategoriPupuh.isRefreshing = false
            }

            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
            val id            = sharedPreferences.getString("ID_ADMIN", null)
            val mesage = sharedPreferences.getString("MESAGE", null)

            fabPupuh.setOnClickListener {
                if(id == null){
                    val bundle = Bundle()
                    val intent = Intent(this, LoginActivity::class.java)
                    bundle.putString("APP", "pupuh")
                    bundle.putInt("id_pupuh", postID)
                    bundle.putString("nama_pupuh", namaPost)
                    bundle.putString("desc_pupuh", descPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }else{
                    val bundle = Bundle()
                    val intent = Intent(this, AllKategoriPupuhUserActivity::class.java)
                    bundle.putInt("id_pupuh", postID)
                    bundle.putString("nama_pupuh", namaPost)
                    bundle.putString("desc_pupuh", descPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriPupuhActivity", message)
    }

    private fun getAllKategoriPupuh(id: Int) {
        ApiService.endpoint.getKategoriPupuh(id)
            .enqueue(object: Callback<ArrayList<KategoriPupuhModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriPupuhModel>>,
                    response: Response<ArrayList<KategoriPupuhModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriPupuh.visibility   = View.VISIBLE
                        shimmerKategoriPupuh.visibility = View.GONE
                        noKategoriPupuh.visibility      = View.GONE
                    }else{
                        swipeKategoriPupuh.visibility   = View.GONE
                        shimmerKategoriPupuh.visibility = View.VISIBLE
                        noKategoriPupuh.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriPupuhAdapter(it,
                        object : KategoriPupuhAdapter.OnAdapterKategoriPupuhListener{
                            override fun onClick(result: KategoriPupuhModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriPupuhActivity, DetailPupuhActivity::class.java)
                                bundle.putInt("id_pupuh", result.id_post)
                                bundle.putString("nama_pupuh", result.nama_post)
                                bundle.putString("nama_tag_pupuh", result.nama_tag)
                                bundle.putString("gambar_pupuh", result.gambar)
                                bundle.putInt("tag_pupuh", result.id_tag)
                                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriPupuh1.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriPupuh.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriPupuh.visibility   = View.GONE
                                    allKategoriPupuh1.visibility = View.VISIBLE
                                    allKategoriPupuh2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriPupuhAdapter = KategoriPupuhAdapter(filter as ArrayList<KategoriPupuhModel>,
                                        object : KategoriPupuhAdapter.OnAdapterKategoriPupuhListener{
                                            override fun onClick(result: KategoriPupuhModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriPupuhActivity, DetailPupuhActivity::class.java)
                                                bundle.putInt("id_pupuh", result.id_post)
                                                bundle.putString("nama_pupuh", result.nama_post)
                                                bundle.putString("nama_tag_pupuh", result.nama_tag)
                                                bundle.putString("gambar_pupuh", result.gambar)
                                                bundle.putInt("tag_pupuh", result.id_tag)
                                                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                                                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                                                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriPupuh.visibility   = View.VISIBLE
                                        allKategoriPupuh1.visibility = View.GONE
                                        allKategoriPupuh2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriPupuh.visibility   = View.GONE
                                        allKategoriPupuh2.visibility = View.VISIBLE
                                        allKategoriPupuh2.adapter    = kategoriPupuhAdapter
                                        allKategoriPupuh1.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriPupuh1.visibility = View.VISIBLE
                                        allKategoriPupuh2.visibility = View.GONE
                                        noKategoriPupuh.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriPupuhModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriPupuh.stopShimmer()
        shimmerKategoriPupuh.visibility = View.GONE
        swipeKategoriPupuh.visibility   = View.VISIBLE
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
        val intent = Intent(this@AllKategoriPupuhActivity, AllPupuhActivity::class.java)
        startActivity(intent)
    }
}