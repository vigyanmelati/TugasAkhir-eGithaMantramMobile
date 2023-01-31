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
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_audio_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_pupuh.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditAudioPupuhAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0
    private lateinit var nama_pupuh :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_audio_pupuh_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val audioID = bundle.getInt("id_audio_pupuh")
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_pupuh").toString()

            setFormData(audioID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Audio Sekar Alit"

            selectEditedImageAudioPupuh.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedAudioPupuh.setOnClickListener {
                val nama_post     = namaEditedAudioPupuh.text.toString()
                val link     = namaEditedLinkAudioPupuh.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedPupuh(audioID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedAudioPupuh.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowAudioPupuhAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailAudioPupuhAdminModel> {
            override fun onResponse(
                call: Call<DetailAudioPupuhAdminModel>,
                response: Response<DetailAudioPupuhAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkAudioPupuh.setText(result.audio)
                    namaEditedAudioPupuh.setText(result.judul_audio)
                    Glide.with(this@EditAudioPupuhAdminActivity)
//                        .load(result.gambar_audio).into(submitEditedImgAudioPupuh)
                        .load(Constant.IMAGE_URL+result.gambar_audio).into(submitEditedImgAudioPupuh)
                }
            }

            override fun onFailure(call: Call<DetailAudioPupuhAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedPupuh(postID: Int, judul_audio: String, gambar_audio: String, audio: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataAudioPupuhAdmin(postID ,judul_audio, gambar_audio, audio)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAudioPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAudioPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditAudioPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllAudioPupuhAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedAudioPupuh.text.toString().isEmpty()){
            layoutEditedNamaAudioPupuh.isErrorEnabled = true
            layoutEditedNamaAudioPupuh.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkAudioPupuh.text.toString().isEmpty()){
            layoutEditedLinkAudioPupuh.isErrorEnabled = true
            layoutEditedLinkAudioPupuh.error = "Link audio tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgAudioPupuh.setImageURI(imgUri) // handle chosen image
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