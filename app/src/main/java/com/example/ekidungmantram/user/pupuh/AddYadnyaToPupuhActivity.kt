package com.example.ekidungmantram.user.pupuh

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllYadnyaNotOnPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.YadnyaPupuhModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_yadnya_to_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddYadnyaToPupuhActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaNotOnPupuhAdapter
    private lateinit var setAdapter   : AllYadnyaNotOnPupuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_yadnya_to_pupuh)
        supportActionBar!!.title = "Daftar Semua Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_user")
            val namaPost = bundle.getString("nama_pupuh_user")
            Log.d("id_pupuh_add", postID.toString())

            allAddPupuhYadnyaUser1.layoutManager = LinearLayoutManager(applicationContext)
            allAddPupuhYadnyaUser2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaPupuh(postID, namaPost!!)

            swipePupuhAddYadnyaUser.setOnRefreshListener {
                getAllDataYadnyaPupuh(postID, namaPost!!)
                swipePupuhAddYadnyaUser.isRefreshing = false
            }
        }
    }

    private fun getAllDataYadnyaPupuh(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllYadnyaNotOnPupuh(postID)
            .enqueue(object: Callback<YadnyaPupuhModel> {
                override fun onResponse(
                    call: Call<YadnyaPupuhModel>,
                    response: Response<YadnyaPupuhModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipePupuhAddYadnyaUser.visibility   = View.VISIBLE
                        shimmerPupuhAddYadnyaUser.visibility = View.GONE
                    }else{
                        swipePupuhAddYadnyaUser.visibility   = View.GONE
                        shimmerPupuhAddYadnyaUser.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaNotOnPupuhAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddYadnyaToPupuhActivity)
                        builder.setTitle("Tambah Yadnya")
                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhGamelan(postID, it.id_post, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddPupuhYadnyaUser1.adapter  = setAdapter
                    noPupuhAddYadnyaUser.visibility = View.GONE
                    setShimmerToStop()

                    cariPupuhAddYadnyaUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noPupuhAddYadnyaUser.visibility   = View.GONE
                                    allAddPupuhYadnyaUser1.visibility = View.VISIBLE
                                    allAddPupuhYadnyaUser2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaNotOnPupuhAdapter(filter as ArrayList<YadnyaPupuhModel.DataL>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddYadnyaToPupuhActivity)
                                        builder.setTitle("Tambah Yadnya")
                                            .setMessage("Apakah anda yakin ingin menambahkan yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTabuhGamelan(postID, it.id_post, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noPupuhAddYadnyaUser.visibility   = View.VISIBLE
                                        allAddPupuhYadnyaUser1.visibility = View.GONE
                                        allAddPupuhYadnyaUser2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noPupuhAddYadnyaUser.visibility   = View.GONE
                                        allAddPupuhYadnyaUser2.visibility = View.VISIBLE
                                        allAddPupuhYadnyaUser2.adapter    = tabuhAdapter
                                        allAddPupuhYadnyaUser1.visibility = View.INVISIBLE
                                    }else{
                                        allAddPupuhYadnyaUser1.visibility = View.VISIBLE
                                        allAddPupuhYadnyaUser2.visibility = View.GONE
                                        noPupuhAddYadnyaUser.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<YadnyaPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTabuhGamelan(postID: Int, idPupuh: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataYadnyaToPupuh(postID, idPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddYadnyaToPupuhActivity, AllYadnyaOnPupuhActivity::class.java)
                    bundle.putInt("id_pupuh_user", postID)
                    bundle.putString("nama_pupuh_user", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaToPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddYadnyaToPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerPupuhAddYadnyaUser.stopShimmer()
        shimmerPupuhAddYadnyaUser.visibility = View.GONE
        swipePupuhAddYadnyaUser.visibility   = View.VISIBLE
    }
}