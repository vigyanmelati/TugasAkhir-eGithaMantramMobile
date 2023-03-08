package com.example.ekidungmantram.admin.kajidharmagita

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.*
import com.example.ekidungmantram.adapter.admin.*
import com.example.ekidungmantram.admin.kajimantram.ListMantramNeedApprovalActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_dharmagita_need_approval.*
import kotlinx.android.synthetic.main.activity_detail_kakawin.*
import kotlinx.android.synthetic.main.activity_detail_kidung.*
import kotlinx.android.synthetic.main.activity_detail_lagu_anak.*
import kotlinx.android.synthetic.main.activity_detail_mantram_need_approval.*
import kotlinx.android.synthetic.main.activity_detail_pupuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDharmagitaNeedApprovalActivity : AppCompatActivity() {
    private var layoutManagerBaitPupuh          : LinearLayoutManager? = null
    private var layoutManagerBaitKakawin          : LinearLayoutManager? = null
    private var layoutManagerBaitKidung          : LinearLayoutManager? = null
    private var layoutManagerBaitLaguAnak         : LinearLayoutManager? = null
    private lateinit var baitKakawinAdapter : BaitDharmagitaAdapter
    private lateinit var baitPupuhAdapter : BaitPupuhApprovalAdapter
    private lateinit var baitKidungAdapter : BaitKidungApprovalAdapter
    private lateinit var baitLaguAnakAdapter : BaitLaguAnakApprovalAdapter
    private var layoutManagerArti          : LinearLayoutManager? = null
    private lateinit var artiKakawinAdapter : ArtiDharmagitaAdapter
    private var layoutManagerArtiPupuh          : LinearLayoutManager? = null
    private lateinit var artiPupuhAdapter : ArtiPupuhNAAdapter
    private var layoutManagerArtiLaguAnak          : LinearLayoutManager? = null
    private lateinit var artiLaguAnakAdapter : ArtiLaguAnakNAAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dharmagita_need_approval)

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_dharmagita")
            val tagID = bundle.getInt("id_tag_dharmagita")
            val namaTag = bundle.getInt("tag_dharmagita")
//            detailDharmagitaNA.text = namaTag.toString()
            Log.d("tag_test", tagID.toString())
            if(tagID == 4){
                detailDharmagitaNA.text = "Sekar Madya"
                getDetailDataKidung(postID)
                getBaitDataKidung(postID)
                setupRecyclerViewBaitKidung()
                arti_visible.visibility = View.GONE
                artiDharmagitaNAList.visibility = View.GONE
            }else if (tagID == 9){
                detailDharmagitaNA.text = "Sekar Rare"
                getDetailDataLaguAnak(postID)
                getBaitDataLaguAnak(postID)
                setupRecyclerViewBaitLaguAnak()
                setupRecyclerViewArtiLaguAnak()
            }else if (tagID == 10){
                detailDharmagitaNA.text = "Sekar Alit"
                getDetailDataPupuh(postID)
                getBaitDataPupuh(postID)
                setupRecyclerViewBaitPupuh()
                setupRecyclerViewArtiPupuh()
            }else if (tagID == 11){
                detailDharmagitaNA.text = "Sekar Agung"
                getDetailDataKakawin(postID)
                getBaitDataKakawin(postID)
                setupRecyclerViewBaitKakawin()
                setupRecyclerViewArtiKakawin()
            }

            acceptDharmagitaNA.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Terima Dharmagita")
                    .setMessage("Apakah anda yakin ingin terima Dharmagita ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "yes")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            rejectDharmagitaNA.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Tolak Dharmagita")
                    .setMessage("Apakah anda yakin ingin menolak Dharmagita ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "no")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToDharmagitaNA.setOnClickListener {
            val intent = Intent(this, ListDharmagitaNeedApprovalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun accMantram(postID: Int, s: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengupdate Data")
        progressDialog.show()
        ApiService.endpoint.approveDharmagita(postID, s).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailDharmagitaNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailDharmagitaNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailDharmagitaNeedApprovalActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDetailDataKakawin(id: Int) {
        ApiService.endpoint.getDetailKakawin(id).enqueue(object: Callback<DetailKakawinModel> {
            override fun onResponse(
                call: Call<DetailKakawinModel>,
                response: Response<DetailKakawinModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiDharmagitaNA.text   = result.deskripsi
                    detailNamaDharmagitaNA.text  = result.nama_post
                    detailDharmagitaNA.text = "Sekar Agung "
                    if(result.gambar != null) {
                        Glide.with(this@DetailDharmagitaNeedApprovalActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailDharmagitaNA)
//                            .load(result.gambar).into(imageDetailDharmagitaNA)
                    }else{
                        imageDetailDharmagitaNA.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKakawinModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("DetailKakawinActivity", message)
    }

    private fun getBaitDataKakawin(id: Int) {
        ApiService.endpoint.getDetailBaitKakawin(id).enqueue(object :
            Callback<DetailBaitKakawinModel> {
            override fun onResponse(
                call: Call<DetailBaitKakawinModel>,
                response: Response<DetailBaitKakawinModel>
            ) {
                showBaitDataKakawin(response.body()!!)
                showArtiDataKakawin(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitKakawinModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun getDetailDataPupuh(id: Int) {
        ApiService.endpoint.getDetailPupuh(id).enqueue(object: Callback<DetailPupuhModel> {
            override fun onResponse(
                call: Call<DetailPupuhModel>,
                response: Response<DetailPupuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiDharmagitaNA.text   = result.deskripsi
                    detailNamaDharmagitaNA.text  = result.nama_post
                    detailDharmagitaNA.text = "Sekar Alit"
                    if(result.gambar != null) {
                        Glide.with(this@DetailDharmagitaNeedApprovalActivity)
                            .load(result.gambar).into(imageDetailDharmagitaNA)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailPupuh)
                    }else{
                        imageDetailDharmagitaNA.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailPupuhModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitDataPupuh(id: Int) {
        ApiService.endpoint.getDetailBaitPupuh(id).enqueue(object :
            Callback<DetailBaitPupuhModel> {
            override fun onResponse(
                call: Call<DetailBaitPupuhModel>,
                response: Response<DetailBaitPupuhModel>
            ) {
                Log.d("bait_pupuh", response.body().toString())
                showBaitPupuhData(response.body()!!)
                showArtiDataPupuh(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitPupuhModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitPupuhData(body: DetailBaitPupuhModel) {
        val results = body.data
        baitPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewBaitPupuh() {
        baitPupuhAdapter = BaitPupuhApprovalAdapter(arrayListOf())
        baitDharmagitaNAList.apply {
            layoutManagerBaitPupuh = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerBaitPupuh
            adapter           = baitPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun showArtiDataPupuh(body: DetailBaitPupuhModel) {
        val results = body.data
        artiPupuhAdapter.setData(results)
    }

    private fun setupRecyclerViewArtiPupuh() {
        artiPupuhAdapter = ArtiPupuhNAAdapter(arrayListOf())
        artiDharmagitaNAList.apply {
            layoutManagerArtiPupuh = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerArtiPupuh
            adapter           = artiPupuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun showBaitDataKakawin(body: DetailBaitKakawinModel) {
        val results = body.data
        baitKakawinAdapter.setData(results)
//        artiKakawinAdapter.setData(results)
    }

    private fun showArtiDataKakawin(body: DetailBaitKakawinModel) {
        val results = body.data
        artiKakawinAdapter.setData(results)
    }

    private fun setupRecyclerViewBaitKakawin() {
        baitKakawinAdapter = BaitDharmagitaAdapter(arrayListOf())
        baitDharmagitaNAList.apply {
            layoutManagerBaitKakawin = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerBaitKakawin
            adapter           = baitKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewArtiKakawin() {
        artiKakawinAdapter = ArtiDharmagitaAdapter(arrayListOf())
        artiDharmagitaNAList.apply {
            layoutManagerArti = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerArti
            adapter           = artiKakawinAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDetailDataKidung(id: Int) {
        ApiService.endpoint.getDetailKidung(id).enqueue(object: Callback<DetailKidungModel> {
            override fun onResponse(
                call: Call<DetailKidungModel>,
                response: Response<DetailKidungModel>
            ) {
                val result = response.body()!!
                Log.d("detail_kidung", response.body().toString())
                result.let {
                    deskripsiDharmagitaNA.text   = result.deskripsi
                    detailNamaDharmagitaNA.text  = result.nama_post
//                    detailJenisDharmagitaNA.text = "Kidung " + result.nama_kategori
                    if(result.gambar != null) {
                        Glide.with(this@DetailDharmagitaNeedApprovalActivity)
                            .load(result.gambar).into(imageDetailDharmagitaNA)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailKidung)
                    }else{
                        imageDetailDharmagitaNA.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKidungModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitDataKidung(id: Int) {
        ApiService.endpoint.getDetailBaitKidung(id).enqueue(object : Callback<DetailBaitKidungModel>{
            override fun onResponse(
                call: Call<DetailBaitKidungModel>,
                response: Response<DetailBaitKidungModel>
            ) {
                showBaitKidungData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitKidungModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitKidungData(body: DetailBaitKidungModel) {
        val results = body.data
        baitKidungAdapter.setData(results)
    }

    private fun setupRecyclerViewBaitKidung() {
        baitKidungAdapter = BaitKidungApprovalAdapter(arrayListOf())
        baitDharmagitaNAList.apply {
            layoutManagerBaitKidung = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerBaitKidung
            adapter           = baitKidungAdapter
            setHasFixedSize(true)
        }
    }


    private fun getDetailDataLaguAnak(id: Int) {
        ApiService.endpoint.getDetailLaguAnak(id).enqueue(object: Callback<DetailLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailLaguAnakModel>,
                response: Response<DetailLaguAnakModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiDharmagitaNA.text   = result.deskripsi
                    detailNamaDharmagitaNA.text  = result.nama_post
//                    detailJenisDharmagitaNA.text = "Lagu Anak "
                    if(result.gambar != null) {
                        Glide.with(this@DetailDharmagitaNeedApprovalActivity)
                            .load(result.gambar).into(imageDetailDharmagitaNA)
//                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailLaguAnak)
                    }else{
                        imageDetailDharmagitaNA.setImageResource(R.drawable.sample_image_yadnya)
                    }
//                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailLaguAnakModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitDataLaguAnak(id: Int) {
        ApiService.endpoint.getDetailBaitLaguAnak(id).enqueue(object :
            Callback<DetailBaitLaguAnakModel> {
            override fun onResponse(
                call: Call<DetailBaitLaguAnakModel>,
                response: Response<DetailBaitLaguAnakModel>
            ) {
                showBaitLaguAnakData(response.body()!!)
                showArtiDataLaguAnak(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitLaguAnakModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitLaguAnakData(body: DetailBaitLaguAnakModel) {
        val results = body.data
        baitLaguAnakAdapter.setData(results)
    }

    private fun setupRecyclerViewBaitLaguAnak() {
        baitLaguAnakAdapter = BaitLaguAnakApprovalAdapter(arrayListOf())
        baitDharmagitaNAList.apply {
            layoutManagerBaitLaguAnak = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerBaitLaguAnak
            adapter           = baitLaguAnakAdapter
            setHasFixedSize(true)
        }
    }
    private fun showArtiDataLaguAnak(body: DetailBaitLaguAnakModel) {
        val results = body.data
        artiLaguAnakAdapter.setData(results)
    }

    private fun setupRecyclerViewArtiLaguAnak() {
        artiLaguAnakAdapter = ArtiLaguAnakNAAdapter(arrayListOf())
        artiDharmagitaNAList.apply {
            layoutManagerArtiLaguAnak = LinearLayoutManager(this@DetailDharmagitaNeedApprovalActivity)
            layoutManager     = layoutManagerArtiLaguAnak
            adapter           = artiLaguAnakAdapter
            setHasFixedSize(true)
        }
    }

    private fun goBack() {
        val intent = Intent(this, ListDharmagitaNeedApprovalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailDharmagitaNA.stopShimmer()
        shimmerDetailDharmagitaNA.visibility = View.GONE
        scrollDetailDharmagitaNA.visibility  = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ListDharmagitaNeedApprovalActivity::class.java)
        startActivity(intent)
    }
}
