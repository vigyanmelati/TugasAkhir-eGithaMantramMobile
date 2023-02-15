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
import com.example.ekidungmantram.adapter.admin.AllDataAudioLaguAnakAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllDataAudioPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddAudioPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllAudioPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.EditAudioPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AudioLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.AudioPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_audio_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_all_audio_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAudioLaguAnakAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataAudioLaguAnakAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_audio_lagu_anak_admin)
        supportActionBar!!.title = "Audio Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")

            namaLaguAnakAudio.text = namaPost
            allAudioLaguAnakAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataAudioLaguAnak(postID, namaPost!!)

            swipeAudioLaguAnakAdmin.setOnRefreshListener {
                getAllDataAudioLaguAnak(postID, namaPost)
                swipeAudioLaguAnakAdmin.isRefreshing = false
            }

            fabAudioLaguAnak.setOnClickListener {
                val intent = Intent(this, AddAudioLaguAnakNewActivity::class.java)
                bundle.putInt("id_lagu_anak", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataAudioLaguAnak(postID: Int, namaPost: String) {
        ApiService.endpoint.getListAudioLaguAnakAdmin(postID)
            .enqueue(object: Callback<AudioLaguAnakAdminModel> {
                override fun onResponse(
                    call: Call<AudioLaguAnakAdminModel>,
                    response: Response<AudioLaguAnakAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeAudioLaguAnakAdmin.visibility   = View.VISIBLE
                        shimmerAudioLaguAnakAdmin.visibility = View.GONE
                    }else{
                        swipeAudioLaguAnakAdmin.visibility   = View.GONE
                        shimmerAudioLaguAnakAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataAudioLaguAnakAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllAudioLaguAnakAdminActivity)
                        builder.setTitle("Hapus Audio Sekar Rare")
                            .setMessage("Apakah anda yakin ingin menghapus audio sekar rare ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusAudioLaguAnak(it.id_audio, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllAudioLaguAnakAdminActivity, EditAudioLaguAnakNewActivity::class.java)
                        bundle.putInt("id_audio_lagu_anak", it.id_audio)
                        bundle.putInt("id_lagu_anak", postID)
                        bundle.putString("nama_lagu_anak", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allAudioLaguAnakAdmin.adapter  = setAdapter
                    noAudioLaguAnakAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<AudioLaguAnakAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusAudioLaguAnak(idAudioLaguAnak: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAudioLaguAnakAdmin(idAudioLaguAnak).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllAudioLaguAnakAdminActivity, AllAudioLaguAnakAdminActivity::class.java)
                    bundle.putInt("id_lagu_anak", postIDs)
                    bundle.putString("nama_lagu_anak", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllAudioLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerAudioLaguAnakAdmin.stopShimmer()
        shimmerAudioLaguAnakAdmin.visibility = View.GONE
        swipeAudioLaguAnakAdmin.visibility   = View.VISIBLE
    }
}