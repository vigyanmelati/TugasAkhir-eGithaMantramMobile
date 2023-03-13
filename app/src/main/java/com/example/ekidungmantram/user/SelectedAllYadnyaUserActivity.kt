package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.SelectedAllYadnyaUserAdapter
import com.example.ekidungmantram.adapter.admin.SelectedAllYadnyaAdminAdapter
import com.example.ekidungmantram.admin.upacarayadnya.AddYadnyaAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.DetailYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_selected_all_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_selected_all_yadnya_admin.namaDaftarYadnya
import kotlinx.android.synthetic.main.activity_selected_all_yadnya_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectedAllYadnyaUserActivity : AppCompatActivity() {
    private lateinit var yadnyaAdapter : SelectedAllYadnyaUserAdapter
    private lateinit var setAdapter    : SelectedAllYadnyaUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_all_yadnya_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_yadnya")
            val katID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaDaftarYadnya.text = "Daftar " + namaPost

            allYadnyaUser1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaUser2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnya(postID)

            swipeYadnyaUser.setOnRefreshListener {
                getAllDataYadnya(postID)
                swipeYadnyaUser.isRefreshing = false
            }

        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllDataYadnya(postID: Int) {
        ApiService.endpoint.getAllListYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllYadnyaAdminModel>>,
                    response: Response<ArrayList<AllYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaUser.visibility   = View.VISIBLE
                        shimmerYadnyaUser.visibility = View.GONE
                        noYadnyaUser.visibility      = View.GONE
                    }else{
                        swipeYadnyaUser.visibility   = View.GONE
                        shimmerYadnyaUser.visibility = View.VISIBLE
                        noYadnyaUser.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { SelectedAllYadnyaUserAdapter(it,
                        object : SelectedAllYadnyaUserAdapter.OnAdapterAllYadnyaAdminListener{
                            override fun onClick(result: AllYadnyaAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@SelectedAllYadnyaUserActivity, DetailYadnyaActivity::class.java)
                                bundle.putInt("id_yadnya", result.id_post)
                                bundle.putInt("id_kategori", postID)
                                bundle.putString("nama_yadnya", result.nama_post)
                                bundle.putString("nama_tag_dhar", "selectYadnya")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allYadnyaUser1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaUser.visibility   = View.GONE
                                    allYadnyaUser1.visibility = View.VISIBLE
                                    allYadnyaUser2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    yadnyaAdapter = SelectedAllYadnyaUserAdapter(filter as ArrayList<AllYadnyaAdminModel>,
                                        object : SelectedAllYadnyaUserAdapter.OnAdapterAllYadnyaAdminListener{
                                            override fun onClick(result: AllYadnyaAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@SelectedAllYadnyaUserActivity, DetailYadnyaActivity::class.java)
                                                bundle.putInt("id_yadnya", result.id_post)
                                                bundle.putInt("id_kategori", postID)
                                                bundle.putString("nama_yadnya", result.nama_post)
                                                bundle.putString("nama_tag_dhar", "selectYadnya")
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noYadnyaUser.visibility   = View.VISIBLE
                                        allYadnyaUser1.visibility = View.GONE
                                        allYadnyaUser2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaUser.visibility   = View.GONE
                                        allYadnyaUser2.visibility = View.VISIBLE
                                        allYadnyaUser2.adapter    = yadnyaAdapter
                                        allYadnyaUser1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaUser1.visibility = View.VISIBLE
                                        allYadnyaUser2.visibility = View.GONE
                                        noYadnyaUser.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerYadnyaUser.stopShimmer()
        shimmerYadnyaUser.visibility = View.GONE
        swipeYadnyaUser.visibility   = View.VISIBLE
    }
}