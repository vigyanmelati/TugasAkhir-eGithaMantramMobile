package com.example.ekidungmantram.admin.kajidharmagita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDharmagitaNeedApprovalAdminAdapter
import com.example.ekidungmantram.model.adminmodel.AllDharmagitaAdminModel
import kotlinx.android.synthetic.main.activity_list_dharmagita_need_approval.*

class ListDharmagitaNeedApprovalActivity : AppCompatActivity() {
    private lateinit var rvDharmagita: RecyclerView
    private lateinit var rvDharmagita2: RecyclerView
    private val list = ArrayList<AllDharmagitaAdminModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_dharmagita_need_approval)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Gita"

        rvDharmagita = findViewById(R.id. allGitaNAAdmin1)
        rvDharmagita.setHasFixedSize(true)

        rvDharmagita2 = findViewById(R.id. allGitaNAAdmin2)
        rvDharmagita2.setHasFixedSize(true)

        list.addAll(listGita)
        showRecyclerList()

        swipeGitaNAAdmin.setOnRefreshListener {
            showRecyclerList()
            swipeGitaNAAdmin.isRefreshing = false
        }

        setShimmerToStop()
    }

    private val listGita: ArrayList<AllDharmagitaAdminModel>
        get() {
            val dataName = resources.getStringArray(R.array.data_nama_gita_app)
            val dataKategori = resources.getStringArray(R.array.data_kategori_gita_test)
            val listGita = ArrayList<AllDharmagitaAdminModel>()
            for (i in dataName.indices) {
                val gita = AllDharmagitaAdminModel(dataName[i],dataKategori[i])
                listGita.add(gita)
            }
            return listGita
        }

    private fun showRecyclerList() {
        noGitaNAAdmin.visibility = View.GONE
        swipeGitaNAAdmin.visibility   = View.VISIBLE
        shimmerGitaNAAdmin.visibility = View.GONE
        rvDharmagita.layoutManager = LinearLayoutManager(this)
        val listGitaAdapter = AllDharmagitaNeedApprovalAdminAdapter(list,  object : AllDharmagitaNeedApprovalAdminAdapter.OnAdapterAllDharmagitaNAAdminListener{

            override fun onClick(result: AllDharmagitaAdminModel) {
                val intent = Intent(this@ListDharmagitaNeedApprovalActivity, DetailDharmagitaNeedApprovalActivity::class.java)
                startActivity(intent)
            }
        })
        rvDharmagita.adapter = listGitaAdapter

    }

    private fun setShimmerToStop() {
        shimmerGitaNAAdmin.stopShimmer()
        shimmerGitaNAAdmin.visibility = View.GONE
        swipeGitaNAAdmin.visibility   = View.VISIBLE
    }
}