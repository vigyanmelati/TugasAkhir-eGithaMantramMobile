package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllGamelanAdapter
import com.example.ekidungmantram.adapter.AllGitaAdapter
import com.example.ekidungmantram.model.AllGitaModel
import kotlinx.android.synthetic.main.activity_all_gamelan.*
import kotlinx.android.synthetic.main.activity_all_gita.*

class AllGitaActivity : AppCompatActivity() {

    private lateinit var rvGita: RecyclerView
    private lateinit var rvGita2: RecyclerView
    private val list = ArrayList<AllGitaModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_gita)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Gita"

        rvGita = findViewById(R.id.allGita1)
        rvGita.setHasFixedSize(true)

        rvGita2 = findViewById(R.id.allGita2)
        rvGita2.setHasFixedSize(true)

        list.addAll(listGita)
        showRecyclerList()

        swipeGita.setOnRefreshListener {
            showRecyclerList()
            swipeGita.isRefreshing = false
        }
    }

    private val listGita: ArrayList<AllGitaModel>
        get() {
            val dataName = resources.getStringArray(R.array.data_nama_gita)
            val dataDescription = resources.getStringArray(R.array.data_desc_gita)
            val listGita = ArrayList<AllGitaModel>()
            for (i in dataName.indices) {
                val gita = AllGitaModel(dataName[i],dataDescription[i])
                listGita.add(gita)
            }
            return listGita
        }

    private fun showRecyclerList() {
        noGita.visibility = View.GONE
        swipeGita.visibility   = View.VISIBLE
        shimmerGita.visibility = View.GONE
        rvGita.layoutManager = LinearLayoutManager(this)
        val listGitaAdapter = AllGitaAdapter(list,  object : AllGitaAdapter.OnAdapterAllGitaListener{
            override fun onClick(result: AllGitaModel) {
                val intent = Intent(this@AllGitaActivity, AllKidungActivity::class.java)
                startActivity(intent)
            }
        })
        rvGita.adapter = listGitaAdapter

    }

    private fun setShimmerToStop() {
        shimmerGita.stopShimmer()
        shimmerGita.visibility = View.GONE
        swipeGita.visibility   = View.VISIBLE
    }
}