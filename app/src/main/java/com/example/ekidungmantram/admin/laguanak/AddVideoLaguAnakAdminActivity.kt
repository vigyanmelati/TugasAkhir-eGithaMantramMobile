package com.example.ekidungmantram.admin.laguanak

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.pupuh.AllVideoPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_video_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_add_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AddVideoLaguAnakAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_lagu_anak : Int = 0

    private lateinit var nama_lagu_anak :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video_lagu_anak_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Video Sekar Rare"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_lagu_anak = bundle.getInt("id_lagu_anak")
            nama_lagu_anak = bundle.getString("nama_kat_lagu_anak_admin").toString()
        }

        selectImageVideoLaguAnakAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitVideoLaguAnakAdmin.setOnClickListener {
            val judul_video     = namaVideoLaguAnakAdmin.text.toString()
            val video     = linkVideoLaguAnakAdmin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postVideoLaguAnakAdmin(judul_video, video, gambar)
            }
        }

        cancelSubmitAddVideoLaguAnakAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun postVideoLaguAnakAdmin(judul_video: String, video: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataVideoLaguAnakAdmin(id_lagu_anak, judul_video, video, gambar)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddVideoLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllVideoLaguAnakAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_lagu_anak", id_lagu_anak)
        bundle.putString("nama_lagu_anak", nama_lagu_anak)
//        bundle.putString("desc_pupuh_admin", desc_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaVideoLaguAnakAdmin.text.toString().isEmpty()){
            layoutNamaVideoLaguAnakAdmin.isErrorEnabled = true
            layoutNamaVideoLaguAnakAdmin.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(linkVideoLaguAnakAdmin.text.toString().isEmpty()){
            layoutLinkVideoLaguAnakAdmin.isErrorEnabled = true
            layoutLinkVideoLaguAnakAdmin.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgVideoLaguAnakAdmin.setImageURI(imgUri) // handle chosen image
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imgUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bitmapToString(bitmap: Bitmap?): String? {
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val array = byteArrayOutputStream.toByteArray()
            return Base64.getEncoder().encodeToString(array)
        }
        return ""
    }
}