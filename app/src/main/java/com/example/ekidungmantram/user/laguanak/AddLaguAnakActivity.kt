package com.example.ekidungmantram.user.laguanak

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
import com.example.ekidungmantram.user.laguanak.AllKategoriLaguAnakUserActivity
import kotlinx.android.synthetic.main.activity_add_lagu_anak.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class AddLaguAnakActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_lagu_anak : Int = 0
    private lateinit var nama_lagu_anak :String
    private lateinit var desc_lagu_anak :String
    private var id_user: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lagu_anak)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Sekar Rare"

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)
        val id            = sharedPreferences.getString("ID_ADMIN", null)
        if (id != null) {
            id_user =  id.toInt()
        }

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_lagu_anak = bundle.getInt("id_kat_lagu_anak")
            nama_lagu_anak = bundle.getString("nama_kat_lagu_anak").toString()
            desc_lagu_anak = bundle.getString("desc_kat_lagu_anak").toString()
        }


        selectImageLaguAnak.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitLaguAnak.setOnClickListener {
            val nama_post     = namaLaguAnak.text.toString()
            val deskripsi     = deskripsiLaguAnak.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postLaguAnak(nama_post, deskripsi, gambar, id_user)
            }
        }

        cancelSubmitAddLaguAnak.setOnClickListener {
            goBack()
        }
    }

    private fun postLaguAnak(namaPost: String, deskripsi: String, gambar: String, id_user: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataLaguAnak(namaPost, deskripsi, gambar, id_lagu_anak, id_user)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddLaguAnakActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddLaguAnakActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLaguAnakActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriLaguAnakUserActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_lagu_anak", id_lagu_anak)
        bundle.putString("nama_lagu_anak", nama_lagu_anak)
        bundle.putString("desc_lagu_anak", desc_lagu_anak)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaLaguAnak.text.toString().isEmpty()){
            layoutNamaLaguAnak.isErrorEnabled = true
            layoutNamaLaguAnak.error = "Nama LaguAnak tidak boleh kosong!"
            return false
        }

        if(deskripsiLaguAnak.text.toString().isEmpty()){
            layoutDeskripsiLaguAnak.isErrorEnabled = true
            layoutDeskripsiLaguAnak.error = "Deskripsi LaguAnak tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgLaguAnak.setImageURI(imgUri) // handle chosen image
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