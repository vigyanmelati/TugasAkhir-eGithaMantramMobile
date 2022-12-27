package com.example.ekidungmantram.admin.kakawin

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
import com.example.ekidungmantram.admin.kakawin.AllAudioKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_audio_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AddAudioKakawinAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kakawin : Int = 0

    private lateinit var nama_kakawin :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_audio_kakawin_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Audio Sekar Agung"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kat_kakawin_admin").toString()
        }

        selectImageAudioKakawinAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitAudioKakawinAdmin.setOnClickListener {
            val judul_audio     = namaAudioKakawinAdmin.text.toString()
            val audio     = linkAudioKakawinAdmin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postAudioKakawinAdmin(judul_audio, audio, gambar)
            }
        }

        cancelSubmitAddAudioKakawinAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun postAudioKakawinAdmin(judul_audio: String, audio: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataAudioKakawinAdmin(id_kakawin, judul_audio, gambar, audio)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddAudioKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddAudioKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddAudioKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllAudioKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
//        bundle.putString("desc_kakawin_admin", desc_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaAudioKakawinAdmin.text.toString().isEmpty()){
            layoutNamaAudioKakawinAdmin.isErrorEnabled = true
            layoutNamaAudioKakawinAdmin.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(linkAudioKakawinAdmin.text.toString().isEmpty()){
            layoutLinkAudioKakawinAdmin.isErrorEnabled = true
            layoutLinkAudioKakawinAdmin.error = "Link audio tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgAudioKakawinAdmin.setImageURI(imgUri) // handle chosen image
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