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
import com.example.ekidungmantram.adapter.AllDataAudioPupuhAdapter
import com.example.ekidungmantram.adapter.admin.AllDataAudioPupuhAdminAdapter
import com.example.ekidungmantram.admin.pupuh.AddAudioPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllAudioPupuhAdminActivity
import com.example.ekidungmantram.admin.pupuh.EditAudioPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AudioPupuhModel
import com.example.ekidungmantram.model.adminmodel.AudioPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_audio_pupuh.*
import kotlinx.android.synthetic.main.activity_all_audio_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAudioPupuhActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataAudioPupuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_audio_pupuh)
        supportActionBar!!.title = "Audio Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")

            namaPupuhAudioUser.text = namaPost
            allAudioPupuhUser.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataAudioPupuh(postID, namaPost!!)

            swipeAudioPupuhUser.setOnRefreshListener {
                getAllDataAudioPupuh(postID, namaPost)
                swipeAudioPupuhUser.isRefreshing = false
            }

            fabAudioPupuhUser.setOnClickListener {
                val intent = Intent(this, AddAudioPupuhActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataAudioPupuh(postID: Int, namaPost: String) {
        ApiService.endpoint.getListAudioPupuh(postID)
            .enqueue(object: Callback<AudioPupuhModel> {
                override fun onResponse(
                    call: Call<AudioPupuhModel>,
                    response: Response<AudioPupuhModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeAudioPupuhUser.visibility   = View.VISIBLE
                        shimmerAudioPupuhUser.visibility = View.GONE
                    }else{
                        swipeAudioPupuhUser.visibility   = View.GONE
                        shimmerAudioPupuhUser.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataAudioPupuhAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllAudioPupuhActivity)
                        builder.setTitle("Hapus Audio Pupuh")
                            .setMessage("Apakah anda yakin ingin menghapus audio pupuh ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusAudioPupuh(it.id_audio, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllAudioPupuhActivity, EditAudioPupuhAdminActivity::class.java)
                        bundle.putInt("id_audio_pupuh", it.id_audio)
                        bundle.putInt("id_pupuh", postID)
                        bundle.putString("nama_pupuh", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allAudioPupuhUser.adapter  = setAdapter
                    noAudioPupuhUser.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<AudioPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusAudioPupuh(idAudioPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAudioPupuh(idAudioPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllAudioPupuhActivity, AllAudioPupuhActivity::class.java)
                    bundle.putInt("id_pupuh", postIDs)
                    bundle.putString("nama_pupuh", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllAudioPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerAudioPupuhUser.stopShimmer()
        shimmerAudioPupuhUser.visibility = View.GONE
        swipeAudioPupuhUser.visibility   = View.VISIBLE
    }
}