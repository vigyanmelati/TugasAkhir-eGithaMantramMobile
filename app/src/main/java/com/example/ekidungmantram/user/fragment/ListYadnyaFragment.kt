package com.example.ekidungmantram.user.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BookmarkedDharmagitaAdapter
import com.example.ekidungmantram.database.data.Dharmagita
import com.example.ekidungmantram.database.setup.DharmagitaDb
import com.example.ekidungmantram.user.DetailKidungActivity
import com.example.ekidungmantram.user.DetailLaguAnakActivity
import kotlinx.android.synthetic.main.fragment_list_yadnya.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListYadnyaFragment : Fragment() {
//    private lateinit var db                : YadnyaDb
    private lateinit var db                : DharmagitaDb
//    private lateinit var bookmarkedAdapter : BookmarkedAdapter
    private lateinit var bookmarkedAdapter : BookmarkedDharmagitaAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_yadnya, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = Room.databaseBuilder(requireActivity(), DharmagitaDb::class.java, "dharmagitabookmarked.db").build()
        getAllDharmagitaBookmarkedData()
        setupRecyclerviewBookmark()
        swipeBook.setOnRefreshListener {
            getAllDharmagitaBookmarkedData()
            setupRecyclerviewBookmark()
            swipeBook.isRefreshing = false
        }
    }

    private fun setupRecyclerviewBookmark() {
        bookmarkedAdapter = BookmarkedDharmagitaAdapter(arrayListOf(), object : BookmarkedDharmagitaAdapter.OnAdapterListener{
            override fun onClick(result: Dharmagita) {
                val bundle = Bundle()
                if(result.id_tag == 4){
                    val intent = Intent(activity, DetailKidungActivity::class.java)
                    bundle.putInt("id_kidung", result.id_dharmagita)
                    bundle.putInt("tag_kidung", result.id_tag)
                    bundle.putString("nama_kidung", result.nama_post)
                    bundle.putString("gambar_kidung", result.gambar)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if (result.id_tag == 9){
                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                    bundle.putInt("id_lagu", result.id_dharmagita)
                    bundle.putInt("tag_lagu", result.id_tag)
                    bundle.putString("nama_lagu", result.nama_post)
                    bundle.putString("gambar_lagu", result.gambar)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if (result.id_tag == 10) {
                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                    bundle.putInt("id_pupuh", result.id_dharmagita)
                    bundle.putInt("tag_pupuh", result.id_tag)
                    bundle.putString("nama_pupuh", result.nama_post)
                    bundle.putString("gambar_pupuh", result.gambar)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if (result.id_tag == 11) {
                    val intent = Intent(activity, DetailLaguAnakActivity::class.java)
                    bundle.putInt("id_kakawin", result.id_dharmagita)
                    bundle.putInt("tag_kakawin", result.id_tag)
                    bundle.putString("nama_kakawin", result.nama_post)
                    bundle.putString("gambar_kakawin", result.gambar)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        })
        allYadnyasBookmarked.apply {
            layoutManager      = LinearLayoutManager(context)
            adapter            = bookmarkedAdapter
            setHasFixedSize(true)
        }
    }

    private fun getAllDharmagitaBookmarkedData() {
        CoroutineScope(Dispatchers.IO).launch {
            val dharmagita = db.dharmagitaDao().getAllBookmarkedDharmagita()
            withContext(Dispatchers.Main){
                if(dharmagita.isNotEmpty()){
                    nobookmark.visibility           = View.GONE
                    allYadnyasBookmarked.visibility = View.VISIBLE
                }else{
                    nobookmark.visibility           = View.VISIBLE
                    allYadnyasBookmarked.visibility = View.GONE
                }
                showData(dharmagita)
            }
        }
    }

    private fun showData(dharmagita: List<Dharmagita>) {
        bookmarkedAdapter.setData(dharmagita)
    }

    private fun printLog(message: String) {
        Log.d("ListdharmagitaFragment", message)
    }

}