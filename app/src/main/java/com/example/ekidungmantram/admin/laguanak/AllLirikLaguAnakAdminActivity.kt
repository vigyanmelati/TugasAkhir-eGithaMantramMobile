package com.example.ekidungmantram.admin.laguanak

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDataLirikLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllDataLirikPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddLirikPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllLirikPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.EditLirikPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllLirikLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.AllLirikPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_lirik_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLirikLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataLirikLaguAnakAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lirik_lagu_anak_admin)
        supportActionBar!!.title = "Lirik Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")

            namaLaguAnakLirik.text = namaPost
            allLirikLaguAnakAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataLirikLaguAnak(postID, namaPost!!)

            swipeLirikLaguAnakAdmin.setOnRefreshListener {
                getAllDataLirikLaguAnak(postID, namaPost)
                swipeLirikLaguAnakAdmin.isRefreshing = false
            }

            fabLirikLaguAnak.setOnClickListener {
                val intent = Intent(this, AddLirikLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataLirikLaguAnak(postID: Int, namaPost: String) {
        ApiService.endpoint.getAllLirikLaguAnakAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllLirikLaguAnakAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLirikLaguAnakAdminModel>>,
                    response: Response<ArrayList<AllLirikLaguAnakAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeLirikLaguAnakAdmin.visibility   = View.VISIBLE
                        shimmerLirikLaguAnakAdmin.visibility = View.GONE
                    }else{
                        swipeLirikLaguAnakAdmin.visibility   = View.GONE
                        shimmerLirikLaguAnakAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataLirikLaguAnakAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllLirikLaguAnakAdminActivity)
                        builder.setTitle("Hapus Lirik Sekar Rare")
                            .setMessage("Apakah anda yakin ingin menghapus lirik sekar rare ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusLirikLaguAnak(it.id_lirik_lagu, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllLirikLaguAnakAdminActivity, EditLirikLaguAnakAdminActivity::class.java)
                        bundle.putInt("id_lirik_lagu_anak", it.id_lirik_lagu)
                        Log.d("id_lagu_anak_all", it.id_lirik_lagu.toString())
                        bundle.putInt("id_lagu_anak", postID)
                        bundle.putString("nama_lagu_anak", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allLirikLaguAnakAdmin.adapter  = setAdapter
                    noLirikLaguAnakAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<ArrayList<AllLirikLaguAnakAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusLirikLaguAnak(idLirikLaguAnak: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataLirikLaguAnakAdmin(idLirikLaguAnak, postIDs).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllLirikLaguAnakAdminActivity, AllLirikLaguAnakAdminActivity::class.java)
                    bundle.putInt("id_lagu_anak", postIDs)
                    bundle.putString("nama_lagu_anak", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllLirikLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLirikLaguAnakAdmin.stopShimmer()
        shimmerLirikLaguAnakAdmin.visibility = View.GONE
        swipeLirikLaguAnakAdmin.visibility   = View.VISIBLE
    }
}