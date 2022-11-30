package com.example.ekidungmantram.admin.laguanak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.KategoriLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.KategoriPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.DetailPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.KategoriLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.KategoriPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_kategori_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var kategoriLaguAnakAdapter : KategoriLaguAnakAdminAdapter
    private lateinit var setAdapter    : KategoriLaguAnakAdminAdapter
    private var id_lagu_anak_admin : Int = 0
    private lateinit var nama_lagu_anak_admin :String
    private lateinit var desc_lagu_anak_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_lagu_anak_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarRare"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak_admin")
            val namaPost = bundle.getString("nama_lagu_anak_admin")
            val descPost = bundle.getString("desc_lagu_anak_admin")
            id_lagu_anak_admin = bundle.getInt("id_lagu_anak_admin")
            nama_lagu_anak_admin = bundle.getString("nama_lagu_anak_admin").toString()
            desc_lagu_anak_admin = bundle.getString("desc_lagu_anak_admin").toString()
            Log.d("id_lagu_anak_admin", postID.toString())

            daftar_namaLaguAdmin.text = "Daftar " + namaPost
            desc_kategori_lagu_anakAdmin.text = descPost
            allKategoriLaguAnak1Admin.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriLaguAnak2Admin.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriLaguAnakAdmin(postID)

            swipeKategoriLaguAnakAdmin.setOnRefreshListener {
                getAllKategoriLaguAnakAdmin(postID)
                swipeKategoriLaguAnakAdmin.isRefreshing = false
            }

            fabLaguAnakAdmin.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AddLaguAnakAdminActivity::class.java)
                bundle.putInt("id_kat_lagu_anak_admin", postID)
                bundle.putString("nama_kat_lagu_anak_admin", namaPost)
                bundle.putString("desc_kat_lagu_anak_admin", descPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriLaguActivity", message)
    }

    private fun getAllKategoriLaguAnakAdmin(id: Int) {
        ApiService.endpoint.getKategoriLaguAnakListAdmin(id)
            .enqueue(object: Callback<ArrayList<KategoriLaguAnakAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriLaguAnakAdminModel>>,
                    response: Response<ArrayList<KategoriLaguAnakAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriLaguAnakAdmin.visibility   = View.VISIBLE
                        shimmerKategoriLaguAnakAdmin.visibility = View.GONE
                        noKategoriLaguAnakAdmin.visibility      = View.GONE
                    }else{
                        swipeKategoriLaguAnakAdmin.visibility   = View.GONE
                        shimmerKategoriLaguAnakAdmin.visibility = View.VISIBLE
                        noKategoriLaguAnakAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriLaguAnakAdminAdapter(it,
                        object : KategoriLaguAnakAdminAdapter.OnAdapterKategoriLaguAnakAdminListener{
                            override fun onClick(result: KategoriLaguAnakAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriLaguAnakAdminActivity, DetailLaguAnakAdminActivity::class.java)
                                bundle.putInt("id_lagu_anak_admin", result.id_post)
                                bundle.putString("nama_lagu_anak_admin", result.nama_post)
                                bundle.putString("nama_tag_lagu_anak_admin", result.nama_tag)
                                bundle.putString("gambar_lagu_anak_admin", result.gambar)
                                bundle.putInt("tag_lagu_anak_admin", result.id_tag)
                                bundle.putInt("id_lagu_anak_admin_kat", id_lagu_anak_admin)
                                bundle.putString("nama_lagu_anak_admin_kat", nama_lagu_anak_admin)
                                bundle.putString("desc_lagu_anak_admin_kat", desc_lagu_anak_admin)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriLaguAnak1Admin.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriLaguAnakAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriLaguAnakAdmin.visibility   = View.GONE
                                    allKategoriLaguAnak1Admin.visibility = View.VISIBLE
                                    allKategoriLaguAnak2Admin.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriLaguAnakAdapter = KategoriLaguAnakAdminAdapter(filter as ArrayList<KategoriLaguAnakAdminModel>,
                                        object : KategoriLaguAnakAdminAdapter.OnAdapterKategoriLaguAnakAdminListener{
                                            override fun onClick(result: KategoriLaguAnakAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriLaguAnakAdminActivity, DetailLaguAnakAdminActivity::class.java)
                                                bundle.putInt("id_lagu_anak_admin", result.id_post)
                                                bundle.putString("nama_lagu_anak_admin", result.nama_post)
                                                bundle.putString("nama_tag_lagu_anak_admin", result.nama_tag)
                                                bundle.putString("gambar_lagu_anak_admin", result.gambar)
                                                bundle.putInt("tag_lagu_anak_admin", result.id_tag)
                                                bundle.putInt("id_lagu_anak_admin_kat", id_lagu_anak_admin)
                                                bundle.putString("nama_lagu_anak_admin_kat", nama_lagu_anak_admin)
                                                bundle.putString("desc_lagu_anak_admin_kat", desc_lagu_anak_admin)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriLaguAnakAdmin.visibility   = View.VISIBLE
                                        allKategoriLaguAnak1Admin.visibility = View.GONE
                                        allKategoriLaguAnak2Admin.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriLaguAnakAdmin.visibility   = View.GONE
                                        allKategoriLaguAnak2Admin.visibility = View.VISIBLE
                                        allKategoriLaguAnak2Admin.adapter    = kategoriLaguAnakAdapter
                                        allKategoriLaguAnak1Admin.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriLaguAnak1Admin.visibility = View.VISIBLE
                                        allKategoriLaguAnak2Admin.visibility = View.GONE
                                        noKategoriLaguAnakAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriLaguAnakAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriLaguAnakAdmin.stopShimmer()
        shimmerKategoriLaguAnakAdmin.visibility = View.GONE
        swipeKategoriLaguAnakAdmin.visibility   = View.VISIBLE
    }
}