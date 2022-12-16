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
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kakawin.AllAudioKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioKakawinAdminModel
import kotlinx.android.synthetic.main.activity_edit_audio_kakawin_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditAudioKakawinAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kakawin : Int = 0
    private lateinit var nama_kakawin :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_audio_kakawin_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val audioID = bundle.getInt("id_audio_kakawin")
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kakawin").toString()

            setFormData(audioID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Audio Sekar Alit"

            selectEditedImageAudioKakawin.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedAudioKakawin.setOnClickListener {
                val nama_post     = namaEditedAudioKakawin.text.toString()
                val link     = namaEditedLinkAudioKakawin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedKakawin(audioID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedAudioKakawin.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowAudioKakawinAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailAudioKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailAudioKakawinAdminModel>,
                response: Response<DetailAudioKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkAudioKakawin.setText(result.audio)
                    namaEditedAudioKakawin.setText(result.judul_audio)
                    Glide.with(this@EditAudioKakawinAdminActivity).load(Constant.IMAGE_URL+result.gambar_audio).into(submitEditedImgAudioKakawin)
                }
            }

            override fun onFailure(call: Call<DetailAudioKakawinAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedKakawin(postID: Int, judul_audio: String, gambar_audio: String, audio: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataAudioKakawinAdmin(postID ,judul_audio, gambar_audio, audio)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAudioKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAudioKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditAudioKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllAudioKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedAudioKakawin.text.toString().isEmpty()){
            layoutEditedNamaAudioKakawin.isErrorEnabled = true
            layoutEditedNamaAudioKakawin.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkAudioKakawin.text.toString().isEmpty()){
            layoutEditedLinkAudioKakawin.isErrorEnabled = true
            layoutEditedLinkAudioKakawin.error = "Link audio tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgAudioKakawin.setImageURI(imgUri) // handle chosen image
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