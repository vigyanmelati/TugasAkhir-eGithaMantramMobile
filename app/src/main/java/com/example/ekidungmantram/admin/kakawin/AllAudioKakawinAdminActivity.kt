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
import com.example.ekidungmantram.adapter.admin.AllDataAudioKakawinAdminAdapter
import com.example.ekidungmantram.admin.kakawin.AddAudioKakawinAdminActivity
import com.example.ekidungmantram.admin.kakawin.AllAudioKakawinAdminActivity
import com.example.ekidungmantram.admin.kakawin.EditAudioKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AudioKakawinAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_audio_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAudioKakawinAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataAudioKakawinAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_audio_kakawin_admin)
        supportActionBar!!.title = "Audio Sekar Agung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")

            namaKakawinAudio.text = namaPost
            allAudioKakawinAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataAudioKakawin(postID, namaPost!!)

            swipeAudioKakawinAdmin.setOnRefreshListener {
                getAllDataAudioKakawin(postID, namaPost)
                swipeAudioKakawinAdmin.isRefreshing = false
            }

            fabAudioKakawin.setOnClickListener {
                val intent = Intent(this, AddAudioKakawinNewActivity::class.java)
                bundle.putInt("id_kakawin", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataAudioKakawin(postID: Int, namaPost: String) {
        ApiService.endpoint.getListAudioKakawinAdmin(postID)
            .enqueue(object: Callback<AudioKakawinAdminModel> {
                override fun onResponse(
                    call: Call<AudioKakawinAdminModel>,
                    response: Response<AudioKakawinAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeAudioKakawinAdmin.visibility   = View.VISIBLE
                        shimmerAudioKakawinAdmin.visibility = View.GONE
                    }else{
                        swipeAudioKakawinAdmin.visibility   = View.GONE
                        shimmerAudioKakawinAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataAudioKakawinAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllAudioKakawinAdminActivity)
                        builder.setTitle("Hapus Audio Sekar Agung")
                            .setMessage("Apakah anda yakin ingin menghapus audio sekar agung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusAudioKakawin(it.id_audio, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllAudioKakawinAdminActivity, EditAudioKakawinNewActivity::class.java)
                        bundle.putInt("id_audio_kakawin", it.id_audio)
                        bundle.putInt("id_kakawin", postID)
                        bundle.putString("nama_kakawin", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allAudioKakawinAdmin.adapter  = setAdapter
                    noAudioKakawinAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<AudioKakawinAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusAudioKakawin(idAudioKakawin: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAudioKakawinAdmin(idAudioKakawin).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllAudioKakawinAdminActivity, AllAudioKakawinAdminActivity::class.java)
                    bundle.putInt("id_kakawin", postIDs)
                    bundle.putString("nama_kakawin", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllAudioKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerAudioKakawinAdmin.stopShimmer()
        shimmerAudioKakawinAdmin.visibility = View.GONE
        swipeAudioKakawinAdmin.visibility   = View.VISIBLE
    }
}