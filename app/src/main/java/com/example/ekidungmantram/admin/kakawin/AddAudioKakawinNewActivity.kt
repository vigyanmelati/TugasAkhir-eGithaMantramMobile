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
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kakawin.AllAudioKakawinAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.uriToFile
import kotlinx.android.synthetic.main.activity_add_audio_kakawin_new.*
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
import java.io.FileOutputStream
import java.util.*

class AddAudioKakawinNewActivity : AppCompatActivity() {
    private val REQUEST_CODE = 100
    private val REQUEST_CODE_AUDIO = 101
    private var bitmap: Bitmap? = null
    private var uriAudio: Uri? = null
    private var audioUri: Uri? = null
    private var file: File? = null
    private var filePath: String? = null
    private var id_kakawin: Int = 0
    val RQS_RECORDING = 1

    private lateinit var nama_kakawin: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_audio_kakawin_new)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Audio Sekar Agung"

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            id_kakawin = bundle.getInt("id_kakawin")
            nama_kakawin = bundle.getString("nama_kakawin").toString()
        }

//        selectImageAudioKakawinUserNew.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, REQUEST_CODE)
//        }

//        selectAudioKakawinUserNew.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*"
//            startActivityForResult(intent, REQUEST_CODE_AUDIO)
//        }

        selectAudioKakawinUserNew.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 112)
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/*"
                startActivityForResult(intent, REQUEST_CODE_AUDIO)
            }
        }

        submitAudioKakawinUserNew.setOnClickListener {
//            audioUri?.let { it1 -> uploadFile(it1) }
//            uploadFile(audioUri!!)
            val judul_audio = namaAudioKakawinUserNew.text.toString()
//            val audio = audioToString(filePath!!)
            val gambar = bitmapToString(bitmap).toString()
            if (validateInput()) {
                uploadAudio(file!!, judul_audio, gambar)
            }

        }

        recordAudioKakawinUserNew.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(this, RecordAudioKakawinActivity::class.java)
            bundle.putInt("id_kakawin", id_kakawin)
            bundle.putString("nama_kakawin", nama_kakawin)
            intent.putExtras(bundle)
            startActivityForResult(intent, RQS_RECORDING);
//            startActivity(intent)
        }

        cancelSubmitAddAudioKakawinUserNew.setOnClickListener {
            goBack()
        }

    }
    

    private fun goBack() {
        val intent = Intent(this, AllAudioKakawinAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_kakawin", id_kakawin)
        bundle.putString("nama_kakawin", nama_kakawin)
//        bundle.putString("desc_kakawin_admin", desc_kakawin)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if (namaAudioKakawinUserNew.text.toString().isEmpty()) {
            layoutNamaAudioKakawinUserNew.isErrorEnabled = true
            layoutNamaAudioKakawinUserNew.error = "Nama audio tidak boleh kosong!"
            return false
        }

        if(audio_file_text_add_kakawin.visibility   == View.GONE){
            Toast.makeText(this,"File audio tidak boleh kosong!",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imgUri: Uri? = data?.data
//            submitImgAudioKakawinUserNew.setImageURI(imgUri) // handle chosen image
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
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
//                selectAudioKakawinUserNew.text = path.toString()
                audio_file_text_add_kakawin.visibility   = View.VISIBLE
                audio_file_text_add_kakawin.text = file!!.name
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
//                        selectAudioKakawinUserNew.text = file!!.name
                        audio_file_text_add_kakawin.visibility   = View.VISIBLE
                        audio_file_text_add_kakawin.text = file!!.name
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

    fun uploadAudio(file: File, judul_audio: String, gambar: String) {
//        val requestFileCoba =RequestBody.create("multipart/form-data".toMediaTypeOrNull(),file)
        val nameAudio = file.name
        val judul = judul_audio.toRequestBody("text/plain".toMediaType())
        val gambar_audio = gambar.toRequestBody("text/plain".toMediaType())
        val nama = nameAudio.toRequestBody("text/plain".toMediaType())
        val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val audioFile = MultipartBody.Part.createFormData("part", file.name, requestFile)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataAudioKakawinAdmin(id_kakawin, judul, gambar_audio, nama, audioFile)
            .enqueue(object : Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if (response.body()?.status == 200) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@AddAudioKakawinNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        goBack()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@AddAudioKakawinNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddAudioKakawinNewActivity, t.message, Toast.LENGTH_SHORT)
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


    fun audioUriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "audio_file.mp3")
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

}