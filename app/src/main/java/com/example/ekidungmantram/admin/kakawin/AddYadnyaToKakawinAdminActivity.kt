package com.example.ekidungmantram.admin.kakawin

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
import com.example.ekidungmantram.adapter.admin.AllYadnyaNotOnKakawinAdminAdapter
import com.example.ekidungmantram.admin.kakawin.AllYadnyaonKakawinAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AddYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaKakawinAdminModel
import kotlinx.android.synthetic.main.activity_add_yadnya_to_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddYadnyaToKakawinAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaNotOnKakawinAdminAdapter
    private lateinit var setAdapter   : AllYadnyaNotOnKakawinAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_yadnya_to_kakawin_admin)
        supportActionBar!!.title = "Daftar Semua Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin_admin")
            val namaPost = bundle.getString("nama_kakawin_admin")
            Log.d("id_kakawin_add", postID.toString())

            allAddKakawinYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddKakawinYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaKakawin(postID, namaPost!!)

            swipeKakawinAddYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaKakawin(postID, namaPost!!)
                swipeKakawinAddYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaKakawinAdmin.setOnClickListener {
//                val bundle = Bundle()
                val intent = Intent(this, AddYadnyaAdminActivity::class.java)
//                bundle.putInt("id_kat_kakawin_admin", postID)
//                bundle.putString("nama_kat_kakawin_admin", namaPost)
//                bundle.putString("desc_kat_kakawin_admin", descPost)
//                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaKakawin(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllYadnyaNotOnKakawinAdmin(postID)
            .enqueue(object: Callback<YadnyaKakawinAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaKakawinAdminModel>,
                    response: Response<YadnyaKakawinAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeKakawinAddYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerKakawinAddYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeKakawinAddYadnyaAdmin.visibility   = View.GONE
                        shimmerKakawinAddYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaNotOnKakawinAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddYadnyaToKakawinAdminActivity)
                        builder.setTitle("Tambah Yadnya")
                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhGamelan(postID, it.id_post, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddKakawinYadnyaAdmin1.adapter  = setAdapter
                    noKakawinAddYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariKakawinAddYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKakawinAddYadnyaAdmin.visibility   = View.GONE
                                    allAddKakawinYadnyaAdmin1.visibility = View.VISIBLE
                                    allAddKakawinYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaNotOnKakawinAdminAdapter(filter as ArrayList<YadnyaKakawinAdminModel.DataL>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddYadnyaToKakawinAdminActivity)
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
                                        noKakawinAddYadnyaAdmin.visibility   = View.VISIBLE
                                        allAddKakawinYadnyaAdmin1.visibility = View.GONE
                                        allAddKakawinYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKakawinAddYadnyaAdmin.visibility   = View.GONE
                                        allAddKakawinYadnyaAdmin2.visibility = View.VISIBLE
                                        allAddKakawinYadnyaAdmin2.adapter    = tabuhAdapter
                                        allAddKakawinYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddKakawinYadnyaAdmin1.visibility = View.VISIBLE
                                        allAddKakawinYadnyaAdmin2.visibility = View.GONE
                                        noKakawinAddYadnyaAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<YadnyaKakawinAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTabuhGamelan(postID: Int, idKakawin: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataYadnyaToKakawinAdmin(postID, idKakawin).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddYadnyaToKakawinAdminActivity, AllYadnyaonKakawinAdminActivity::class.java)
                    bundle.putInt("id_kakawin_admin", postID)
                    bundle.putString("nama_kakawin_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddYadnyaToKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerKakawinAddYadnyaAdmin.stopShimmer()
        shimmerKakawinAddYadnyaAdmin.visibility = View.GONE
        swipeKakawinAddYadnyaAdmin.visibility   = View.VISIBLE
    }
}