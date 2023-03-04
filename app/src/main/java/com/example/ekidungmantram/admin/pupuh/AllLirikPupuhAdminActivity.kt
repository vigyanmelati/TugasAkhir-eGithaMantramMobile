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
import com.example.ekidungmantram.admin.kakawin.DetailKakawinAdminActivity
import com.example.ekidungmantram.admin.kidung.AddLirikKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.EditLirikKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllLirikPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_lirik_kidung_admin.*
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh.*
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLirikPupuhAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataLirikPupuhAdminAdapter
    private lateinit var padalingsa :String
    private var id_pupuh : Int = 0
    private var id_pupuh_admin : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var nama_pupuh_admin :String
    private lateinit var desc_pupuh_admin :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lirik_pupuh_admin)
        supportActionBar!!.title = "Lirik Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            padalingsa = bundle.getString("padalingsa").toString()

            id_pupuh = postID
            nama_pupuh = namaPost.toString()
            id_pupuh_admin = bundle.getInt("id_pupuh_kat")
            nama_pupuh_admin = bundle.getString("nama_pupuh_kat").toString()
            desc_pupuh_admin = bundle.getString("padalingsa").toString()
            tag_user = bundle.getString("tag_user").toString()

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
                bundle.putString("padalingsa", padalingsa)
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

    override fun onBackPressed() {
        super.onBackPressed()
        val bundle = Bundle()
        val intent = Intent(this, DetailPupuhAdminActivity::class.java)
        bundle.putInt("id_pupuh_admin", id_pupuh)
        bundle.putInt("id_pupuh_admin_kat", id_pupuh_admin)
        bundle.putString("nama_pupuh_admin", nama_pupuh)
        bundle.putString("nama_pupuh_admin_kat", nama_pupuh_admin)
        bundle.putString("desc_pupuh_admin_kat", desc_pupuh_admin)
        bundle.putString("tag_user", tag_user)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}