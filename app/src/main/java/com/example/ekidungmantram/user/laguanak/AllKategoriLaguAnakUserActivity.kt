package com.example.ekidungmantram.user.laguanak

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.KategoriLaguAnakUserAdapter
import com.example.ekidungmantram.admin.laguanak.DetailLaguAnakAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriLaguAnakUserModel
import com.example.ekidungmantram.user.laguanak.AddLaguAnakActivity
import kotlinx.android.synthetic.main.activity_all_kategori_lagu_anak_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriLaguAnakUserActivity : AppCompatActivity() {
    private lateinit var kategoriLaguAnakAdapter : KategoriLaguAnakUserAdapter
    private lateinit var setAdapter    : KategoriLaguAnakUserAdapter
    private var id_lagu_anak : Int = 0
    private lateinit var nama_lagu_anak :String
    private lateinit var desc_lagu_anak :String
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_lagu_anak_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAgung"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")
            val descPost = bundle.getString("desc_lagu_anak")
            id_lagu_anak = bundle.getInt("id_lagu_anak")
            nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()
            desc_lagu_anak = bundle.getString("desc_lagu_anak").toString()
            Log.d("id_lagu_anak", postID.toString())

            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
            val id            = sharedPreferences.getString("ID_ADMIN", null)
            val mesage = sharedPreferences.getString("MESAGE", null)

            daftar_nama_user.text = "Daftar " + namaPost
            desc_kategori_lagu_anak_user.text = descPost
            allKategoriLaguAnak1User.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriLaguAnak2User.layoutManager = LinearLayoutManager(applicationContext)
            if (id != null) {
                getAllKategoriLaguAnakUser(postID, id.toInt())
                swipeKategoriLaguAnakUser.setOnRefreshListener {
                    getAllKategoriLaguAnakUser(postID, id.toInt())
                    swipeKategoriLaguAnakUser.isRefreshing = false
                }
            }

            fabLaguAnakUser.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AddLaguAnakActivity::class.java)
                bundle.putInt("id_kat_lagu_anak", postID)
                bundle.putString("nama_kat_lagu_anak", namaPost)
                bundle.putString("desc_kat_lagu_anak", descPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriLaguActivity", message)
    }

    private fun getAllKategoriLaguAnakUser(id: Int, id_user: Int) {
        ApiService.endpoint.getKategoriLaguAnakUser(id, id_user)
            .enqueue(object: Callback<ArrayList<KategoriLaguAnakUserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriLaguAnakUserModel>>,
                    response: Response<ArrayList<KategoriLaguAnakUserModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriLaguAnakUser.visibility   = View.VISIBLE
                        shimmerKategoriLaguAnakUser.visibility = View.GONE
                        noKategoriLaguAnakUser.visibility      = View.GONE
                    }else{
                        swipeKategoriLaguAnakUser.visibility   = View.GONE
                        shimmerKategoriLaguAnakUser.visibility = View.VISIBLE
                        noKategoriLaguAnakUser.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriLaguAnakUserAdapter(it,
                        object : KategoriLaguAnakUserAdapter.OnAdapterKategoriLaguAnakUserListener{
                            override fun onClick(result: KategoriLaguAnakUserModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriLaguAnakUserActivity, DetailLaguAnakAdminActivity::class.java)
                                bundle.putInt("id_lagu_anak_admin", result.id_post)
                                bundle.putString("nama_lagu_anak_admin", result.nama_post)
                                bundle.putString("nama_tag_lagu_anak_admin", result.nama_tag)
                                bundle.putString("gambar_lagu_anak_admin", result.gambar)
                                bundle.putInt("tag_lagu_anak_admin", result.id_tag)
                                bundle.putInt("id_lagu_anak_admin_kat", id_lagu_anak)
                                bundle.putString("nama_lagu_anak_admin_kat", nama_lagu_anak)
                                bundle.putString("desc_lagu_anak_admin_kat", desc_lagu_anak)
                                bundle.putString("tag_user_anak","Pengguna")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriLaguAnak1User.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriLaguAnakUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriLaguAnakUser.visibility   = View.GONE
                                    allKategoriLaguAnak1User.visibility = View.VISIBLE
                                    allKategoriLaguAnak2User.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriLaguAnakAdapter = KategoriLaguAnakUserAdapter(filter as ArrayList<KategoriLaguAnakUserModel>,
                                        object : KategoriLaguAnakUserAdapter.OnAdapterKategoriLaguAnakUserListener{
                                            override fun onClick(result: KategoriLaguAnakUserModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriLaguAnakUserActivity, DetailLaguAnakAdminActivity::class.java)
                                                bundle.putInt("id_lagu_anak_admin", result.id_post)
                                                bundle.putString("nama_lagu_anak_admin", result.nama_post)
                                                bundle.putString("nama_tag_lagu_anak_admin", result.nama_tag)
                                                bundle.putString("gambar_lagu_anak_admin", result.gambar)
                                                bundle.putInt("tag_lagu_anak_admin", result.id_tag)
                                                bundle.putInt("id_lagu_anak_admin_kat", id_lagu_anak)
                                                bundle.putString("nama_lagu_anak_admin_kat", nama_lagu_anak)
                                                bundle.putString("desc_lagu_anak_admin_kat", desc_lagu_anak)
                                                bundle.putString("tag_user_anak","Admin")
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriLaguAnakUser.visibility   = View.VISIBLE
                                        allKategoriLaguAnak1User.visibility = View.GONE
                                        allKategoriLaguAnak2User.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriLaguAnakUser.visibility   = View.GONE
                                        allKategoriLaguAnak2User.visibility = View.VISIBLE
                                        allKategoriLaguAnak2User.adapter    = kategoriLaguAnakAdapter
                                        allKategoriLaguAnak1User.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriLaguAnak1User.visibility = View.VISIBLE
                                        allKategoriLaguAnak2User.visibility = View.GONE
                                        noKategoriLaguAnakUser.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriLaguAnakUserModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriLaguAnakUser.stopShimmer()
        shimmerKategoriLaguAnakUser.visibility = View.GONE
        swipeKategoriLaguAnakUser.visibility   = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val bundle = Bundle()
        val intent = Intent(this@AllKategoriLaguAnakUserActivity, AllKategoriLaguAnakActivity::class.java)
        bundle.putInt("id_lagu_anak", id_lagu_anak)
        bundle.putString("nama_lagu_anak", nama_lagu_anak)
        bundle.putString("desc_lagu_anak", desc_lagu_anak)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}