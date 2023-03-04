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
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.KategoriKakawinUserAdapter
import com.example.ekidungmantram.admin.kakawin.AddKakawinAdminActivity
import com.example.ekidungmantram.admin.kakawin.DetailKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriKakawinUserModel
import kotlinx.android.synthetic.main.activity_all_kategori_kakawin_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriKakawinUserActivity : AppCompatActivity() {
    private lateinit var kategoriKakawinAdapter : KategoriKakawinUserAdapter
    private lateinit var setAdapter    : KategoriKakawinUserAdapter
    private var id_kakawin : Int = 0
    private lateinit var nama_kakawin :String
    private lateinit var desc_kakawin :String
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_kakawin_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAgung"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")
            val descPost = bundle.getString("desc_kakawin")
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kakawin").toString()
            desc_kakawin = bundle.getString("desc_kakawin").toString()
            Log.d("id_kakawin", postID.toString())

            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
            val id            = sharedPreferences.getString("ID_ADMIN", null)
            val mesage = sharedPreferences.getString("MESAGE", null)

            daftar_nama_user.text = "Daftar " + namaPost
            desc_kategori_kakawin_user.text = descPost
            allKategoriKakawin1User.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriKakawin2User.layoutManager = LinearLayoutManager(applicationContext)
            if (id != null) {
                getAllKategoriKakawinUser(postID, id.toInt())
                swipeKategoriKakawinUser.setOnRefreshListener {
                    getAllKategoriKakawinUser(postID, id.toInt())
                    swipeKategoriKakawinUser.isRefreshing = false
                }
            }

            fabKakawinUser.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AddKakawinActivity::class.java)
                bundle.putInt("id_kat_kakawin", postID)
                bundle.putString("nama_kat_kakawin", namaPost)
                bundle.putString("desc_kat_kakawin", descPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriKakawinActivity", message)
    }

    private fun getAllKategoriKakawinUser(id: Int, id_user: Int) {
        ApiService.endpoint.getKategoriKakawinUser(id, id_user)
            .enqueue(object: Callback<ArrayList<KategoriKakawinUserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriKakawinUserModel>>,
                    response: Response<ArrayList<KategoriKakawinUserModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriKakawinUser.visibility   = View.VISIBLE
                        shimmerKategoriKakawinUser.visibility = View.GONE
                        noKategoriKakawinUser.visibility      = View.GONE
                    }else{
                        swipeKategoriKakawinUser.visibility   = View.GONE
                        shimmerKategoriKakawinUser.visibility = View.VISIBLE
                        noKategoriKakawinUser.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriKakawinUserAdapter(it,
                        object : KategoriKakawinUserAdapter.OnAdapterKategoriKakawinUserListener{
                            override fun onClick(result: KategoriKakawinUserModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriKakawinUserActivity, DetailKakawinAdminActivity::class.java)
                                bundle.putInt("id_kakawin_admin", result.id_post)
                                bundle.putString("nama_kakawin_admin", result.nama_post)
                                bundle.putString("nama_tag_kakawin_admin", result.nama_tag)
                                bundle.putString("gambar_kakawin_admin", result.gambar)
                                bundle.putInt("tag_kakawin_admin", result.id_tag)
                                bundle.putInt("id_kakawin_admin_kat", id_kakawin)
                                bundle.putString("nama_kakawin_admin_kat", nama_kakawin)
                                bundle.putString("desc_kakawin_admin_kat", desc_kakawin)
                                bundle.putString("tag_user","Pengguna")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriKakawin1User.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriKakawinUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriKakawinUser.visibility   = View.GONE
                                    allKategoriKakawin1User.visibility = View.VISIBLE
                                    allKategoriKakawin2User.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriKakawinAdapter = KategoriKakawinUserAdapter(filter as ArrayList<KategoriKakawinUserModel>,
                                        object : KategoriKakawinUserAdapter.OnAdapterKategoriKakawinUserListener{
                                            override fun onClick(result: KategoriKakawinUserModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriKakawinUserActivity, DetailKakawinAdminActivity::class.java)
                                                bundle.putInt("id_kakawin_admin", result.id_post)
                                                bundle.putString("nama_kakawin_admin", result.nama_post)
                                                bundle.putString("nama_tag_kakawin_admin", result.nama_tag)
                                                bundle.putString("gambar_kakawin_admin", result.gambar)
                                                bundle.putInt("tag_kakawin_admin", result.id_tag)
                                                bundle.putInt("id_kakawin_admin_kat", id_kakawin)
                                                bundle.putString("nama_kakawin_admin_kat", nama_kakawin)
                                                bundle.putString("desc_kakawin_admin_kat", desc_kakawin)
                                                bundle.putString("tag_user","Pengguna")
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriKakawinUser.visibility   = View.VISIBLE
                                        allKategoriKakawin1User.visibility = View.GONE
                                        allKategoriKakawin2User.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriKakawinUser.visibility   = View.GONE
                                        allKategoriKakawin2User.visibility = View.VISIBLE
                                        allKategoriKakawin2User.adapter    = kategoriKakawinAdapter
                                        allKategoriKakawin1User.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriKakawin1User.visibility = View.VISIBLE
                                        allKategoriKakawin2User.visibility = View.GONE
                                        noKategoriKakawinUser.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriKakawinUserModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriKakawinUser.stopShimmer()
        shimmerKategoriKakawinUser.visibility = View.GONE
        swipeKategoriKakawinUser.visibility   = View.VISIBLE
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
        val bundle = Bundle()
        val intent = Intent(this@AllKategoriKakawinUserActivity, AllKategoriKakawinActivity::class.java)
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
        bundle.putString("desc_kakawin", desc_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}