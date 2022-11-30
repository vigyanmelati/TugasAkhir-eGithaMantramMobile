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
import com.example.ekidungmantram.admin.pupuh.AllVideoPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_video_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_pupuh.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditVideoLaguAnakAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_lagu_anak : Int = 0
    private lateinit var nama_lagu_anak :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video_lagu_anak_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val videoID = bundle.getInt("id_video_lagu_anak")
            id_lagu_anak = bundle.getInt("id_lagu_anak")
            nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()

            setFormData(videoID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Video Sekar Rare"

            selectEditedImageVideoLaguAnak.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedVideoLaguAnak.setOnClickListener {
                val nama_post     = namaEditedVideoLaguAnak.text.toString()
                val link     = namaEditedLinkVideoLaguAnak.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedLaguAnak(videoID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedVideoLaguAnak.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowVideoLaguAnakAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailVideoLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoLaguAnakAdminModel>,
                response: Response<DetailVideoLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkVideoLaguAnak.setText(result.video)
                    namaEditedVideoLaguAnak.setText(result.judul_video)
                    Glide.with(this@EditVideoLaguAnakAdminActivity).load(Constant.IMAGE_URL+result.gambar_video).into(submitEditedImgVideoLaguAnak)
                }
            }

            override fun onFailure(call: Call<DetailVideoLaguAnakAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedLaguAnak(postID: Int, judul_video: String, gambar_video: String, video: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataVideoLaguAnakAdmin(postID ,judul_video, gambar_video, video)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditVideoLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllVideoLaguAnakAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_lagu_anak", id_lagu_anak)
        bundle.putString("nama_lagu_anak", nama_lagu_anak)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedVideoLaguAnak.text.toString().isEmpty()){
            layoutEditedNamaVideoLaguAnak.isErrorEnabled = true
            layoutEditedNamaVideoLaguAnak.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkVideoLaguAnak.text.toString().isEmpty()){
            layoutEditedLinkVideoLaguAnak.isErrorEnabled = true
            layoutEditedLinkVideoLaguAnak.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgVideoLaguAnak.setImageURI(imgUri) // handle chosen image
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