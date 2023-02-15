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
import com.example.ekidungmantram.adapter.admin.AllDataVideoKakawinAdminAdapter
import com.example.ekidungmantram.admin.kakawin.AddVideoKakawinAdminActivity
import com.example.ekidungmantram.admin.kakawin.AllVideoKakawinAdminActivity
import com.example.ekidungmantram.admin.kakawin.EditVideoKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoKakawinAdminModel
import kotlinx.android.synthetic.main.activity_all_video_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVideoKakawinAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataVideoKakawinAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_video_kakawin_admin)
        supportActionBar!!.title = "Video Sekar Agung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")

            namaKakawinVideo.text = namaPost
            allVideoKakawinAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataVideoKakawin(postID, namaPost!!)

            swipeVideoKakawinAdmin.setOnRefreshListener {
                getAllDataVideoKakawin(postID, namaPost)
                swipeVideoKakawinAdmin.isRefreshing = false
            }

            fabVideoKakawin.setOnClickListener {
                val intent = Intent(this, AddVideoKakawinAdminActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataVideoKakawin(postID: Int, namaPost: String) {
        ApiService.endpoint.getListVideoKakawinAdmin(postID)
            .enqueue(object: Callback<VideoKakawinAdminModel> {
                override fun onResponse(
                    call: Call<VideoKakawinAdminModel>,
                    response: Response<VideoKakawinAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeVideoKakawinAdmin.visibility   = View.VISIBLE
                        shimmerVideoKakawinAdmin.visibility = View.GONE
                    }else{
                        swipeVideoKakawinAdmin.visibility   = View.GONE
                        shimmerVideoKakawinAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataVideoKakawinAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllVideoKakawinAdminActivity)
                        builder.setTitle("Hapus Video Sekar Agung")
                            .setMessage("Apakah anda yakin ingin menghapus video Sekar Agung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusVideoKakawin(it.id_video, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllVideoKakawinAdminActivity, EditVideoKakawinAdminActivity::class.java)
                        bundle.putInt("id_video_kakawin", it.id_video)
                        bundle.putInt("id_kakawin", postID)
                        bundle.putString("nama_kakawin", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allVideoKakawinAdmin.adapter  = setAdapter
                    noVideoKakawinAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<VideoKakawinAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusVideoKakawin(idVideoKakawin: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataVideoKakawinAdmin(idVideoKakawin).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllVideoKakawinAdminActivity, AllVideoKakawinAdminActivity::class.java)
                    bundle.putInt("id_kakawin", postIDs)
                    bundle.putString("nama_kakawin", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllVideoKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerVideoKakawinAdmin.stopShimmer()
        shimmerVideoKakawinAdmin.visibility = View.GONE
        swipeVideoKakawinAdmin.visibility   = View.VISIBLE
    }
}