package com.example.ekidungmantram.admin.pupuh

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_lirik_kidung_admin.*
import kotlinx.android.synthetic.main.activity_add_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Response

class AddLirikPupuhAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lirik_pupuh_admin)
        supportActionBar!!.title = "Tambah Lirik Sekar Alit"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            Log.d("id_add_pupuh", postID.toString())

            submitLirikPupuh.setOnClickListener {
                val lirikPupuh     = lirikPupuhForm.text.toString()
                if(validateInput()){
                    postLirik(postID, lirikPupuh, namaPost!!)
                }
            }

            cancelSubmitLirikPupuh.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun postLirik(postID: Int, lirikPupuh: String, nama:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataLirikPupuhAdmin(postID, lirikPupuh).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddLirikPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikPupuhForm.text.toString().isEmpty()){
            layoutAddLirikPupuh.isErrorEnabled = true
            layoutAddLirikPupuh.error = "Lirik Pupuh tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun goBack(id: Int, nama: String) {
        val bundle = Bundle()
        val intent = Intent(this, AllLirikPupuhAdminActivity::class.java)
        bundle.putInt("id_pupuh", id)
        bundle.putString("nama_pupuh", nama)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}