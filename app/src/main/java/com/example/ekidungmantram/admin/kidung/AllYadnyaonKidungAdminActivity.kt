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
import com.example.ekidungmantram.adapter.admin.AllYadnyaonKidungAdminAdapter
import com.example.ekidungmantram.admin.kidung.AddYadnyaToKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaKidungAdminModel
import kotlinx.android.synthetic.main.activity_all_yadnyaon_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllYadnyaonKidungAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaonKidungAdminAdapter
    private lateinit var setAdapter   : AllYadnyaonKidungAdminAdapter
    private var id_kidung : Int = 0
    private lateinit var nama_kidung :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_yadnyaon_kidung_admin)
        supportActionBar!!.title = "Daftar Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung_admin")
            val namaPost = bundle.getString("nama_kidung_admin")

            id_kidung = postID
            nama_kidung = namaPost.toString()
            tag_user = bundle.getString("tag_user_kidung").toString()

            namaKidungYadnya.text = namaPost
            allKidungYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allKidungYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaKidung(postID, namaPost!!)

            swipeKidungYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaKidung(postID, namaPost!!)
                swipeKidungYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaAddKidung.setOnClickListener {
                val intent = Intent(this, AddYadnyaToKidungAdminActivity::class.java)
                bundle.putInt("id_kidung_admin", postID)
                bundle.putString("nama_kidung_admin", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaKidung(postID: Int, name: String) {
        ApiService.endpoint.getYadnyaKidungAdmin(postID)
            .enqueue(object: Callback<YadnyaKidungAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaKidungAdminModel>,
                    response: Response<YadnyaKidungAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeKidungYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerKidungYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeKidungYadnyaAdmin.visibility   = View.GONE
                        shimmerKidungYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaonKidungAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllYadnyaonKidungAdminActivity)
                        builder.setTitle("Hapus Yadnya dari Sekar Madya")
                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar madya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhGamelan(it.id, postID, name)
                                Log.d("id_detail_post", it.id.toString())
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allKidungYadnyaAdmin1.adapter  = setAdapter
                    noKidungYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariKidungYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKidungYadnyaAdmin.visibility   = View.GONE
                                    allKidungYadnyaAdmin1.visibility = View.VISIBLE
                                    allKidungYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaonKidungAdminAdapter(filter as ArrayList<YadnyaKidungAdminModel.DataL>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllYadnyaonKidungAdminActivity)
                                        builder.setTitle("Hapus Yadnya dari Sekar Madya")
                                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar rare ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhGamelan(it.id, postID, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noKidungYadnyaAdmin.visibility   = View.VISIBLE
                                        allKidungYadnyaAdmin1.visibility = View.GONE
                                        allKidungYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKidungYadnyaAdmin.visibility   = View.GONE
                                        allKidungYadnyaAdmin2.visibility = View.VISIBLE
                                        allKidungYadnyaAdmin2.adapter    = tabuhAdapter
                                        allKidungYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allKidungYadnyaAdmin1.visibility = View.VISIBLE
                                        allKidungYadnyaAdmin2.visibility = View.GONE
                                        noKidungYadnyaAdmin.visibility   = View.GONE
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

    private fun hapusTabuhGamelan(id: Int, postID: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataYadnyaOnKidungAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaonKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllYadnyaonKidungAdminActivity, AllYadnyaonKidungAdminActivity::class.java)
                    bundle.putInt("id_kidung_admin", postID)
                    bundle.putString("nama_kidung_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaonKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllYadnyaonKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerKidungYadnyaAdmin.stopShimmer()
        shimmerKidungYadnyaAdmin.visibility = View.GONE
        swipeKidungYadnyaAdmin.visibility   = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val bundle = Bundle()
        val intent = Intent(this, DetailKidungAdminActivity::class.java)
        bundle.putInt("id_kidung", id_kidung)
        bundle.putString("nama_kidung", nama_kidung)
        bundle.putString("tag_user_kidung", tag_user)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}