package com.example.ekidungmantram.admin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ekidungmantram.AboutAppActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.adminmanager.AllAdminActivity
import com.example.ekidungmantram.admin.adminmanager.DetailProfileActivity
import com.example.ekidungmantram.admin.fragment.HomeAdminFragment
import com.example.ekidungmantram.admin.gamelan.AllGamelanAdminActivity
import com.example.ekidungmantram.admin.gita.AllGitaAdminActivity
import com.example.ekidungmantram.admin.kajiahlidharmagita.ListAhliNeedApprovalActivity
import com.example.ekidungmantram.admin.kajidharmagita.ListAllDharmagitaNotApprovalActivity
import com.example.ekidungmantram.admin.kajidharmagita.ListDharmagitaNeedApprovalActivity
import com.example.ekidungmantram.admin.kajimantram.ListMantramNeedApprovalActivity
import com.example.ekidungmantram.admin.kakawin.AllKakawinAdminActivity
import com.example.ekidungmantram.admin.kidung.AllKidungAdminActivity
import com.example.ekidungmantram.admin.laguanak.AllLaguAnakAdminActivity
import com.example.ekidungmantram.admin.mantram.AllMantramAdminActivity
import com.example.ekidungmantram.admin.prosesiupacara.AllProsesiAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllPupuhAdminActivity
import com.example.ekidungmantram.admin.tabuh.AllTabuhAdminActivity
import com.example.ekidungmantram.admin.tari.AllTariAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AllYadnyaAdminActivity
import com.example.ekidungmantram.admin.usermanager.AllUserActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AdminModel
import com.example.ekidungmantram.user.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeAdminActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var toggle  : ActionBarDrawerToggle
    private val fm: FragmentManager = supportFragmentManager
    private val homeFragment        = HomeAdminFragment()
    private var active : Fragment   = homeFragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
        setTitleActionBar("Beranda Admin")

        fm.beginTransaction().add(R.id.fragment_container_admin,homeFragment).commit()

        val drawerLayout : DrawerLayout = app_drawer_admin
        val navView : NavigationView = nav_view_admin
        val botView : BottomNavigationView = bottomnav_view_admin

        toggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.open, R.string.close
        )
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)
        val id            = sharedPreferences.getString("ID_ADMIN", null)
        val loged           = sharedPreferences.getString("LOGGED", null)

        if(loged == "1" && role == "3"){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        if(role != "1") {
            val nav_Menu: Menu = navView.getMenu()
//            nav_Menu.findItem(R.id.approval).setVisible(false)
            nav_Menu.findItem(R.id.kelola_admin).setVisible(false)
            nav_Menu.findItem(R.id.approval_ahli).setVisible(false)
            nav_Menu.findItem(R.id.kelola_user).setVisible(false)
            nav_Menu.findItem(R.id.approval_dharmagita).setVisible(false)
//            nav_Menu.findItem(R.id.gita_approve).setVisible(false)
        }

        if(role == "2"){
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.approval_dharmagita).setVisible(true)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.yadnya_admin -> goToYadnya()
//                R.id.tari_bali_admin -> goToTari()
//                R.id.tabuh_admin -> goToTabuh()
//                R.id.gamelan_bali_admin -> goToGamelan()
                R.id.sekar_madya_admin -> goToKidung()
                R.id.sekar_alit_admin -> goToPupuh()
                R.id.sekar_rare_admin -> goToLaguAnak()
                R.id.sekar_agung_admin -> goToKakawin()
//                R.id.mantram_admin -> goToMantram()
//                R.id.approval -> goToKajiMantram()
//                R.id.gita_admin ->goToGita()
//                R.id.gita_approve -> goToKajiGita()
                R.id.prosesi_upacara_admin -> goToProsesi()
                R.id.kelola_admin -> goToAdmin()
                R.id.kelola_user -> goToUser()
                R.id.approval_ahli -> goToApprovalAhli()
                R.id.approval_dharmagita -> goToKajiGita()
                R.id.logout -> goToLogout(id?.toInt())
                R.id.about_admin -> goToAbout()
                R.id.profile_admin -> goToDetailProfile()
            }

            true
        }
        botView.selectedItemId = R.id.adminHome
        botView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.adminHome -> {
                    fm.beginTransaction().show(homeFragment).commit()
                    active = homeFragment
                    setTitleActionBar("Beranda Admin")
                }
            }
            true
        }
    }

    private fun goToDetailProfile() {
        val intent = Intent(this, DetailProfileActivity::class.java)
        startActivity(intent)
    }

    private fun goToPupuh() {
        val intent = Intent(this, AllPupuhAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToKakawin() {
        val intent = Intent(this, AllKakawinAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToLaguAnak() {
        val intent = Intent(this, AllLaguAnakAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToGita() {
        val intent = Intent(this, AllGitaAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToKajiGita() {
        val intent = Intent(this, ListAllDharmagitaNotApprovalActivity::class.java)
        startActivity(intent)
    }

    private fun goToApprovalAhli() {
        val intent = Intent(this, ListAhliNeedApprovalActivity::class.java)
        startActivity(intent)
    }

    private fun goToYadnya() {
        val intent = Intent(this, AllYadnyaAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToProsesi() {
        val intent = Intent(this, AllProsesiAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToTari() {
        val intent = Intent(this, AllTariAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToGamelan() {
        val intent = Intent(this, AllGamelanAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToMantram() {
        val intent = Intent(this, AllMantramAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToKajiMantram() {
        val intent = Intent(this, ListMantramNeedApprovalActivity::class.java)
        startActivity(intent)
    }

    private fun goToAdmin() {
        val intent = Intent(this, AllAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToUser() {
        val intent = Intent(this, AllUserActivity::class.java)
        startActivity(intent)
    }

    private fun goToTabuh() {
        val intent = Intent(this, AllTabuhAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToKidung() {
        val intent = Intent(this, AllKidungAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogout(id: Int?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out")
            .setMessage("Apakah anda yakin ingin keluar dari halaman admin?")
            .setCancelable(true)
            .setPositiveButton("Iya") { _, _ ->
                invalidateAdminSession(id)
            }.setNegativeButton("Batal") { dialogInterface, _ ->
                dialogInterface.cancel()
            }.show()
    }

    private fun invalidateAdminSession(id: Int?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mencoba Logout")
        progressDialog.show()
        ApiService.endpoint.logoutAdmin(id!!)
            .enqueue(object: Callback<AdminModel> {
                override fun onResponse(
                    call: Call<AdminModel>,
                    response: Response<AdminModel>
                ) {
                    if(!response.body()?.error!!){
                        sharedPreferences = getSharedPreferences("is_logged", Context.MODE_PRIVATE)
                        sharedPreferences.edit().remove("ID_ADMIN").apply()
                        sharedPreferences.edit().remove("NAMA").apply()
                        sharedPreferences.edit().remove("ROLE").apply()
                        sharedPreferences.edit().remove("MESAGE").apply()
                        sharedPreferences.edit().remove("LOGGED").apply()
                        val intent = Intent(this@HomeAdminActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@HomeAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }else{
                        Toast.makeText(this@HomeAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<AdminModel>, t: Throwable) {
                    Toast.makeText(this@HomeAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            })
    }

    private fun goToAbout() {
        val intent = Intent(this, AboutAppActivity::class.java)
        startActivity(intent)
    }

    private fun setTitleActionBar(s: String) {
        supportActionBar!!.title = s
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}