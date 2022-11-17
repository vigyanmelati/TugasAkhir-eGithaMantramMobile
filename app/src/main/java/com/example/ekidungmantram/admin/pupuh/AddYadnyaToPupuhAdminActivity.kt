package com.example.ekidungmantram.admin.pupuh

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllTabuhNotOnGamelanAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaNotOnPupuhAdminAdapter
import com.example.ekidungmantram.admin.gamelan.DetailGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel
import kotlinx.android.synthetic.main.activity_add_tabuh_to_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_add_yadnya_to_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddYadnyaToPupuhAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaNotOnPupuhAdminAdapter
    private lateinit var setAdapter   : AllYadnyaNotOnPupuhAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_yadnya_to_pupuh_admin)
        supportActionBar!!.title = "Daftar Semua Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_admin")
            val namaPost = bundle.getString("nama_pupuh_admin")
            Log.d("id_pupuh_add", postID.toString())

            allAddPupuhYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddPupuhYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaPupuh(postID, namaPost!!)

            swipePupuhAddYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaPupuh(postID, namaPost!!)
                swipePupuhAddYadnyaAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataYadnyaPupuh(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllYadnyaNotOnPupuhAdmin(postID)
            .enqueue(object: Callback<YadnyaPupuhAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaPupuhAdminModel>,
                    response: Response<YadnyaPupuhAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipePupuhAddYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerPupuhAddYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipePupuhAddYadnyaAdmin.visibility   = View.GONE
                        shimmerPupuhAddYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaNotOnPupuhAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddYadnyaToPupuhAdminActivity)
                        builder.setTitle("Tambah Yadnya")
                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhGamelan(postID, it.id_post, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddPupuhYadnyaAdmin1.adapter  = setAdapter
                    noPupuhAddYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariPupuhAddYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noPupuhAddYadnyaAdmin.visibility   = View.GONE
                                    allAddPupuhYadnyaAdmin1.visibility = View.VISIBLE
                                    allAddPupuhYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaNotOnPupuhAdminAdapter(filter as ArrayList<YadnyaPupuhAdminModel.DataL>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddYadnyaToPupuhAdminActivity)
                                        builder.setTitle("Tambah Yadnya")
                                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTabuhGamelan(postID, it.id_post, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noPupuhAddYadnyaAdmin.visibility   = View.VISIBLE
                                        allAddPupuhYadnyaAdmin1.visibility = View.GONE
                                        allAddPupuhYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noPupuhAddYadnyaAdmin.visibility   = View.GONE
                                        allAddPupuhYadnyaAdmin2.visibility = View.VISIBLE
                                        allAddPupuhYadnyaAdmin2.adapter    = tabuhAdapter
                                        allAddPupuhYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddPupuhYadnyaAdmin1.visibility = View.VISIBLE
                                        allAddPupuhYadnyaAdmin2.visibility = View.GONE
                                        noPupuhAddYadnyaAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<YadnyaPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTabuhGamelan(postID: Int, idPupuh: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataYadnyaToPupuhAdmin(postID, idPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddYadnyaToPupuhAdminActivity, AllYadnyaOnPupuhAdminActivity::class.java)
                    bundle.putInt("id_pupuh_admin", postID)
                    bundle.putString("nama_pupuh_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddYadnyaToPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerPupuhAddYadnyaAdmin.stopShimmer()
        shimmerPupuhAddYadnyaAdmin.visibility = View.GONE
        swipePupuhAddYadnyaAdmin.visibility   = View.VISIBLE
    }
}