package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllKakawinAdapter
import com.example.ekidungmantram.adapter.AllKidungAdapter
import com.example.ekidungmantram.adapter.AllPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllKakawinModel
import com.example.ekidungmantram.model.AllKidungModel
import kotlinx.android.synthetic.main.activity_all_kakawin.*
import kotlinx.android.synthetic.main.activity_all_kidung.*
import kotlinx.android.synthetic.main.activity_all_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKakawinActivity : AppCompatActivity() {
    private lateinit var kakawinAdapter : AllKakawinAdapter
    private lateinit var setAdapter    : AllKakawinAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kakawin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAgung"

        allKakawin1.layoutManager = LinearLayoutManager(applicationContext)
        allKakawin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllKakawin()

        swipeKakawin.setOnRefreshListener {
            getAllKakawin()
            swipeKakawin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllKakawinActivity", message)
    }

    private fun getAllKakawin() {
        ApiService.endpoint.getKakawinMasterList()
            .enqueue(object: Callback<ArrayList<AllKakawinModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllKakawinModel>>,
                    response: Response<ArrayList<AllKakawinModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKakawin.visibility   = View.VISIBLE
                        shimmerKakawin.visibility = View.GONE
                        noKakawin.visibility      = View.GONE
                    }else{
                        swipeKakawin.visibility   = View.GONE
                        shimmerKakawin.visibility = View.VISIBLE
                        noKakawin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllKakawinAdapter(it,
                        object : AllKakawinAdapter.OnAdapterAllKakawinListener{
                            override fun onClick(result: AllKakawinModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKakawinActivity, DetailKidungActivity::class.java)
                                bundle.putInt("id_kidung", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKakawin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariKakawin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKakawin.visibility   = View.GONE
                                    allKakawin1.visibility = View.VISIBLE
                                    allKakawin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kakawinAdapter = AllKakawinAdapter(filter as ArrayList<AllKakawinModel>,
                                        object : AllKakawinAdapter.OnAdapterAllKakawinListener{
                                            override fun onClick(result: AllKakawinModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKakawinActivity, DetailKidungActivity::class.java)
                                                bundle.putInt("id_kidung", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKakawin.visibility   = View.VISIBLE
                                        allKakawin1.visibility = View.GONE
                                        allKakawin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKakawin.visibility   = View.GONE
                                        allKakawin2.visibility = View.VISIBLE
                                        allKakawin2.adapter    = kakawinAdapter
                                        allKakawin1.visibility = View.INVISIBLE
                                    }else{
                                        allKakawin1.visibility = View.VISIBLE
                                        allKakawin2.visibility = View.GONE
                                        noKakawin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllKakawinModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKakawin.stopShimmer()
        shimmerKakawin.visibility = View.GONE
        swipeKakawin.visibility   = View.VISIBLE
    }
}