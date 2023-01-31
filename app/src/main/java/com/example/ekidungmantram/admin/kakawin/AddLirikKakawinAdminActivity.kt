package com.example.ekidungmantram.admin.kakawin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kakawin.AllLirikKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_lirik_kakawin_admin.*
import retrofit2.Call
import retrofit2.Response

class AddLirikKakawinAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lirik_kakawin_admin)
        supportActionBar!!.title = "Tambah Lirik Sekar Agung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")
            Log.d("id_add_kakawin", postID.toString())

            submitLirikKakawin.setOnClickListener {
                val lirikKakawin     = lirikKakawinForm.text.toString()
                val artiKakawin     = artiKakawinForm.text.toString()
                if(validateInput()){
                    postLirik(postID, lirikKakawin, namaPost!!, artiKakawin)
                }
            }

            cancelSubmitLirikKakawin.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun postLirik(postID: Int, lirikKakawin: String, nama:String, artiKakawin: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataLirikKakawinAdmin(postID, lirikKakawin, artiKakawin).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddLirikKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikKakawinForm.text.toString().isEmpty()){
            layoutAddLirikKakawin.isErrorEnabled = true
            layoutAddLirikKakawin.error = "Lirik Sekar Agung tidak boleh kosong!"
            return false
        }

        if(artiKakawinForm.text.toString().isEmpty()){
            layoutAddArtiKakawin.isErrorEnabled = true
            layoutAddArtiKakawin.error = "Arti Sekar Agung tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun goBack(id: Int, nama: String) {
        val bundle = Bundle()
        val intent = Intent(this, AllLirikKakawinAdminActivity::class.java)
        bundle.putInt("id_kakawin", id)
        bundle.putString("nama_kakawin", nama)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}