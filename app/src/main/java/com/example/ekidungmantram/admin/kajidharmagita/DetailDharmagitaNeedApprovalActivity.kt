package com.example.ekidungmantram.admin.kajidharmagita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.kajimantram.ListMantramNeedApprovalActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_dharmagita_need_approval.*
import kotlinx.android.synthetic.main.activity_detail_mantram_need_approval.*

class DetailDharmagitaNeedApprovalActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dharmagita_need_approval)

        detailNamaGitaNAAdmin.text  = "Meong-Meong"
        deskripsiGitaNAAdmin.text   = "Lagu ini kerap dinyanyikan saat anak-anak bermain Meong-meongan yang merupakan permainan tradisional masyarakat Bali. Permainan Meong-meongan menceritakan usaha kucing (meong) untuk menangkap tikus (bikul)."
        detailJenisGitaNAAdmin.text = "Sekar Rare"
        baitGitaNAAdmin.text = "Meong-meong\n" +
                "\n" +
                "Alih ja bikule\n" +
                "\n" +
                "Bikul gede gede\n" +
                "\n" +
                "Buin mokoh-mokoh\n" +
                "\n" +
                "Kereng pesan ngerusuhin\n" +
                "\n" +
                "Juk meng... Juk kul...\n" +
                "\n" + ""
        artiGitaNAAdmin.text = "Kucing-kucing\n" +
                "\n" +
                "Carilah si tikus\n" +
                "\n" +
                "Tikus besar-besar\n" +
                "\n" +
                "Juga gemuk-gemuk\n" +
                "\n" +
                "Selalu membuat masalah\n" +
                "\n" +
                "Juk meng... Juk kul...\n" +
                "\n" + ""
        catatanGitaNAAdmin.text ="Sudah Berkoordinasi dengan Pencipta Lagu"
        playYoutubeVideo("xzzNota-Oac")


        acceptGita.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Terima Dharmagita")
                .setMessage("Apakah anda yakin ingin terima Dharmagita ini untuk dipublish?")
                .setCancelable(true)
                .setPositiveButton("Iya") { _, _ ->
//                    accMantram(postID, "yes")
                }.setNegativeButton("Batal") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }.show()
        }

        rejectGita.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Tolak Mantram")
                .setMessage("Apakah anda yakin ingin menolak mantram ini untuk dipublish?")
                .setCancelable(true)
                .setPositiveButton("Iya") { _, _ ->
//                    accMantram(postID, "no")
                }.setNegativeButton("Batal") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }.show()
        }
        setShimmerToStop()
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerGitaNAAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1!!.loadVideo(video)
                p1.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setShimmerToStop() {
        shimmerDetailGitaNAAdmin.stopShimmer()
        shimmerDetailGitaNAAdmin.visibility = View.GONE
        scrollDetailGitaNAAdmin.visibility  = View.VISIBLE
    }
}
