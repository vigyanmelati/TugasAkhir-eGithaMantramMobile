package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllLaguAnakAdapter
import com.example.ekidungmantram.adapter.AllPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllLaguAnakModel
import com.example.ekidungmantram.model.AllPupuhModel
import kotlinx.android.synthetic.main.activity_all_pupuh.*
import kotlinx.android.synthetic.main.activity_alll_lagu_anak.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlllLaguAnakActivity : AppCompatActivity() {
    private lateinit var laguAnakAdapter : AllLaguAnakAdapter
    private lateinit var setAdapter    : AllLaguAnakAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alll_lagu_anak)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarRare"

        allLaguAnak1.layoutManager = LinearLayoutManager(applicationContext)
        allLaguAnak2.layoutManager = LinearLayoutManager(applicationContext)
        getAllLaguAnak()

        swipeLaguAnak.setOnRefreshListener {
            getAllLaguAnak()
            swipeLaguAnak.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllLaguAnakActivity", message)
    }

    private fun getAllLaguAnak() {
        ApiService.endpoint.getLaguAnakMasterList()
            .enqueue(object: Callback<ArrayList<AllLaguAnakModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLaguAnakModel>>,
                    response: Response<ArrayList<AllLaguAnakModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeLaguAnak.visibility   = View.VISIBLE
                        shimmerLaguAnak.visibility = View.GONE
                        noLaguAnak.visibility      = View.GONE
                    }else{
                        swipeLaguAnak.visibility   = View.GONE
                        shimmerLaguAnak.visibility = View.VISIBLE
                        noLaguAnak.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllLaguAnakAdapter(it,
                        object : AllLaguAnakAdapter.OnAdapterAllLaguAnakListener{
                            override fun onClick(result: AllLaguAnakModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AlllLaguAnakActivity, AllKategoriLaguAnakActivity::class.java)
                                bundle.putInt("id_lagu_anak", result.id_post)
                                bundle.putString("nama_lagu_anak", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allLaguAnak1.adapter  = setAdapter
                    setShimmerToStop()

                    cariLaguAnak.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noLaguAnak.visibility   = View.GONE
                                    allLaguAnak1.visibility = View.VISIBLE
                                    allLaguAnak2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    laguAnakAdapter = AllLaguAnakAdapter(filter as ArrayList<AllLaguAnakModel>,
                                        object : AllLaguAnakAdapter.OnAdapterAllLaguAnakListener{
                                            override fun onClick(result: AllLaguAnakModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AlllLaguAnakActivity, DetailLaguAnakActivity::class.java)
                                                bundle.putInt("id_lagu_anak", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noLaguAnak.visibility   = View.VISIBLE
                                        allLaguAnak1.visibility = View.GONE
                                        allLaguAnak2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noLaguAnak.visibility   = View.GONE
                                        allLaguAnak2.visibility = View.VISIBLE
                                        allLaguAnak2.adapter    = laguAnakAdapter
                                        allLaguAnak1.visibility = View.INVISIBLE
                                    }else{
                                        allLaguAnak1.visibility = View.VISIBLE
                                        allLaguAnak2.visibility = View.GONE
                                        noLaguAnak.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllLaguAnakModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerLaguAnak.stopShimmer()
        shimmerLaguAnak.visibility = View.GONE
        swipeLaguAnak.visibility   = View.VISIBLE
    }

}