package com.example.ekidungmantram.admin.kajiahlidharmagita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllAhliNeedApprovalAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllMantramNeedApprovalAdminAdapter
import com.example.ekidungmantram.admin.kajimantram.DetailMantramNeedApprovalActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import kotlinx.android.synthetic.main.activity_list_ahli_need_approval.*
import kotlinx.android.synthetic.main.activity_list_mantram_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAhliNeedApprovalActivity : AppCompatActivity() {
    private lateinit var ahliAdapter  : AllAhliNeedApprovalAdminAdapter
    private lateinit var setAdapter      : AllAhliNeedApprovalAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ahli_need_approval)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Dharmagita"

        allAhliNAAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allAhliNAAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllAhliNAAdmin()

        swipeAhliNAAdmin.setOnRefreshListener {
            getAllAhliNAAdmin()
            swipeAhliNAAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllAhliNAAdmin() {
        ApiService.endpoint.getAllNotApprovedAhliListAdmin()
            .enqueue(object: Callback<ArrayList<AllDataAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDataAdminModel>>,
                    response: Response<ArrayList<AllDataAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeAhliNAAdmin.visibility   = View.VISIBLE
                        shimmerAhliNAAdmin.visibility = View.GONE
                        noAhliNAAdmin.visibility      = View.GONE
                    }else{
                        swipeAhliNAAdmin.visibility   = View.GONE
                        shimmerAhliNAAdmin.visibility = View.VISIBLE
                        noAhliNAAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllAhliNeedApprovalAdminAdapter(it,
                        object : AllAhliNeedApprovalAdminAdapter.OnAdapterAllAhliNAAdminListener{
                            override fun onClick(result: AllDataAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@ListAhliNeedApprovalActivity, DetailAhliNeedApprovalActivity::class.java)
                                bundle.putInt("id_user", result.id_user)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allAhliNAAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariAhliNAAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noAhliNAAdmin.visibility   = View.GONE
                                    allAhliNAAdmin1.visibility = View.VISIBLE
                                    allAhliNAAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.name.contains("$p0", true) }
                                    ahliAdapter = AllAhliNeedApprovalAdminAdapter(filter as ArrayList<AllDataAdminModel>,
                                        object : AllAhliNeedApprovalAdminAdapter.OnAdapterAllAhliNAAdminListener{
                                            override fun onClick(result: AllDataAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@ListAhliNeedApprovalActivity, DetailMantramNeedApprovalActivity::class.java)
                                                bundle.putInt("id_user", result.id_user)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noAhliNAAdmin.visibility   = View.VISIBLE
                                        allAhliNAAdmin1.visibility = View.GONE
                                        allAhliNAAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noAhliNAAdmin.visibility   = View.GONE
                                        allAhliNAAdmin2.visibility = View.VISIBLE
                                        allAhliNAAdmin2.adapter    = ahliAdapter
                                        allAhliNAAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAhliNAAdmin1.visibility = View.VISIBLE
                                        allAhliNAAdmin2.visibility = View.GONE
                                        noAhliNAAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllDataAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerAhliNAAdmin.stopShimmer()
        shimmerAhliNAAdmin.visibility = View.GONE
        swipeAhliNAAdmin.visibility   = View.VISIBLE
    }
}