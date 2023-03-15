package com.example.ekidungmantram.admin.laguanak

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.laguanak.AllAudioLaguAnakAdminActivity
import com.example.ekidungmantram.admin.laguanak.RecordAudioEditLaguAnakActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioLaguAnakAdminModel
import com.example.ekidungmantram.uriToFile
import kotlinx.android.synthetic.main.activity_edit_audio_kidung_new.*
import kotlinx.android.synthetic.main.activity_edit_audio_lagu_anak_new.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class EditAudioLaguAnakNewActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private val REQUEST_CODE_AUDIO = 101
    private var bitmap: Bitmap?  = null
    private var uriAudio: Uri? = null
    private var audioUri: Uri? = null
    private var file: File? = null
    private var filePath: String? = null
    private var id_lagu_anak : Int = 0
    private lateinit var nama_lagu_anak :String
    val RQS_RECORDING = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_audio_lagu_anak_new)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val audioID = bundle.getInt("id_audio_lagu_anak")
            id_lagu_anak = bundle.getInt("id_lagu_anak")
            nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()

            setFormData(audioID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Audio Sekar Alit"

//            selectEditedImageAudioLaguAnakUser.setOnClickListener {
//                val intent = Intent(Intent.ACTION_PICK)
//                intent.type = "image/*"
//                startActivityForResult(intent, REQUEST_CODE)
//            }

            selectAudioLaguAnakUserNewEdit.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 112)
                } else {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "audio/*"
                    startActivityForResult(intent, REQUEST_CODE_AUDIO)
                }
            }

            submitEditedAudioLaguAnakUser.setOnClickListener {
                val nama_post     = namaEditedAudioLaguAnakUser.text.toString()
//                val link     = namaEditedLinkAudioLaguAnakUser.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    uploadAudio(file!!,audioID, nama_post,gambar)
                }
            }

            recordAudioLaguAnakUserNewEdit.setOnClickListener {
                val bundle = Bundle()
                val intent = Intent(this, RecordAudioEditLaguAnakActivity::class.java)
                bundle.putInt("id_lagu_anak", id_lagu_anak)
                bundle.putInt("id_audio_lagu_anak", audioID)
                bundle.putString("nama_lagu_anak", nama_lagu_anak)
                intent.putExtras(bundle)
                startActivityForResult(intent, RQS_RECORDING);
//            startActivity(intent)
            }

            cancelSubmitEditedAudioLaguAnakUser.setOnClickListener {
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
//                    namaEditedLinkAudioLaguAnakUser.setText(result.audio)
                    namaEditedAudioLaguAnakUser.setText(result.judul_audio)
                    audio_file_text_edit_lagu_anak.visibility   = View.VISIBLE
                    audio_file_text_edit_lagu_anak.text = result.audio
//                    Glide.with(this@EditAudioLaguAnakNewActivity)
////                        .load(result.gambar_audio).into(submitEditedImgAudioLaguAnak)
//                        .load(Constant.IMAGE_URL+result.gambar_audio).into(submitEditedImgAudioLaguAnakUser)
                }
            }

            override fun onFailure(call: Call<DetailAudioLaguAnakAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun postEditedLaguAnak(postID: Int, judul_audio: String, gambar_audio: String, audio: String) {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Mengunggah Data")
//        progressDialog.show()
//        ApiService.endpoint.updateDataAudioLaguAnak(postID ,judul_audio, gambar_audio, audio)
//            .enqueue(object: retrofit2.Callback<CrudModel> {
//                override fun onResponse(
//                    call: Call<CrudModel>,
//                    response: Response<CrudModel>
//                ) {
//                    if(response.body()?.status == 200){
//                        progressDialog.dismiss()
//                        Toast.makeText(this@EditAudioLaguAnakNewActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                        goBack()
//                    }else{
//                        progressDialog.dismiss()
//                        Toast.makeText(this@EditAudioLaguAnakNewActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Toast.makeText(this@EditAudioLaguAnakNewActivity, t.message, Toast.LENGTH_SHORT).show()
//                }
//
//            })
//    }


    private fun goBack() {
        val intent = Intent(this, AllAudioLaguAnakAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_lagu_anak", id_lagu_anak)
        bundle.putString("nama_lagu_anak", nama_lagu_anak)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedAudioLaguAnakUser.text.toString().isEmpty()){
            layoutEditedNamaAudioLaguAnakUser.isErrorEnabled = true
            layoutEditedNamaAudioLaguAnakUser.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(audio_file_text_edit_lagu_anak.visibility   == View.GONE){
            Toast.makeText(this,"File audio tidak boleh kosong!",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
//            submitEditedImgAudioLaguAnakUser.setImageURI(imgUri) // handle chosen image
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imgUri)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUDIO) {
            audioUri = data?.data as Uri
            Log.d("audio_uri", audioUri.toString())
            val myFile = audioUri?.let { uriToFile(it, this) }
            Log.d("myfile_audio", myFile.toString())
            file =  myFile
            Log.d("file_audio", file.toString())
            if (audioUri != null) {
//                val path = audioUri?.let { getPathFromUri(this, it) }
//                filePath = audioUri!!.path
//                selectAudioLaguAnakUserNewEdit.text = path.toString()
                audio_file_text_edit_lagu_anak.visibility   = View.VISIBLE
                audio_file_text_edit_lagu_anak.text = file!!.name
            }
            uriAudio = MediaStore.Audio.Media.getContentUriForPath(audioUri.toString())
        }

        if (requestCode === RQS_RECORDING) {
            if (resultCode === RESULT_OK) {

                // Great! User has recorded and saved the audio file
                val result: String = data?.getStringExtra("result").toString()
                if (result != null) {
                    val file_audio = File(result)
                    if(file_audio != null){
                        file = file_audio
//                        selectAudioLaguAnakUserNewEdit.text = file!!.name
                        audio_file_text_edit_lagu_anak.visibility   = View.VISIBLE
                        audio_file_text_edit_lagu_anak.text = file!!.name
                    }else{
                        Toast.makeText(this,"File Aneh", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Gada filepath rekaman", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this, "Saved: $result", Toast.LENGTH_LONG).show()
                Log.d("path_audio", "Saved Path::$result")
            }
            if (resultCode === RESULT_CANCELED) {
                // Oops! User has canceled the recording / back button
            }
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

    fun uploadAudio(file: File, id_audio:Int, judul_audio: String, gambar: String) {
        val nameAudio = file.name
        val judul = judul_audio.toRequestBody("text/plain".toMediaType())
        val gambar_audio = gambar.toRequestBody("text/plain".toMediaType())
        val nama = nameAudio.toRequestBody("text/plain".toMediaType())
        val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val audioFile = MultipartBody.Part.createFormData("part", file.name, requestFile)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataAudioLaguAnakAdmin(id_audio, judul, gambar_audio, nama, audioFile)
            .enqueue(object : Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if (response.body()?.status == 200) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@EditAudioLaguAnakNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        goBack()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@EditAudioLaguAnakNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditAudioLaguAnakNewActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    companion object {
        private const val TAG = "AudioUploadService"
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 112 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, REQUEST_CODE_AUDIO)
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}