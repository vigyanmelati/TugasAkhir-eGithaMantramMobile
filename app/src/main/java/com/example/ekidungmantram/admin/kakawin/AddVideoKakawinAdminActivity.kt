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
import com.example.ekidungmantram.admin.kakawin.AllVideoKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_video_kakawin_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AddVideoKakawinAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kakawin : Int = 0

    private lateinit var nama_kakawin :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video_kakawin_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Video Sekar Agung"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kat_kakawin_admin").toString()
        }

        selectImageVideoKakawinAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitVideoKakawinAdmin.setOnClickListener {
            val judul_video     = namaVideoKakawinAdmin.text.toString()
            val video     = linkVideoKakawinAdmin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postVideoKakawinAdmin(judul_video, video, gambar)
            }
        }

        cancelSubmitAddVideoKakawinAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun postVideoKakawinAdmin(judul_video: String, video: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataVideoKakawinAdmin(id_kakawin, judul_video, video, gambar)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddVideoKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllVideoKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
//        bundle.putString("desc_kakawin_admin", desc_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaVideoKakawinAdmin.text.toString().isEmpty()){
            layoutNamaVideoKakawinAdmin.isErrorEnabled = true
            layoutNamaVideoKakawinAdmin.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(linkVideoKakawinAdmin.text.toString().isEmpty()){
            layoutLinkVideoKakawinAdmin.isErrorEnabled = true
            layoutLinkVideoKakawinAdmin.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgVideoKakawinAdmin.setImageURI(imgUri) // handle chosen image
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