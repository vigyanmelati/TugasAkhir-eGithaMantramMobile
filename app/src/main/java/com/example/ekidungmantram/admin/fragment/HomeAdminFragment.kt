package com.example.ekidungmantram.admin.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDharmagitaAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaHomeAdminAdapter
import com.example.ekidungmantram.admin.ListYadnyaAdminActivity
import com.example.ekidungmantram.admin.kajiahlidharmagita.ListAhliNeedApprovalActivity
import com.example.ekidungmantram.admin.kajidharmagita.ListAllDharmagitaNotApprovalActivity
import com.example.ekidungmantram.admin.kajidharmagita.ListDharmagitaNeedApprovalActivity
import com.example.ekidungmantram.admin.kakawin.AllKakawinAdminActivity
import com.example.ekidungmantram.admin.kidung.AllKidungAdminActivity
import com.example.ekidungmantram.admin.laguanak.AllLaguAnakAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllPupuhAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.SelectedAllYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllDharmagitaHomeAdminModel
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel
import com.example.ekidungmantram.model.adminmodel.JumlahModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_home_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeAdminFragment : Fragment() {
    private lateinit var yadnyaAdapter     : AllYadnyaHomeAdminAdapter
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_admin, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yadnyaAdminHome.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        getAdminHomeDharmagitaData()
        getAdminHomeAhliNoApprovalData()
        getAdminHomeDharmagitaNoApprovalData()
        sharedPreferences = getActivity()!!.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val nama          = sharedPreferences.getString("NAMA", null)
        val role          = sharedPreferences.getString("ROLE", null)
        namaAdmin.text    = "Selamat Datang, $nama!"

        if(role != "1"){
            card_ahli.visibility = View.GONE
        }
        swipeAdmin.setOnRefreshListener {
            getAdminHomeDharmagitaData()
            swipeAdmin.isRefreshing = false
        }
        to_ahli_dharmagita.setOnClickListener {
            val intent = Intent(activity, ListAhliNeedApprovalActivity::class.java)
            startActivity(intent)
        }
        to_dharmagita.setOnClickListener {
            val intent = Intent(activity, ListDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun getAdminHomeYadnyaData() {
//        ApiService.endpoint.getYadnyaAdminHomeList()
//            .enqueue(object: Callback<ArrayList<AllYadnyaHomeAdminModel>> {
//                override fun onResponse(
//                    call: Call<ArrayList<AllYadnyaHomeAdminModel>>,
//                    response: Response<ArrayList<AllYadnyaHomeAdminModel>>
//                ) {
//                    val datalist   = response.body()
//                    if(datalist != null){
//                        swipeAdmin.visibility = View.VISIBLE
//                        shimmerHomeAdmin.visibility = View.GONE
//                    }else{
//                        swipeAdmin.visibility = View.GONE
//                        shimmerHomeAdmin.visibility = View.VISIBLE
//                    }
//                    yadnyaAdapter = datalist?.let { AllYadnyaHomeAdminAdapter(it,
//                        object : AllYadnyaHomeAdminAdapter.OnAdapterAllYadnyaHomeAdminListener{
//                            override fun onClick(result: AllYadnyaHomeAdminModel) {
//                                val bundle = Bundle()
//                                val intent = Intent(activity, SelectedAllYadnyaAdminActivity::class.java)
//                                bundle.putInt("id_yadnya", result.id_kategori)
//                                bundle.putString("nama_yadnya", result.nama_kategori)
//                                intent.putExtras(bundle)
//                                startActivity(intent)
//                            }
//                        }) }!!
//
//                    yadnyaAdminHome.adapter = yadnyaAdapter
//                    setShimmerToStop()
//                }
//
//                override fun onFailure(call: Call<ArrayList<AllYadnyaHomeAdminModel>>, t: Throwable) {
//                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
//                    setShimmerToStop()
//                }
//
//            })
//    }

    private fun getAdminHomeDharmagitaNoApprovalData() {
        ApiService.endpoint.getDharmagitaNoApprovalAdminHomeList()
            .enqueue(object: Callback<JumlahModel> {
                override fun onResponse(
                    call: Call<JumlahModel>,
                    response: Response<JumlahModel>
                ) {
                    val datalist   = response.body()?.jumlah
                    if(datalist != null){
                        swipeAdmin.visibility = View.VISIBLE
                        shimmerHomeAdmin.visibility = View.GONE
                    }else{
                        swipeAdmin.visibility = View.GONE
                        shimmerHomeAdmin.visibility = View.VISIBLE
                    }
                    no_approve.text = datalist.toString()
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<JumlahModel>, t: Throwable) {
                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun getAdminHomeAhliNoApprovalData() {
        ApiService.endpoint.getAhliNoApprovalAdminHomeList()
            .enqueue(object: Callback<JumlahModel> {
                override fun onResponse(
                    call: Call<JumlahModel>,
                    response: Response<JumlahModel>
                ) {
                    val datalist   = response.body()?.jumlah
                    if(datalist != null){
                        swipeAdmin.visibility = View.VISIBLE
                        shimmerHomeAdmin.visibility = View.GONE
                    }else{
                        swipeAdmin.visibility = View.GONE
                        shimmerHomeAdmin.visibility = View.VISIBLE
                    }
                    ahli_approve.text = datalist.toString()
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<JumlahModel>, t: Throwable) {
                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

//    private fun getAdminHomeDharmagitaApprovalData() {
//        ApiService.endpoint.getDharmagitaApprovalAdminHomeList()
//            .enqueue(object: Callback<JumlahModel> {
//                override fun onResponse(
//                    call: Call<JumlahModel>,
//                    response: Response<JumlahModel>
//                ) {
//                    val datalist   = response.body()?.jumlah
//                    Log.d("approve", datalist.toString())
//                    if(datalist != null){
//                        swipeAdmin.visibility = View.VISIBLE
//                        shimmerHomeAdmin.visibility = View.GONE
//                    }else{
//                        swipeAdmin.visibility = View.GONE
//                        shimmerHomeAdmin.visibility = View.VISIBLE
//                    }
//                    approve.text = datalist.toString()
//                    setShimmerToStop()
//                }
//
//                override fun onFailure(call: Call<JumlahModel>, t: Throwable) {
//                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
//                    setShimmerToStop()
//                }
//
//            })
//    }

    private fun getAdminHomeDharmagitaData() {
        ApiService.endpoint.getDharmagitaAdminHomeList()
            .enqueue(object: Callback<ArrayList<AllDharmagitaHomeAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDharmagitaHomeAdminModel>>,
                    response: Response<ArrayList<AllDharmagitaHomeAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeAdmin.visibility = View.VISIBLE
                        shimmerHomeAdmin.visibility = View.GONE
                    }else{
                        swipeAdmin.visibility = View.GONE
                        shimmerHomeAdmin.visibility = View.VISIBLE
                    }
                    yadnyaAdapter = datalist?.let { AllYadnyaHomeAdminAdapter(it,
                        object : AllYadnyaHomeAdminAdapter.OnAdapterAllYadnyaHomeAdminListener{
                            override fun onClick(result: AllDharmagitaHomeAdminModel) {
                                if(result.nama_post == "Sekar Agung"){
                                    val intent = Intent(activity, AllKakawinAdminActivity::class.java)
                                    startActivity(intent)
                                }else if(result.nama_post == "Sekar Madya"){
                                    val intent = Intent(activity, AllKidungAdminActivity::class.java)
                                    startActivity(intent)
                                }else if(result.nama_post == "Sekar Alit"){
                                    val intent = Intent(activity, AllPupuhAdminActivity::class.java)
                                    startActivity(intent)
                                }else if(result.nama_post == "Sekar Rare"){
                                    val intent = Intent(activity, AllLaguAnakAdminActivity::class.java)
                                    startActivity(intent)
                                }
//                                val bundle = Bundle()
//                                val intent = Intent(activity, SelectedAllYadnyaAdminActivity::class.java)
//                                bundle.putInt("id_dharmagita", result.id_kategori)
//                                intent.putExtras(bundle)
//                                startActivity(intent)
                            }
                        }) }!!

                    yadnyaAdminHome.adapter = yadnyaAdapter
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<ArrayList<AllDharmagitaHomeAdminModel>>, t: Throwable) {
                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerHomeAdmin.stopShimmer()
        shimmerHomeAdmin.visibility = View.GONE
        swipeAdmin.visibility       = View.VISIBLE
    }
}