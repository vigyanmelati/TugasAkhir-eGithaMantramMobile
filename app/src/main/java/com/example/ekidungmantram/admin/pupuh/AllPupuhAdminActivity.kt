package com.example.ekidungmantram.admin.pupuh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllPupuhAdapter
import com.example.ekidungmantram.adapter.admin.AllPupuhAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllPupuhModel
import com.example.ekidungmantram.model.adminmodel.AllPupuhAdminModel
import com.example.ekidungmantram.user.AllKategoriPupuhActivity
import kotlinx.android.synthetic.main.activity_all_pupuh.*
import kotlinx.android.synthetic.main.activity_all_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllPupuhAdminActivity : AppCompatActivity() {
    private lateinit var pupuhAdapter : AllPupuhAdminAdapter
    private lateinit var setAdapter    : AllPupuhAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_pupuh_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAlit"

        allPupuh1Admin.layoutManager = LinearLayoutManager(applicationContext)
        allPupuh2Admin.layoutManager = LinearLayoutManager(applicationContext)
        getAllPupuhAdmin()

        swipePupuhAdmin.setOnRefreshListener {
            getAllPupuhAdmin()
            swipePupuhAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllPupuhAdminActivity", message)
    }

    private fun getAllPupuhAdmin() {
        ApiService.endpoint.getPupuhMasterListAdmin()
            .enqueue(object: Callback<ArrayList<AllPupuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllPupuhAdminModel>>,
                    response: Response<ArrayList<AllPupuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipePupuhAdmin.visibility   = View.VISIBLE
                        shimmerPupuhAdmin.visibility = View.GONE
                        noPupuhAdmin.visibility      = View.GONE
                    }else{
                        swipePupuhAdmin.visibility   = View.GONE
                        shimmerPupuhAdmin.visibility = View.VISIBLE
                        noPupuhAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllPupuhAdminAdapter(it,
                        object : AllPupuhAdminAdapter.OnAdapterAllPupuhAdminListener{
                            override fun onClick(result: AllPupuhAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllPupuhAdminActivity, AllKategoriPupuhAdminActivity::class.java)
                                bundle.putInt("id_pupuh_admin", result.id_post)
                                bundle.putString("nama_pupuh_admin", result.nama_post)
                                bundle.putString("desc_pupuh_admin", result.deskripsi)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allPupuh1Admin.adapter  = setAdapter
                    setShimmerToStop()

                    cariPupuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noPupuhAdmin.visibility   = View.GONE
                                    allPupuh1Admin.visibility = View.VISIBLE
                                    allPupuh2Admin.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    pupuhAdapter = AllPupuhAdminAdapter(filter as ArrayList<AllPupuhAdminModel>,
                                        object : AllPupuhAdminAdapter.OnAdapterAllPupuhAdminListener{
                                            override fun onClick(result: AllPupuhAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllPupuhAdminActivity, AllKategoriPupuhAdminActivity::class.java)
                                                bundle.putInt("id_pupuh_admin", result.id_post)
                                                bundle.putString("nama_pupuh_admin", result.nama_post)
                                                bundle.putString("desc_pupuh_admin", result.deskripsi)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noPupuhAdmin.visibility   = View.VISIBLE
                                        allPupuh1Admin.visibility = View.GONE
                                        allPupuh2Admin.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noPupuhAdmin.visibility   = View.GONE
                                        allPupuh2Admin.visibility = View.VISIBLE
                                        allPupuh2Admin.adapter    = pupuhAdapter
                                        allPupuh1Admin.visibility = View.INVISIBLE
                                    }else{
                                        allPupuh1Admin.visibility = View.VISIBLE
                                        allPupuh2Admin.visibility = View.GONE
                                        noPupuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllPupuhAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerPupuhAdmin.stopShimmer()
        shimmerPupuhAdmin.visibility = View.GONE
        swipePupuhAdmin.visibility   = View.VISIBLE
    }
}