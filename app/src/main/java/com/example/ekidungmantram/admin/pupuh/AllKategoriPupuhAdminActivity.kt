package com.example.ekidungmantram.admin.pupuh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.KategoriPupuhAdminAdapter
import com.example.ekidungmantram.admin.laguanak.AllLaguAnakAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.KategoriPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriPupuhAdminActivity : AppCompatActivity() {
    private lateinit var kategoriPupuhAdapter : KategoriPupuhAdminAdapter
    private lateinit var setAdapter    : KategoriPupuhAdminAdapter
    private var id_pupuh_admin : Int = 0
    private lateinit var nama_pupuh_admin :String
    private lateinit var desc_pupuh_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_pupuh_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAlit"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_admin")
            val namaPost = bundle.getString("nama_pupuh_admin")
            val descPost = bundle.getString("desc_pupuh_admin")
            id_pupuh_admin = bundle.getInt("id_pupuh_admin")
            nama_pupuh_admin = bundle.getString("nama_pupuh_admin").toString()
            desc_pupuh_admin = bundle.getString("desc_pupuh_admin").toString()
            Log.d("id_pupuh_admin", postID.toString())

            daftar_namaAdmin.text = "Daftar " + namaPost
            desc_kategori_pupuhAdmin.text = descPost
            allKategoriPupuh1Admin.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriPupuh2Admin.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriPupuhAdmin(postID)

            swipeKategoriPupuhAdmin.setOnRefreshListener {
                getAllKategoriPupuhAdmin(postID)
                swipeKategoriPupuhAdmin.isRefreshing = false
            }

            fabPupuhAdmin.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AddPupuhAdminActivity::class.java)
                bundle.putInt("id_kat_pupuh_admin", postID)
                bundle.putString("nama_kat_pupuh_admin", namaPost)
                bundle.putString("desc_kat_pupuh_admin", descPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriPupuhActivity", message)
    }

    private fun getAllKategoriPupuhAdmin(id: Int) {
        ApiService.endpoint.getKategoriPupuhListAdmin(id)
            .enqueue(object: Callback<ArrayList<KategoriPupuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriPupuhAdminModel>>,
                    response: Response<ArrayList<KategoriPupuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriPupuhAdmin.visibility   = View.VISIBLE
                        shimmerKategoriPupuhAdmin.visibility = View.GONE
                        noKategoriPupuhAdmin.visibility      = View.GONE
                    }else{
                        swipeKategoriPupuhAdmin.visibility   = View.GONE
                        shimmerKategoriPupuhAdmin.visibility = View.VISIBLE
                        noKategoriPupuhAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriPupuhAdminAdapter(it,
                        object : KategoriPupuhAdminAdapter.OnAdapterKategoriPupuhAdminListener{
                            override fun onClick(result: KategoriPupuhAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriPupuhAdminActivity, DetailPupuhAdminActivity::class.java)
                                bundle.putInt("id_pupuh_admin", result.id_post)
                                bundle.putString("nama_pupuh_admin", result.nama_post)
                                bundle.putString("nama_tag_pupuh_admin", result.nama_tag)
                                bundle.putString("gambar_pupuh_admin", result.gambar)
                                bundle.putInt("tag_pupuh_admin", result.id_tag)
                                bundle.putInt("id_pupuh_admin_kat", id_pupuh_admin)
                                bundle.putString("nama_pupuh_admin_kat", nama_pupuh_admin)
                                bundle.putString("desc_pupuh_admin_kat", desc_pupuh_admin)
                                bundle.putString("tag_user_pupuh","Admin")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriPupuh1Admin.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriPupuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriPupuhAdmin.visibility   = View.GONE
                                    allKategoriPupuh1Admin.visibility = View.VISIBLE
                                    allKategoriPupuh2Admin.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriPupuhAdapter = KategoriPupuhAdminAdapter(filter as ArrayList<KategoriPupuhAdminModel>,
                                        object : KategoriPupuhAdminAdapter.OnAdapterKategoriPupuhAdminListener{
                                            override fun onClick(result: KategoriPupuhAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriPupuhAdminActivity, DetailPupuhAdminActivity::class.java)
                                                bundle.putInt("id_pupuh_admin", result.id_post)
                                                bundle.putString("nama_pupuh_admin", result.nama_post)
                                                bundle.putString("nama_tag_pupuh_admin", result.nama_tag)
                                                bundle.putString("gambar_pupuh_admin", result.gambar)
                                                bundle.putInt("tag_pupuh_admin", result.id_tag)
                                                bundle.putInt("id_pupuh_admin_kat", id_pupuh_admin)
                                                bundle.putString("nama_pupuh_admin_kat", nama_pupuh_admin)
                                                bundle.putString("desc_pupuh_admin_kat", desc_pupuh_admin)
                                                bundle.putString("tag_user_pupuh","Admin")
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriPupuhAdmin.visibility   = View.VISIBLE
                                        allKategoriPupuh1Admin.visibility = View.GONE
                                        allKategoriPupuh2Admin.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriPupuhAdmin.visibility   = View.GONE
                                        allKategoriPupuh2Admin.visibility = View.VISIBLE
                                        allKategoriPupuh2Admin.adapter    = kategoriPupuhAdapter
                                        allKategoriPupuh1Admin.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriPupuh1Admin.visibility = View.VISIBLE
                                        allKategoriPupuh2Admin.visibility = View.GONE
                                        noKategoriPupuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriPupuhAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriPupuhAdmin.stopShimmer()
        shimmerKategoriPupuhAdmin.visibility = View.GONE
        swipeKategoriPupuhAdmin.visibility   = View.VISIBLE
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
        val intent = Intent(this, AllPupuhAdminActivity::class.java)
        startActivity(intent)
    }
}