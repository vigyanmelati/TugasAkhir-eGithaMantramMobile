package com.example.ekidungmantram.admin.pupuh

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
import com.example.ekidungmantram.adapter.admin.AllDataLirikKidungAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllDataLirikPupuhAdminAdapter
import com.example.ekidungmantram.admin.kidung.AddLirikKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.EditLirikKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllLirikPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_lirik_kidung_admin.*
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLirikPupuhAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataLirikPupuhAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lirik_pupuh_admin)
        supportActionBar!!.title = "Lirik Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")

            namaPupuhLirik.text = namaPost
            allLirikPupuhAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataLirikPupuh(postID, namaPost!!)

            swipeLirikPupuhAdmin.setOnRefreshListener {
                getAllDataLirikPupuh(postID, namaPost)
                swipeLirikPupuhAdmin.isRefreshing = false
            }

            fabLirikPupuh.setOnClickListener {
                val intent = Intent(this, AddLirikPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataLirikPupuh(postID: Int, namaPost: String) {
        ApiService.endpoint.getAllLirikPupuhAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllLirikPupuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLirikPupuhAdminModel>>,
                    response: Response<ArrayList<AllLirikPupuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeLirikPupuhAdmin.visibility   = View.VISIBLE
                        shimmerLirikPupuhAdmin.visibility = View.GONE
                    }else{
                        swipeLirikPupuhAdmin.visibility   = View.GONE
                        shimmerLirikPupuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataLirikPupuhAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllLirikPupuhAdminActivity)
                        builder.setTitle("Hapus Lirik Pupuh")
                            .setMessage("Apakah anda yakin ingin menghapus lirik pupuh ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusLirikPupuh(it.id_lirik_pupuh, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllLirikPupuhAdminActivity, EditLirikPupuhAdminActivity::class.java)
                        bundle.putInt("id_lirik_pupuh", it.id_lirik_pupuh)
                        bundle.putInt("id_pupuh", postID)
                        bundle.putString("nama_pupuh", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allLirikPupuhAdmin.adapter  = setAdapter
                    noLirikPupuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<ArrayList<AllLirikPupuhAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusLirikPupuh(idLirikPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataLirikPupuhAdmin(idLirikPupuh, postIDs).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllLirikPupuhAdminActivity, AllLirikPupuhAdminActivity::class.java)
                    bundle.putInt("id_pupuh", postIDs)
                    bundle.putString("nama_pupuh", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllLirikPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLirikPupuhAdmin.stopShimmer()
        shimmerLirikPupuhAdmin.visibility = View.GONE
        swipeLirikPupuhAdmin.visibility   = View.VISIBLE
    }
}