package com.example.ekidungmantram.admin.kidung

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
import com.example.ekidungmantram.admin.kidung.AllAudioKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_audio_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AddAudioKidungAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kidung : Int = 0

    private lateinit var nama_kidung :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_audio_kidung_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Audio Sekar Madya"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kidung = bundle.getInt("id_kidung")
            nama_kidung = bundle.getString("nama_kat_kidung_admin").toString()
        }

        selectImageAudioKidungAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitAudioKidungAdmin.setOnClickListener {
            val judul_audio     = namaAudioKidungAdmin.text.toString()
            val audio     = linkAudioKidungAdmin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
//            if(validateInput()){
//                postAudioKidungAdmin(judul_audio, audio, gambar)
//            }
        }

        cancelSubmitAddAudioKidungAdmin.setOnClickListener {
            goBack()
        }
    }

//    private fun postAudioKidungAdmin(judul_audio: String, audio: String, gambar: String) {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Mengunggah Data")
//        progressDialog.show()
//        ApiService.endpoint.createDataAudioKidungAdmin(id_kidung, judul_audio, gambar, audio)
//            .enqueue(object: Callback<CrudModel> {
//                override fun onResponse(
//                    call: Call<CrudModel>,
//                    response: Response<CrudModel>
//                ) {
//                    if(response.body()?.status == 200){
//                        progressDialog.dismiss()
//                        Toast.makeText(this@AddAudioKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                        goBack()
//                    }else{
//                        progressDialog.dismiss()
//                        Toast.makeText(this@AddAudioKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Toast.makeText(this@AddAudioKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
//                }
//
//            })
//    }

    private fun goBack() {
        val intent = Intent(this, AllAudioKidungAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kidung", id_kidung)
        bundle.putString("nama_kidung", nama_kidung)
//        bundle.putString("desc_kidung_admin", desc_kidung)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaAudioKidungAdmin.text.toString().isEmpty()){
            layoutNamaAudioKidungAdmin.isErrorEnabled = true
            layoutNamaAudioKidungAdmin.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(linkAudioKidungAdmin.text.toString().isEmpty()){
            layoutLinkAudioKidungAdmin.isErrorEnabled = true
            layoutLinkAudioKidungAdmin.error = "Link audio tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgAudioKidungAdmin.setImageURI(imgUri) // handle chosen image
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