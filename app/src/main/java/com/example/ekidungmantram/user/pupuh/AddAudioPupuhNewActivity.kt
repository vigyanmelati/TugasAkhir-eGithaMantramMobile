package com.example.ekidungmantram.user.pupuh

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.createCustomTempFile
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.uriToFile
import com.example.ekidungmantram.user.AllAudioPupuhActivity
import kotlinx.android.synthetic.main.activity_add_audio_pupuh_new.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*


class AddAudioPupuhNewActivity : AppCompatActivity() {
    private val REQUEST_CODE = 100
    private val REQUEST_CODE_AUDIO = 101
    private var bitmap: Bitmap? = null
    private var uriAudio: Uri? = null
    private var audioUri: Uri? = null
    private var file: File? = null
    private var filePath: String? = null
    private var id_pupuh: Int = 0
    val RQS_RECORDING = 1

    private lateinit var nama_pupuh: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_audio_pupuh_new)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Audio Pupuh"

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            id_pupuh = bundle.getInt("id_pupuh")
            nama_pupuh = bundle.getString("nama_kat_pupuh_user").toString()
        }

        selectImageAudioPupuhUserNew.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

//        selectAudioPupuhUserNew.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*"
//            startActivityForResult(intent, REQUEST_CODE_AUDIO)
//        }

        selectAudioPupuhUserNew.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 112)
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/*"
                startActivityForResult(intent, REQUEST_CODE_AUDIO)
            }
        }

        submitAudioPupuhUserNew.setOnClickListener {
//            audioUri?.let { it1 -> uploadFile(it1) }
//            uploadFile(audioUri!!)
            val judul_audio = namaAudioPupuhUserNew.text.toString()
//            val audio = audioToString(filePath!!)
            val gambar = bitmapToString(bitmap).toString()
            if (validateInput()) {
                uploadAudio(file!!, judul_audio, gambar)
            }

        }

        recordAudioPupuhUserNew.setOnClickListener {
            val intent = Intent(this, RecordAudioPupuhActivity::class.java)
            startActivityForResult(intent, RQS_RECORDING);
//            startActivity(intent)
        }

        cancelSubmitAddAudioPupuhUserNew.setOnClickListener {
            goBack()
        }

    }

//    private fun postAudioPupuhAdmin(judul_audio: String, gambar: String, part: MultipartBody.Part) {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Mengunggah Data")
//        progressDialog.show()
//        ApiService.endpoint.createDataAudioPupuh(id_pupuh, judul_audio, gambar, part)
//            .enqueue(object : Callback<CrudModel> {
//                override fun onResponse(
//                    call: Call<CrudModel>,
//                    response: Response<CrudModel>
//                ) {
//                    if (response.body()?.status == 200) {
//                        progressDialog.dismiss()
//                        Toast.makeText(
//                            this@AddAudioPupuhNewActivity,
//                            response.body()?.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        goBack()
//                    } else {
//                        progressDialog.dismiss()
//                        Toast.makeText(
//                            this@AddAudioPupuhNewActivity,
//                            response.body()?.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Toast.makeText(this@AddAudioPupuhNewActivity, t.message, Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//            })
//    }

    private fun goBack() {
        val intent = Intent(this, AllAudioPupuhActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id_pupuh", id_pupuh)
        bundle.putString("nama_pupuh", nama_pupuh)
//        bundle.putString("desc_pupuh_admin", desc_pupuh)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if (namaAudioPupuhUserNew.text.toString().isEmpty()) {
            layoutNamaAudioPupuhUserNew.isErrorEnabled = true
            layoutNamaAudioPupuhUserNew.error = "Nama audio tidak boleh kosong!"
            return false
        }

//        if(linkAudioPupuhUser.text.toString().isEmpty()){
//            layoutLinkAudioPupuhUser.isErrorEnabled = true
//            layoutLinkAudioPupuhUser.error = "Link audio tidak boleh kosong!"
//            return false
//        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imgUri: Uri? = data?.data
            submitImgAudioPupuhUserNew.setImageURI(imgUri) // handle chosen image
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
                val path = audioUri?.let { getPathFromUri(this, it) }
                filePath = audioUri!!.path
                selectAudioPupuhUserNew.text = path.toString()
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
                        selectAudioPupuhUserNew.text = file!!.name
                    }else{
                        Toast.makeText(this,"File Aneh",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Gada filepath rekaman",Toast.LENGTH_SHORT).show()
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
        ApiService.endpoint.createDataAudioPupuh(id_pupuh, judul, gambar_audio, nama, audioFile)
            .enqueue(object : Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if (response.body()?.status == 200) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@AddAudioPupuhNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        goBack()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@AddAudioPupuhNewActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddAudioPupuhNewActivity, t.message, Toast.LENGTH_SHORT)
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