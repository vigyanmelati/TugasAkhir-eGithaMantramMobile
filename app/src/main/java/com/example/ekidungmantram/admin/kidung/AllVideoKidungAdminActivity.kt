package com.example.ekidungmantram.admin.kidung

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
import com.example.ekidungmantram.adapter.admin.AllDataVideoKidungAdminAdapter
import com.example.ekidungmantram.admin.kakawin.DetailKakawinAdminActivity
import com.example.ekidungmantram.admin.kidung.AllVideoKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoKidungAdminModel
import kotlinx.android.synthetic.main.activity_all_video_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVideoKidungAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataVideoKidungAdminAdapter
    private var id_kidung : Int = 0
    private lateinit var nama_kidung :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_video_kidung_admin)
        supportActionBar!!.title = "Video Sekar Madya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            val namaPost = bundle.getString("nama_kidung")

            id_kidung = postID
            nama_kidung = namaPost.toString()
            tag_user = bundle.getString("tag_user_kidung").toString()

            namaKidungVideo.text = namaPost
            allVideoKidungAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataVideoKidung(postID, namaPost!!)

            swipeVideoKidungAdmin.setOnRefreshListener {
                getAllDataVideoKidung(postID, namaPost)
                swipeVideoKidungAdmin.isRefreshing = false
            }

            fabVideoKidung.setOnClickListener {
                val intent = Intent(this, AddVideoKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataVideoKidung(postID: Int, namaPost: String) {
        ApiService.endpoint.getListVideoKidungAdmin(postID)
            .enqueue(object: Callback<VideoKidungAdminModel> {
                override fun onResponse(
                    call: Call<VideoKidungAdminModel>,
                    response: Response<VideoKidungAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeVideoKidungAdmin.visibility   = View.VISIBLE
                        shimmerVideoKidungAdmin.visibility = View.GONE
                    }else{
                        swipeVideoKidungAdmin.visibility   = View.GONE
                        shimmerVideoKidungAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataVideoKidungAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllVideoKidungAdminActivity)
                        builder.setTitle("Hapus Video Kidung")
                            .setMessage("Apakah anda yakin ingin menghapus video kidung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusVideoKidung(it.id_video, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllVideoKidungAdminActivity, EditVideoKidungAdminActivity::class.java)
                        bundle.putInt("id_video_kidung", it.id_video)
                        bundle.putInt("id_kidung", postID)
                        bundle.putString("nama_kidung", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allVideoKidungAdmin.adapter  = setAdapter
                    noVideoKidungAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<VideoKidungAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusVideoKidung(idVideoKidung: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataVideoKidungAdmin(idVideoKidung).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllVideoKidungAdminActivity, AllVideoKidungAdminActivity::class.java)
                    bundle.putInt("id_kidung", postIDs)
                    bundle.putString("nama_kidung", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllVideoKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerVideoKidungAdmin.stopShimmer()
        shimmerVideoKidungAdmin.visibility = View.GONE
        swipeVideoKidungAdmin.visibility   = View.VISIBLE
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