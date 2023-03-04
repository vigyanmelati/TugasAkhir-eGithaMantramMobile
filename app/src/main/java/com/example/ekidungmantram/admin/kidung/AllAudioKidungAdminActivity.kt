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
import com.example.ekidungmantram.adapter.admin.AllDataAudioKidungAdminAdapter
import com.example.ekidungmantram.admin.kidung.EditAudioKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AudioKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_audio_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAudioKidungAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataAudioKidungAdminAdapter
    private var id_kidung : Int = 0
    private lateinit var nama_kidung :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_audio_kidung_admin)
        supportActionBar!!.title = "Audio Sekar Madya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            val namaPost = bundle.getString("nama_kidung")

            id_kidung = postID
            nama_kidung = namaPost.toString()
            tag_user = bundle.getString("tag_user_kidung").toString()

            namaKidungAudio.text = namaPost
            allAudioKidungAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataAudioKidung(postID, namaPost!!)

            swipeAudioKidungAdmin.setOnRefreshListener {
                getAllDataAudioKidung(postID, namaPost)
                swipeAudioKidungAdmin.isRefreshing = false
            }

            fabAudioKidung.setOnClickListener {
                val intent = Intent(this, AddAudioKidungNewActivity::class.java)
                bundle.putInt("id_kidung", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataAudioKidung(postID: Int, namaPost: String) {
        ApiService.endpoint.getListAudioKidungAdmin(postID)
            .enqueue(object: Callback<AudioKidungAdminModel> {
                override fun onResponse(
                    call: Call<AudioKidungAdminModel>,
                    response: Response<AudioKidungAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeAudioKidungAdmin.visibility   = View.VISIBLE
                        shimmerAudioKidungAdmin.visibility = View.GONE
                    }else{
                        swipeAudioKidungAdmin.visibility   = View.GONE
                        shimmerAudioKidungAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataAudioKidungAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllAudioKidungAdminActivity)
                        builder.setTitle("Hapus Audio Kidung")
                            .setMessage("Apakah anda yakin ingin menghapus audio kidung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusAudioKidung(it.id_audio, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllAudioKidungAdminActivity, EditAudioKidungNewActivity::class.java)
                        bundle.putInt("id_audio_kidung", it.id_audio)
                        bundle.putInt("id_kidung", postID)
                        bundle.putString("nama_kidung", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allAudioKidungAdmin.adapter  = setAdapter
                    noAudioKidungAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<AudioKidungAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusAudioKidung(idAudioKidung: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAudioKidungAdmin(idAudioKidung).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllAudioKidungAdminActivity, AllAudioKidungAdminActivity::class.java)
                    bundle.putInt("id_kidung", postIDs)
                    bundle.putString("nama_kidung", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllAudioKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerAudioKidungAdmin.stopShimmer()
        shimmerAudioKidungAdmin.visibility = View.GONE
        swipeAudioKidungAdmin.visibility   = View.VISIBLE
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