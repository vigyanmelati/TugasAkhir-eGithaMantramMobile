package com.example.ekidungmantram.admin.pupuh

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
import com.example.ekidungmantram.adapter.admin.AllTabuhOnGamelanAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaOnPupuhAdminAdapter
import com.example.ekidungmantram.admin.gamelan.AddTabuhToGamelanAdminActivity
import com.example.ekidungmantram.admin.gamelan.DetailGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllYadnyaOnPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel
import kotlinx.android.synthetic.main.activity_all_tabuh_on_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_all_tabuh_on_gamelan_admin.fabGamelanAddTabuh
import kotlinx.android.synthetic.main.activity_all_yadnya_on_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllYadnyaOnPupuhAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllYadnyaOnPupuhAdminAdapter
    private lateinit var setAdapter   : AllYadnyaOnPupuhAdminAdapter
    private var id_pupuh : Int = 0
    private var id_pupuh_admin : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var nama_pupuh_admin :String
    private lateinit var desc_pupuh_admin :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_yadnya_on_pupuh_admin)
        supportActionBar!!.title = "Daftar Tabuh Gamelan"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh_admin")
            val namaPost = bundle.getString("nama_pupuh_admin")

            id_pupuh = postID
            nama_pupuh = namaPost.toString()
            id_pupuh_admin = bundle.getInt("id_pupuh_kat")
            nama_pupuh_admin = bundle.getString("nama_pupuh_kat").toString()
            desc_pupuh_admin = bundle.getString("padalingsa").toString()
            tag_user = bundle.getString("tag_user").toString()

            namaPupuhYadnya.text = namaPost
            allPupuhYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allPupuhYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnyaPupuh(postID, namaPost!!)

            swipePupuhYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnyaPupuh(postID, namaPost!!)
                swipePupuhYadnyaAdmin.isRefreshing = false
            }

            fabYadnyaAddPupuh.setOnClickListener {
                val intent = Intent(this, AddYadnyaToPupuhAdminActivity::class.java)
                bundle.putInt("id_pupuh_admin", postID)
                bundle.putString("nama_pupuh_admin", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataYadnyaPupuh(postID: Int, name: String) {
        ApiService.endpoint.getYadnyaPupuhAdmin(postID)
            .enqueue(object: Callback<YadnyaPupuhAdminModel> {
                override fun onResponse(
                    call: Call<YadnyaPupuhAdminModel>,
                    response: Response<YadnyaPupuhAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipePupuhYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerPupuhYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipePupuhYadnyaAdmin.visibility   = View.GONE
                        shimmerPupuhYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllYadnyaOnPupuhAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllYadnyaOnPupuhAdminActivity)
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

                    allPupuhYadnyaAdmin1.adapter  = setAdapter
                    noPupuhYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariPupuhYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noPupuhYadnyaAdmin.visibility   = View.GONE
                                    allPupuhYadnyaAdmin1.visibility = View.VISIBLE
                                    allPupuhYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllYadnyaOnPupuhAdminAdapter(filter as ArrayList<YadnyaPupuhAdminModel.DataL>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllYadnyaOnPupuhAdminActivity)
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
                                        noPupuhYadnyaAdmin.visibility   = View.VISIBLE
                                        allPupuhYadnyaAdmin1.visibility = View.GONE
                                        allPupuhYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noPupuhYadnyaAdmin.visibility   = View.GONE
                                        allPupuhYadnyaAdmin2.visibility = View.VISIBLE
                                        allPupuhYadnyaAdmin2.adapter    = tabuhAdapter
                                        allPupuhYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allPupuhYadnyaAdmin1.visibility = View.VISIBLE
                                        allPupuhYadnyaAdmin2.visibility = View.GONE
                                        noPupuhYadnyaAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<YadnyaPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusTabuhGamelan(id: Int, postID: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataYadnyaOnPupuhAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaOnPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllYadnyaOnPupuhAdminActivity, DetailPupuhAdminActivity::class.java)
                    bundle.putInt("id_pupuh_admin", postID)
                    bundle.putString("nama_pupuh_admin", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllYadnyaOnPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllYadnyaOnPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerPupuhYadnyaAdmin.stopShimmer()
        shimmerPupuhYadnyaAdmin.visibility = View.GONE
        swipePupuhYadnyaAdmin.visibility   = View.VISIBLE
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