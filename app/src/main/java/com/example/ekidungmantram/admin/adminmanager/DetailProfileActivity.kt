package com.example.ekidungmantram.admin.adminmanager

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.HomeAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailDataAdminModel
import com.example.ekidungmantram.user.MainActivity
import kotlinx.android.synthetic.main.activity_detail_admin.*
import kotlinx.android.synthetic.main.activity_detail_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detail Profile"

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)
        val id            = sharedPreferences.getInt("ID_ADMIN_INT", 0)

        getDetailData(id)

            toEditProfile.setOnClickListener {
                goToEditAdmin(id)
            }

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val userID = bundle.getInt("id_user")
            getDetailData(userID)

//            toEditAdmin.setOnClickListener {
//                goToEditAdmin(userID)
//            }
        }

            deleteProfile.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Akun")
                    .setMessage("Apakah anda yakin ingin menghapus akun ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        if (role != null) {
                            hapusAdmin(id, role)
                        }
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()

        }
    }

    private fun hapusAdmin(userID: Int, id: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataAdmin(userID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    sharedPreferences = getSharedPreferences("is_logged", Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove("ID_ADMIN").apply()
                    sharedPreferences.edit().remove("NAMA").apply()
                    sharedPreferences.edit().remove("ROLE").apply()
                    sharedPreferences.edit().remove("MESAGE").apply()
                    sharedPreferences.edit().remove("LOGGED").apply()
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailProfileActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(id)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailProfileActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailProfileActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goToEditAdmin(userID: Int) {
        val bundle = Bundle()
        val intent = Intent(this, EditAdminActivity::class.java)
        bundle.putInt("id_user", userID)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun getDetailData(userID: Int) {
        ApiService.endpoint.getDetailAdmin(userID).enqueue(object: Callback<DetailDataAdminModel> {
            override fun onResponse(
                call: Call<DetailDataAdminModel>,
                response: Response<DetailDataAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    namaDetailProfile.setText(result.name)
                    emailDetailProfile.setText(result.email)
                }
            }

            override fun onFailure(call: Call<DetailDataAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack(id: String) {
        if(id != "3"){
            val intent = Intent(this, HomeAdminActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}