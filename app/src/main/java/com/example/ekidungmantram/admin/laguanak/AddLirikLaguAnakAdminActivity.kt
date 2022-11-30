package com.example.ekidungmantram.admin.laguanak

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
import kotlinx.android.synthetic.main.activity_add_lirik_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_add_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Response

class AddLirikLaguAnakAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lirik_lagu_anak_admin)
        supportActionBar!!.title = "Tambah Lirik Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")
            Log.d("id_add_lagu_anak", postID.toString())

            submitLirikLaguAnak.setOnClickListener {
                val lirikLaguAnak     = lirikLaguAnakForm.text.toString()
                if(validateInput()){
                    postLirik(postID, lirikLaguAnak, namaPost!!)
                }
            }

            cancelSubmitLirikLaguAnak.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun postLirik(postID: Int, lirikLaguAnak: String, nama:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataLirikLaguAnakAdmin(postID, lirikLaguAnak).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddLirikLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikLaguAnakForm.text.toString().isEmpty()){
            layoutAddLirikLaguAnak.isErrorEnabled = true
            layoutAddLirikLaguAnak.error = "Lirik Sekar Rare tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun goBack(id: Int, nama: String) {
        val bundle = Bundle()
        val intent = Intent(this, AllLirikLaguAnakAdminActivity::class.java)
        bundle.putInt("id_lagu_anak", id)
        bundle.putString("nama_lagu_anak", nama)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}