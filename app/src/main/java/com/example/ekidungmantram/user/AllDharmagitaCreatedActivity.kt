package com.example.ekidungmantram.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.LoginActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllDharmagitaUserAdapter
import com.example.ekidungmantram.adapter.KategoriPupuhAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllDharmagitaUserModel
import com.example.ekidungmantram.user.pupuh.DetailPupuhActivity
import kotlinx.android.synthetic.main.activity_all_dharmagita_created.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllDharmagitaCreatedActivity : AppCompatActivity() {
    private lateinit var kategoriPupuhAdapter : AllDharmagitaUserAdapter
    private lateinit var setAdapter    : AllDharmagitaUserAdapter
    private lateinit var sharedPreferences: SharedPreferences
    var id_user: Int = 0
    var id_dharmagita: Int = 0
    lateinit var nama_pupuh_kat: String
    lateinit var desc_pupuh_kat: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_dharmagita_created)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            id_user = bundle.getInt("id_user")
            id_dharmagita = bundle.getInt("id_dharmagita")
            Log.d("id_dd", id_dharmagita.toString())
            Log.d("id_uu", id_user.toString())

            if(id_dharmagita == 4){
                supportActionBar!!.title = "E-SekarMadya"
                daftar_namaDhar.text = "Daftar Sekar Madya"
                desc_Dhar.text ="Sekar Madya atau kidung adalah bentuk Dharmagita yang menggunakan bahasa Bali Tengah atau Kawi dan sering digunakan untuk upacara keagamaan"
            }else if(id_dharmagita == 9){
                supportActionBar!!.title = "E-SekarRare"
                daftar_namaDhar.text = "Daftar Sekar Rare"
                desc_Dhar.text = "Sekar Rare merupakan kumpulan lagu anak-anak yang bernuansa permainan. Sekar Rare umumnya menggunakan Bahasa Bali sederhana yang bersifat riang gembira dan dinamis sehingga mudah dilantunkan dalam suasana bermain."
            }else if(id_dharmagita == 10){
                supportActionBar!!.title = "E-SekarAlit"
                daftar_namaDhar.text = "Daftar Sekar Alit"
                desc_Dhar.text ="Sekar Alit adalah lagu yang mengandung cerita atau dapat juga berupa nasehat yang mengajarkan kebaikan. "
            }else if(id_dharmagita == 11){
                supportActionBar!!.title = "E-SekarAgung"
                daftar_namaDhar.text = "Daftar Sekar Agung"
                desc_Dhar.text ="Sekar Agung atau Tembang Gede meliputi lagu-lagu berbahasa Kawi"
            }



            allDhar1.layoutManager = LinearLayoutManager(applicationContext)
            allDhar2.layoutManager = LinearLayoutManager(applicationContext)
            getAllKategoriPupuh(id_user,id_dharmagita)

            swipeDhar.setOnRefreshListener {
                getAllKategoriPupuh(id_user,id_dharmagita)
                swipeDhar.isRefreshing = false
            }

            sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val role          = sharedPreferences.getString("ROLE", null)
           val id            = sharedPreferences.getInt("ID_ADMIN_INT", 0)
            val mesage = sharedPreferences.getString("MESAGE", null)

//            fabPupuh.setOnClickListener {
//                if(id_user == 0){
//                    val bundle = Bundle()
//                    val intent = Intent(this, LoginActivity::class.java)
//                    bundle.putString("APP", "pupuh")
//                    bundle.putInt("id_pupuh", postID)
//                    bundle.putString("nama_pupuh", namaPost)
//                    bundle.putString("desc_pupuh", descPost)
//                    intent.putExtras(bundle)
//                    startActivity(intent)
//                    finish()
//                }else{
//                    val bundle = Bundle()
//                    val intent = Intent(this, AllKategoriPupuhUserActivity::class.java)
//                    bundle.putInt("id_pupuh", postID)
//                    bundle.putString("nama_pupuh", namaPost)
//                    bundle.putString("desc_pupuh", descPost)
//                    intent.putExtras(bundle)
//                    startActivity(intent)
//                }
//            }
        }



    }

    private fun printLog(message: String) {
        Log.d("KategoriPupuhActivity", message)
    }

    private fun getAllKategoriPupuh(id_us: Int, id_dhar : Int) {
        ApiService.endpoint.getListDharmagitaUser(id_us,id_dhar)
            .enqueue(object: Callback<ArrayList<AllDharmagitaUserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDharmagitaUserModel>>,
                    response: Response<ArrayList<AllDharmagitaUserModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeDhar.visibility   = View.VISIBLE
                        shimmerDhar.visibility = View.GONE
                        noDhar.visibility      = View.GONE
                    }else{
                        swipeDhar.visibility   = View.GONE
                        shimmerDhar.visibility = View.VISIBLE
                        noDhar.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllDharmagitaUserAdapter(it,
                        object : AllDharmagitaUserAdapter.OnAdapterAllDharmagitaUserListener{
                            override fun onClick(result: AllDharmagitaUserModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllDharmagitaCreatedActivity, DetailPupuhActivity::class.java)
                                bundle.putInt("id_pupuh", result.id_post)
                                bundle.putString("nama_pupuh", result.nama_post)
                                bundle.putString("nama_tag_pupuh", result.nama_tag)
                                bundle.putString("gambar_pupuh", result.gambar)
                                bundle.putInt("tag_pupuh", result.id_tag)
                                bundle.putInt("id_pupuh_kat",  result.id_post)
                                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allDhar1.adapter  = setAdapter
                    setShimmerToStop()

                    cariDhar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noDhar.visibility   = View.GONE
                                    allDhar1.visibility = View.VISIBLE
                                    allDhar2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kategoriPupuhAdapter = AllDharmagitaUserAdapter(filter as ArrayList<AllDharmagitaUserModel>,
                                        object : AllDharmagitaUserAdapter.OnAdapterAllDharmagitaUserListener{
                                            override fun onClick(result: AllDharmagitaUserModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllDharmagitaCreatedActivity, DetailPupuhActivity::class.java)
                                                bundle.putInt("id_pupuh", result.id_post)
                                                bundle.putString("nama_pupuh", result.nama_post)
                                                bundle.putString("nama_tag_pupuh", result.nama_tag)
                                                bundle.putString("gambar_pupuh", result.gambar)
                                                bundle.putInt("tag_pupuh", result.id_tag)
//                                bundle.putInt("id_pupuh_kat", id_pupuh_kat)
                                                bundle.putString("nama_pupuh_kat", nama_pupuh_kat)
                                                bundle.putString("desc_pupuh_kat", desc_pupuh_kat)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noDhar.visibility   = View.VISIBLE
                                        allDhar1.visibility = View.GONE
                                        allDhar2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noDhar.visibility   = View.GONE
                                        allDhar2.visibility = View.VISIBLE
                                        allDhar2.adapter    = kategoriPupuhAdapter
                                        allDhar1.visibility = View.INVISIBLE
                                    }else{
                                        allDhar1.visibility = View.VISIBLE
                                        allDhar2.visibility = View.GONE
                                        noDhar.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllDharmagitaUserModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerDhar.stopShimmer()
        shimmerDhar.visibility = View.GONE
        swipeDhar.visibility   = View.VISIBLE
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


}