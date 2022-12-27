package com.example.ekidungmantram.user

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
import kotlinx.android.synthetic.main.activity_add_video_pupuh.*
import kotlinx.android.synthetic.main.activity_add_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AddVideoPupuhActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0

    private lateinit var nama_pupuh :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video_pupuh)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Video Pupuh"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_kat_pupuh_user").toString()
        }

        selectImageVideoPupuhUser.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitVideoPupuhUser.setOnClickListener {
            val judul_video     = namaVideoPupuhUser.text.toString()
            val video     = linkVideoPupuhUser.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postVideoPupuhUser(judul_video, video, gambar)
            }
        }

        cancelSubmitAddVideoPupuhUser.setOnClickListener {
            goBack()
        }
    }

    private fun postVideoPupuhUser(judul_video: String, video: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataVideoPupuh(id_pupuh, judul_video, gambar, video)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddVideoPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllVideoPupuhActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
//        bundle.putString("desc_pupuh_admin", desc_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaVideoPupuhUser.text.toString().isEmpty()){
            layoutNamaVideoPupuhUser.isErrorEnabled = true
            layoutNamaVideoPupuhUser.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(linkVideoPupuhUser.text.toString().isEmpty()){
            layoutLinkVideoPupuhUser.isErrorEnabled = true
            layoutLinkVideoPupuhUser.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgVideoPupuhUser.setImageURI(imgUri) // handle chosen image
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