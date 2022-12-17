package com.example.ekidungmantram.admin.kidung

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
import com.example.ekidungmantram.admin.kidung.AllVideoKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailVideoKidungAdminModel
import kotlinx.android.synthetic.main.activity_edit_video_kidung_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditVideoKidungAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var id_kidung : Int = 0
    private lateinit var nama_kidung :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video_kidung_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val videoID = bundle.getInt("id_video_kidung")
            id_kidung = bundle.getInt("id_kidung")
            nama_kidung = bundle.getString("nama_kidung").toString()

            setFormData(videoID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Video Sekar Madya"

            selectEditedImageVideoKidung.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedVideoKidung.setOnClickListener {
                val nama_post     = namaEditedVideoKidung.text.toString()
                val link     = namaEditedLinkVideoKidung.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedKidung(videoID, nama_post,gambar,link)
                }
            }

            cancelSubmitEditedVideoKidung.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowVideoKidungAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailVideoKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailVideoKidungAdminModel>,
                response: Response<DetailVideoKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaEditedLinkVideoKidung.setText(result.video)
                    namaEditedVideoKidung.setText(result.judul_video)
                    Glide.with(this@EditVideoKidungAdminActivity).load(Constant.IMAGE_URL+result.gambar_video).into(submitEditedImgVideoKidung)
                }
            }

            override fun onFailure(call: Call<DetailVideoKidungAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedKidung(postID: Int, judul_video: String, gambar_video: String, video: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataVideoKidungAdmin(postID ,judul_video, gambar_video, video)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditVideoKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditVideoKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack() {
        val intent = Intent(this, AllVideoKidungAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kidung", id_kidung)
        bundle.putString("nama_kidung", nama_kidung)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedVideoKidung.text.toString().isEmpty()){
            layoutEditedNamaVideoKidung.isErrorEnabled = true
            layoutEditedNamaVideoKidung.error = "Nama video tidak boleh kosong!"
            return false
        }

        if(namaEditedLinkVideoKidung.text.toString().isEmpty()){
            layoutEditedLinkVideoKidung.isErrorEnabled = true
            layoutEditedLinkVideoKidung.error = "Link video tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgVideoKidung.setImageURI(imgUri) // handle chosen image
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