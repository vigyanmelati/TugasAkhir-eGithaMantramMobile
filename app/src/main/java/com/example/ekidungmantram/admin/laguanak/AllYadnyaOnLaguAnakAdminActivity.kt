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
import com.example.ekidungmantram.adapter.admin.AllYadnyaOnLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaOnPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddYadnyaToPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.DetailPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_yadnya_on_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_all_yadnya_on_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllYadnyaOnLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaOnLaguAnakAdminAdapter
    private lateinit var setAdapter   : AllYadnyaOnLaguAnakAdminAdapter
    private var id_lagu_anak : Int = 0
    private var id_lagu_anak_admin : Int = 0
    private lateinit var nama_lagu_anak :String
    private lateinit var nama_lagu_anak_admin :String
    private lateinit var desc_lagu_anak_admin :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_yadnya_on_lagu_anak_admin)
        supportActionBar!!.title = "Daftar Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak_admin")
            val namaPost = bundle.getString("nama_lagu_anak_admin")

            id_lagu_anak = postID
            nama_lagu_anak = namaPost.toString()
            id_lagu_anak_admin = bundle.getInt("id_lagu_anak_kat")
            nama_lagu_anak_admin = bundle.getString("nama_lagu_anak_kat").toString()
            desc_lagu_anak_admin = bundle.getString("desc_lagu_anak_kat").toString()
            tag_user = bundle.getString("tag_user_anak").toString()

            namaLaguAnakYadnya.text = namaPost
            allLaguAnakYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allLaguAnakYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaLaguAnak(postID, namaPost!!)

            swipeLaguAnakYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaLaguAnak(postID, namaPost!!)
                swipeLaguAnakYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaAddLaguAnak.setOnClickListener {
                val intent = Intent(this, AddYadnyaToLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak_admin", postID)
                bundle.putString("nama_lagu_anak_admin", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaLaguAnak(postID: Int, name: String) {
        ApiService.endpoint.getYadnyaLaguAnakAdmin(postID)
            .enqueue(object: Callback<YadnyaLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaLaguAnakAdminModel>,
                    response: Response<YadnyaLaguAnakAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeLaguAnakYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerLaguAnakYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeLaguAnakYadnyaAdmin.visibility   = View.GONE
                        shimmerLaguAnakYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaOnLaguAnakAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllYadnyaOnLaguAnakAdminActivity)
                        builder.setTitle("Hapus Yadnya dari Sekar Rare")
                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar rare ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhGamelan(it.id, postID, name)
                                Log.d("id_detail_post", it.id.toString())
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allLaguAnakYadnyaAdmin1.adapter  = setAdapter
                    noLaguAnakYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariLaguAnakYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noLaguAnakYadnyaAdmin.visibility   = View.GONE
                                    allLaguAnakYadnyaAdmin1.visibility = View.VISIBLE
                                    allLaguAnakYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaOnLaguAnakAdminAdapter(filter as ArrayList<YadnyaLaguAnakAdminModel.DataL>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllYadnyaOnLaguAnakAdminActivity)
                                        builder.setTitle("Hapus Yadnya dari Sekar Rare")
                                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar rare ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhGamelan(it.id, postID, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noLaguAnakYadnyaAdmin.visibility   = View.VISIBLE
                                        allLaguAnakYadnyaAdmin1.visibility = View.GONE
                                        allLaguAnakYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noLaguAnakYadnyaAdmin.visibility   = View.GONE
                                        allLaguAnakYadnyaAdmin2.visibility = View.VISIBLE
                                        allLaguAnakYadnyaAdmin2.adapter    = tabuhAdapter
                                        allLaguAnakYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allLaguAnakYadnyaAdmin1.visibility = View.VISIBLE
                                        allLaguAnakYadnyaAdmin2.visibility = View.GONE
                                        noLaguAnakYadnyaAdmin.visibility   = View.GONE
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

    private fun hapusTabuhGamelan(id: Int, postID: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataYadnyaOnLaguAnakAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaOnLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllYadnyaOnLaguAnakAdminActivity, DetailLaguAnakAdminActivity::class.java)
                    bundle.putInt("id_lagu_anak_admin", postID)
                    bundle.putString("nama_lagu_anak_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaOnLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllYadnyaOnLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLaguAnakYadnyaAdmin.stopShimmer()
        shimmerLaguAnakYadnyaAdmin.visibility = View.GONE
        swipeLaguAnakYadnyaAdmin.visibility   = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val bundle = Bundle()
        val intent = Intent(this, DetailLaguAnakAdminActivity::class.java)
        bundle.putInt("id_lagu_anak_admin", id_lagu_anak)
        bundle.putInt("id_lagu_anak_admin_kat", id_lagu_anak_admin)
        bundle.putString("nama_lagu_anak_admin", nama_lagu_anak)
        bundle.putString("nama_lagu_anak_admin_kat", nama_lagu_anak_admin)
        bundle.putString("desc_lagu_anak_admin_kat", desc_lagu_anak_admin)
        bundle.putString("tag_user_anak", tag_user)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}