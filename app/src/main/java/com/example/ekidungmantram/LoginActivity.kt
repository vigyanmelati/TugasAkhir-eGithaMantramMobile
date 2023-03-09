package com.example.ekidungmantram

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ekidungmantram.admin.HomeAdminActivity
import com.example.ekidungmantram.admin.RegisterAhliActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AdminModel
import com.example.ekidungmantram.user.*
import com.example.ekidungmantram.user.kakawin.AllKategoriKakawinUserActivity
import com.example.ekidungmantram.user.kidung.AllKidungUserActivity
import com.example.ekidungmantram.user.laguanak.AllKategoriLaguAnakUserActivity
import com.example.ekidungmantram.user.pupuh.AllKategoriPupuhUserActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var back: TextView
    private lateinit var submit: Button
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.hide()
//        back     = findViewById(R.id.back)
        submit   = findViewById(R.id.login_button)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        back_arrow.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        regis.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        regis_ahli.setOnClickListener {
            val intent = Intent(this, RegisterAhliActivity::class.java)
            startActivity(intent)
            finish()
        }

        submit.setOnClickListener{
            val inputusername = username.text.toString()
            val inputpassword = password.text.toString()
            if(validateInput()){
                login(inputusername, inputpassword)
            }
        }

    }

    private fun login(inputusername: String, inputpassword: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mencoba Login")
        progressDialog.show()
        ApiService.endpoint.loginAdmin(inputusername, inputpassword)
            .enqueue(object: Callback<AdminModel>{
                override fun onResponse(
                    call: Call<AdminModel>,
                    response: Response<AdminModel>
                ) {
                    if(!response.body()?.error!!){
                        saveData(response.body()?.id_admin, response.body()?.nama, response.body()?.role, response.body()?.message, response.body()!!.mobile_is_logged, response.body()!!.is_approved)
                        progressDialog.dismiss()
                    }else{
                        Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<AdminModel>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            })
    }

    private fun validateInput(): Boolean {
        if(username.text.toString().isEmpty()){
            usernameLayout.isErrorEnabled = true
            usernameLayout.error = "Email tidak boleh kosong!"
            return false
        }

        if(password.text.toString().isEmpty()){
            passwordLayout.isErrorEnabled = true
            passwordLayout.error = "Password tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun saveData(idAdmin: Int?, nama: String?, roleAdmin: Int?, mesage: String?, logged: Int, is_approved: Int) {
        sharedPreferences = getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val editor        = sharedPreferences.edit()
        editor.apply{
            putString("ID_ADMIN", idAdmin?.toString())
            if (idAdmin != null) {
                putInt("ID_ADMIN_INT", idAdmin)
            }
            putString("NAMA", nama)
            putString("ROLE", roleAdmin?.toString())
            putString("MESAGE", mesage)
            putString("LOGGED", logged.toString())
            putInt("IS_APPROVED", is_approved)
        }.apply()
        Toast.makeText(this, "Log In Sukses", Toast.LENGTH_SHORT).show()
        if (roleAdmin.toString() != "3"){
            goToAdmin()
        }else{
            goToUser()
        }
    }

    private fun goToAdmin() {
        val intent = Intent(this, HomeAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToUser(){
        val bundle :Bundle ?= intent.extras
        val nama_app = bundle?.getString("APP").toString()
        if (nama_app == "pupuh") {
            goToPupuhUser()
        }else if(nama_app == "kakawin") {
            goToKakawinUser()
        }else if(nama_app == "laguanak") {
            goToLaguAnakUser()
        }else if(nama_app == "kidung") {
            goToKidungUser()
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun goToPupuhUser() {
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            val id_pupuh = bundle.getInt("id_pupuh")
            val nama_pupuh = bundle.getString("nama_pupuh").toString()
            val desc_pupuh = bundle.getString("desc_pupuh").toString()

            val bundle = Bundle()
            val intent = Intent(this, AllKategoriPupuhUserActivity::class.java)
            bundle.putInt("id_pupuh", id_pupuh)
            bundle.putString("nama_pupuh", nama_pupuh)
            bundle.putString("desc_pupuh", desc_pupuh)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

    }

    private fun goToKakawinUser() {
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            val id_kakawin = bundle.getInt("id_kakawin")
            val nama_kakawin = bundle.getString("nama_kakawin").toString()
            val desc_kakawin = bundle.getString("desc_kakawin").toString()

            val bundle = Bundle()
            val intent = Intent(this, AllKategoriKakawinUserActivity::class.java)
            bundle.putInt("id_kakawin", id_kakawin)
            bundle.putString("nama_kakawin", nama_kakawin)
            bundle.putString("desc_kakawin", desc_kakawin)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

    }

    private fun goToLaguAnakUser() {
        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            val id_lagu_anak = bundle.getInt("id_lagu_anak")
            val nama_lagu_anak = bundle.getString("nama_lagu_anak").toString()
            val desc_lagu_anak = bundle.getString("desc_lagu_anak").toString()

            val bundle = Bundle()
            val intent = Intent(this, AllKategoriLaguAnakUserActivity::class.java)
            bundle.putInt("id_lagu_anak", id_lagu_anak)
            bundle.putString("nama_lagu_anak", nama_lagu_anak)
            bundle.putString("desc_lagu_anak", desc_lagu_anak)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

    }

    private fun goToKidungUser() {
            val intent = Intent(this, AllKidungUserActivity::class.java)
            startActivity(intent)
            finish()
        }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}