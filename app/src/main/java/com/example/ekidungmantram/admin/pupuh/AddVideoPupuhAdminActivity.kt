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
import kotlinx.android.synthetic.main.activity_add_pupuh.*
import kotlinx.android.synthetic.main.activity_add_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_add_video_pupuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AddVideoPupuhAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0

    private lateinit var nama_pupuh :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video_pupuh_admin)
        supportActionBar!!.title = "Tambah Video Sekar Alit"

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_kat_pupuh_admin").toString()
        }

        selectImageVideoPupuhAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitVideoPupuhAdmin.setOnClickListener {
            val judul_video     = namaVideoPupuhAdmin.text.toString()
            val video     = linkVideoPupuhAdmin.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postVideoPupuhAdmin(judul_video, video, gambar)
            }
        }

        cancelSubmitAddVideoPupuhAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun postVideoPupuhAdmin(judul_video: String, video: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataVideoPupuhAdmin(id_pupuh, judul_video, gambar, video)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddVideoPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddVideoPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllVideoPupuhAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
//        bundle.putString("desc_pupuh_admin", desc_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaVideoPupuhAdmin.text.toString().isEmpty()){
            layoutNamaVideoPupuhAdmin.isErrorEnabled = true
            layoutNamaVideoPupuhAdmin.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(linkVideoPupuhAdmin.text.toString().isEmpty()){
            layoutLinkVideoPupuhAdmin.isErrorEnabled = true
            layoutLinkVideoPupuhAdmin.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgVideoPupuhAdmin.setImageURI(imgUri) // handle chosen image
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