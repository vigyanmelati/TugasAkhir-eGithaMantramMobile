package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllKidungAdapter
import com.example.ekidungmantram.adapter.AllPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllKidungModel
import com.example.ekidungmantram.model.AllPupuhModel
import kotlinx.android.synthetic.main.activity_all_kidung.*
import kotlinx.android.synthetic.main.activity_all_kidung.allKidung2
import kotlinx.android.synthetic.main.activity_all_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllPupuhActivity : AppCompatActivity() {
    private lateinit var pupuhAdapter : AllPupuhAdapter
    private lateinit var setAdapter    : AllPupuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_pupuh)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-SekarAlit"

        allPupuh1.layoutManager = LinearLayoutManager(applicationContext)
        allPupuh2.layoutManager = LinearLayoutManager(applicationContext)
        getAllPupuh()

        swipePupuh.setOnRefreshListener {
            getAllPupuh()
            swipePupuh.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllPupuhActivity", message)
    }

    private fun getAllPupuh() {
        ApiService.endpoint.getPupuhMasterList()
            .enqueue(object: Callback<ArrayList<AllPupuhModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllPupuhModel>>,
                    response: Response<ArrayList<AllPupuhModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipePupuh.visibility   = View.VISIBLE
                        shimmerPupuh.visibility = View.GONE
                        noPupuh.visibility      = View.GONE
                    }else{
                        swipePupuh.visibility   = View.GONE
                        shimmerPupuh.visibility = View.VISIBLE
                        noPupuh.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllPupuhAdapter(it,
                        object : AllPupuhAdapter.OnAdapterAllPupuhListener{
                            override fun onClick(result: AllPupuhModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllPupuhActivity, AllKategoriPupuhActivity::class.java)
                                bundle.putInt("id_pupuh", result.id_post)
                                bundle.putString("nama_pupuh", result.nama_post)
                                bundle.putString("desc_pupuh", result.deskripsi)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allPupuh1.adapter  = setAdapter
                    setShimmerToStop()

                    cariPupuh.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noPupuh.visibility   = View.GONE
                                    allPupuh1.visibility = View.VISIBLE
                                    allPupuh2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    pupuhAdapter = AllPupuhAdapter(filter as ArrayList<AllPupuhModel>,
                                        object : AllPupuhAdapter.OnAdapterAllPupuhListener{
                                            override fun onClick(result: AllPupuhModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllPupuhActivity, AllKategoriPupuhActivity::class.java)
                                                bundle.putInt("id_pupuh", result.id_post)
                                                bundle.putString("nama_pupuh", result.nama_post)
                                                bundle.putString("desc_pupuh", result.deskripsi)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noPupuh.visibility   = View.VISIBLE
                                        allPupuh1.visibility = View.GONE
                                        allPupuh2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noPupuh.visibility   = View.GONE
                                        allPupuh2.visibility = View.VISIBLE
                                        allPupuh2.adapter    = pupuhAdapter
                                        allPupuh1.visibility = View.INVISIBLE
                                    }else{
                                        allPupuh1.visibility = View.VISIBLE
                                        allPupuh2.visibility = View.GONE
                                        noPupuh.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllPupuhModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerPupuh.stopShimmer()
        shimmerPupuh.visibility = View.GONE
        swipePupuh.visibility   = View.VISIBLE
    }
}