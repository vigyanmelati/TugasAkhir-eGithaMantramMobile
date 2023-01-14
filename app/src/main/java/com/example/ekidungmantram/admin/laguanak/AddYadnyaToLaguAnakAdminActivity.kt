package com.example.ekidungmantram.admin.laguanak

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
import com.example.ekidungmantram.adapter.admin.AllYadnyaNotOnLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaNotOnPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AllYadnyaOnPupuhAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AddYadnyaAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AllYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel
import kotlinx.android.synthetic.main.activity_add_yadnya_to_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_add_yadnya_to_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddYadnyaToLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaNotOnLaguAnakAdminAdapter
    private lateinit var setAdapter   : AllYadnyaNotOnLaguAnakAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_yadnya_to_lagu_anak_admin)
        supportActionBar!!.title = "Daftar Semua Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak_admin")
            val namaPost = bundle.getString("nama_lagu_anak_admin")
            Log.d("id_lagu_anak_add", postID.toString())

            allAddLaguAnakYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddLaguAnakYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaLaguAnak(postID, namaPost!!)

            swipeLaguAnakAddYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaLaguAnak(postID, namaPost!!)
                swipeLaguAnakAddYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaLaguAnakAdmin.setOnClickListener {
//                val bundle = Bundle()
                val intent = Intent(this, AllYadnyaAdminActivity::class.java)
//                bundle.putInt("id_kat_pupuh_admin", postID)
//                bundle.putString("nama_kat_pupuh_admin", namaPost)
//                bundle.putString("desc_kat_pupuh_admin", descPost)
//                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaLaguAnak(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllYadnyaNotOnLaguAnakAdmin(postID)
            .enqueue(object: Callback<YadnyaLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaLaguAnakAdminModel>,
                    response: Response<YadnyaLaguAnakAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeLaguAnakAddYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerLaguAnakAddYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeLaguAnakAddYadnyaAdmin.visibility   = View.GONE
                        shimmerLaguAnakAddYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaNotOnLaguAnakAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddYadnyaToLaguAnakAdminActivity)
                        builder.setTitle("Tambah Yadnya")
                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhGamelan(postID, it.id_post, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddLaguAnakYadnyaAdmin1.adapter  = setAdapter
                    noLaguAnakAddYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariLaguAnakAddYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noLaguAnakAddYadnyaAdmin.visibility   = View.GONE
                                    allAddLaguAnakYadnyaAdmin1.visibility = View.VISIBLE
                                    allAddLaguAnakYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaNotOnLaguAnakAdminAdapter(filter as ArrayList<YadnyaLaguAnakAdminModel.DataL>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddYadnyaToLaguAnakAdminActivity)
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
                                        noLaguAnakAddYadnyaAdmin.visibility   = View.VISIBLE
                                        allAddLaguAnakYadnyaAdmin1.visibility = View.GONE
                                        allAddLaguAnakYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noLaguAnakAddYadnyaAdmin.visibility   = View.GONE
                                        allAddLaguAnakYadnyaAdmin2.visibility = View.VISIBLE
                                        allAddLaguAnakYadnyaAdmin2.adapter    = tabuhAdapter
                                        allAddLaguAnakYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddLaguAnakYadnyaAdmin1.visibility = View.VISIBLE
                                        allAddLaguAnakYadnyaAdmin2.visibility = View.GONE
                                        noLaguAnakAddYadnyaAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<YadnyaLaguAnakAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTabuhGamelan(postID: Int, idLagu: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataYadnyaToLaguAnakAdmin(postID, idLagu).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddYadnyaToLaguAnakAdminActivity, AllYadnyaOnLaguAnakAdminActivity::class.java)
                    bundle.putInt("id_lagu_anak_admin", postID)
                    bundle.putString("nama_lagu_anak_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddYadnyaToLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLaguAnakAddYadnyaAdmin.stopShimmer()
        shimmerLaguAnakAddYadnyaAdmin.visibility = View.GONE
        swipeLaguAnakAddYadnyaAdmin.visibility   = View.VISIBLE
    }
}