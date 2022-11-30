package com.example.ekidungmantram.admin.laguanak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AllKategoriPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.AllPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_all_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var laguanakAdapter : AllLaguAnakAdminAdapter
    private lateinit var setAdapter    : AllLaguAnakAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lagu_anak_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarRare"

        allLaguAnak1Admin.layoutManager = LinearLayoutManager(applicationContext)
        allLaguAnak2Admin.layoutManager = LinearLayoutManager(applicationContext)
        getAllLaguAnakAdmin()

        swipeLaguAnakAdmin.setOnRefreshListener {
            getAllLaguAnakAdmin()
            swipeLaguAnakAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllLaguAdminActivity", message)
    }

    private fun getAllLaguAnakAdmin() {
        ApiService.endpoint.getLaguAnakMasterListAdmin()
            .enqueue(object: Callback<ArrayList<AllLaguAnakAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLaguAnakAdminModel>>,
                    response: Response<ArrayList<AllLaguAnakAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeLaguAnakAdmin.visibility   = View.VISIBLE
                        shimmerLaguAnakAdmin.visibility = View.GONE
                        noLaguAnakAdmin.visibility      = View.GONE
                    }else{
                        swipeLaguAnakAdmin.visibility   = View.GONE
                        shimmerLaguAnakAdmin.visibility = View.VISIBLE
                        noLaguAnakAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllLaguAnakAdminAdapter(it,
                        object : AllLaguAnakAdminAdapter.OnAdapterAllLaguAnakAdminListener{
                            override fun onClick(result: AllLaguAnakAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllLaguAnakAdminActivity, AllKategoriLaguAnakAdminActivity::class.java)
                                bundle.putInt("id_lagu_anak_admin", result.id_post)
                                bundle.putString("nama_lagu_anak_admin", result.nama_post)
                                bundle.putString("desc_lagu_anak_admin", result.deskripsi)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allLaguAnak1Admin.adapter  = setAdapter
                    setShimmerToStop()

                    cariLaguAnakAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noLaguAnakAdmin.visibility   = View.GONE
                                    allLaguAnak1Admin.visibility = View.VISIBLE
                                    allLaguAnak2Admin.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    laguanakAdapter = AllLaguAnakAdminAdapter(filter as ArrayList<AllLaguAnakAdminModel>,
                                        object : AllLaguAnakAdminAdapter.OnAdapterAllLaguAnakAdminListener{
                                            override fun onClick(result: AllLaguAnakAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllLaguAnakAdminActivity, AllKategoriLaguAnakAdminActivity::class.java)
                                                bundle.putInt("id_lagu_anak_admin", result.id_post)
                                                bundle.putString("nama_lagu_anak_admin", result.nama_post)
                                                bundle.putString("desc_lagu_anak_admin", result.deskripsi)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noLaguAnakAdmin.visibility   = View.VISIBLE
                                        allLaguAnak1Admin.visibility = View.GONE
                                        allLaguAnak2Admin.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noLaguAnakAdmin.visibility   = View.GONE
                                        allLaguAnak2Admin.visibility = View.VISIBLE
                                        allLaguAnak2Admin.adapter    = laguanakAdapter
                                        allLaguAnak1Admin.visibility = View.INVISIBLE
                                    }else{
                                        allLaguAnak1Admin.visibility = View.VISIBLE
                                        allLaguAnak2Admin.visibility = View.GONE
                                        noLaguAnakAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllLaguAnakAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerLaguAnakAdmin.stopShimmer()
        shimmerLaguAnakAdmin.visibility = View.GONE
        swipeLaguAnakAdmin.visibility   = View.VISIBLE
    }
}