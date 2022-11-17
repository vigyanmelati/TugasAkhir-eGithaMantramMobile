package com.example.ekidungmantram.user

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
import com.example.ekidungmantram.adapter.AllYadnyaOnPupuhAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaOnPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddYadnyaToPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.DetailPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.YadnyaPupuhModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_yadnya_on_pupuh.*
import kotlinx.android.synthetic.main.activity_all_yadnya_on_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllYadnyaOnPupuhActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaOnPupuhAdapter
    private lateinit var setAdapter   : AllYadnyaOnPupuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_yadnya_on_pupuh)
        supportActionBar!!.title = "Daftar Tabuh Gamelan"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_user")
            val namaPost = bundle.getString("nama_pupuh_user")

            namaPupuhYadnyaUser.text = namaPost
            allPupuhYadnyaUser1.layoutManager = LinearLayoutManager(applicationContext)
            allPupuhYadnyaUser2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaPupuh(postID, namaPost!!)

            swipePupuhYadnyaUser.setOnRefreshListener {
                getAllDataYadnyaPupuh(postID, namaPost!!)
                swipePupuhYadnyaUser.isRefreshing = false
            }

            fabYadnyaAddPupuhUser.setOnClickListener {
                val intent = Intent(this, AddYadnyaToPupuhActivity::class.java)
                bundle.putInt("id_pupuh_user", postID)
                bundle.putString("nama_pupuh_user", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaPupuh(postID: Int, name: String) {
        ApiService.endpoint.getYadnyaPupuh(postID)
            .enqueue(object: Callback<YadnyaPupuhModel> {
                override fun onResponse(
                    call: Call<YadnyaPupuhModel>,
                    response: Response<YadnyaPupuhModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipePupuhYadnyaUser.visibility   = View.VISIBLE
                        shimmerPupuhYadnyaUser.visibility = View.GONE
                    }else{
                        swipePupuhYadnyaUser.visibility   = View.GONE
                        shimmerPupuhYadnyaUser.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaOnPupuhAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllYadnyaOnPupuhActivity)
                        builder.setTitle("Hapus Yadnya dari Sekar Rare")
                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar rare ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhGamelan(it.id, postID, name)
                                Log.d("id_detail_post", it.id.toString())
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allPupuhYadnyaUser1.adapter  = setAdapter
                    noPupuhYadnyaUser.visibility = View.GONE
                    setShimmerToStop()

                    cariPupuhYadnyaUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noPupuhYadnyaUser.visibility   = View.GONE
                                    allPupuhYadnyaUser1.visibility = View.VISIBLE
                                    allPupuhYadnyaUser2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaOnPupuhAdapter(filter as ArrayList<YadnyaPupuhModel.DataL>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllYadnyaOnPupuhActivity)
                                        builder.setTitle("Hapus Yadnya dari Sekar Rare")
                                            .setMessage("Apakah anda yakin ingin menghapus yadnya dari sekar rare ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhGamelan(it.id, postID, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noPupuhYadnyaUser.visibility   = View.VISIBLE
                                        allPupuhYadnyaUser1.visibility = View.GONE
                                        allPupuhYadnyaUser2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noPupuhYadnyaUser.visibility   = View.GONE
                                        allPupuhYadnyaUser2.visibility = View.VISIBLE
                                        allPupuhYadnyaUser2.adapter    = tabuhAdapter
                                        allPupuhYadnyaUser1.visibility = View.INVISIBLE
                                    }else{
                                        allPupuhYadnyaUser1.visibility = View.VISIBLE
                                        allPupuhYadnyaUser2.visibility = View.GONE
                                        noPupuhYadnyaUser.visibility   = View.GONE
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

    private fun hapusTabuhGamelan(id: Int, postID: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataYadnyaOnPupuh(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaOnPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllYadnyaOnPupuhActivity, AllYadnyaOnPupuhActivity::class.java)
                    bundle.putInt("id_pupuh_user", postID)
                    bundle.putString("nama_pupuh_user", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaOnPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllYadnyaOnPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerPupuhYadnyaUser.stopShimmer()
        shimmerPupuhYadnyaUser.visibility = View.GONE
        swipePupuhYadnyaUser.visibility   = View.VISIBLE
    }
}