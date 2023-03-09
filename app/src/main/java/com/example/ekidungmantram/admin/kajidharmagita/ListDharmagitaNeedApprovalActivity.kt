package com.example.ekidungmantram.admin.kajidharmagita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDharmagitaNeedApprovalAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllMantramNeedApprovalAdminAdapter
import com.example.ekidungmantram.admin.kajimantram.DetailMantramNeedApprovalActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllDharmagitaApprovalModel
import com.example.ekidungmantram.model.adminmodel.AllDharmagitaAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import kotlinx.android.synthetic.main.activity_list_dharmagita_need_approval.*
import kotlinx.android.synthetic.main.activity_list_mantram_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListDharmagitaNeedApprovalActivity : AppCompatActivity() {
    private lateinit var mantramAdapter  : AllDharmagitaNeedApprovalAdminAdapter
    private lateinit var setAdapter      : AllDharmagitaNeedApprovalAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_dharmagita_need_approval)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Dharmagita"

        allGitaNAAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allGitaNAAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDharmagitaNAAdmin()

        swipeGitaNAAdmin.setOnRefreshListener {
            getAllDharmagitaNAAdmin()
            swipeGitaNAAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllDharmagitaNAAdmin() {
        ApiService.endpoint.getAllNotApprovedDharmagitaListAdmin()
            .enqueue(object: Callback<ArrayList<AllDharmagitaApprovalModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDharmagitaApprovalModel>>,
                    response: Response<ArrayList<AllDharmagitaApprovalModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeGitaNAAdmin.visibility   = View.VISIBLE
                        shimmerGitaNAAdmin.visibility = View.GONE
                        noGitaNAAdmin.visibility      = View.GONE
                    }else{
                        swipeGitaNAAdmin.visibility   = View.GONE
                        shimmerGitaNAAdmin.visibility = View.VISIBLE
                        noGitaNAAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllDharmagitaNeedApprovalAdminAdapter(it,
                        object : AllDharmagitaNeedApprovalAdminAdapter.OnAdapterAllDharmagitaNAAdminListener{
                            override fun onClick(result: AllDharmagitaApprovalModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@ListDharmagitaNeedApprovalActivity, DetailDharmagitaNeedApprovalActivity::class.java)
                                bundle.putInt("id_dharmagita", result.id_post)
                                bundle.putInt("id_tag_dharmagita", result.id_tag)
                                bundle.putString("tag_dharmagita", result.nama_tag)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allGitaNAAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariGitaNAAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noGitaNAAdmin.visibility   = View.GONE
                                    allGitaNAAdmin1.visibility = View.VISIBLE
                                    allGitaNAAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllDharmagitaNeedApprovalAdminAdapter(filter as ArrayList<AllDharmagitaApprovalModel>,
                                        object : AllDharmagitaNeedApprovalAdminAdapter.OnAdapterAllDharmagitaNAAdminListener{
                                            override fun onClick(result: AllDharmagitaApprovalModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@ListDharmagitaNeedApprovalActivity, DetailDharmagitaNeedApprovalActivity::class.java)
                                                bundle.putInt("id_dharmagita", result.id_post)
                                                bundle.putInt("id_tag_dharmagita", result.id_tag)
                                                bundle.putString("tag_dharmagita", result.nama_tag)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noGitaNAAdmin.visibility   = View.VISIBLE
                                        allGitaNAAdmin1.visibility = View.GONE
                                        allGitaNAAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noGitaNAAdmin.visibility   = View.GONE
                                        allGitaNAAdmin2.visibility = View.VISIBLE
                                        allGitaNAAdmin2.adapter    = mantramAdapter
                                        allGitaNAAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allGitaNAAdmin1.visibility = View.VISIBLE
                                        allGitaNAAdmin2.visibility = View.GONE
                                        noGitaNAAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllDharmagitaApprovalModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerGitaNAAdmin.stopShimmer()
        shimmerGitaNAAdmin.visibility = View.GONE
        swipeGitaNAAdmin.visibility   = View.VISIBLE
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

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, ListAllDharmagitaNotApprovalActivity::class.java)
//        startActivity(intent)
//    }
}