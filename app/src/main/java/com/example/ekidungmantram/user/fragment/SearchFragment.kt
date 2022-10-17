package com.example.ekidungmantram.user.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllDharmagitaModel
import com.example.ekidungmantram.model.AllYadnyaModel
import com.example.ekidungmantram.user.DetailKidungActivity
import com.example.ekidungmantram.user.DetailLaguAnakActivity
import com.example.ekidungmantram.user.DetailYadnyaActivity
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
//    private lateinit var allyadnyaAdapter: AllYadnyaAdapter
    private lateinit var alldharmagitaAdapter: AllDharmagitaAdapter
//    private lateinit var setAdapter      : AllYadnyaAdapter
    private lateinit var setAdapter      : AllDharmagitaAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allYadnya1.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        allYadnya2.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        getAllGitaData()
        swipeSearch.setOnRefreshListener {
            getAllGitaData()
            swipeSearch.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("SearchFragment", message)
    }

//    private fun getAllYadnyaData() {
//        ApiService.endpoint.getYadnyaAllList()
//            .enqueue(object: Callback<ArrayList<AllYadnyaModel>> {
//                override fun onResponse(
//                    call: Call<ArrayList<AllYadnyaModel>>,
//                    response: Response<ArrayList<AllYadnyaModel>>
//                ) {
//                    val datalist   = response.body()
//                    if(datalist != null){
//                        swipeSearch.visibility = View.VISIBLE
//                        shimmerSearch.visibility = View.GONE
//                    }else{
//                        swipeSearch.visibility = View.GONE
//                        shimmerSearch.visibility = View.VISIBLE
//                    }
//                    setAdapter = datalist?.let { AllYadnyaAdapter(it,
//                        object : AllYadnyaAdapter.OnAdapterAllYadnyaListener{
//                            override fun onClick(result: AllYadnyaModel) {
//                                val bundle = Bundle()
//                                val intent = Intent(activity, DetailYadnyaActivity::class.java)
//                                bundle.putInt("id_yadnya", result.id_post)
//                                bundle.putInt("id_kategori", result.id_kategori)
//                                bundle.putString("nama_yadnya", result.nama_post)
//                                bundle.putString("kategori", result.kategori)
//                                bundle.putString("gambar", result.gambar)
//                                intent.putExtras(bundle)
//                                startActivity(intent)
//                            }
//                        }) }!!
//
//                    allYadnya1.adapter         = setAdapter
//                    noallyadnyadata.visibility = View.GONE
//                    setShimmerToStop()
//
//                    cariYadnya.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
//                        override fun onQueryTextSubmit(p0: String?): Boolean = false
//
//                        override fun onQueryTextChange(p0: String?): Boolean {
//                            if(p0 != null){
//                                if(p0.isEmpty()){
//                                    noallyadnyadata.visibility = View.GONE
//                                    allYadnya1.visibility      = View.VISIBLE
//                                    allYadnya2.visibility      = View.GONE
//                                }else if(p0.length > 2){
//                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
//                                    allyadnyaAdapter = AllYadnyaAdapter(filter as ArrayList<AllYadnyaModel>,
//                                        object : AllYadnyaAdapter.OnAdapterAllYadnyaListener{
//                                            override fun onClick(result: AllYadnyaModel) {
//                                                val bundle = Bundle()
//                                                val intent = Intent(activity, DetailYadnyaActivity::class.java)
//                                                bundle.putInt("id_yadnya", result.id_post)
//                                                bundle.putInt("id_kategori", result.id_kategori)
//                                                bundle.putString("nama_yadnya", result.nama_post)
//                                                bundle.putString("kategori", result.kategori)
//                                                bundle.putString("gambar", result.gambar)
//                                                intent.putExtras(bundle)
//                                                startActivity(intent)
//                                            }
//                                        })
//                                    if(filter.isEmpty()){
//                                        noallyadnyadata.visibility = View.VISIBLE
//                                        allYadnya1.visibility      = View.GONE
//                                        allYadnya2.visibility      = View.GONE
//                                    }
//                                    if(p0.isNotEmpty()){
//                                        noallyadnyadata.visibility = View.GONE
//                                        allYadnya2.visibility      = View.VISIBLE
//                                        allYadnya2.adapter         = allyadnyaAdapter
//                                        allYadnya1.visibility      = View.INVISIBLE
//                                    }else{
//                                        allYadnya1.visibility      = View.VISIBLE
//                                        allYadnya2.visibility      = View.GONE
//                                        noallyadnyadata.visibility = View.GONE
//                                    }
//                                }
//                            }
//                            return false
//                        }
//
//                    })
//                }
//
//                override fun onFailure(call: Call<ArrayList<AllYadnyaModel>>, t: Throwable) {
//                    printLog("on failure: $t")
//                }
//
//            })
//    }

    private fun getAllGitaData() {
        ApiService.endpoint.getGitaAllList()
            .enqueue(object: Callback<ArrayList<AllDharmagitaModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDharmagitaModel>>,
                    response: Response<ArrayList<AllDharmagitaModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeSearch.visibility = View.VISIBLE
                        shimmerSearch.visibility = View.GONE
                    }else{
                        swipeSearch.visibility = View.GONE
                        shimmerSearch.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllDharmagitaAdapter(it,
                        object : AllDharmagitaAdapter.OnAdapterAllDharmagitaListener{
                            override fun onClick(result: AllDharmagitaModel) {
                                val bundle = Bundle()
                                if(result.id_tag == 4){
                                    val intent = Intent(activity, DetailKidungActivity::class.java)
                                    bundle.putInt("id_kidung", result.id_post)
                                    bundle.putInt("tag_kidung", result.id_tag)
                                    bundle.putString("nama_kidung", result.nama_post)
                                    bundle.putString("gambar_kidung", result.gambar)
                                    intent.putExtras(bundle)
                                    startActivity(intent)
                                }else if (result.id_tag == 9){
                                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                                    bundle.putInt("id_lagu", result.id_post)
                                    bundle.putInt("tag_lagu", result.id_tag)
                                    bundle.putString("nama_lagu", result.nama_post)
                                    bundle.putString("gambar_lagu", result.gambar)
                                    intent.putExtras(bundle)
                                    startActivity(intent)
                                }else if (result.id_tag == 10) {
                                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                                    bundle.putInt("id_pupuh", result.id_post)
                                    bundle.putInt("tag_pupuh", result.id_tag)
                                    bundle.putString("nama_pupuh", result.nama_post)
                                    bundle.putString("gambar_pupuh", result.gambar)
                                    intent.putExtras(bundle)
                                    startActivity(intent)
                                }else if (result.id_tag == 11) {
                                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                                    bundle.putInt("id_kakawin", result.id_post)
                                    bundle.putInt("tag_kakawin", result.id_tag)
                                    bundle.putString("nama_kakawin", result.nama_post)
                                    bundle.putString("gambar_kakawin", result.gambar)
                                    intent.putExtras(bundle)
                                    startActivity(intent)
                                }
                            }
                        }) }!!

                    allYadnya1.adapter         = setAdapter
                    noallyadnyadata.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnya.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noallyadnyadata.visibility = View.GONE
                                    allYadnya1.visibility      = View.VISIBLE
                                    allYadnya2.visibility      = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    alldharmagitaAdapter = AllDharmagitaAdapter(filter as ArrayList<AllDharmagitaModel>,
                                        object : AllDharmagitaAdapter.OnAdapterAllDharmagitaListener{
                                            override fun onClick(result: AllDharmagitaModel) {
                                                val bundle = Bundle()
                                                if(result.id_tag == 4){
                                                    val intent = Intent(activity, DetailKidungActivity::class.java)
                                                    bundle.putInt("id_kidung", result.id_post)
                                                    bundle.putInt("tag_kidung", result.id_tag)
                                                    bundle.putString("nama_kidung", result.nama_post)
                                                    bundle.putString("gambar_kidung", result.gambar)
                                                    intent.putExtras(bundle)
                                                    startActivity(intent)
                                                }else if (result.id_tag == 9){
                                                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                                                    bundle.putInt("id_lagu", result.id_post)
                                                    bundle.putInt("tag_lagu", result.id_tag)
                                                    bundle.putString("nama_lagu", result.nama_post)
                                                    bundle.putString("gambar_lagu", result.gambar)
                                                    intent.putExtras(bundle)
                                                    startActivity(intent)
                                                }else if (result.id_tag == 10) {
                                                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                                                    bundle.putInt("id_pupuh", result.id_post)
                                                    bundle.putInt("tag_pupuh", result.id_tag)
                                                    bundle.putString("nama_pupuh", result.nama_post)
                                                    bundle.putString("gambar_pupuh", result.gambar)
                                                    intent.putExtras(bundle)
                                                    startActivity(intent)
                                                }else if (result.id_tag == 11) {
                                                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                                                    bundle.putInt("id_kakawin", result.id_post)
                                                    bundle.putInt("tag_kakawin", result.id_tag)
                                                    bundle.putString("nama_kakawin", result.nama_post)
                                                    bundle.putString("gambar_kakawin", result.gambar)
                                                    intent.putExtras(bundle)
                                                    startActivity(intent)
                                                }
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noallyadnyadata.visibility = View.VISIBLE
                                        allYadnya1.visibility      = View.GONE
                                        allYadnya2.visibility      = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noallyadnyadata.visibility = View.GONE
                                        allYadnya2.visibility      = View.VISIBLE
                                        allYadnya2.adapter         = alldharmagitaAdapter
                                        allYadnya1.visibility      = View.INVISIBLE
                                    }else{
                                        allYadnya1.visibility      = View.VISIBLE
                                        allYadnya2.visibility      = View.GONE
                                        noallyadnyadata.visibility = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllDharmagitaModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerSearch.stopShimmer()
        shimmerSearch.visibility = View.GONE
    }
}