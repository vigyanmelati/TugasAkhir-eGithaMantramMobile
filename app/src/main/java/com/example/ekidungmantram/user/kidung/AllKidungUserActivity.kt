package com.example.ekidungmantram.user.kidung

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.KategoriKidungUserAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.KategoriKidungUserModel
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.admin.kidung.AddKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.user.kakawin.AllKategoriKakawinActivity
import kotlinx.android.synthetic.main.activity_all_kategori_kakawin_user.*
import kotlinx.android.synthetic.main.activity_all_kategori_kakawin_user.daftar_nama_user
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import kotlinx.android.synthetic.main.activity_all_kidung_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKidungUserActivity : AppCompatActivity() {
    private lateinit var kategoriKidungAdapter : KategoriKidungUserAdapter
    private lateinit var setAdapter    : KategoriKidungUserAdapter
//    private var id_kidung : Int = 0
//    private lateinit var nama_kidung :String
//    private lateinit var desc_kidung :String
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kidung_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarMadya"

        allKategoriKidung1User.layoutManager = LinearLayoutManager(applicationContext)
        allKategoriKidung2User.layoutManager = LinearLayoutManager(applicationContext)

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)
        val id            = sharedPreferences.getString("ID_ADMIN", null)
        val mesage = sharedPreferences.getString("MESAGE", null)
        if (id != null) {
            getAllKategoriKidungUser(id.toInt())
        }

        swipeKategoriKidungUser.setOnRefreshListener {
            if (id != null) {
                getAllKategoriKidungUser(id.toInt())
            }
            swipeKategoriKidungUser.isRefreshing = false
        }


        fabKidungUser.setOnClickListener {
            val intent = Intent(this, AddKidungActivity::class.java)
            startActivity(intent)
        }

    }

    private fun printLog(message: String) {
        Log.d("KategoriKidungActivity", message)
    }

    private fun getAllKategoriKidungUser( id_user: Int) {
        ApiService.endpoint.getKategoriKidungUser( id_user)
            .enqueue(object: Callback<ArrayList<KategoriKidungUserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KategoriKidungUserModel>>,
                    response: Response<ArrayList<KategoriKidungUserModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKategoriKidungUser.visibility   = View.VISIBLE
                        shimmerKategoriKidungUser.visibility = View.GONE
                        noKategoriKidungUser.visibility      = View.GONE
                    }else{
                        swipeKategoriKidungUser.visibility   = View.GONE
                        shimmerKategoriKidungUser.visibility = View.VISIBLE
                        noKategoriKidungUser.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { KategoriKidungUserAdapter(it,
                        object : KategoriKidungUserAdapter.OnAdapterKategoriKidungUserListener{
                            override fun onClick(result: KategoriKidungUserModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKidungUserActivity, DetailKidungAdminActivity::class.java)
                                bundle.putInt("id_kidung", result.id_post)
                                bundle.putString("nama_kidung", result.nama_post)
                                bundle.putString("tag_user_kidung","Pengguna")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKategoriKidung1User.adapter  = setAdapter
                    setShimmerToStop()

                    cariKategoriKidungUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKategoriKidungUser.visibility   = View.GONE
                                    allKategoriKidung1User.visibility = View.VISIBLE
                                    allKategoriKidung2User.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriKidungAdapter = KategoriKidungUserAdapter(filter as ArrayList<KategoriKidungUserModel>,
                                        object : KategoriKidungUserAdapter.OnAdapterKategoriKidungUserListener{
                                            override fun onClick(result: KategoriKidungUserModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKidungUserActivity, DetailKidungAdminActivity::class.java)
                                                bundle.putInt("id_kidung", result.id_post)
                                                bundle.putString("nama_kidung", result.nama_post)
                                                bundle.putString("tag_user_kidung","Pengguna")
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKategoriKidungUser.visibility   = View.VISIBLE
                                        allKategoriKidung1User.visibility = View.GONE
                                        allKategoriKidung2User.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKategoriKidungUser.visibility   = View.GONE
                                        allKategoriKidung2User.visibility = View.VISIBLE
                                        allKategoriKidung2User.adapter    = kategoriKidungAdapter
                                        allKategoriKidung1User.visibility = View.INVISIBLE
                                    }else{
                                        allKategoriKidung1User.visibility = View.VISIBLE
                                        allKategoriKidung2User.visibility = View.GONE
                                        noKategoriKidungUser.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<KategoriKidungUserModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKategoriKidungUser.stopShimmer()
        shimmerKategoriKidungUser.visibility = View.GONE
        swipeKategoriKidungUser.visibility   = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AllKidungUserActivity, AllKidungActivity::class.java)
        startActivity(intent)
    }
}