package com.example.ekidungmantram.admin.kakawin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllKakawinAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AllKategoriPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllKakawinAdminModel
import com.example.ekidungmantram.model.adminmodel.AllPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_all_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKakawinAdminActivity : AppCompatActivity() {
    private lateinit var kakawinAdapter : AllKakawinAdminAdapter
    private lateinit var setAdapter    : AllKakawinAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kakawin_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAgung"

        allKakawin1Admin.layoutManager = LinearLayoutManager(applicationContext)
        allKakawin2Admin.layoutManager = LinearLayoutManager(applicationContext)
        getAllKakawinAdmin()

        swipeKakawinAdmin.setOnRefreshListener {
            getAllKakawinAdmin()
            swipeKakawinAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllKakawinAdminActivity", message)
    }

    private fun getAllKakawinAdmin() {
        ApiService.endpoint.getKakawinMasterListAdmin()
            .enqueue(object: Callback<ArrayList<AllKakawinAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllKakawinAdminModel>>,
                    response: Response<ArrayList<AllKakawinAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKakawinAdmin.visibility   = View.VISIBLE
                        shimmerKakawinAdmin.visibility = View.GONE
                        noKakawinAdmin.visibility      = View.GONE
                    }else{
                        swipeKakawinAdmin.visibility   = View.GONE
                        shimmerKakawinAdmin.visibility = View.VISIBLE
                        noKakawinAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllKakawinAdminAdapter(it,
                        object : AllKakawinAdminAdapter.OnAdapterAllKakawinAdminListener{
                            override fun onClick(result: AllKakawinAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKakawinAdminActivity, AllKategoriKakawinAdminActivity::class.java)
                                bundle.putInt("id_kakawin_admin", result.id_post)
                                bundle.putString("nama_kakawin_admin", result.nama_post)
                                bundle.putString("desc_kakawin_admin", result.deskripsi)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKakawin1Admin.adapter  = setAdapter
                    setShimmerToStop()

                    cariKakawinAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKakawinAdmin.visibility   = View.GONE
                                    allKakawin1Admin.visibility = View.VISIBLE
                                    allKakawin2Admin.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kakawinAdapter = AllKakawinAdminAdapter(filter as ArrayList<AllKakawinAdminModel>,
                                        object : AllKakawinAdminAdapter.OnAdapterAllKakawinAdminListener{
                                            override fun onClick(result: AllKakawinAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKakawinAdminActivity, AllKategoriKakawinAdminActivity::class.java)
                                                bundle.putInt("id_kakawin_admin", result.id_post)
                                                bundle.putString("nama_kakawin_admin", result.nama_post)
                                                bundle.putString("desc_kakawin_admin", result.deskripsi)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKakawinAdmin.visibility   = View.VISIBLE
                                        allKakawin1Admin.visibility = View.GONE
                                        allKakawin2Admin.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKakawinAdmin.visibility   = View.GONE
                                        allKakawin2Admin.visibility = View.VISIBLE
                                        allKakawin2Admin.adapter    = kakawinAdapter
                                        allKakawin1Admin.visibility = View.INVISIBLE
                                    }else{
                                        allKakawin1Admin.visibility = View.VISIBLE
                                        allKakawin2Admin.visibility = View.GONE
                                        noKakawinAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllKakawinAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKakawinAdmin.stopShimmer()
        shimmerKakawinAdmin.visibility = View.GONE
        swipeKakawinAdmin.visibility   = View.VISIBLE
    }
}