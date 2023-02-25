package com.example.ekidungmantram.admin.kakawin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.KategoriKakawinAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.DetailPupuhAdminActivity
//import com.example.ekidungmantram.admin.kakawin.AddKakawinAdminActivity
//import com.example.ekidungmantram.admin.kakawin.DetailKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.KategoriKakawinAdminModel
import kotlinx.android.synthetic.main.activity_all_kategori_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriKakawinAdminActivity : AppCompatActivity() {
    private lateinit var kategoriKakawinAdapter : KategoriKakawinAdminAdapter
    private lateinit var setAdapter    : KategoriKakawinAdminAdapter
    private var id_kakawin_admin : Int = 0
    private lateinit var nama_kakawin_admin :String
    private lateinit var desc_kakawin_admin :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_kakawin_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAgung"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin_admin")
            val namaPost = bundle.getString("nama_kakawin_admin")
            val descPost = bundle.getString("desc_kakawin_admin")
            id_kakawin_admin = bundle.getInt("id_kakawin_admin")
            nama_kakawin_admin = bundle.getString("nama_kakawin_admin").toString()
            desc_kakawin_admin = bundle.getString("desc_kakawin_admin").toString()
            Log.d("id_kakawin_admin", postID.toString())

            daftar_namaKakawinAdmin.text = "Daftar " + namaPost
            desc_kategori_kakawinAdmin.text = descPost
            allKategoriKakawin1Admin.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriKakawin2Admin.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriKakawinAdmin(postID)

            swipeKategoriKakawinAdmin.setOnRefreshListener {
                getAllKategoriKakawinAdmin(postID)
                swipeKategoriKakawinAdmin.isRefreshing = false
            }

            fabKakawinAdmin.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AddKakawinAdminActivity::class.java)
                bundle.putInt("id_kat_kakawin_admin", postID)
                bundle.putString("nama_kat_kakawin_admin", namaPost)
                bundle.putString("desc_kat_kakawin_admin", descPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriKakawinActivity", message)
    }

    private fun getAllKategoriKakawinAdmin(id: Int) {
        ApiService.endpoint.getKategoriKakawinListAdmin(id)
            .enqueue(object: Callback<ArrayList<KategoriKakawinAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriKakawinAdminModel>>,
                    response: Response<ArrayList<KategoriKakawinAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriKakawinAdmin.visibility   = View.VISIBLE
                        shimmerKategoriKakawinAdmin.visibility = View.GONE
                        noKategoriKakawinAdmin.visibility      = View.GONE
                    }else{
                        swipeKategoriKakawinAdmin.visibility   = View.GONE
                        shimmerKategoriKakawinAdmin.visibility = View.VISIBLE
                        noKategoriKakawinAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriKakawinAdminAdapter(it,
                        object : KategoriKakawinAdminAdapter.OnAdapterKategoriKakawinAdminListener{
                            override fun onClick(result: KategoriKakawinAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriKakawinAdminActivity, DetailKakawinAdminActivity::class.java)
                                bundle.putInt("id_kakawin_admin", result.id_post)
                                bundle.putString("nama_kakawin_admin", result.nama_post)
                                bundle.putString("nama_tag_kakawin_admin", result.nama_tag)
                                bundle.putString("gambar_kakawin_admin", result.gambar)
                                bundle.putInt("tag_kakawin_admin", result.id_tag)
                                bundle.putInt("id_kakawin_admin_kat", id_kakawin_admin)
                                bundle.putString("nama_kakawin_admin_kat", nama_kakawin_admin)
                                bundle.putString("desc_kakawin_admin_kat", desc_kakawin_admin)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriKakawin1Admin.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriKakawinAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriKakawinAdmin.visibility   = View.GONE
                                    allKategoriKakawin1Admin.visibility = View.VISIBLE
                                    allKategoriKakawin2Admin.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriKakawinAdapter = KategoriKakawinAdminAdapter(filter as ArrayList<KategoriKakawinAdminModel>,
                                        object : KategoriKakawinAdminAdapter.OnAdapterKategoriKakawinAdminListener{
                                            override fun onClick(result: KategoriKakawinAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriKakawinAdminActivity, DetailKakawinAdminActivity::class.java)
                                                bundle.putInt("id_kakawin_admin", result.id_post)
                                                bundle.putString("nama_kakawin_admin", result.nama_post)
                                                bundle.putString("nama_tag_kakawin_admin", result.nama_tag)
                                                bundle.putString("gambar_kakawin_admin", result.gambar)
                                                bundle.putInt("tag_kakawin_admin", result.id_tag)
                                                bundle.putInt("id_kakawin_admin_kat", id_kakawin_admin)
                                                bundle.putString("nama_kakawin_admin_kat", nama_kakawin_admin)
                                                bundle.putString("desc_kakawin_admin_kat", desc_kakawin_admin)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriKakawinAdmin.visibility   = View.VISIBLE
                                        allKategoriKakawin1Admin.visibility = View.GONE
                                        allKategoriKakawin2Admin.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriKakawinAdmin.visibility   = View.GONE
                                        allKategoriKakawin2Admin.visibility = View.VISIBLE
                                        allKategoriKakawin2Admin.adapter    = kategoriKakawinAdapter
                                        allKategoriKakawin1Admin.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriKakawin1Admin.visibility = View.VISIBLE
                                        allKategoriKakawin2Admin.visibility = View.GONE
                                        noKategoriKakawinAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriKakawinAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriKakawinAdmin.stopShimmer()
        shimmerKategoriKakawinAdmin.visibility = View.GONE
        swipeKategoriKakawinAdmin.visibility   = View.VISIBLE
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
}