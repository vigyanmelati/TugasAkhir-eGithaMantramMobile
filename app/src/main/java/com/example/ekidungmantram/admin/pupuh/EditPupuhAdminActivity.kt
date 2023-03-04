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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kidung.AllKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailPupuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_kakawin_admin.*
import kotlinx.android.synthetic.main.activity_edit_kidung_admin.*
import kotlinx.android.synthetic.main.activity_edit_pupuh_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditPupuhAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null
    private var id_pupuh : Int = 0
    private var id_pupuh_admin : Int = 0
    private lateinit var nama_pupuh :String
    private lateinit var nama_pupuh_admin :String
    private lateinit var desc_pupuh_admin :String
    private lateinit var tag_user :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pupuh_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_pupuh")

            id_pupuh = postID
            nama_pupuh = bundle.getString("nama_pupuh").toString()
            id_pupuh_admin = bundle.getInt("id_pupuh_kat")
            nama_pupuh_admin = bundle.getString("nama_pupuh_kat").toString()
            desc_pupuh_admin = bundle.getString("padalingsa").toString()
            tag_user = bundle.getString("tag_user").toString()

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Sekar Alit"

            selectEditedImagePupuh.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedPupuh.setOnClickListener {
                val nama_post     = namaEditedPupuh.text.toString()
                val deskripsi     = deskripsiEditedPupuh.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedPupuh(postID, nama_post, deskripsi, gambar)
                }
            }

            cancelSubmitEditedPupuh.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowPupuhAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailPupuhAdminModel> {
            override fun onResponse(
                call: Call<DetailPupuhAdminModel>,
                response: Response<DetailPupuhAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedPupuh.setText(result.deskripsi)
                    namaEditedPupuh.setText(result.nama_post)
                    Glide.with(this@EditPupuhAdminActivity)
//                        .load(result.gambar).into(submitEditedImgPupuh)
                        .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgPupuh)
                }
            }

            override fun onFailure(call: Call<DetailPupuhAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedPupuh(postID: Int, namaPost: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updatePupuhAdmin(postID ,namaPost, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditPupuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditPupuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.getItemAtPosition(p2).toString()
        yadnya = item
    }

    private fun goBack() {
        val bundle = Bundle()
        val intent = Intent(this, DetailPupuhAdminActivity::class.java)
        bundle.putInt("id_pupuh_admin", id_pupuh)
        bundle.putInt("id_pupuh_admin_kat", id_pupuh_admin)
        bundle.putString("nama_pupuh_admin", nama_pupuh)
        bundle.putString("nama_pupuh_admin_kat", nama_pupuh_admin)
        bundle.putString("desc_pupuh_admin_kat", desc_pupuh_admin)
        bundle.putString("tag_user", tag_user)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    private fun validateInput(): Boolean {
        if(namaEditedPupuh.text.toString().isEmpty()){
            layoutEditedNamaPupuh.isErrorEnabled = true
            layoutEditedNamaPupuh.error = "Nama Pupuh tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedPupuh.text.toString().isEmpty()){
            layoutEditedDeskripsiPupuh.isErrorEnabled = true
            layoutEditedDeskripsiPupuh.error = "Deskripsi Pupuh tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgPupuh.setImageURI(imgUri) // handle chosen image
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