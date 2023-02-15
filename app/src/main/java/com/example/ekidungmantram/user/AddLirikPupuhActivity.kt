package com.example.ekidungmantram.user

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.pupuh.AllLirikPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_lirik_pupuh.*
import kotlinx.android.synthetic.main.activity_add_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Response

class AddLirikPupuhActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lirik_pupuh)
        supportActionBar!!.title = "Tambah Lirik Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            val padalingsa = bundle.getString("padalingsa")
            Log.d("id_add_pupuh", postID.toString())

            submitLirikPupuhUser.setOnClickListener {
                val lirikPupuh     = lirikPupuhForm.text.toString()
                val artiPupuh     = artiPupuhForm.text.toString()
                if(validateInput()){
                    postLirik(postID, lirikPupuh, namaPost!!, artiPupuh)
                }
            }

            padalingsa_user.text = padalingsa

            cancelSubmitLirikPupuhUser.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun postLirik(postID: Int, lirikPupuh: String, nama:String, artiPupuh: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataLirikPupuh(postID, lirikPupuh, artiPupuh).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddLirikPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikPupuhFormUser.text.toString().isEmpty()){
            layoutAddLirikPupuhUser.isErrorEnabled = true
            layoutAddLirikPupuhUser.error = "Lirik Pupuh tidak boleh kosong!"
            return false
        }
        if(artiPupuhFormUser.text.toString().isEmpty()){
            layoutAddArtiPupuhUser.isErrorEnabled = true
            layoutAddArtiPupuhUser.error = "Arti lirik Pupuh tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun goBack(id: Int, nama: String) {
        val bundle = Bundle()
        val intent = Intent(this, AllLirikPupuhActivity::class.java)
        bundle.putInt("id_pupuh", id)
        bundle.putString("nama_pupuh", nama)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}