package com.example.ekidungmantram.user

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
import com.example.ekidungmantram.adapter.KategoriPupuhUserAdapter
import com.example.ekidungmantram.adapter.admin.KategoriPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.DetailPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriPupuhUserModel
import com.example.ekidungmantram.model.adminmodel.KategoriPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_all_kategori_pupuh_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKategoriPupuhUserActivity : AppCompatActivity() {
    private lateinit var kategoriPupuhAdapter : KategoriPupuhUserAdapter
    private lateinit var setAdapter    : KategoriPupuhUserAdapter
    private var id_pupuh : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var desc_pupuh :String
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kategori_pupuh_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAlit"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            val descPost = bundle.getString("desc_pupuh")
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_pupuh").toString()
            desc_pupuh = bundle.getString("desc_pupuh").toString()
            Log.d("id_pupuh", postID.toString())

            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
            val id            = sharedPreferences.getString("ID_ADMIN", null)
            val mesage = sharedPreferences.getString("MESAGE", null)

            daftar_nama_user.text = "Daftar " + namaPost
            desc_kategori_pupuh_user.text = descPost
            allKategoriPupuh1User.layoutManager = LinearLayoutManager(applicationContext)
            allKategoriPupuh2User.layoutManager = LinearLayoutManager(applicationContext)
            if (id != null) {
                getAllKategoriPupuhUser(postID, id.toInt())
                swipeKategoriPupuhUser.setOnRefreshListener {
                    getAllKategoriPupuhUser(postID, id.toInt())
                    swipeKategoriPupuhUser.isRefreshing = false
                }
            }

            fabPupuhUser.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, AddPupuhActivity::class.java)
                bundle.putInt("id_kat_pupuh", postID)
                bundle.putString("nama_kat_pupuh", namaPost)
                bundle.putString("desc_kat_pupuh", descPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriPupuhActivity", message)
    }

    private fun getAllKategoriPupuhUser(id: Int, id_user: Int) {
        ApiService.endpoint.getKategoriPupuhUser(id, id_user)
            .enqueue(object: Callback<ArrayList<KategoriPupuhUserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriPupuhUserModel>>,
                    response: Response<ArrayList<KategoriPupuhUserModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriPupuhUser.visibility   = View.VISIBLE
                        shimmerKategoriPupuhUser.visibility = View.GONE
                        noKategoriPupuhUser.visibility      = View.GONE
                    }else{
                        swipeKategoriPupuhUser.visibility   = View.GONE
                        shimmerKategoriPupuhUser.visibility = View.VISIBLE
                        noKategoriPupuhUser.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriPupuhUserAdapter(it,
                        object : KategoriPupuhUserAdapter.OnAdapterKategoriPupuhUserListener{
                            override fun onClick(result: KategoriPupuhUserModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKategoriPupuhUserActivity, DetailPupuhUserActivity::class.java)
                                bundle.putInt("id_pupuh_user", result.id_post)
                                bundle.putString("nama_pupuh_user", result.nama_post)
                                bundle.putString("nama_tag_pupuh_user", result.nama_tag)
                                bundle.putString("gambar_pupuh_user", result.gambar)
                                bundle.putInt("tag_pupuh_user", result.id_tag)
                                bundle.putInt("id_pupuh_user_kat", id_pupuh)
                                bundle.putString("nama_pupuh_user_kat", nama_pupuh)
                                bundle.putString("desc_pupuh_user_kat", desc_pupuh)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriPupuh1User.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriPupuhUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriPupuhUser.visibility   = View.GONE
                                    allKategoriPupuh1User.visibility = View.VISIBLE
                                    allKategoriPupuh2User.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriPupuhAdapter = KategoriPupuhUserAdapter(filter as ArrayList<KategoriPupuhUserModel>,
                                        object : KategoriPupuhUserAdapter.OnAdapterKategoriPupuhUserListener{
                                            override fun onClick(result: KategoriPupuhUserModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKategoriPupuhUserActivity, DetailPupuhUserActivity::class.java)
                                                bundle.putInt("id_pupuh_user", result.id_post)
                                                bundle.putString("nama_pupuh_user", result.nama_post)
                                                bundle.putString("nama_tag_pupuh_user", result.nama_tag)
                                                bundle.putString("gambar_pupuh_user", result.gambar)
                                                bundle.putInt("tag_pupuh_user", result.id_tag)
                                                bundle.putInt("id_pupuh_user_kat", id_pupuh)
                                                bundle.putString("nama_pupuh_user_kat", nama_pupuh)
                                                bundle.putString("desc_pupuh_user_kat", desc_pupuh)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriPupuhUser.visibility   = View.VISIBLE
                                        allKategoriPupuh1User.visibility = View.GONE
                                        allKategoriPupuh2User.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriPupuhUser.visibility   = View.GONE
                                        allKategoriPupuh2User.visibility = View.VISIBLE
                                        allKategoriPupuh2User.adapter    = kategoriPupuhAdapter
                                        allKategoriPupuh1User.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriPupuh1User.visibility = View.VISIBLE
                                        allKategoriPupuh2User.visibility = View.GONE
                                        noKategoriPupuhUser.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriPupuhUserModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriPupuhUser.stopShimmer()
        shimmerKategoriPupuhUser.visibility = View.GONE
        swipeKategoriPupuhUser.visibility   = View.VISIBLE
    }
}