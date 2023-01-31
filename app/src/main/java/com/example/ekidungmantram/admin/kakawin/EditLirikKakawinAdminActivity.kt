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
import com.example.ekidungmantram.model.adminmodel.DetailLirikKakawinAdminModel
import kotlinx.android.synthetic.main.activity_edit_lirik_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLirikKakawinAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lirik_kakawin_admin)
        supportActionBar!!.title = "Edit Lirik Sekar Agung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val lirikID = bundle.getInt("id_lirik_kakawin")
            val postID = bundle.getInt("id_kakawin")
            val namaPost = bundle.getString("nama_kakawin")
            Log.d("id_edit_lirik", lirikID.toString())
            getDetailData(lirikID)

            submitEditedLirikKakawin.setOnClickListener {
                val lirikKakawin = lirikEditedKakawin.text.toString()
                val artiKakawin = artiEditedKakawin.text.toString()
                if(validateInput()){
                    editLirik(postID, lirikID, lirikKakawin, namaPost!!, artiKakawin)
                }
            }

            cancelSubmitEditedLirikKakawin.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun editLirik(postId:Int, lirikID: Int, lirikKakawin: String, nama: String, artiKakawin: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataLirikKakawinAdmin(lirikID, lirikKakawin, artiKakawin).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postId, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@EditLirikKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikEditedKakawin.text.toString().isEmpty()){
            layoutEditedLirikKakawin.isErrorEnabled = true
            layoutEditedLirikKakawin.error = "Lirik Sekar Agung tidak boleh kosong!"
            return false
        }
        if(artiEditedKakawin.text.toString().isEmpty()){
            layoutEditedArtiKakawin.isErrorEnabled = true
            layoutEditedArtiKakawin.error = "Arti Lirik Sekar Agung tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun getDetailData(lirikID: Int) {
        ApiService.endpoint.getShowLirikKakawinAdmin(lirikID).enqueue(object:
            Callback<DetailLirikKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailLirikKakawinAdminModel>,
                response: Response<DetailLirikKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    lirikEditedKakawin.setText(result.bait_sekar_agung)
                    artiEditedKakawin.setText(result.arti_sekar_agung)
                }
            }

            override fun onFailure(call: Call<DetailLirikKakawinAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
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