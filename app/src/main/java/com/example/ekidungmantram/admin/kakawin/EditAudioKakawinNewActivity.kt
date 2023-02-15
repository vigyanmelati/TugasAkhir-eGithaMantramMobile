package com.example.ekidungmantram.admin.kakawin

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAudioKakawinAdminModel
import com.example.ekidungmantram.uriToFile
import kotlinx.android.synthetic.main.activity_edit_audio_kakawin_new.*
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

class EditAudioKakawinNewActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private val REQUEST_CODE_AUDIO = 101
    private var bitmap: Bitmap?  = null
    private var uriAudio: Uri? = null
    private var audioUri: Uri? = null
    private var file: File? = null
    private var filePath: String? = null
    private var id_kakawin : Int = 0
    private lateinit var nama_kakawin :String
    val RQS_RECORDING = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_audio_kakawin_new)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val audioID = bundle.getInt("id_audio_kakawin")
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kakawin").toString()

            setFormData(audioID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Audio Sekar Alit"

            selectEditedImageAudioKakawinUser.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            selectAudioKakawinUserNewEdit.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 112)
                } else {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "audio/*"
                    startActivityForResult(intent, REQUEST_CODE_AUDIO)
                }
            }

            submitEditedAudioKakawinUser.setOnClickListener {
                val nama_post     = namaEditedAudioKakawinUser.text.toString()
//                val link     = namaEditedLinkAudioKakawinUser.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    uploadAudio(file!!,audioID, nama_post,gambar)
                }
            }

            recordAudioKakawinUserNewEdit.setOnClickListener {
                val intent = Intent(this, RecordAudioEditKakawinActivity::class.java)
                startActivityForResult(intent, RQS_RECORDING);
//            startActivity(intent)
            }

            cancelSubmitEditedAudioKakawinUser.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowAudioKakawinAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailAudioKakawinAdminModel> {
            override fun onResponse(
                call: Call<DetailAudioKakawinAdminModel>,
                response: Response<DetailAudioKakawinAdminModel>
            ) {
                val result = response.body()!!
                result.let {
//                    namaEditedLinkAudioKakawinUser.setText(result.audio)
                    namaEditedAudioKakawinUser.setText(result.judul_audio)
                    selectAudioKakawinUserNewEdit.text = result.audio
                    Glide.with(this@EditAudioKakawinNewActivity)
//                        .load(result.gambar_audio).into(submitEditedImgAudioKakawin)
                        .load(Constant.IMAGE_URL+result.gambar_audio).into(submitEditedImgAudioKakawinUser)
                }
            }

            override fun onFailure(call: Call<DetailAudioKakawinAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun postEditedKakawin(postID: Int, judul_audio: String, gambar_audio: String, audio: String) {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Mengunggah Data")
//        progressDialog.show()
//        ApiService.endpoint.updateDataAudioKakawin(postID ,judul_audio, gambar_audio, audio)
//            .enqueue(object: retrofit2.Callback<CrudModel> {
//                override fun onResponse(
//                    call: Call<CrudModel>,
//                    response: Response<CrudModel>
//                ) {
//                    if(response.body()?.status == 200){
//                        progressDialog.dismiss()
//                        Toast.makeText(this@EditAudioKakawinNewActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                        goBack()
//                    }else{
//                        progressDialog.dismiss()
//                        Toast.makeText(this@EditAudioKakawinNewActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Toast.makeText(this@EditAudioKakawinNewActivity, t.message, Toast.LENGTH_SHORT).show()
//                }
//
//            })
//    }


    private fun goBack() {
        val intent = Intent(this, AllAudioKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun validateInput(): Boolean {
        if(namaEditedAudioKakawinUser.text.toString().isEmpty()){
            layoutEditedNamaAudioKakawinUser.isErrorEnabled = true
            layoutEditedNamaAudioKakawinUser.error = "Nama audio tidak boleh kosong!"
            return false
        }

//        if(namaEditedLinkAudioKakawinUser.text.toString().isEmpty()){
//            layoutEditedLinkAudioKakawinUser.isErrorEnabled = true
//            layoutEditedLinkAudioKakawinUser.error = "Link audio tidak boleh kosong!"
//            return false
//        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgAudioKakawinUser.setImageURI(imgUri) // handle chosen image
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
                val path = audioUri?.let { getPathFromUri(this, it) }
                filePath = audioUri!!.path
                selectAudioKakawinUserNewEdit.text = path.toString()
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
                        selectAudioKakawinUserNewEdit.text = file!!.name
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
        ApiService.endpoint.updateDataAudioKakawinAdmin(id_audio, judul, gambar_audio, nama, audioFile)
            .enqueue(object : Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if (response.body()?.status == 200) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@EditAudioKakawinNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        goBack()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@EditAudioKakawinNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditAudioKakawinNewActivity, t.message, Toast.LENGTH_SHORT)
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