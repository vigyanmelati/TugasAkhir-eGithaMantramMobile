package com.example.ekidungmantram.user.pupuh

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
import com.example.ekidungmantram.adapter.AllDataLirikPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllLirikPupuhModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_lirik_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLirikPupuhActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataLirikPupuhAdapter
    private lateinit var padalingsa :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lirik_pupuh)
        supportActionBar!!.title = "Lirik Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            padalingsa = bundle.getString("padalingsa").toString()
            namaPupuhLirikUser.text = namaPost
            allLirikPupuhUser.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataLirikPupuh(postID, namaPost!!)

            swipeLirikPupuhUser.setOnRefreshListener {
                getAllDataLirikPupuh(postID, namaPost)
                swipeLirikPupuhUser.isRefreshing = false
            }

            fabLirikPupuhUser.setOnClickListener {
                val intent = Intent(this, AddLirikPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                bundle.putString("padalingsa", padalingsa)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataLirikPupuh(postID: Int, namaPost: String) {
        ApiService.endpoint.getAllLirikPupuh(postID)
            .enqueue(object: Callback<ArrayList<AllLirikPupuhModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLirikPupuhModel>>,
                    response: Response<ArrayList<AllLirikPupuhModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeLirikPupuhUser.visibility   = View.VISIBLE
                        shimmerLirikPupuhUser.visibility = View.GONE
                    }else{
                        swipeLirikPupuhUser.visibility   = View.GONE
                        shimmerLirikPupuhUser.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataLirikPupuhAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllLirikPupuhActivity)
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
                        val intent = Intent(this@AllLirikPupuhActivity, EditLirikPupuhActivity::class.java)
                        bundle.putInt("id_lirik_pupuh", it.id_lirik_pupuh)
                        bundle.putInt("id_pupuh", postID)
                        bundle.putString("nama_pupuh", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allLirikPupuhUser.adapter  = setAdapter
                    noLirikPupuhUser.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<ArrayList<AllLirikPupuhModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusLirikPupuh(idLirikPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataLirikPupuh(idLirikPupuh, postIDs).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllLirikPupuhActivity, AllLirikPupuhActivity::class.java)
                    bundle.putInt("id_pupuh", postIDs)
                    bundle.putString("nama_pupuh", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllLirikPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLirikPupuhUser.stopShimmer()
        shimmerLirikPupuhUser.visibility = View.GONE
        swipeLirikPupuhUser.visibility   = View.VISIBLE
    }
}