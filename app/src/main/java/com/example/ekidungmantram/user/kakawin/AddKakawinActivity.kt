package com.example.ekidungmantram.user.kakawin

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
import com.example.ekidungmantram.user.kakawin.AllKategoriKakawinUserActivity
import kotlinx.android.synthetic.main.activity_add_kakawin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class AddKakawinActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kakawin : Int = 0
    private lateinit var nama_kakawin :String
    private lateinit var desc_kakawin :String
    private var id_user: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kakawin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Sekar Agung"

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)
        val id            = sharedPreferences.getString("ID_ADMIN", null)
        if (id != null) {
            id_user =  id.toInt()
        }

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kat_kakawin")
            nama_kakawin = bundle.getString("nama_kat_kakawin").toString()
            desc_kakawin = bundle.getString("desc_kat_kakawin").toString()
        }


        selectImageKakawin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitKakawin.setOnClickListener {
            val nama_post     = namaKakawin.text.toString()
            val deskripsi     = deskripsiKakawin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postKakawin(nama_post, deskripsi, gambar, id_user)
            }
        }

        cancelSubmitAddKakawin.setOnClickListener {
            goBack()
        }
    }

    private fun postKakawin(namaPost: String, deskripsi: String, gambar: String, id_user: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataKakawin(namaPost, deskripsi, gambar, id_kakawin, id_user)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddKakawinActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddKakawinActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddKakawinActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriKakawinUserActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
        bundle.putString("desc_kakawin", desc_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaKakawin.text.toString().isEmpty()){
            layoutNamaKakawin.isErrorEnabled = true
            layoutNamaKakawin.error = "Nama Kakawin tidak boleh kosong!"
            return false
        }

        if(deskripsiKakawin.text.toString().isEmpty()){
            layoutDeskripsiKakawin.isErrorEnabled = true
            layoutDeskripsiKakawin.error = "Deskripsi Kakawin tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgKakawin.setImageURI(imgUri) // handle chosen image
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