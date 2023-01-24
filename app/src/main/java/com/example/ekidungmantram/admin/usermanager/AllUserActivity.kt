package com.example.ekidungmantram.admin.usermanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDataAdminAdapter
import com.example.ekidungmantram.adapter.user.AllDataUserAdapter
import com.example.ekidungmantram.admin.adminmanager.DetailAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel
import kotlinx.android.synthetic.main.activity_all_admin.*
import kotlinx.android.synthetic.main.activity_all_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllUserActivity : AppCompatActivity() {
    private lateinit var adminAdapter  : AllDataUserAdapter
    private lateinit var setAdapter    : AllDataUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Kelola Pengguna"

        allDataUser1.layoutManager = LinearLayoutManager(applicationContext)
        allDataUser2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataUser()

        swipeUserManager.setOnRefreshListener {
            getAllDataUser()
            swipeUserManager.isRefreshing = false
        }

//        fabAdmin.setOnClickListener {
//            val intent = Intent(this, AddAdminActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllDataUser() {
        ApiService.endpoint.getAllListUser()
            .enqueue(object: Callback<ArrayList<AllDataAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDataAdminModel>>,
                    response: Response<ArrayList<AllDataAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeUserManager.visibility   = View.VISIBLE
                        shimmerUserManager.visibility = View.GONE
                    }else{
                        swipeUserManager.visibility   = View.GONE
                        shimmerUserManager.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllDataUserAdapter(it,
                        object : AllDataUserAdapter.OnAdapterAllDataUserListener{
                            override fun onClick(result: AllDataAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllUserActivity, DetailUserActivity::class.java)
                                bundle.putInt("id_user", result.id_user)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!
                    allDataUser1.adapter  = setAdapter
                    noUserData.visibility = View.GONE
                    setShimmerToStop()

                    cariDataUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noUserData.visibility   = View.GONE
                                    allDataUser1.visibility = View.VISIBLE
                                    allDataUser2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.name.contains("$p0", true) }
                                    adminAdapter = AllDataUserAdapter(filter as ArrayList<AllDataAdminModel>,
                                        object : AllDataUserAdapter.OnAdapterAllDataUserListener{
                                            override fun onClick(result: AllDataAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllUserActivity, DetailUserActivity::class.java)
                                                bundle.putInt("id_user", result.id_user)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noUserData.visibility   = View.VISIBLE
                                        allDataUser1.visibility = View.GONE
                                        allDataUser2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noUserData.visibility   = View.GONE
                                        allDataUser2.visibility = View.VISIBLE
                                        allDataUser2.adapter    = adminAdapter
                                        allDataUser1.visibility = View.INVISIBLE
                                    }else{
                                        allDataUser1.visibility = View.VISIBLE
                                        allDataUser2.visibility = View.GONE
                                        noUserData.visibility   = View.GONE
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
        shimmerUserManager.stopShimmer()
        shimmerUserManager.visibility = View.GONE
        swipeUserManager.visibility   = View.VISIBLE
    }
}