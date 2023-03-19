package com.example.ekidungmantram.user.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllDharmagitaHomeAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaHomeAdminAdapter
import com.example.ekidungmantram.admin.kajiahlidharmagita.ListAhliNeedApprovalActivity
import com.example.ekidungmantram.admin.kajidharmagita.ListDharmagitaNeedApprovalActivity
import com.example.ekidungmantram.admin.kakawin.AllKakawinAdminActivity
import com.example.ekidungmantram.admin.kidung.AllKidungAdminActivity
import com.example.ekidungmantram.admin.laguanak.AllLaguAnakAdminActivity
import com.example.ekidungmantram.admin.pupuh.AllPupuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllDharmagitaHomePenggunaModel
import com.example.ekidungmantram.model.adminmodel.AllDharmagitaHomeAdminModel
import com.example.ekidungmantram.model.adminmodel.JumlahModel
import com.example.ekidungmantram.user.AllDharmagitaCreatedActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home_admin.*
import kotlinx.android.synthetic.main.fragment_home_admin.namaAdmin
import kotlinx.android.synthetic.main.fragment_home_admin.shimmerHomeAdmin
import kotlinx.android.synthetic.main.fragment_home_admin.swipeAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment : Fragment() {
    private lateinit var yadnyaAdapter     : AllDharmagitaHomeAdapter
    private lateinit var sharedPreferences : SharedPreferences
    private var id_admin : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DharmagitaPenggunaHome.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        getAdminHomeDharmagitaData()
        sharedPreferences = getActivity()!!.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val nama          = sharedPreferences.getString("NAMA", null)
        val role          = sharedPreferences.getString("ROLE", null)
        id_admin         = sharedPreferences.getInt("ID_ADMIN_INT", 0)
        namaPengguna.text    = "Selamat Datang, $nama!"

        swipePengguna.setOnRefreshListener {
            getAdminHomeDharmagitaData()
            swipePengguna.isRefreshing = false
        }

    }

    private fun getAdminHomeDharmagitaData() {
        ApiService.endpoint.getDharmagitaPenggunaHomeList(id_admin)
            .enqueue(object: Callback<ArrayList<AllDharmagitaHomePenggunaModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDharmagitaHomePenggunaModel>>,
                    response: Response<ArrayList<AllDharmagitaHomePenggunaModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipePengguna.visibility = View.VISIBLE
                        shimmerHomePengguna.visibility = View.GONE
                    }else{
                        swipePengguna.visibility = View.GONE
                        shimmerHomePengguna.visibility = View.VISIBLE
                    }
                    yadnyaAdapter = datalist?.let { AllDharmagitaHomeAdapter(it,
                        object : AllDharmagitaHomeAdapter.OnAdapterAllDharmagitaHomePenggunaListener{
                            override fun onClick(result: AllDharmagitaHomePenggunaModel) {
//                                if(result.nama_tag == "Sekar Agung"){
//                                    val intent = Intent(activity, AllKakawinAdminActivity::class.java)
//                                    startActivity(intent)
//                                }else if(result.nama_tag == "Sekar Madya"){
//                                    val intent = Intent(activity, AllKidungAdminActivity::class.java)
//                                    startActivity(intent)
//                                }else if(result.nama_tag == "Sekar Alit"){
//                                    val intent = Intent(activity, AllPupuhAdminActivity::class.java)
//                                    startActivity(intent)
//                                }else if(result.nama_tag == "Sekar Rare"){
//                                    val intent = Intent(activity, AllLaguAnakAdminActivity::class.java)
//                                    startActivity(intent)
//                                }
                                val bundle = Bundle()
                                val intent = Intent(activity, AllDharmagitaCreatedActivity::class.java)
                                bundle.putInt("id_dharmagita", result.id_tag)
                                bundle.putInt("id_user", id_admin)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    DharmagitaPenggunaHome.adapter = yadnyaAdapter
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<ArrayList<AllDharmagitaHomePenggunaModel>>, t: Throwable) {
                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerHomePengguna.stopShimmer()
        shimmerHomePengguna.visibility = View.GONE
        swipePengguna.visibility       = View.VISIBLE
    }
}