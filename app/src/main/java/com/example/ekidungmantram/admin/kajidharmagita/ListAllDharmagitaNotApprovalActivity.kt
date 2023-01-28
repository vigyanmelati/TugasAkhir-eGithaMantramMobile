package com.example.ekidungmantram.admin.kajidharmagita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kidung.AddLirikKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.JumlahModel
import kotlinx.android.synthetic.main.activity_list_all_dharmagita_not_approval.*
import kotlinx.android.synthetic.main.fragment_home_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAllDharmagitaNotApprovalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_dharmagita_not_approval)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Dharmagita"
        getAdminHomeDharmagitaNoApprovalData()
        getAdminHomeVideoDharmagitaNoApprovalData()
        getAdminHomeAudioDharmagitaNoApprovalData()
        toListDharmagita.setOnClickListener {
            val intent = Intent(this, ListDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
        }
        toListVideo.setOnClickListener {
            val intent = Intent(this, ListVideoDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
        }
        toListAudio.setOnClickListener {
            val intent = Intent(this, ListAudioDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAdminHomeDharmagitaNoApprovalData() {
        ApiService.endpoint.getDharmagitaNoApprovalAdminHomeList()
            .enqueue(object: Callback<JumlahModel> {
                override fun onResponse(
                    call: Call<JumlahModel>,
                    response: Response<JumlahModel>
                ) {
                    val datalist   = response.body()?.jumlah
//                    if(datalist != null){
//                        swipeAdmin.visibility = View.VISIBLE
//                        shimmerHomeAdmin.visibility = View.GONE
//                    }else{
//                        swipeAdmin.visibility = View.GONE
//                        shimmerHomeAdmin.visibility = View.VISIBLE
//                    }
                    no_approve_dharmagita.text = datalist.toString()
                }

                override fun onFailure(call: Call<JumlahModel>, t: Throwable) {
                    Toast.makeText(this@ListAllDharmagitaNotApprovalActivity, "No Connection", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getAdminHomeVideoDharmagitaNoApprovalData() {
        ApiService.endpoint.getVideoNoApprovalAdminHomeList()
            .enqueue(object: Callback<JumlahModel> {
                override fun onResponse(
                    call: Call<JumlahModel>,
                    response: Response<JumlahModel>
                ) {
                    val datalist   = response.body()?.jumlah
                    Log.d("video_jumlah",datalist.toString())
//                    if(datalist != null){
//                        swipeAdmin.visibility = View.VISIBLE
//                        shimmerHomeAdmin.visibility = View.GONE
//                    }else{
//                        swipeAdmin.visibility = View.GONE
//                        shimmerHomeAdmin.visibility = View.VISIBLE
//                    }
                    no_approve_video.text = datalist.toString()
                }

                override fun onFailure(call: Call<JumlahModel>, t: Throwable) {
                    Toast.makeText(this@ListAllDharmagitaNotApprovalActivity, "No Connection", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getAdminHomeAudioDharmagitaNoApprovalData() {
        ApiService.endpoint.getAudioNoApprovalAdminHomeList()
            .enqueue(object: Callback<JumlahModel> {
                override fun onResponse(
                    call: Call<JumlahModel>,
                    response: Response<JumlahModel>
                ) {
                    val datalist   = response.body()?.jumlah
                    Log.d("audio_jumlah",datalist.toString())
//                    if(datalist != null){
//                        swipeAdmin.visibility = View.VISIBLE
//                        shimmerHomeAdmin.visibility = View.GONE
//                    }else{
//                        swipeAdmin.visibility = View.GONE
//                        shimmerHomeAdmin.visibility = View.VISIBLE
//                    }
                    no_approve_audio.text = datalist.toString()
                }

                override fun onFailure(call: Call<JumlahModel>, t: Throwable) {
                    Toast.makeText(this@ListAllDharmagitaNotApprovalActivity, "No Connection", Toast.LENGTH_SHORT).show()
                }

            })
    }
}