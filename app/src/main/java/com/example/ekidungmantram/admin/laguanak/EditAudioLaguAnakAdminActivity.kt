package com.example.ekidungmantram.admin.laguanak

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
import com.example.ekidungmantram.admin.pupuh.AllAudioPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_audio_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_edit_audio_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_kakawin_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditAudioLaguAnakAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_lagu_anak : Int = 0
    private lateinit var nama_lagu_anak :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_audio_lagu_anak_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val audioID = bundle.getInt("id_audio_lagu_anak")
            id_lagu_anak = bundle.getInt("id_lagu_anak")
            nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()

            setFormData(audioID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Audio Sekar Rare"

            selectEditedImageAudioLaguAnak.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedAudioLaguAnak.setOnClickListener {
                val nama_post     = namaEditedAudioLaguAnak.text.toString()
                val link     = namaEditedLinkAudioLaguAnak.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedLaguAnak(audioID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedAudioLaguAnak.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowAudioLaguAnakAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailAudioLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailAudioLaguAnakAdminModel>,
                response: Response<DetailAudioLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkAudioLaguAnak.setText(result.audio)
                    namaEditedAudioLaguAnak.setText(result.judul_audio)
                    Glide.with(this@EditAudioLaguAnakAdminActivity)
                        .load(result.gambar_audio).into(submitEditedImgAudioLaguAnak)
//                        .load(Constant.IMAGE_URL+result.gambar_audio).into(submitEditedImgAudioLaguAnak)
                }
            }

            override fun onFailure(call: Call<DetailAudioLaguAnakAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedLaguAnak(postID: Int, judul_audio: String, gambar_audio: String, audio: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataAudioLaguAnakAdmin(postID ,judul_audio, gambar_audio, audio)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAudioLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAudioLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditAudioLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllAudioLaguAnakAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_lagu_anak)", id_lagu_anak)
        bundle.putString("nama_lagu_anak", nama_lagu_anak)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedAudioLaguAnak.text.toString().isEmpty()){
            layoutEditedNamaAudioLaguAnak.isErrorEnabled = true
            layoutEditedNamaAudioLaguAnak.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkAudioLaguAnak.text.toString().isEmpty()){
            layoutEditedLinkAudioLaguAnak.isErrorEnabled = true
            layoutEditedLinkAudioLaguAnak.error = "Link audio tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgAudioLaguAnak.setImageURI(imgUri) // handle chosen image
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