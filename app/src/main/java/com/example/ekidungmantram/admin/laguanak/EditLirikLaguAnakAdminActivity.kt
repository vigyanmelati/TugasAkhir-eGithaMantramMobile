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
import com.example.ekidungmantram.model.adminmodel.DetailLirikLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailLirikPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_lirik_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_edit_lirik_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLirikLaguAnakAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lirik_lagu_anak_admin)
        supportActionBar!!.title = "Edit Lirik Sekar Rare"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val lirikID = bundle.getInt("id_lirik_lagu_anak")
            val postID = bundle.getInt("id_lagu_anak")
            val namaPost = bundle.getString("nama_lagu_anak")
            Log.d("id_edit_lirik", lirikID.toString())
            getDetailData(lirikID)

            submitEditedLirikLaguAnak.setOnClickListener {
                val lirikLaguAnak = lirikEditedLaguAnak.text.toString()
                if(validateInput()){
                    editLirik(postID, lirikID, lirikLaguAnak, namaPost!!)
                }
            }

            cancelSubmitEditedLirikLaguAnak.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun editLirik(postId:Int, lirikID: Int, lirikLaguAnak: String, nama: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataLirikLaguAnakAdmin(lirikID, lirikLaguAnak).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postId, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@EditLirikLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikEditedLaguAnak.text.toString().isEmpty()){
            layoutEditedLirikLaguAnak.isErrorEnabled = true
            layoutEditedLirikLaguAnak.error = "Lirik Sekar Rare tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun getDetailData(lirikID: Int) {
        ApiService.endpoint.getShowLirikLaguAnakAdmin(lirikID).enqueue(object:
            Callback<DetailLirikLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailLirikLaguAnakAdminModel>,
                response: Response<DetailLirikLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    lirikEditedLaguAnak.setText(result.bait_lagu)
                }
            }

            override fun onFailure(call: Call<DetailLirikLaguAnakAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
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