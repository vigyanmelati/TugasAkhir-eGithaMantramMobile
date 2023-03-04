package com.example.ekidungmantram.user.pupuh

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_add_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class AddPupuhActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var desc_pupuh :String
    private var id_user: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pupuh)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Pupuh"

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)
        val id            = sharedPreferences.getString("ID_ADMIN", null)
        if (id != null) {
           id_user =  id.toInt()
        }

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_kat_pupuh")
            nama_pupuh = bundle.getString("nama_kat_pupuh").toString()
            desc_pupuh = bundle.getString("desc_kat_pupuh").toString()
        }


        selectImagePupuh.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitPupuh.setOnClickListener {
            val nama_post     = namaPupuh.text.toString()
            val deskripsi     = deskripsiPupuh.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postPupuh(nama_post, deskripsi, gambar, id_user)
            }
        }

        cancelSubmitAddPupuh.setOnClickListener {
            goBack()
        }
    }

    private fun postPupuh(namaPost: String, deskripsi: String, gambar: String, id_user: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataPupuh(namaPost, deskripsi, gambar, id_pupuh, id_user)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriPupuhUserActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
        bundle.putString("desc_pupuh", desc_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaPupuh.text.toString().isEmpty()){
            layoutNamaPupuh.isErrorEnabled = true
            layoutNamaPupuh.error = "Nama Pupuh tidak boleh kosong!"
            return false
        }

        if(deskripsiPupuh.text.toString().isEmpty()){
            layoutDeskripsiPupuh.isErrorEnabled = true
            layoutDeskripsiPupuh.error = "Deskripsi Pupuh tidak boleh kosong!"
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