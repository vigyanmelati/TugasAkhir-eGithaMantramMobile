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
import com.example.ekidungmantram.adapter.admin.AllDataLirikPupuhAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllDataVideoPupuhAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.VideoPupuhModel
import com.example.ekidungmantram.model.adminmodel.AllLirikPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.fabLirikPupuh
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.shimmerLirikPupuhAdmin
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh_admin.swipeLirikPupuhAdmin
import kotlinx.android.synthetic.main.activity_all_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVideoPupuhAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataVideoPupuhAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_video_pupuh_admin)
        supportActionBar!!.title = "Video Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")

            namaPupuhVideo.text = namaPost
            allVideoPupuhAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataVideoPupuh(postID, namaPost!!)

            swipeVideoPupuhAdmin.setOnRefreshListener {
                getAllDataVideoPupuh(postID, namaPost)
                swipeVideoPupuhAdmin.isRefreshing = false
            }

            fabVideoPupuh.setOnClickListener {
                val intent = Intent(this, AddVideoPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataVideoPupuh(postID: Int, namaPost: String) {
        ApiService.endpoint.getListVideoPupuhAdmin(postID)
            .enqueue(object: Callback<VideoPupuhAdminModel> {
                override fun onResponse(
                    call: Call<VideoPupuhAdminModel>,
                    response: Response<VideoPupuhAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeVideoPupuhAdmin.visibility   = View.VISIBLE
                        shimmerVideoPupuhAdmin.visibility = View.GONE
                    }else{
                        swipeVideoPupuhAdmin.visibility   = View.GONE
                        shimmerVideoPupuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataVideoPupuhAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllVideoPupuhAdminActivity)
                        builder.setTitle("Hapus Video Pupuh")
                            .setMessage("Apakah anda yakin ingin menghapus video pupuh ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusVideoPupuh(it.id_video, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllVideoPupuhAdminActivity, EditVideoPupuhActivity::class.java)
                        bundle.putInt("id_video_pupuh", it.id_video)
                        bundle.putInt("id_pupuh", postID)
                        bundle.putString("nama_pupuh", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allVideoPupuhAdmin.adapter  = setAdapter
                    noVideoPupuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<VideoPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusVideoPupuh(idVideoPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataVideoPupuhAdmin(idVideoPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllVideoPupuhAdminActivity, AllVideoPupuhAdminActivity::class.java)
                    bundle.putInt("id_pupuh", postIDs)
                    bundle.putString("nama_pupuh", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllVideoPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerVideoPupuhAdmin.stopShimmer()
        shimmerVideoPupuhAdmin.visibility = View.GONE
        swipeVideoPupuhAdmin.visibility   = View.VISIBLE
    }
}