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
import com.example.ekidungmantram.admin.kakawin.AllVideoKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoKakawinAdminModel
import kotlinx.android.synthetic.main.activity_detail_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_edit_video_kakawin_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditVideoKakawinAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kakawin : Int = 0
    private lateinit var nama_kakawin :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video_kakawin_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val videoID = bundle.getInt("id_video_kakawin")
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kakawin").toString()

            setFormData(videoID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Video Sekar Agung"

            selectEditedImageVideoKakawin.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedVideoKakawin.setOnClickListener {
                val nama_post     = namaEditedVideoKakawin.text.toString()
                val link     = namaEditedLinkVideoKakawin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedKakawin(videoID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedVideoKakawin.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowVideoKakawinAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailVideoKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoKakawinAdminModel>,
                response: Response<DetailVideoKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkVideoKakawin.setText(result.video)
                    namaEditedVideoKakawin.setText(result.judul_video)
                    Glide.with(this@EditVideoKakawinAdminActivity)
//                        .load(Constant.IMAGE_URL+result.gambar_video).into(submitEditedImgVideoKakawin)
                        .load(result.gambar_video).into(submitEditedImgVideoKakawin)
                }
            }

            override fun onFailure(call: Call<DetailVideoKakawinAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedKakawin(postID: Int, judul_video: String, gambar_video: String, video: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataVideoKakawinAdmin(postID ,judul_video, gambar_video, video)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoKakawinAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditVideoKakawinAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllVideoKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedVideoKakawin.text.toString().isEmpty()){
            layoutEditedNamaVideoKakawin.isErrorEnabled = true
            layoutEditedNamaVideoKakawin.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkVideoKakawin.text.toString().isEmpty()){
            layoutEditedLinkVideoKakawin.isErrorEnabled = true
            layoutEditedLinkVideoKakawin.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgVideoKakawin.setImageURI(imgUri) // handle chosen image
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