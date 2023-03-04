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
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.pupuh.AllKategoriPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_edit_lagu_anak_admin.*
import kotlinx.android.synthetic.main.activity_edit_pupuh_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditLaguAnakAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null
    private var id_lagu_anak : Int = 0
    private var id_lagu_anak_admin : Int = 0
    private lateinit var nama_lagu_anak :String
    private lateinit var nama_lagu_anak_admin :String
    private lateinit var desc_lagu_anak_admin :String
    private lateinit var tag_user :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lagu_anak_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_lagu_anak")


            id_lagu_anak = postID
            nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()
            id_lagu_anak_admin = bundle.getInt("id_lagu_anak_kat")
            nama_lagu_anak_admin = bundle.getString("nama_lagu_anak_kat").toString()
            desc_lagu_anak_admin = bundle.getString("desc_lagu_anak_kat").toString()
            tag_user = bundle.getString("tag_user_anak").toString()

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Sekar Rare"

            selectEditedImageLaguAnak.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedLaguAnak.setOnClickListener {
                val nama_post     = namaEditedLaguAnak.text.toString()
                val deskripsi     = deskripsiEditedLaguAnak.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedLaguAnak(postID, nama_post, deskripsi, gambar)
                }
            }

            cancelSubmitEditedLaguAnak.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowLaguAnakAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailLaguAnakAdminModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakAdminModel>,
                response: Response<DetailLaguAnakAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedLaguAnak.setText(result.deskripsi)
                    namaEditedLaguAnak.setText(result.nama_post)
                    Glide.with(this@EditLaguAnakAdminActivity)
//                        .load(result.gambar).into(submitEditedImgLaguAnak)
                        .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgLaguAnak)
                }
            }

            override fun onFailure(call: Call<DetailLaguAnakAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedLaguAnak(postID: Int, namaPost: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateLaguAnakAdmin(postID ,namaPost, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditLaguAnakAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLaguAnakAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.getItemAtPosition(p2).toString()
        yadnya = item
    }

    private fun goBack() {
        val bundle = Bundle()
        val intent = Intent(this, DetailLaguAnakAdminActivity::class.java)
        bundle.putInt("id_lagu_anak_admin", id_lagu_anak)
        bundle.putInt("id_lagu_anak_admin_kat", id_lagu_anak_admin)
        bundle.putString("nama_lagu_anak_admin", nama_lagu_anak)
        bundle.putString("nama_lagu_anak_admin_kat", nama_lagu_anak_admin)
        bundle.putString("desc_lagu_anak_admin_kat", desc_lagu_anak_admin)
        bundle.putString("tag_user_anak", tag_user)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    private fun validateInput(): Boolean {
        if(namaEditedLaguAnak.text.toString().isEmpty()){
            layoutEditedNamaLaguAnak.isErrorEnabled = true
            layoutEditedNamaLaguAnak.error = "Nama Sekar Rare tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedLaguAnak.text.toString().isEmpty()){
            layoutEditedDeskripsiLaguAnak.isErrorEnabled = true
            layoutEditedDeskripsiLaguAnak.error = "Deskripsi Sekar Rare tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgLaguAnak.setImageURI(imgUri) // handle chosen image
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