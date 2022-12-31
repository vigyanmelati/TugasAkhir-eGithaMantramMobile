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
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kakawin.AllKategoriKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailKakawinAdminModel
import kotlinx.android.synthetic.main.activity_edit_audio_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_edit_kakawin_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditKakawinAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kakawin_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kakawin")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Sekar Agung"

            selectEditedImageKakawin.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedKakawin.setOnClickListener {
                val nama_post     = namaEditedKakawin.text.toString()
                val deskripsi     = deskripsiEditedKakawin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedKakawin(postID, nama_post, deskripsi, gambar)
                }
            }

            cancelSubmitEditedKakawin.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowKakawinAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailKakawinAdminModel>,
                response: Response<DetailKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedKakawin.setText(result.deskripsi)
                    namaEditedKakawin.setText(result.nama_post)
                    Glide.with(this@EditKakawinAdminActivity)
                        .load(result.gambar).into(submitEditedImgKakawin)
//                        .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgKakawin)
                }
            }

            override fun onFailure(call: Call<DetailKakawinAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedKakawin(postID: Int, namaPost: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateKakawinAdmin(postID ,namaPost, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.getItemAtPosition(p2).toString()
        yadnya = item
    }

    private fun goBack() {
        val intent = Intent(this, AllKategoriKakawinAdminActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedKakawin.text.toString().isEmpty()){
            layoutEditedNamaKakawin.isErrorEnabled = true
            layoutEditedNamaKakawin.error = "Nama Kakawin tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedKakawin.text.toString().isEmpty()){
            layoutEditedDeskripsiKakawin.isErrorEnabled = true
            layoutEditedDeskripsiKakawin.error = "Deskripsi Kakawin tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgKakawin.setImageURI(imgUri) // handle chosen image
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