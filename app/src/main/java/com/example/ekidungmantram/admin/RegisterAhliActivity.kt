package com.example.ekidungmantram.admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
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
import com.example.ekidungmantram.LoginActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.uriToFile
import com.example.ekidungmantram.uriToFilePdf
import com.example.ekidungmantram.user.MainActivity
import com.example.ekidungmantram.user.RegisterActivity
import kotlinx.android.synthetic.main.activity_add_mantram_admin.*
import kotlinx.android.synthetic.main.activity_alll_lagu_anak.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_ahli.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterAhliActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var file: File?  = null
    private var fileUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_ahli)
        supportActionBar!!.hide()

        back_arrow_regis_ahli.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_regis_ahli.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        selectFileAhli.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE)
        }

        signup_button_ahli.setOnClickListener{
            val inputname = name_regis_ahli.text.toString()
            val inputusername = username_regis_ahli.text.toString()
            val inputpassword = password_regis_ahli.text.toString()
//            val file       = file_regis_ahli.text.toString()
            if(validateInput()){
                file?.let { it1 -> register(inputusername, inputpassword, inputname, it1) }
            }
        }
    }

    private fun register(email: String, password: String, name: String, file: File) {
        val name_file = file.name
        val email_ahli = email.toRequestBody("text/plain".toMediaType())
        val pass_ahli = password.toRequestBody("text/plain".toMediaType())
        val name_ahli = name.toRequestBody("text/plain".toMediaType())
        val name_file_ahli = name_file.toRequestBody("text/plain".toMediaType())
        val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val ahliFile = MultipartBody.Part.createFormData("part", file.name, requestFile)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.registerAhli(email_ahli, pass_ahli,name_ahli, name_file_ahli, ahliFile)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@RegisterAhliActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@RegisterAhliActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@RegisterAhliActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(name_regis_ahli.text.toString().isEmpty()){
            nameLayout_regis_ahli.isErrorEnabled = true
            nameLayout_regis_ahli.error = "Nama tidak boleh kosong!"
            return false
        }
        if(username_regis_ahli.text.toString().isEmpty()){
            usernameLayout_regis_ahli.isErrorEnabled = true
            usernameLayout_regis_ahli.error = "Email tidak boleh kosong!"
            return false
        }

        if(password_regis_ahli.text.toString().isEmpty()){
            passwordLayout_regis_ahli.isErrorEnabled = true
            passwordLayout_regis_ahli.error = "Password tidak boleh kosong!"
            return false
        }

//        if(file_regis_ahli.text.toString().isEmpty()){
//            fileLayout_regis_ahli.isErrorEnabled = true
//            fileLayout_regis_ahli.error = "Password tidak boleh kosong!"
//            return false
//        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            fileUri = data?.data as Uri
            Log.d("file_uri", fileUri.toString())
            val myFile = fileUri?.let { uriToFilePdf(it, this) }
            Log.d("myfile_ahli", myFile.toString())
            file =  myFile
            file_ahli_text.visibility   = View.VISIBLE
            file_ahli_text.text = file!!.name
        }
    }
}