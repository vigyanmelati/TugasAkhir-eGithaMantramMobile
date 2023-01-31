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
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailPupuhAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_audio_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_edit_pupuh_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_pupuh.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditVideoPupuhActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0
    private lateinit var nama_pupuh :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video_pupuh)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val videoID = bundle.getInt("id_video_pupuh")
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_pupuh").toString()

            setFormData(videoID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Video Sekar Alit"

            selectEditedImageVideoPupuh.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedVideoPupuh.setOnClickListener {
                val nama_post     = namaEditedVideoPupuh.text.toString()
                val link     = namaEditedLinkVideoPupuh.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedPupuh(videoID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedVideoPupuh.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowVideoPupuhAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailVideoPupuhAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoPupuhAdminModel>,
                response: Response<DetailVideoPupuhAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkVideoPupuh.setText(result.video)
                    namaEditedVideoPupuh.setText(result.judul_video)
                    Glide.with(this@EditVideoPupuhActivity)
//                        .load(result.gambar_video).into(submitEditedImgVideoPupuh)
                        .load(Constant.IMAGE_URL+result.gambar_video).into(submitEditedImgVideoPupuh)
                }
            }

            override fun onFailure(call: Call<DetailVideoPupuhAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedPupuh(postID: Int, judul_video: String, gambar_video: String, video: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataVideoPupuhAdmin(postID ,judul_video, gambar_video, video)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoPupuhActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditVideoPupuhActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllVideoPupuhAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedVideoPupuh.text.toString().isEmpty()){
            layoutEditedNamaVideoPupuh.isErrorEnabled = true
            layoutEditedNamaVideoPupuh.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkVideoPupuh.text.toString().isEmpty()){
            layoutEditedLinkVideoPupuh.isErrorEnabled = true
            layoutEditedLinkVideoPupuh.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgVideoPupuh.setImageURI(imgUri) // handle chosen image
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