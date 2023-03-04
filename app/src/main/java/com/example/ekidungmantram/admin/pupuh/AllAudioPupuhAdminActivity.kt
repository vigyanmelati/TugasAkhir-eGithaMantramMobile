package com.example.ekidungmantram.admin.pupuh

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
import com.example.ekidungmantram.adapter.admin.AllDataAudioPupuhAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllDataVideoPupuhAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AudioPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel
import com.example.ekidungmantram.user.pupuh.AddAudioPupuhNewActivity
import com.example.ekidungmantram.user.pupuh.EditAudioPupuhNewActivity
import kotlinx.android.synthetic.main.activity_all_audio_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_all_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAudioPupuhAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataAudioPupuhAdminAdapter
    private var id_pupuh : Int = 0
    private var id_pupuh_admin : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var nama_pupuh_admin :String
    private lateinit var desc_pupuh_admin :String
    private lateinit var tag_user :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_audio_pupuh_admin)
        supportActionBar!!.title = "Audio Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")

            id_pupuh = postID
            nama_pupuh = namaPost.toString()
            id_pupuh_admin = bundle.getInt("id_pupuh_kat")
            nama_pupuh_admin = bundle.getString("nama_pupuh_kat").toString()
            desc_pupuh_admin = bundle.getString("padalingsa").toString()
            tag_user = bundle.getString("tag_user").toString()

            namaPupuhAudio.text = namaPost
            allAudioPupuhAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataAudioPupuh(postID, namaPost!!)

            swipeAudioPupuhAdmin.setOnRefreshListener {
                getAllDataAudioPupuh(postID, namaPost)
                swipeAudioPupuhAdmin.isRefreshing = false
            }

            fabAudioPupuh.setOnClickListener {
                val intent = Intent(this, AddAudioPupuhNewActivity::class.java)
                bundle.putInt("id_pupuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataAudioPupuh(postID: Int, namaPost: String) {
        ApiService.endpoint.getListAudioPupuhAdmin(postID)
            .enqueue(object: Callback<AudioPupuhAdminModel> {
                override fun onResponse(
                    call: Call<AudioPupuhAdminModel>,
                    response: Response<AudioPupuhAdminModel>
                ) {
                    val datalist   = response.body()?.data
                    if(datalist != null){
                        swipeAudioPupuhAdmin.visibility   = View.VISIBLE
                        shimmerAudioPupuhAdmin.visibility = View.GONE
                    }else{
                        swipeAudioPupuhAdmin.visibility   = View.GONE
                        shimmerAudioPupuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataAudioPupuhAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllAudioPupuhAdminActivity)
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
                        val intent = Intent(this@AllAudioPupuhAdminActivity, EditAudioPupuhNewActivity::class.java)
                        bundle.putInt("id_audio_pupuh", it.id_audio)
                        bundle.putInt("id_pupuh", postID)
                        bundle.putString("nama_pupuh", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allAudioPupuhAdmin.adapter  = setAdapter
                    noAudioPupuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<AudioPupuhAdminModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusAudioPupuh(idAudioPupuh: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAudioPupuhAdmin(idAudioPupuh).enqueue(object:
            Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllAudioPupuhAdminActivity, AllAudioPupuhAdminActivity::class.java)
                    bundle.putInt("id_pupuh", postIDs)
                    bundle.putString("nama_pupuh", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllAudioPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllAudioPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerAudioPupuhAdmin.stopShimmer()
        shimmerAudioPupuhAdmin.visibility = View.GONE
        swipeAudioPupuhAdmin.visibility   = View.VISIBLE
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