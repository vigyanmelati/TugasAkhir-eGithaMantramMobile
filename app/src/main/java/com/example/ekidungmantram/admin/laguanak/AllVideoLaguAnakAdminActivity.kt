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
import com.example.ekidungmantram.adapter.admin.AllDataVideoLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllDataVideoPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddVideoPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllVideoPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.EditVideoPupuhActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_video_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_all_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVideoLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataVideoLaguAnakAdminAdapter
    private var id_lagu_anak : Int = 0
    private var id_lagu_anak_admin : Int = 0
    private lateinit var nama_lagu_anak :String
    private lateinit var nama_lagu_anak_admin :String
    private lateinit var desc_lagu_anak_admin :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_video_lagu_anak_admin)
        supportActionBar!!.title = "Video Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")

            id_lagu_anak = postID
            nama_lagu_anak = namaPost.toString()
            id_lagu_anak_admin = bundle.getInt("id_lagu_anak_kat")
            nama_lagu_anak_admin = bundle.getString("nama_lagu_anak_kat").toString()
            desc_lagu_anak_admin = bundle.getString("desc_lagu_anak_kat").toString()
            tag_user = bundle.getString("tag_user_anak").toString()

            namaLaguAnakVideo.text = namaPost
            allVideoLaguAnakAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataVideoLaguAnak(postID, namaPost!!)

            swipeVideoLaguAnakAdmin.setOnRefreshListener {
                getAllDataVideoLaguAnak(postID, namaPost)
                swipeVideoLaguAnakAdmin.isRefreshing = false
            }

            fabVideoLaguAnak.setOnClickListener {
                val intent = Intent(this, AddVideoLaguAnakAdminActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataVideoLaguAnak(postID: Int, namaPost: String) {
        ApiService.endpoint.getListVideoLaguAnakAdmin(postID)
            .enqueue(object: Callback<VideoLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<VideoLaguAnakAdminModel>,
                    response: Response<VideoLaguAnakAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeVideoLaguAnakAdmin.visibility   = View.VISIBLE
                        shimmerVideoLaguAnakAdmin.visibility = View.GONE
                    }else{
                        swipeVideoLaguAnakAdmin.visibility   = View.GONE
                        shimmerVideoLaguAnakAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataVideoLaguAnakAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllVideoLaguAnakAdminActivity)
                        builder.setTitle("Hapus Video Sekar Rare")
                            .setMessage("Apakah anda yakin ingin menghapus video sekar rare ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusVideoLaguAnak(it.id_video, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllVideoLaguAnakAdminActivity, EditVideoLaguAnakAdminActivity::class.java)
                        bundle.putInt("id_video_lagu_anak", it.id_video)
                        bundle.putInt("id_lagu_anak", postID)
                        bundle.putString("nama_lagu_anak", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allVideoLaguAnakAdmin.adapter  = setAdapter
                    noVideoLaguAnakAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<VideoLaguAnakAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusVideoLaguAnak(idVideoPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataVideoLaguAnakAdmin(idVideoPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllVideoLaguAnakAdminActivity, AllVideoPupuhAdminActivity::class.java)
                    bundle.putInt("id_lagu_anak", postIDs)
                    bundle.putString("nama_lagu_anak", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllVideoLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerVideoLaguAnakAdmin.stopShimmer()
        shimmerVideoLaguAnakAdmin.visibility = View.GONE
        swipeVideoLaguAnakAdmin.visibility   = View.VISIBLE
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