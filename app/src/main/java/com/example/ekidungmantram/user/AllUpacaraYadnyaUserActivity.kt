package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllYadnyaUserAdapter
import com.example.ekidungmantram.adapter.admin.AllYadnyaAdminAdapter
import com.example.ekidungmantram.admin.upacarayadnya.SelectedAllYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel
import kotlinx.android.synthetic.main.activity_all_upacara_yadnya_user.*
import kotlinx.android.synthetic.main.activity_all_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllUpacaraYadnyaUserActivity : AppCompatActivity() {
    private lateinit var yadnyaAdapter : AllYadnyaUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_upacara_yadnya_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Yadnya"

        allListYadnyaAdminUser.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataYadnya()

        swipeListYadnyaUser.setOnRefreshListener {
            getAllDataYadnya()
            swipeListYadnyaUser.isRefreshing = false
        }
    }

    private fun getAllDataYadnya() {
        ApiService.endpoint.getYadnyaAdminHomeList()
            .enqueue(object: Callback<ArrayList<AllYadnyaHomeAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllYadnyaHomeAdminModel>>,
                    response: Response<ArrayList<AllYadnyaHomeAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeListYadnyaUser.visibility = View.VISIBLE
                        shimmerListYadnyaUser.visibility = View.GONE
                    }else{
                        swipeListYadnyaUser.visibility = View.GONE
                        shimmerListYadnyaUser.visibility = View.VISIBLE
                    }
                    yadnyaAdapter = datalist?.let { AllYadnyaUserAdapter(it,
                        object : AllYadnyaUserAdapter.OnAdapterAllYadnyaHomeAdminListener{
                            override fun onClick(result: AllYadnyaHomeAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllUpacaraYadnyaUserActivity, SelectedAllYadnyaAdminActivity::class.java)
                                bundle.putInt("id_yadnya", result.id_kategori)
                                bundle.putString("nama_yadnya", result.nama_kategori)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allListYadnyaAdminUser.adapter = yadnyaAdapter
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<ArrayList<AllYadnyaHomeAdminModel>>, t: Throwable) {
                    Toast.makeText(this@AllUpacaraYadnyaUserActivity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerListYadnyaUser.stopShimmer()
        shimmerListYadnyaUser.visibility = View.GONE
        swipeListYadnyaUser.visibility       = View.VISIBLE
    }
}