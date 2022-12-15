package com.example.ekidungmantram.admin.kakawin

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
import com.example.ekidungmantram.adapter.admin.AllDataLirikKakawinAdminAdapter
import com.example.ekidungmantram.admin.kakawin.AllLirikKakawinAdminActivity
import com.example.ekidungmantram.admin.pupuh.AddLirikPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.EditLirikPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllLirikKakawinAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_lirik_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLirikKakawinAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataLirikKakawinAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lirik_kakawin_admin)
        supportActionBar!!.title = "Lirik Sekar Agung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")

            namaKakawinLirik.text = namaPost
            allLirikKakawinAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataLirikKakawin(postID, namaPost!!)

            swipeLirikKakawinAdmin.setOnRefreshListener {
                getAllDataLirikKakawin(postID, namaPost)
                swipeLirikKakawinAdmin.isRefreshing = false
            }

            fabLirikKakawin.setOnClickListener {
                val intent = Intent(this, AddLirikPupuhAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataLirikKakawin(postID: Int, namaPost: String) {
        ApiService.endpoint.getAllLirikKakawinAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllLirikKakawinAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLirikKakawinAdminModel>>,
                    response: Response<ArrayList<AllLirikKakawinAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeLirikKakawinAdmin.visibility   = View.VISIBLE
                        shimmerLirikKakawinAdmin.visibility = View.GONE
                    }else{
                        swipeLirikKakawinAdmin.visibility   = View.GONE
                        shimmerLirikKakawinAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataLirikKakawinAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllLirikKakawinAdminActivity)
                        builder.setTitle("Hapus Lirik Kakawin")
                            .setMessage("Apakah anda yakin ingin menghapus lirik kakawin ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusLirikKakawin(it.id_lirik_sekar_agung, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllLirikKakawinAdminActivity, EditLirikPupuhAdminActivity::class.java)
                        bundle.putInt("id_lirik_kakawin", it.id_lirik_sekar_agung)
                        bundle.putInt("id_kakawin", postID)
                        bundle.putString("nama_kakawin", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allLirikKakawinAdmin.adapter  = setAdapter
                    noLirikKakawinAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<ArrayList<AllLirikKakawinAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusLirikKakawin(idLirikKakawin: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataLirikKakawinAdmin(idLirikKakawin, postIDs).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllLirikKakawinAdminActivity, AllLirikKakawinAdminActivity::class.java)
                    bundle.putInt("id_kakawin", postIDs)
                    bundle.putString("nama_kakawin", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllLirikKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLirikKakawinAdmin.stopShimmer()
        shimmerLirikKakawinAdmin.visibility = View.GONE
        swipeLirikKakawinAdmin.visibility   = View.VISIBLE
    }
}