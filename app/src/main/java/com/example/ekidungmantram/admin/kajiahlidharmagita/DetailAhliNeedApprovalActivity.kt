package com.example.ekidungmantram.admin.kajiahlidharmagita

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailDataAdminModel
import kotlinx.android.synthetic.main.activity_detail_admin.*
import kotlinx.android.synthetic.main.activity_detail_ahli_need_approval.*
import kotlinx.android.synthetic.main.activity_detail_mantram_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailAhliNeedApprovalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ahli_need_approval)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detail Ahli Dharmagita"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val userID = bundle.getInt("id_user")
            getDetailData(userID)

            acceptAhli.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Terima Ahli Dharmagita")
                    .setMessage("Apakah anda yakin ingin terima ahli Dharmagita ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accAhli(userID, "yes")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            rejectAhli.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Tolak Ahli Dharmagita")
                    .setMessage("Apakah anda yakin ingin menolak hli Dharmagita ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accAhli(userID, "no")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

    }

    private fun accAhli(userID: Int, s: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengupdate Data")
        progressDialog.show()
        ApiService.endpoint.approveAhli(userID, s).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailAhliNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailAhliNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailAhliNeedApprovalActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDetailData(userID: Int) {
        ApiService.endpoint.getDetailNeedApprovalAhliAdmin(userID).enqueue(object: Callback<DetailDataAdminModel> {
            override fun onResponse(
                call: Call<DetailDataAdminModel>,
                response: Response<DetailDataAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaDetailAhliNA.setText(result.name)
                    emailDetailAhliNA.setText(result.email)
//                    toFileAhli.setOnClickListener {
//                        val bundle = Bundle()
//                        val intent = Intent(this@DetailAhliNeedApprovalActivity, PdfViewAhliDharmagitaActivity::class.java)
//                        bundle.putString("file_ahli", result.file)
//                        intent.putExtras(bundle)
//                        startActivity(intent)
//                    }
//                    toFileAhli.setOnClickListener {
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.file))
//                        startActivity(intent)
//                    }

                    toFileAhli.setOnClickListener {
                        val pdfUrl = Constant.PDF_URL + result.file
                        val pdfIntent = Intent(Intent.ACTION_VIEW)
                        pdfIntent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
                        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        try {
                            startActivity(pdfIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this@DetailAhliNeedApprovalActivity, "No PDF viewer app found", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this, ListAhliNeedApprovalActivity::class.java)
        startActivity(intent)
        finish()
    }
}