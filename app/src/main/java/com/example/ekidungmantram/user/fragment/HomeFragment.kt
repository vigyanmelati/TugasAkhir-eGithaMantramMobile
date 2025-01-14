package com.example.ekidungmantram.user.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ekidungmantram.adapter.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.data.CardSliderData
import com.example.ekidungmantram.databinding.FragmentHomeBinding
import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.user.kakawin.AllKategoriKakawinActivity
import com.example.ekidungmantram.user.kidung.AllKidungActivity
import com.example.ekidungmantram.user.kidung.DetailKidungActivity
import com.example.ekidungmantram.user.laguanak.AllKategoriLaguAnakActivity
import com.example.ekidungmantram.user.pupuh.AllKategoriPupuhActivity
import com.example.ekidungmantram.user.pupuh.AllPupuhActivity
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding        : FragmentHomeBinding
    private lateinit var dots           : ArrayList<TextView>
    private val yadnyaList              = ArrayList<String>()
    private val list                    = ArrayList<CardSliderData>()
    private lateinit var handler        : Handler
    private lateinit var runnable       : Runnable
    private lateinit var cardAdapter    : CardSliderAdapter
    private lateinit var yadnyaAdapter  : NewYadnyaAdapter
    private lateinit var kidungAdapter  : NewKidungAdapter
    private lateinit var mantramAdapter : NewMantramAdapter
    private lateinit var pupuhAdapter : NewPupuhAdapter
    private lateinit var dharmagitaAdapter : NewDharmagitaAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var gridLayoutManagerK      : GridLayoutManager? = null
    private var gridLayoutManagerM      : GridLayoutManager? = null
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        namaUser.visibility = View.GONE
        sharedPreferences = requireActivity().getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val nama          = sharedPreferences.getString("NAMA", null)
        val islogged          = sharedPreferences.getString("LOGGED", null)
        if (nama != null){
            namaUser.visibility = View.VISIBLE
            namaUser.text    = "Selamat Datang, $nama!"
        }else{
            namaUser.visibility = View.GONE
        }

//        getYadnyaMasterData()
        getDharmagitaMasterData()
//        setupRecyclerViewY()
        setupRecyclerViewD()
        setupRecyclerViewK()
        setupRecyclerViewP()
        getLatestDharmagitaData()
        getLatestKidungData()
        getLatestPupuhData()
//        getLatestMantramData()
        runAutoSlideCard()

        lihatSemua.visibility = View.GONE
        lihatSemua2.setOnClickListener {
            val intent = Intent(activity, AllKidungActivity::class.java)
            startActivity(intent)
        }

        lihatSemua3.setOnClickListener {
            val intent = Intent(activity, AllPupuhActivity::class.java)
            startActivity(intent)
        }

        val swiped = binding.swipe
        swiped.setOnRefreshListener {
//            setupRecyclerViewY()
            setupRecyclerViewD()
            setupRecyclerViewK()
            setupRecyclerViewP()
//            setupRecyclerViewM()
//            getLatestYadnyaData()
            getLatestDharmagitaData()
            getLatestKidungData()
            getLatestPupuhData()
            swiped.isRefreshing = false
        }
    }

    private fun runAutoSlideCard() {
        handler  = Handler(Looper.myLooper()!!)
        runnable = object : Runnable {
            var index = 0
            override fun run() {
                if(index == list.size){
                    index = 0
                }
                binding.viewPager.currentItem = index
                index++
                handler.postDelayed(this, 5000)
            }
        }
        handler.post(runnable)
    }


    private fun setupRecyclerViewD() {
        dharmagitaAdapter = NewDharmagitaAdapter(arrayListOf(), object : NewDharmagitaAdapter.OnAdapterListener{
            override fun onClick(result: NewDharmagitaModel.Data) {
                val bundle = Bundle()
                if(result.id_tag == 4){
                    val bundle = Bundle()
                    val intent = Intent(activity, DetailKidungActivity::class.java)
                    bundle.putInt("id_kidung", result.id_post)
                    bundle.putInt("tag_kidung", result.id_tag)
                    bundle.putString("nama_kidung", result.nama_post)
//                    bundle.putString("nama_tag_kidung", result.nama_tag)
                    bundle.putString("gambar_kidung", result.gambar)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if(result.id_tag == 9){
                    val bundle = Bundle()
                    val intent = Intent(activity, AllKategoriLaguAnakActivity::class.java)
                    bundle.putInt("id_lagu_anak", result.id_post)
                    bundle.putString("nama_lagu_anak", result.nama_post)
                    bundle.putString("desc_lagu_anak", result.deskripsi)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if(result.id_tag ==10){
                    val bundle = Bundle()
                    val intent = Intent(activity, AllKategoriPupuhActivity::class.java)
                    bundle.putInt("id_pupuh", result.id_post)
                    bundle.putString("nama_pupuh", result.nama_post)
                    bundle.putString("desc_pupuh", result.deskripsi)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if(result.id_tag ==11){
                    val bundle = Bundle()
                    val intent = Intent(activity, AllKategoriKakawinActivity::class.java)
                    bundle.putInt("id_kakawin", result.id_post)
                    bundle.putString("nama_kakawin", result.nama_post)
                    bundle.putString("desc_kakawin", result.deskripsi)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        })
        binding.yadnyaBaru.apply {
            gridLayoutManagerY = GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = dharmagitaAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewK() {
        kidungAdapter = NewKidungAdapter(arrayListOf(), object : NewKidungAdapter.OnAdapterKidungListener{
            override fun onClick(result: NewKidungModel.DataK) {
                val bundle = Bundle()
                val intent = Intent(activity, DetailKidungActivity::class.java)
                bundle.putInt("id_kidung", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        binding.kidungBaru.apply {
            gridLayoutManagerK = GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerK
            adapter            = kidungAdapter
            setHasFixedSize(true)
        }
    }

//    private fun setupRecyclerViewM() {
//        mantramAdapter = NewMantramAdapter(arrayListOf(), object : NewMantramAdapter.OnAdapterMantramListener{
//            override fun onClick(result: NewMantramModel.DataM) {
//                val bundle = Bundle()
//                val intent = Intent(activity, DetailMantramActivity::class.java)
//                bundle.putInt("id_mantram", result.id_post)
//                intent.putExtras(bundle)
//                startActivity(intent)
//            }
//        })
//        binding.mantramBaru.apply {
//            gridLayoutManagerM = GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false)
//            layoutManager      = gridLayoutManagerM
//            adapter            = mantramAdapter
//            setHasFixedSize(true)
//        }
//    }

    private fun setupRecyclerViewP() {
        pupuhAdapter = NewPupuhAdapter(arrayListOf(), object : NewPupuhAdapter.OnAdapterPupuhListener{
            override fun onClick(result: NewPupuhModel.DataP) {
                val bundle = Bundle()
                val intent = Intent(activity, AllKategoriPupuhActivity::class.java)
                bundle.putInt("id_pupuh", result.id_post)
                bundle.putString("nama_pupuh", result.nama_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        binding.mantramBaru.apply {
            gridLayoutManagerM = GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerM
            adapter            = pupuhAdapter
            setHasFixedSize(true)
        }
    }

//    private fun getLatestYadnyaData() {
//        ApiService.endpoint.getYadnyaNewList()
//            .enqueue(object: Callback<NewYadnyaModel>{
//                override fun onResponse(
//                    call: Call<NewYadnyaModel>,
//                    response: Response<NewYadnyaModel>
//                ) {
//                    showYadnyaData(response.body()!!)
//                    binding.nodatayadnya.visibility  = View.GONE
//                }
//
//                override fun onFailure(call: Call<NewYadnyaModel>, t: Throwable) {
//                    printLog("on failure: $t")
//                }
//
//            })
//    }

    private fun getLatestKidungData() {
        ApiService.endpoint.getKidungNewList()
            .enqueue(object: Callback<NewKidungModel>{
                override fun onResponse(
                    call: Call<NewKidungModel>,
                    response: Response<NewKidungModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        binding.nodatakidung.visibility  = View.VISIBLE
                    }else{
                        binding.nodatakidung.visibility  = View.GONE
                        showKidungData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<NewKidungModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

//    private fun getLatestMantramData() {
//        ApiService.endpoint.getMantramNewList()
//            .enqueue(object: Callback<NewMantramModel>{
//                override fun onResponse(
//                    call: Call<NewMantramModel>,
//                    response: Response<NewMantramModel>
//                ) {
//                    if(response.body()!!.data.toString() == "[]"){
//                        binding.nodatamantram.visibility  = View.VISIBLE
//                    }else{
//                        showMantramData(response.body()!!)
//                        binding.nodatamantram.visibility  = View.GONE
//                    }
//                }
//
//                override fun onFailure(call: Call<NewMantramModel>, t: Throwable) {
//                    printLog("on failure: $t")
//                }
//
//            })
//    }

    private fun getLatestPupuhData() {
        ApiService.endpoint.getPupuhNewList()
            .enqueue(object: Callback<NewPupuhModel>{
                override fun onResponse(
                    call: Call<NewPupuhModel>,
                    response: Response<NewPupuhModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        binding.nodatamantram.visibility  = View.VISIBLE
                    }else{
                        showPupuhData(response.body()!!)
                        binding.nodatamantram.visibility  = View.GONE
                    }
                }

                override fun onFailure(call: Call<NewPupuhModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getLatestDharmagitaData() {
        ApiService.endpoint.getDharmagitaNewList()
            .enqueue(object: Callback<NewDharmagitaModel>{
                override fun onResponse(
                    call: Call<NewDharmagitaModel>,
                    response: Response<NewDharmagitaModel>
                ) {
                    showDharmagitaData(response.body()!!)
                    binding.nodatayadnya.visibility  = View.GONE
                }


                override fun onFailure(call: Call<NewDharmagitaModel>, t: Throwable) {
                    printLog("on failure dhar: $t")
                }

            })
    }

    private fun showYadnyaData(data: NewYadnyaModel) {
        val results = data.data
        yadnyaAdapter.setData(results)
    }

    private fun showDharmagitaData(data: NewDharmagitaModel) {
        val results = data.data
        dharmagitaAdapter.setData(results)
    }

    private fun showKidungData(data: NewKidungModel) {
        val results = data.data
        kidungAdapter.setData(results)
    }

    private fun showMantramData(data: NewMantramModel) {
        val results = data.data
        mantramAdapter.setData(results)
    }

    private fun showPupuhData(data: NewPupuhModel) {
        val results = data.data
        pupuhAdapter.setData(results)
    }

//    private fun getYadnyaMasterData() {
//        ApiService.endpoint.getYadnyaMasterList()
//            .enqueue(object:Callback<List<HomeModel>>{
//                override fun onResponse(
//                    call: Call<List<HomeModel>>,
//                    response: Response<List<HomeModel>>
//                ) {
//                    if(response.isSuccessful){
//                        val result = response.body()
//                        printLog(result.toString())
//                        fetchData(result!!)
//
//                        cardAdapter               = CardSliderAdapter(list)
//                        binding.viewPager.adapter = cardAdapter
//                        dots                      = ArrayList()
//                        setIndicator()
//
//                        binding.viewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
//                            override fun onPageSelected(position: Int) {
//                                selectedDot(position)
//                                super.onPageSelected(position)
//                            }
//                        })
//                        setShimmerToStop()
//                    }
//                }
//
//                override fun onFailure(call: Call<List<HomeModel>>, t: Throwable) {
//                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
//                    setShimmerToStop()
//                }
//
//            })
//    }

    private fun getDharmagitaMasterData() {
        ApiService.endpoint.getDharmagitaMasterList()
            .enqueue(object:Callback<List<HomeModel>>{
                override fun onResponse(
                    call: Call<List<HomeModel>>,
                    response: Response<List<HomeModel>>
                ) {
                    if(response.isSuccessful){
                        val result = response.body()
                        printLog(result.toString())
                        fetchData(result!!)

                        cardAdapter               = CardSliderAdapter(list)
                        binding.viewPager.adapter = cardAdapter
                        dots                      = ArrayList()
                        setIndicator()

                        binding.viewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
                            override fun onPageSelected(position: Int) {
                                selectedDot(position)
                                super.onPageSelected(position)
                            }
                        })
                        setShimmerToStop()
                    }
                }

                override fun onFailure(call: Call<List<HomeModel>>, t: Throwable) {
                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        binding.shimmerHome.stopShimmer()
        binding.shimmerHome.visibility = View.GONE
        binding.swipe.visibility       = View.VISIBLE
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    override fun onDestroyView() {
        clearList()
        handler.removeCallbacks(runnable)
        super.onDestroyView()
    }

    private fun clearList() {
        yadnyaList.clear()
        list.clear()
    }

    private fun selectedDot(position: Int) {
        for(i in 0 until list.size){
            if(i == position)
                dots[i].setTextColor(Color.parseColor("#E32027"))
            else
                dots[i].setTextColor(Color.parseColor("#545454"))
        }
    }

    private fun setIndicator() {
        val size = list.size
        if(size != null) {
            for (i in 0 until size) {
                dots.add(TextView(activity))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dots[i].text = Html.fromHtml("&#9679 ", Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    dots[i].text = Html.fromHtml("&#9679 ")
                }
                dots[i].textSize = 8f
                binding.dotsIndicator.addView(dots[i])
            }
        }else{
            for (i in 0 until 5) {
                dots.add(TextView(context))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dots[i].text = Html.fromHtml("&#9679 ", Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    dots[i].text = Html.fromHtml("&#9679 ")
                }
                dots[i].textSize = 8f
                binding.dotsIndicator.addView(dots[i])
            }
        }
    }

    private fun fetchData(titles: List<HomeModel>) {
        for (title in titles) {
            yadnyaList.add(
                title.nama_post
            )
        }

        for (i in yadnyaList.indices) {
            list.add(
                CardSliderData(
                    yadnyaList[i]
                )
            )
        }
    }
}
