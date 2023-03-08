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
import com.example.ekidungmantram.adapter.admin.AllAudioDharmagitaNeedApprovalAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllVideoDharmagitaNeedApprovalAdminAdapter
import com.example.ekidungmantram.admin.kajimantram.DetailMantramNeedApprovalActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllAudioApprovalModel
import com.example.ekidungmantram.model.AllVideoApprovalModel
import kotlinx.android.synthetic.main.activity_list_audio_dharmagita_need_approval.*
import kotlinx.android.synthetic.main.activity_list_video_dharmagita_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAudioDharmagitaNeedApprovalActivity : AppCompatActivity() {
    private lateinit var mantramAdapter  : AllAudioDharmagitaNeedApprovalAdminAdapter
    private lateinit var setAdapter      : AllAudioDharmagitaNeedApprovalAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_audio_dharmagita_need_approval)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Dharmagita"

        allAudioNAAdmin.layoutManager = LinearLayoutManager(applicationContext)
        allAudioNAAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllAudioDharmagitaNAAdmin()

        swipeAudioNAAdmin.setOnRefreshListener {
            getAllAudioDharmagitaNAAdmin()
            swipeAudioNAAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllAudioDharmagitaNAAdmin() {
        ApiService.endpoint.getAllNotApprovedAudioDharmagitaListAdmin()
            .enqueue(object: Callback<ArrayList<AllAudioApprovalModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllAudioApprovalModel>>,
                    response: Response<ArrayList<AllAudioApprovalModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeAudioNAAdmin.visibility   = View.VISIBLE
                        shimmerAudioNAAdmin.visibility = View.GONE
                        noAudioNAAdmin.visibility      = View.GONE
                    }else{
                        swipeAudioNAAdmin.visibility   = View.GONE
                        shimmerAudioNAAdmin.visibility = View.VISIBLE
                        noAudioNAAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllAudioDharmagitaNeedApprovalAdminAdapter(it,
                        object : AllAudioDharmagitaNeedApprovalAdminAdapter.OnAdapterAllAudioDharmagitaNAAdminListener{
                            override fun onClick(result: AllAudioApprovalModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@ListAudioDharmagitaNeedApprovalActivity, DetailAudioDharmagitaNeedApprovalActivity::class.java)
                                bundle.putInt("id_audio_dharmagita", result.id_audio)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allAudioNAAdmin.adapter  = setAdapter
                    setShimmerToStop()

                    cariAudioNAAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noAudioNAAdmin.visibility   = View.GONE
                                    allAudioNAAdmin.visibility = View.VISIBLE
                                    allAudioNAAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllAudioDharmagitaNeedApprovalAdminAdapter(filter as ArrayList<AllAudioApprovalModel>,
                                        object : AllAudioDharmagitaNeedApprovalAdminAdapter.OnAdapterAllAudioDharmagitaNAAdminListener{
                                            override fun onClick(result: AllAudioApprovalModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@ListAudioDharmagitaNeedApprovalActivity, DetailAudioDharmagitaNeedApprovalActivity::class.java)
                                                bundle.putInt("id_audio_dharmagita", result.id_audio)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noAudioNAAdmin.visibility   = View.VISIBLE
                                        allAudioNAAdmin.visibility = View.GONE
                                        allAudioNAAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noAudioNAAdmin.visibility   = View.GONE
                                        allAudioNAAdmin2.visibility = View.VISIBLE
                                        allAudioNAAdmin2.adapter    = mantramAdapter
                                        allAudioNAAdmin.visibility = View.INVISIBLE
                                    }else{
                                        allAudioNAAdmin.visibility = View.VISIBLE
                                        allAudioNAAdmin2.visibility = View.GONE
                                        noAudioNAAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllAudioApprovalModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerAudioNAAdmin.stopShimmer()
        shimmerAudioNAAdmin.visibility = View.GONE
        swipeAudioNAAdmin.visibility   = View.VISIBLE
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