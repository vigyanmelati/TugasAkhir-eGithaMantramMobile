package com.example.ekidungmantram.admin.kajidharmagita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDharmagitaNeedApprovalAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllVideoDharmagitaNeedApprovalAdminAdapter
import com.example.ekidungmantram.admin.kajimantram.DetailMantramNeedApprovalActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllDharmagitaApprovalModel
import com.example.ekidungmantram.model.AllVideoApprovalModel
import kotlinx.android.synthetic.main.activity_list_dharmagita_need_approval.*
import kotlinx.android.synthetic.main.activity_list_video_dharmagita_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListVideoDharmagitaNeedApprovalActivity : AppCompatActivity() {
    private lateinit var mantramAdapter  : AllVideoDharmagitaNeedApprovalAdminAdapter
    private lateinit var setAdapter      : AllVideoDharmagitaNeedApprovalAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_video_dharmagita_need_approval)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Dharmagita"

        allVideoNAAdmin.layoutManager = LinearLayoutManager(applicationContext)
        allVideoNAAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllVideoDharmagitaNAAdmin()

        swipeVideoNAAdmin.setOnRefreshListener {
            getAllVideoDharmagitaNAAdmin()
            swipeVideoNAAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllVideoDharmagitaNAAdmin() {
        ApiService.endpoint.getAllNotApprovedVideoDharmagitaListAdmin()
            .enqueue(object: Callback<ArrayList<AllVideoApprovalModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllVideoApprovalModel>>,
                    response: Response<ArrayList<AllVideoApprovalModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeVideoNAAdmin.visibility   = View.VISIBLE
                        shimmerVideoNAAdmin.visibility = View.GONE
                        noVideoNAAdmin.visibility      = View.GONE
                    }else{
                        swipeVideoNAAdmin.visibility   = View.GONE
                        shimmerVideoNAAdmin.visibility = View.VISIBLE
                        noVideoNAAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllVideoDharmagitaNeedApprovalAdminAdapter(it,
                        object : AllVideoDharmagitaNeedApprovalAdminAdapter.OnAdapterAllVideoDharmagitaNAAdminListener{
                            override fun onClick(result: AllVideoApprovalModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@ListVideoDharmagitaNeedApprovalActivity, DetailVideoDharmagitaNeedApprovalActivity::class.java)
                                bundle.putInt("id_video_dharmagita", result.id_video)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allVideoNAAdmin.adapter  = setAdapter
                    setShimmerToStop()

                    cariVideoNAAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noVideoNAAdmin.visibility   = View.GONE
                                    allVideoNAAdmin.visibility = View.VISIBLE
                                    allVideoNAAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllVideoDharmagitaNeedApprovalAdminAdapter(filter as ArrayList<AllVideoApprovalModel>,
                                        object : AllVideoDharmagitaNeedApprovalAdminAdapter.OnAdapterAllVideoDharmagitaNAAdminListener{
                                            override fun onClick(result: AllVideoApprovalModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@ListVideoDharmagitaNeedApprovalActivity, DetailVideoDharmagitaNeedApprovalActivity::class.java)
                                                bundle.putInt("id_video_dharmagita", result.id_video)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noVideoNAAdmin.visibility   = View.VISIBLE
                                        allVideoNAAdmin.visibility = View.GONE
                                        allVideoNAAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noVideoNAAdmin.visibility   = View.GONE
                                        allVideoNAAdmin2.visibility = View.VISIBLE
                                        allVideoNAAdmin2.adapter    = mantramAdapter
                                        allVideoNAAdmin.visibility = View.INVISIBLE
                                    }else{
                                        allVideoNAAdmin.visibility = View.VISIBLE
                                        allVideoNAAdmin2.visibility = View.GONE
                                        noVideoNAAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllVideoApprovalModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerVideoNAAdmin.stopShimmer()
        shimmerVideoNAAdmin.visibility = View.GONE
        swipeVideoNAAdmin.visibility   = View.VISIBLE
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
        val intent = Intent(this, ListAllDharmagitaNotApprovalActivity::class.java)
        startActivity(intent)
    }
}