package com.example.ekidungmantram.user

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.ekidungmantram.LoginActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.databinding.ActivityMainBinding
import com.example.ekidungmantram.model.AdminModel
import com.example.ekidungmantram.user.fragment.HomeFragment
import com.example.ekidungmantram.user.fragment.ListYadnyaFragment
import com.example.ekidungmantram.user.fragment.SearchFragment
import com.example.ekidungmantram.user.kakawin.AllKakawinActivity
import com.example.ekidungmantram.user.kidung.AllKidungActivity
import com.example.ekidungmantram.user.pupuh.AllPupuhActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var toggle  : ActionBarDrawerToggle
    private var doubleBackToExitPressedOnce = false
    private val homeFragment                = HomeFragment()
    private val searchFragment              = SearchFragment()
    private val listYadnya                  = ListYadnyaFragment()
    private val fm: FragmentManager         = supportFragmentManager
    private var active : Fragment           = homeFragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleActionBar("Beranda")

        fm.beginTransaction().add(R.id.fragment_container, listYadnya).hide(listYadnya).commit()
        fm.beginTransaction().add(R.id.fragment_container, searchFragment).hide(searchFragment).commit()
        fm.beginTransaction().add(R.id.fragment_container,homeFragment).commit()

        val drawerLayout : DrawerLayout    = binding.appDrawer
        val navView : NavigationView       = binding.navView
        val botView : BottomNavigationView = binding.bottomnavView

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
        Log.d("id_admin", id.toString())

        if(role != null) {
            val nav_Menu: Menu = navView.getMenu()
//            nav_Menu.findItem(R.id.approval).setVisible(false)
            nav_Menu.findItem(R.id.logout_user).setVisible(true)
            nav_Menu.findItem(R.id.login).setVisible(false)
//            nav_Menu.findItem(R.id.gita_approve).setVisible(false)
        }else{
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.logout_user).setVisible(false)
            nav_Menu.findItem(R.id.login).setVisible(true)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
//                R.id.tari_bali -> goToTari()
//                R.id.tabuh -> goToTabuh()
//                R.id.gamelan_bali -> goToGamelan()
                R.id.sekar_madya -> goToKidung()
                R.id.sekar_agung -> goToKakawin()
                R.id.sekar_alit -> goToPupuh()
                R.id.sekar_rare -> goToLaguAnak()
//                R.id.gita -> goToGita()
//                R.id.mantram -> goToMantram()
//                R.id.prosesi_upacara -> goToProsesi()
                R.id.login -> goToLogin()
                R.id.logout_user -> goToLogout(id?.toInt())
                R.id.about -> goToAbout()
            }

            true
        }

        botView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.home -> {
                    fm.beginTransaction().hide(active).show(homeFragment).commit()
                    active = homeFragment
                    setTitleActionBar("Beranda")
                }
                R.id.cari -> {
                    fm.beginTransaction().hide(active).show(searchFragment).commit()
                    active = searchFragment
                    setTitleActionBar("Cari Dharmagita")
                }
                R.id.list_yadnya -> {
                    fm.beginTransaction().hide(active).show(listYadnya).commit()
                    active = listYadnya
                    setTitleActionBar("Dharmagita Ditandai")
                }
            }
            true
        }

    }

    private fun setTitleActionBar(s: String) {
        supportActionBar!!.title = s
    }

    private fun goToLogout(id: Int?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out")
            .setMessage("Apakah anda yakin ingin keluar ?")
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
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }else{
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<AdminModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            })
    }

    private fun goToTari() {
        val intent = Intent(this, AllTariActivity::class.java)
        startActivity(intent)
    }

    private fun goToTabuh() {
        val intent = Intent(this, AllTabuhActivity::class.java)
        startActivity(intent)
    }

    private fun goToGamelan() {
        val intent = Intent(this, AllGamelanActivity::class.java)
        startActivity(intent)
    }

    private fun goToKidung() {
        val intent = Intent(this, AllKidungActivity::class.java)
        startActivity(intent)
    }

    private fun goToPupuh() {
        val intent = Intent(this, AllPupuhActivity::class.java)
        startActivity(intent)
    }

    private fun goToLaguAnak() {
        val intent = Intent(this, AlllLaguAnakActivity::class.java)
        startActivity(intent)
    }

    private fun goToKakawin() {
        val intent = Intent(this, AllKakawinActivity::class.java)
        startActivity(intent)
    }

    private fun goToGita() {
        val intent = Intent(this, AllGitaActivity::class.java)
        startActivity(intent)
    }

    private fun goToMantram() {
        val intent = Intent(this, AllMantramActivity::class.java)
        startActivity(intent)
    }

    private fun goToProsesi() {
        val intent = Intent(this, AllProsesiActivity::class.java)
        startActivity(intent)
    }

    private fun goToAbout() {
        val intent = Intent(this, AboutAppActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin() {
        val bundle = Bundle()
        val intent = Intent(this, LoginActivity::class.java)
        bundle.putString("APP", "main")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
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

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}