package com.example.ekidungmantram.user.pupuh

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
import com.example.ekidungmantram.model.DetailVideoPupuhModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_edit_video_pupuh.*
import kotlinx.android.synthetic.main.activity_edit_video_pupuh_user.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditVideoPupuhUserActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_pupuh : Int = 0
    private lateinit var nama_pupuh :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video_pupuh_user)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val videoID = bundle.getInt("id_video_pupuh")
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_pupuh").toString()

            setFormData(videoID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Video Sekar Rare"

            selectEditedImageVideoPupuhUser.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedVideoPupuhUser.setOnClickListener {
                val nama_post     = namaEditedVideoPupuhUser.text.toString()
                val link     = namaEditedLinkVideoPupuhUser.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedPupuh(videoID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedVideoPupuhUser.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowVideoPupuh(postID).enqueue(object:
            retrofit2.Callback<DetailVideoPupuhModel> {
            override fun onResponse(
                call: Call<DetailVideoPupuhModel>,
                response: Response<DetailVideoPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkVideoPupuh.setText(result.video)
                    namaEditedVideoPupuh.setText(result.judul_video)
                    Glide.with(this@EditVideoPupuhUserActivity)
//                        .load(result.gambar_video).into(submitEditedImgVideoPupuh)
                        .load(Constant.IMAGE_URL+result.gambar_video).into(submitEditedImgVideoPupuh)
                }
            }

            override fun onFailure(call: Call<DetailVideoPupuhModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedPupuh(postID: Int, judul_video: String, gambar_video: String, video: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataVideoPupuh(postID ,judul_video, gambar_video, video)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoPupuhUserActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoPupuhUserActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditVideoPupuhUserActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllVideoPupuhActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedVideoPupuhUser.text.toString().isEmpty()){
            layoutEditedNamaVideoPupuhUser.isErrorEnabled = true
            layoutEditedNamaVideoPupuhUser.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkVideoPupuhUser.text.toString().isEmpty()){
            layoutEditedLinkVideoPupuhUser.isErrorEnabled = true
            layoutEditedLinkVideoPupuhUser.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgVideoPupuhUser.setImageURI(imgUri) // handle chosen image
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