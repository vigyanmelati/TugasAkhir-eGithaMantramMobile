package com.example.ekidungmantram.admin.kidung

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
import com.example.ekidungmantram.adapter.admin.AllYadnyaNotOnKidungAdminAdapter
import com.example.ekidungmantram.admin.kidung.AllYadnyaonKidungAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AddYadnyaAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AllYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaKidungAdminModel
import kotlinx.android.synthetic.main.activity_add_yadnya_to_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddYadnyaToKidungAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaNotOnKidungAdminAdapter
    private lateinit var setAdapter   : AllYadnyaNotOnKidungAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_yadnya_to_kidung_admin)
        supportActionBar!!.title = "Daftar Semua Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung_admin")
            val namaPost = bundle.getString("nama_kidung_admin")
            Log.d("id_kidung_add", postID.toString())

            allAddKidungYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddKidungYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaKidung(postID, namaPost!!)

            swipeKidungAddYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaKidung(postID, namaPost!!)
                swipeKidungAddYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaKidungAdmin.setOnClickListener {
//                val bundle = Bundle()
                val intent = Intent(this, AllYadnyaAdminActivity::class.java)
//                bundle.putInt("id_kat_kidung_admin", postID)
//                bundle.putString("nama_kat_kidung_admin", namaPost)
//                bundle.putString("desc_kat_kidung_admin", descPost)
//                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaKidung(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllYadnyaNotOnKidungAdmin(postID)
            .enqueue(object: Callback<YadnyaKidungAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaKidungAdminModel>,
                    response: Response<YadnyaKidungAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeKidungAddYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerKidungAddYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeKidungAddYadnyaAdmin.visibility   = View.GONE
                        shimmerKidungAddYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaNotOnKidungAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddYadnyaToKidungAdminActivity)
                        builder.setTitle("Tambah Yadnya")
                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhGamelan(postID, it.id_post, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddKidungYadnyaAdmin1.adapter  = setAdapter
                    noKidungAddYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariKidungAddYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKidungAddYadnyaAdmin.visibility   = View.GONE
                                    allAddKidungYadnyaAdmin1.visibility = View.VISIBLE
                                    allAddKidungYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaNotOnKidungAdminAdapter(filter as ArrayList<YadnyaKidungAdminModel.DataL>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddYadnyaToKidungAdminActivity)
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
                                        noKidungAddYadnyaAdmin.visibility   = View.VISIBLE
                                        allAddKidungYadnyaAdmin1.visibility = View.GONE
                                        allAddKidungYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKidungAddYadnyaAdmin.visibility   = View.GONE
                                        allAddKidungYadnyaAdmin2.visibility = View.VISIBLE
                                        allAddKidungYadnyaAdmin2.adapter    = tabuhAdapter
                                        allAddKidungYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddKidungYadnyaAdmin1.visibility = View.VISIBLE
                                        allAddKidungYadnyaAdmin2.visibility = View.GONE
                                        noKidungAddYadnyaAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<YadnyaKidungAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTabuhGamelan(postID: Int, idKidung: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataYadnyaToKidungAdmin(postID, idKidung).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddYadnyaToKidungAdminActivity, AllYadnyaonKidungAdminActivity::class.java)
                    bundle.putInt("id_kidung_admin", postID)
                    bundle.putString("nama_kidung_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddYadnyaToKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerKidungAddYadnyaAdmin.stopShimmer()
        shimmerKidungAddYadnyaAdmin.visibility = View.GONE
        swipeKidungAddYadnyaAdmin.visibility   = View.VISIBLE
    }
}