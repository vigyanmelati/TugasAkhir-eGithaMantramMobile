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
import com.example.ekidungmantram.model.DetailLirikPupuhModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailLirikPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_lirik_pupuh.*
import kotlinx.android.synthetic.main.activity_edit_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLirikPupuhActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lirik_pupuh)
        supportActionBar!!.title = "Edit Lirik Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val lirikID = bundle.getInt("id_lirik_pupuh")
            val postID = bundle.getInt("id_pupuh")
            val namaPost = bundle.getString("nama_pupuh")
            Log.d("id_edit_lirik", lirikID.toString())
            getDetailData(lirikID)

            submitEditedLirikPupuhUser.setOnClickListener {
                val lirikPupuh = lirikEditedPupuh.text.toString()
                if(validateInput()){
                    editLirik(postID, lirikID, lirikPupuh, namaPost!!)
                }
            }

            cancelSubmitEditedLirikPupuhUser.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun editLirik(postId:Int, lirikID: Int, lirikPupuh: String, nama: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataLirikPupuh(lirikID, lirikPupuh).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postId, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@EditLirikPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikEditedPupuhUser.text.toString().isEmpty()){
            layoutEditedLirikPupuhUser.isErrorEnabled = true
            layoutEditedLirikPupuhUser.error = "Lirik Pupuh tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun getDetailData(lirikID: Int) {
        ApiService.endpoint.getShowLirikPupuh(lirikID).enqueue(object:
            Callback<DetailLirikPupuhModel> {
            override fun onResponse(
                call: Call<DetailLirikPupuhModel>,
                response: Response<DetailLirikPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    lirikEditedPupuhUser.setText(result.bait_pupuh)
                }
            }

            override fun onFailure(call: Call<DetailLirikPupuhModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
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