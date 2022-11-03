package com.example.ekidungmantram.admin.pupuh

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
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.user.AllKategoriPupuhActivity
import kotlinx.android.synthetic.main.activity_add_pupuh.*
import kotlinx.android.synthetic.main.activity_add_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class AddPupuhAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var desc_pupuh :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pupuh_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Pupuh"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_kat_pupuh_admin")
            nama_pupuh = bundle.getString("nama_kat_pupuh_admin").toString()
           desc_pupuh = bundle.getString("desc_kat_pupuh_admin").toString()
        }

        selectImagePupuhAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitPupuhAdmin.setOnClickListener {
            val nama_post     = namaPupuhAdmin.text.toString()
            val deskripsi     = deskripsiPupuhAdmin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postPupuhAdmin(nama_post, deskripsi, gambar)
            }
        }

        cancelSubmitAddPupuhAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun postPupuhAdmin(namaPost: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataPupuhAdmin(namaPost, deskripsi, gambar, id_pupuh)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriPupuhAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh_admin", id_pupuh)
        bundle.putString("nama_pupuh_admin", nama_pupuh)
        bundle.putString("desc_pupuh_admin", desc_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaPupuhAdmin.text.toString().isEmpty()){
            layoutNamaPupuhAdmin.isErrorEnabled = true
            layoutNamaPupuhAdmin.error = "Nama Pupuh tidak boleh kosong!"
            return false
        }

        if(deskripsiPupuhAdmin.text.toString().isEmpty()){
            layoutDeskripsiPupuhAdmin.isErrorEnabled = true
            layoutDeskripsiPupuhAdmin.error = "Deskripsi Pupuh tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgPupuh.setImageURI(imgUri) // handle chosen image
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