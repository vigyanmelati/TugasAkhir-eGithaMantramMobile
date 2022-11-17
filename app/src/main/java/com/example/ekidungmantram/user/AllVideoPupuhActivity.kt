package com.example.ekidungmantram.user

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
import com.example.ekidungmantram.adapter.AllDataVideoPupuhAdapter
import com.example.ekidungmantram.adapter.admin.AllDataVideoPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddVideoPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllVideoPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.EditVideoPupuhActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.VideoPupuhModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_video_pupuh.*
import kotlinx.android.synthetic.main.activity_all_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVideoPupuhActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataVideoPupuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_video_pupuh)
        supportActionBar!!.title = "Video Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")

            namaPupuhVideoUser.text = namaPost
            allVideoPupuhUser.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataVideoPupuh(postID, namaPost!!)

            swipeVideoPupuhUser.setOnRefreshListener {
                getAllDataVideoPupuh(postID, namaPost)
                swipeVideoPupuhUser.isRefreshing = false
            }

            fabVideoPupuhUser.setOnClickListener {
                val intent = Intent(this, AddVideoPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataVideoPupuh(postID: Int, namaPost: String) {
        ApiService.endpoint.getListVideoPupuh(postID)
            .enqueue(object: Callback<VideoPupuhModel> {
                override fun onResponse(
                    call: Call<VideoPupuhModel>,
                    response: Response<VideoPupuhModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeVideoPupuhUser.visibility   = View.VISIBLE
                        shimmerVideoPupuhUser.visibility = View.GONE
                    }else{
                        swipeVideoPupuhUser.visibility   = View.GONE
                        shimmerVideoPupuhUser.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataVideoPupuhAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllVideoPupuhActivity)
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
                        val intent = Intent(this@AllVideoPupuhActivity, EditVideoPupuhActivity::class.java)
                        bundle.putInt("id_video_pupuh", it.id_video)
                        bundle.putInt("id_pupuh", postID)
                        bundle.putString("nama_pupuh", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allVideoPupuhUser.adapter  = setAdapter
                    noVideoPupuhUser.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<VideoPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusVideoPupuh(idVideoPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataVideoPupuh(idVideoPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllVideoPupuhActivity, AllVideoPupuhAdminActivity::class.java)
                    bundle.putInt("id_pupuh", postIDs)
                    bundle.putString("nama_pupuh", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllVideoPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllVideoPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerVideoPupuhUser.stopShimmer()
        shimmerVideoPupuhUser.visibility = View.GONE
        swipeVideoPupuhUser.visibility   = View.VISIBLE
    }
}