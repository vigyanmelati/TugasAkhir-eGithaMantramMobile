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
import com.example.ekidungmantram.adapter.admin.AllYadnyaonKakawinAdminAdapter
import com.example.ekidungmantram.admin.kakawin.AddYadnyaToKakawinAdminActivity
import com.example.ekidungmantram.admin.kakawin.DetailKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaKakawinAdminModel
import kotlinx.android.synthetic.main.activity_all_yadnyaon_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_all_yadnyaon_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllYadnyaonKakawinAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaonKakawinAdminAdapter
    private lateinit var setAdapter   : AllYadnyaonKakawinAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_yadnyaon_kakawin_admin)
        supportActionBar!!.title = "Daftar Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin_admin")
            val namaPost = bundle.getString("nama_kakawin_admin")

            namaKakawinYadnya.text = namaPost
            allKakawinYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allKakawinYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaKakawin(postID, namaPost!!)

            swipeKakawinYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaKakawin(postID, namaPost!!)
                swipeKakawinYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaAddKakawin.setOnClickListener {
                val intent = Intent(this, AddYadnyaToKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin_admin", postID)
                bundle.putString("nama_kakawin_admin", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaKakawin(postID: Int, name: String) {
        ApiService.endpoint.getYadnyaKakawinAdmin(postID)
            .enqueue(object: Callback<YadnyaKakawinAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaKakawinAdminModel>,
                    response: Response<YadnyaKakawinAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeKakawinYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerKakawinYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeKakawinYadnyaAdmin.visibility   = View.GONE
                        shimmerKakawinYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaonKakawinAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllYadnyaonKakawinAdminActivity)
                        builder.setTitle("Hapus Yadnya dari Sekar Agung")
                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar agung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhGamelan(it.id, postID, name)
                                Log.d("id_detail_post", it.id.toString())
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allKakawinYadnyaAdmin1.adapter  = setAdapter
                    noKakawinYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariKakawinYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKakawinYadnyaAdmin.visibility   = View.GONE
                                    allKakawinYadnyaAdmin1.visibility = View.VISIBLE
                                    allKakawinYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaonKakawinAdminAdapter(filter as ArrayList<YadnyaKakawinAdminModel.DataL>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllYadnyaonKakawinAdminActivity)
                                        builder.setTitle("Hapus Yadnya dari Sekar Agung")
                                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar agung ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhGamelan(it.id, postID, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noKakawinYadnyaAdmin.visibility   = View.VISIBLE
                                        allKakawinYadnyaAdmin1.visibility = View.GONE
                                        allKakawinYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKakawinYadnyaAdmin.visibility   = View.GONE
                                        allKakawinYadnyaAdmin2.visibility = View.VISIBLE
                                        allKakawinYadnyaAdmin2.adapter    = tabuhAdapter
                                        allKakawinYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allKakawinYadnyaAdmin1.visibility = View.VISIBLE
                                        allKakawinYadnyaAdmin2.visibility = View.GONE
                                        noKakawinYadnyaAdmin.visibility   = View.GONE
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

    private fun hapusTabuhGamelan(id: Int, postID: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataYadnyaOnKakawinAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaonKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllYadnyaonKakawinAdminActivity, DetailKakawinAdminActivity::class.java)
                    bundle.putInt("id_kakawin_admin", postID)
                    bundle.putString("nama_kakawin_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaonKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllYadnyaonKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerKakawinYadnyaAdmin.stopShimmer()
        shimmerKakawinYadnyaAdmin.visibility = View.GONE
        swipeKakawinYadnyaAdmin.visibility   = View.VISIBLE
    }
}