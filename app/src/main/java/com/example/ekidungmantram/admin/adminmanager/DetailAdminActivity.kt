package com.example.ekidungmantram.admin.adminmanager

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.mantram.AllMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailDataAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import kotlinx.android.synthetic.main.activity_detail_admin.*
import kotlinx.android.synthetic.main.activity_detail_ahli_need_approval.*
import kotlinx.android.synthetic.main.activity_detail_mantram_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detail Ahli Dharmagita"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val userID = bundle.getInt("id_user")
            getDetailData(userID)

//            toEditAdmin.setOnClickListener {
//                goToEditAdmin(userID)
//            }

            deleteAdmin.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Ahli Dharmagita")
                    .setMessage("Apakah anda yakin ingin menghapus ahli Dharmagita ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusAdmin(userID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }
    }

    private fun hapusAdmin(userID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAdmin(userID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goToEditAdmin(userID: Int) {
        val bundle = Bundle()
        val intent = Intent(this, EditAdminActivity::class.java)
        bundle.putInt("id_user", userID)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun getDetailData(userID: Int) {
        ApiService.endpoint.getDetailAdmin(userID).enqueue(object: Callback<DetailDataAdminModel> {
            override fun onResponse(
                call: Call<DetailDataAdminModel>,
                response: Response<DetailDataAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaDetailAdmin.setText(result.name)
                    emailDetailAdmin.setText(result.email)
                    toFileAhliDharmagita.setOnClickListener {
                        val pdfUrl = Constant.PDF_URL + result.file
                        val pdfIntent = Intent(Intent.ACTION_VIEW)
                        pdfIntent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
                        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        try {
                            startActivity(pdfIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this@DetailAdminActivity, "No PDF viewer app found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DetailDataAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, AllAdminActivity::class.java)
        startActivity(intent)
        finish()
    }
}