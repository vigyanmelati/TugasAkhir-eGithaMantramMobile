package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.model.adminmodel.*
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {
    //Auth
    @FormUrlEncoded
    @POST("login")
    fun loginAdmin (
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<AdminModel>

    @FormUrlEncoded
    @POST("logout")
    fun logoutAdmin (
        @Field("id_admin") idAdmin:Int
    ):Call<AdminModel>

    @FormUrlEncoded
    @POST("register")
    fun registerUser (
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("name") name:String,
    ):Call<CrudModel>

    //Admin
    //Home
    @GET("admin/listyadnya")
    fun getYadnyaAdminHomeList(): Call<ArrayList<AllYadnyaHomeAdminModel>>
    @GET("admin/listdharmagita")
    fun getDharmagitaAdminHomeList(): Call<ArrayList<AllDharmagitaHomeAdminModel>>

    //Mantram
    @GET("admin/listallmantram")
    fun getAllMantramListAdmin() : Call<ArrayList<AllMantramAdminModel>>

    @GET("admin/detailmantram/{id_post}")
    fun getDetailMantramAdmin(@Path("id_post") id:Int) : Call<DetailMantramAdminModel>

    @FormUrlEncoded
    @POST("admin/createmantram")
    fun createMantramAdmin (
        @Field("role_admin") roleAdmin: Int,
        @Field("nama_post") namaPost:String,
        @Field("jenis_mantram") jenisMantram:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("kategori") kategori:String,
        @Field("gambar") gambar:String,
        @Field("approval_notes") approve:String
    ):Call<CrudModel>

    @GET("admin/showmantram/{id_post}")
    fun getShowMantramAdmin(@Path("id_post") id:Int) : Call<DetailMantramAdminModel>

    @FormUrlEncoded
    @POST("admin/updatemantram/{id_post}")
    fun updateMantramAdmin (
        @Field("role_admin") roleAdmin: Int,
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("jenis_mantram") jenisMantram:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("kategori") kategori:String,
        @Field("gambar") gambar:String,
        @Field("approval_notes") approve:String
    ):Call<CrudModel>

    @POST("admin/deletemantram/{id_post}")
    fun deleteMantramAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editbaitmantram/{id_post}")
    fun editBaitMantram (
        @Path("id_post") id:Int,
        @Field("bait_mantra") baitMantra:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editartimantram/{id_post}")
    fun editArtiMantram (
        @Path("id_post") id:Int,
        @Field("arti_mantra") artiMantra:String,
    ):Call<CrudModel>

    @GET("admin/listnotapprovedmantram")
    fun getAllNotApprovedMantramListAdmin() : Call<ArrayList<AllMantramAdminModel>>

    @GET("admin/detailneedapprovalmantram/{id_post}")
    fun getDetailNeedApprovalMantramAdmin(@Path("id_post") id:Int) : Call<DetailMantramAdminModel>

    @FormUrlEncoded
    @POST("admin/approvemantram/{id_post}")
    fun approveMantram (
        @Path("id_post") id:Int,
        @Field("stats") stats:String,
    ):Call<CrudModel>

    //Admin Management
    @GET("admin/listadmin")
    fun getAllListAdmin() : Call<ArrayList<AllDataAdminModel>>

    @GET("admin/detailadmin/{id_user}")
    fun getDetailAdmin(@Path("id_user") id:Int) : Call<DetailDataAdminModel>

    @FormUrlEncoded
    @POST("admin/createadmin")
    fun createDataAdmin (
        @Field("name") namaAdmin:String,
        @Field("email") emailAdmin:String,
        @Field("password") passwordAdmin:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editadmin/{id_user}")
    fun updateDataAdmin (
        @Path("id_user") id:Int,
        @Field("name") namaAdmin:String,
        @Field("email") emailAdmin:String,
        @Field("password") passwordAdmin:String,
    ):Call<CrudModel>

    @POST("admin/deleteadmin/{id_user}")
    fun deleteDataAdmin (
        @Path("id_user") id:Int
    ):Call<CrudModel>

    //Tabuh
    @GET("admin/listalltabuhadmin")
    fun getAllListTabuhAdmin() : Call<ArrayList<AllTabuhAdminModel>>

    @GET("admin/detailtabuhadmin/{id_post}")
    fun getDetailTabuhAdmin(@Path("id_post") id:Int) : Call<DetailTabuhAdminModel>

    @FormUrlEncoded
    @POST("admin/createtabuh")
    fun createDataTabuhAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showtabuh/{id_post}")
    fun getShowTabuhAdmin(@Path("id_post") id:Int) : Call<DetailTabuhAdminModel>

    @FormUrlEncoded
    @POST("admin/edittabuh/{id_post}")
    fun updateDataTabuhAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletetabuh/{id_post}")
    fun deleteDataTabuhAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Kidung
    @GET("admin/listallkidungadmin")
    fun getAllListKidungAdmin() : Call<ArrayList<AllKidungAdminModel>>

    @GET("admin/detailkidungadmin/{id_post}")
    fun getDetailKidungAdmin(@Path("id_post") id:Int) : Call<DetailKidungAdminModel>

    @GET("admin/listlirikkidungadmin/{id_post}")
    fun getDetailAllLirikKidungAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllLirikKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/createkidung")
    fun createDataKidungAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("kategori") kategori:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showkidung/{id_post}")
    fun getShowKidungAdmin(@Path("id_post") id:Int) : Call<DetailKidungAdminModel>

    @FormUrlEncoded
    @POST("admin/editkidung/{id_post}")
    fun updateDataKidungAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("kategori") kategori:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletekidung/{id_post}")
    fun deleteDataKidungAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/lirikkidungadmin/{id_post}")
    fun getAllLirikKidungAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllLirikKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/addlirikkidung/{id_post}")
    fun createDataLirikKidungAdmin (
        @Path("id_post") id:Int,
        @Field("bait_kidung") baitKidung:String,
    ):Call<CrudModel>

    @GET("admin/showlirikkidung/{id_det_post}")
    fun getShowLirikKidungAdmin(@Path("id_det_post") id:Int) : Call<DetailLirikKidungAdminModel>

    @FormUrlEncoded
    @POST("admin/editlirikkidung/{id_det_post}")
    fun updateDataLirikKidungAdmin (
        @Path("id_det_post") id:Int,
        @Field("bait_kidung") lirikKidung:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deletelirikkidung/{id_det_post}")
    fun deleteDataLirikKidungAdmin (
        @Path("id_det_post") id:Int,
        @Field("kidung_id") idKidung:Int,
    ):Call<CrudModel>

    //Gamelan
    @GET("admin/listallgamelanadmin")
    fun getAllListGamelanAdmin() : Call<ArrayList<AllGamelanAdminModel>>

    @GET("admin/detailgamelanadmin/{id_post}")
    fun getDetailGamelanAdmin(@Path("id_post") id:Int) : Call<DetailGamelanAdminModel>

    @GET("admin/listtabuhongamelan/{id_post}")
    fun getDetailAllTabuhOnGamelanAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTabuhOnGamelanAdminModel>>

    @FormUrlEncoded
    @POST("admin/creategamelan")
    fun createDataGamelanAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showgamelan/{id_post}")
    fun getShowGamelanAdmin(@Path("id_post") id:Int) : Call<DetailGamelanAdminModel>

    @FormUrlEncoded
    @POST("admin/editgamelan/{id_post}")
    fun updateDataGamelanAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletegamelan/{id_post}")
    fun deleteDataGamelanAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtabuhnotongamelan/{id_post}")
    fun getDetailAllTabuhNotOnGamelanAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTabuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtabuhongamelan/{id_post}")
    fun addDataTabuhToGamelanAdmin (
        @Path("id_post") id:Int,
        @Field("id_tabuh") idTabuh:Int,
    ):Call<CrudModel>

    @POST("admin/deletetabuhongamelan/{id_post}")
    fun deleteDataTabuhOnGamelanAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Tari
    @GET("admin/listalltariadmin")
    fun getAllListTariAdmin() : Call<ArrayList<AllTariAdminModel>>

    @GET("admin/detailtariadmin/{id_post}")
    fun getDetailTariAdmin(@Path("id_post") id:Int) : Call<DetailTariAdminModel>

    @GET("admin/listtabuhontari/{id_post}")
    fun getDetailAllTabuhOnTariAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTabuhOnTariAdminModel>>

    @FormUrlEncoded
    @POST("admin/createtari")
    fun createDataTariAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showtari/{id_post}")
    fun getShowTariAdmin(@Path("id_post") id:Int) : Call<DetailTariAdminModel>

    @FormUrlEncoded
    @POST("admin/edittari/{id_post}")
    fun updateDataTariAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletetari/{id_post}")
    fun deleteDataTariAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtabuhnotontari/{id_post}")
    fun getDetailAllTabuhNotOnTariAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTabuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtabuhontari/{id_post}")
    fun addDataTabuhToTariAdmin (
        @Path("id_post") id:Int,
        @Field("id_tabuh") idTabuh:Int,
    ):Call<CrudModel>

    @POST("admin/deletetabuhontari/{id_post}")
    fun deleteDataTabuhOnTariAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Prosesi
    @GET("admin/listallprosesiadmin")
    fun getAllListProsesiAdmin() : Call<ArrayList<AllProsesiAdminModel>>

    @GET("admin/detailprosesiadmin/{id_post}")
    fun getDetailProsesiAdmin(@Path("id_post") id:Int) : Call<DetailProsesiAdminModel>

    @GET("admin/listgamelanonprosesi/{id_post}")
    fun getDetailAllGamelanOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllGamelanOnProsesiAdminModel>>

    @GET("admin/listtarionprosesi/{id_post}")
    fun getDetailAllTariOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTariOnProsesiAdminModel>>

    @GET("admin/listkidungonprosesi/{id_post}")
    fun getDetailAllKidungOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllKidungOnProsesiAdminModel>>

    @GET("admin/listtabuhonprosesi/{id_post}")
    fun getDetailAllTabuhOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTabuhOnProsesiAdminModel>>

    @GET("admin/listmantramonprosesi/{id_post}")
    fun getDetailAllMantramOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllMantramOnProsesiAdminModel>>

    @GET("admin/listprosesikhusus/{id_prosesi}/{id_yadnya}")
    fun getDetailAllProsesiKhusus(
        @Path("id_prosesi") idProsesi:Int,
        @Path("id_yadnya") idYadnya:Int
    ) : Call<ArrayList<DetailAllProsesiKhususAdminModel>>

    @FormUrlEncoded
    @POST("admin/createprosesi")
    fun createDataProsesiAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showprosesi/{id_post}")
    fun getShowProsesiAdmin(@Path("id_post") id:Int) : Call<DetailProsesiAdminModel>

    @FormUrlEncoded
    @POST("admin/editprosesi/{id_post}")
    fun updateDataProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deleteprosesi/{id_post}")
    fun deleteDataProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listgamelannotonprosesi/{id_post}")
    fun getDetailAllGamelanNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllGamelanAdminModel>>

    @FormUrlEncoded
    @POST("admin/addgamelanonprosesi/{id_post}")
    fun addDataGamelanToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_gamelan") idGamelan:Int,
    ):Call<CrudModel>

    @POST("admin/deletegamelanonprosesi/{id_post}")
    fun deleteDataGamelanOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtarinotonprosesi/{id_post}")
    fun getDetailAllTariNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTariAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtarionprosesi/{id_post}")
    fun addDataTariToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_tari") idTari:Int,
    ):Call<CrudModel>

    @POST("admin/deletetarionprosesi/{id_post}")
    fun deleteDataTariOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listkidungnotonprosesi/{id_post}")
    fun getDetailAllKidungNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/addkidungonprosesi/{id_post}")
    fun addDataKidungToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_kidung") idKidung:Int,
    ):Call<CrudModel>

    @POST("admin/deletekidungonprosesi/{id_post}")
    fun deleteDataKidungOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtabuhnotonprosesi/{id_post}")
    fun getDetailAllTabuhNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTabuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtabuhonprosesi/{id_post}")
    fun addDataTabuhToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_tabuh") idTabuh:Int,
    ):Call<CrudModel>

    @POST("admin/deletetabuhonprosesi/{id_post}")
    fun deleteDataTabuhOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listmantramnotonprosesi/{id_post}")
    fun getDetailAllMantramNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllMantramAdminModel>>

    @FormUrlEncoded
    @POST("admin/addmantramonprosesi/{id_post}")
    fun addDataMantramToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_mantram") idMantram:Int,
    ):Call<CrudModel>

    @POST("admin/deletemantramonprosesi/{id_post}")
    fun deleteDataMantramOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listprosesikhususnotyet/{id_prosesi}/{id_yadnya}")
    fun getDetailAllProsesiKhususNotYet(
        @Path("id_prosesi") idProsesi:Int,
        @Path("id_yadnya") idYadnya:Int
    ) : Call<ArrayList<AllProsesiAdminModel>>

    @FormUrlEncoded
    @POST("admin/addprosesikhusus/{id_prosesi}/{id_yadnya}")
    fun addDataProsesiKhusus (
        @Path("id_prosesi") idProsesi:Int,
        @Path("id_yadnya") idYadnya:Int,
        @Field("id_prosesis") idProsesis:Int,
    ):Call<CrudModel>

    @POST("admin/deleteprosesikhusus/{id}")
    fun deleteprosesikhusus (
        @Path("id") id:Int
    ):Call<CrudModel>

    //Yadnya
    @GET("admin/listallyadnyaadmin/{id_yadnya}")
    fun getAllListYadnyaAdmin(
        @Path("id_yadnya") id:Int
    ) : Call<ArrayList<AllYadnyaAdminModel>>

    @GET("admin/detailyadnyaadmin/{id_post}")
    fun getDetailYadnyaAdmin(@Path("id_post") id:Int) : Call<DetailYadnyaAdminModel>

    @GET("admin/listprosesiawalonyadnya/{id_post}")
    fun getDetailAllProsesiAwalOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>

    @GET("admin/listprosesipuncakonyadnya/{id_post}")
    fun getDetailAllProsesiPuncakOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>>

    @GET("admin/listprosesiakhironyadnya/{id_post}")
    fun getDetailAllProsesiAkhirOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>>

    @GET("admin/listgamelanonyadnya/{id_post}")
    fun getDetailAllGamelansOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllGamelanOnYadnyaAdminModel>>

    @GET("admin/listtarionyadnya/{id_post}")
    fun getDetailAllTariOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTariOnYadnyaAdminModel>>

    @GET("admin/listkidungonyadnya/{id_post}")
    fun getDetailAllKidungOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllKidungOnYadnyaAdminModel>>

    @FormUrlEncoded
    @POST("admin/createyadnya")
    fun createDataYadnyaAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
        @Field("kategori") kategori:String,
    ):Call<CrudModel>

    @GET("admin/showyadnya/{id_post}")
    fun getShowYadnyaAdmin(@Path("id_post") id:Int) : Call<DetailYadnyaAdminModel>

    @FormUrlEncoded
    @POST("admin/edityadnya/{id_post}")
    fun updateDataYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deleteyadnya/{id_post}")
    fun deleteDataYadnyaAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listprosesiawalnotonyadnya/{id_post}")
    fun getDetailAllProsesiAwalNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllProsesiAdminModel>>

    @FormUrlEncoded
    @POST("admin/addprosesiawalonyadnya/{id_post}")
    fun addDataProsesiAwalToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/upprosesiawalonyadnya/{id_post}")
    fun upDataProsesiAwalAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/downprosesiawalonyadnya/{id_post}")
    fun downDataProsesiAwalAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deleteprosesiawalonyadnya/{id_post}")
    fun deleteDataProsesiAwalOnYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("yadnya_id") idYadnya:Int,
    ):Call<CrudModel>

    @GET("admin/listprosesipuncaknotonyadnya/{id_post}")
    fun getDetailAllProsesiPuncakNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllProsesiAdminModel>>

    @FormUrlEncoded
    @POST("admin/addprosesipuncakonyadnya/{id_post}")
    fun addDataProsesiPuncakToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/upprosesipuncakonyadnya/{id_post}")
    fun upDataProsesiPuncakAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/downprosesipuncakonyadnya/{id_post}")
    fun downDataProsesiPuncakAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deleteprosesipuncakonyadnya/{id_post}")
    fun deleteDataProsesiPuncakOnYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("yadnya_id") idYadnya:Int,
    ):Call<CrudModel>

    @GET("admin/listprosesiakhirnotonyadnya/{id_post}")
    fun getDetailAllProsesiAkhirNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllProsesiAdminModel>>

    @FormUrlEncoded
    @POST("admin/addprosesiakhironyadnya/{id_post}")
    fun addDataProsesiAkhirToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/upprosesiakhironyadnya/{id_post}")
    fun upDataProsesiAkhirAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/downprosesiakhironyadnya/{id_post}")
    fun downDataProsesiAkhirAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deleteprosesiakhironyadnya/{id_post}")
    fun deleteDataProsesiAkhirOnYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("yadnya_id") idYadnya:Int,
    ):Call<CrudModel>

    @GET("admin/listgamelannotonyadnya/{id_post}")
    fun getDetailAllGamelanNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllGamelanAdminModel>>

    @FormUrlEncoded
    @POST("admin/addgamelanonyadnya/{id_post}")
    fun addDataGamelanToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_gamelan") idGamelan:Int,
    ):Call<CrudModel>

    @POST("admin/deletegamelanonyadnya/{id_post}")
    fun deleteDataGamelanOnYadnyaAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtarinotonyadnya/{id_post}")
    fun getDetailAllTariNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTariAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtarionyadnya/{id_post}")
    fun addDataTariToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_tari") idTari:Int,
    ):Call<CrudModel>

    @POST("admin/deletetarionyadnya/{id_post}")
    fun deleteDataTariOnYadnyaAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listkidungnotonyadnya/{id_post}")
    fun getDetailAllKidungNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/addkidungonyadnya/{id_post}")
    fun addDataKidungToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_kidung") idKidung:Int,
    ):Call<CrudModel>

    @POST("admin/deletekidungonyadnya/{id_post}")
    fun deleteDataKidungOnYadnyaAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Pupuh
    @GET("admin/listallpupuhadmin")
    fun getPupuhMasterListAdmin() : Call<ArrayList<AllPupuhAdminModel>>
    @GET("admin/listkategoripupuhadmin/{id_pupuh}")
    fun getKategoriPupuhListAdmin(@Path("id_pupuh") id: Int) : Call<ArrayList<KategoriPupuhAdminModel>>
    @FormUrlEncoded
    @POST("admin/createpupuhadmin")
    fun createDataPupuhAdmin (
        @Field("nama_post") namaPost:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
        @Field("id_pupuh") id_pupuh:Int,
    ):Call<CrudModel>
    @GET("admin/detailpupuhadmin/{id_post}")
    fun getDetailPupuhAdmin(@Path("id_post") id: Int) : Call<DetailPupuhAdminModel>
    @GET("admin/detailbaitpupuhadmin/{id_post}")
    fun getDetailBaitPupuhAdmin(@Path("id_post") id:Int) : Call<DetailBaitPupuhAdminModel>
    @GET("admin/listvideopupuhadmin/{id_pupuh}")
    fun getListVideoPupuhAdmin(@Path("id_pupuh") id:Int): Call<VideoPupuhAdminModel>
    @GET("admin/listaudiopupuhadmin/{id_post}")
    fun getListAudioPupuhAdmin(@Path("id_post") id:Int): Call<AudioPupuhAdminModel>
    @GET("admin/yadnyapupuhadmin/{id_pupuh}")
    fun getYadnyaPupuhAdmin(@Path("id_pupuh") id:Int): Call<YadnyaPupuhAdminModel>
    @GET("admin/showpupuh/{id_post}")
    fun getShowPupuhAdmin(@Path("id_post") id:Int) : Call<DetailPupuhAdminModel>
    @FormUrlEncoded
    @POST("admin/editpupuhadmin/{id_post}")
    fun updatePupuhAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletepupuhadmin/{id_post}")
    fun deletePupuhAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listbaitpupuhadmin/{id_post}")
    fun getAllLirikPupuhAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllLirikPupuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addlirikpupuhadmin/{id_post}")
    fun createDataLirikPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("bait_pupuh") baitPupuh:String,
    ):Call<CrudModel>

    @GET("admin/showlirikpupuhadmin/{id_det_post}")
    fun getShowLirikPupuhAdmin(@Path("id_det_post") id:Int) : Call<DetailLirikPupuhAdminModel>

    @FormUrlEncoded
    @POST("admin/editlirikpupuhadmin/{id_det_post}")
    fun updateDataLirikPupuhAdmin (
        @Path("id_det_post") id:Int,
        @Field("bait_pupuh") lirikPupuh:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deletelirikpupuhadmin/{id_post}")
    fun deleteDataLirikPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("pupuh_id") idPupuh:Int,
    ):Call<CrudModel>

    @GET("admin/showvideopupuhadmin/{id_post}")
    fun getShowVideoPupuhAdmin(@Path("id_post") id:Int) : Call<DetailVideoPupuhAdminModel>

    @FormUrlEncoded
    @POST("admin/addvideoonpupuhadmin/{id_post}")
    fun createDataVideoPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("judul_video") judulVideo:String,
        @Field("gambar_video") gambarVideo:String,
        @Field("video") Video:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editvideopupuhadmin/{id_post}")
    fun updateDataVideoPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("judul_video") judulVideo:String,
        @Field("gambar_video") gambarVideo:String,
        @Field("video") Video:String,
    ):Call<CrudModel>

    @POST("admin/deletevideoonpupuhadmin/{id_post}")
    fun deleteDataVideoPupuhAdmin (
        @Path("id_post") id:Int,
    ):Call<CrudModel>

    @GET("admin/showaudiopupuhadmin/{id_post}")
    fun getShowAudioPupuhAdmin(@Path("id_post") id:Int) : Call<DetailAudioPupuhAdminModel>

    @FormUrlEncoded
    @POST("admin/addaudioonpupuhadmin/{id_post}")
    fun createDataAudioPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("judul_audio") judulAudio:String,
        @Field("gambar_audio") gambarAudio:String,
        @Field("audio") Audio:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editaudiopupuhadmin/{id_post}")
    fun updateDataAudioPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("judul_audio") judulAudio:String,
        @Field("gambar_audio") gambarAudio:String,
        @Field("audio") Audio:String,
    ):Call<CrudModel>

    @POST("admin/deleteaudioonpupuhadmin/{id_post}")
    fun deleteDataAudioPupuhAdmin (
        @Path("id_post") id:Int,
    ):Call<CrudModel>

    @GET("admin/listyadnyanotonpupuh/{id_post}")
    fun getDetailAllYadnyaNotOnPupuhAdmin(@Path("id_post") id:Int) : Call<YadnyaPupuhAdminModel>

    @FormUrlEncoded
    @POST("admin/addyadnyaonpupuh/{id_post}")
    fun addDataYadnyaToPupuhAdmin (
        @Path("id_post") id:Int,
        @Field("id_pupuh") idPupuh:Int,
    ):Call<CrudModel>

    @POST("admin/deleteyadnyaonpupuh/{id_post}")
    fun deleteDataYadnyaOnPupuhAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //LaguAnak
    @GET("admin/listalllaguanakadmin")
    fun getLaguAnakMasterListAdmin() : Call<ArrayList<AllLaguAnakAdminModel>>
    @GET("admin/listkategorilaguanakadmin/{id_lagu_anak}")
    fun getKategoriLaguAnakListAdmin(@Path("id_lagu_anak") id: Int) : Call<ArrayList<KategoriLaguAnakAdminModel>>
    @FormUrlEncoded
    @POST("admin/createlaguanakadmin")
    fun createDataLaguAnakAdmin (
        @Field("nama_post") namaPost:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
        @Field("id_lagu_anak") id_lagu_anak:Int,
    ):Call<CrudModel>
    @GET("admin/detaillaguanakadmin/{id_post}")
    fun getDetailLaguAnakAdmin(@Path("id_post") id: Int) : Call<DetailLaguAnakAdminModel>
    @GET("admin/detailbaitlaguanakadmin/{id_post}")
    fun getDetailBaitLaguAnakAdmin(@Path("id_post") id:Int) : Call<DetailBaitLaguAnakAdminModel>
    @GET("admin/listvideolaguanakadmin/{id_lagu_anak}")
    fun getListVideoLaguAnakAdmin(@Path("id_lagu_anak") id:Int): Call<VideoLaguAnakAdminModel>
    @GET("admin/listaudiolaguanakadmin/{id_post}")
    fun getListAudioLaguAnakAdmin(@Path("id_post") id:Int): Call<AudioLaguAnakAdminModel>
    @GET("admin/yadnyalaguanakadmin/{id_lagu_anak}")
    fun getYadnyaLaguAnakAdmin(@Path("id_lagu_anak") id:Int): Call<YadnyaLaguAnakAdminModel>
    @GET("admin/showlaguanak/{id_post}")
    fun getShowLaguAnakAdmin(@Path("id_post") id:Int) : Call<DetailLaguAnakAdminModel>
    @FormUrlEncoded
    @POST("admin/editlaguanakadmin/{id_post}")
    fun updateLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletelaguanakadmin/{id_post}")
    fun deleteLaguAnakAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listbaitlaguanakadmin/{id_post}")
    fun getAllLirikLaguAnakAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllLirikLaguAnakAdminModel>>

    @FormUrlEncoded
    @POST("admin/addliriklaguanakadmin/{id_post}")
    fun createDataLirikLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("bait_lagu") baitLagu:String,
    ):Call<CrudModel>

    @GET("admin/showliriklaguanakadmin/{id_det_post}")
    fun getShowLirikLaguAnakAdmin(@Path("id_det_post") id:Int) : Call<DetailLirikLaguAnakAdminModel>

    @FormUrlEncoded
    @POST("admin/editliriklaguanakadmin/{id_det_post}")
    fun updateDataLirikLaguAnakAdmin (
        @Path("id_det_post") id:Int,
        @Field("bait_lagu") baitLagu:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deleteliriklaguanakadmin/{id_post}")
    fun deleteDataLirikLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("lagu_anak_id") idLaguAnak:Int,
    ):Call<CrudModel>

    @GET("admin/showvideolaguanakadmin/{id_post}")
    fun getShowVideoLaguAnakAdmin(@Path("id_post") id:Int) : Call<DetailVideoLaguAnakAdminModel>

    @FormUrlEncoded
    @POST("admin/addvideoonlaguanakadmin/{id_post}")
    fun createDataVideoLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("judul_video") judulVideo:String,
        @Field("gambar_video") gambarVideo:String,
        @Field("video") Video:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editvideolaguanakadmin/{id_post}")
    fun updateDataVideoLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("judul_video") judulVideo:String,
        @Field("gambar_video") gambarVideo:String,
        @Field("video") Video:String,
    ):Call<CrudModel>

    @POST("admin/deletevideoonlaguanakadmin/{id_post}")
    fun deleteDataVideoLaguAnakAdmin (
        @Path("id_post") id:Int,
    ):Call<CrudModel>

    @GET("admin/showaudiolaguanakadmin/{id_post}")
    fun getShowAudioLaguAnakAdmin(@Path("id_post") id:Int) : Call<DetailAudioLaguAnakAdminModel>

    @FormUrlEncoded
    @POST("admin/addaudioonlaguanakadmin/{id_post}")
    fun createDataAudioLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("judul_audio") judulAudio:String,
        @Field("gambar_audio") gambarAudio:String,
        @Field("audio") Audio:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editaudiolaguanakadmin/{id_post}")
    fun updateDataAudioLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("judul_audio") judulAudio:String,
        @Field("gambar_audio") gambarAudio:String,
        @Field("audio") Audio:String,
    ):Call<CrudModel>

    @POST("admin/deleteaudioonlaguanakadmin/{id_post}")
    fun deleteDataAudioLaguAnakAdmin (
        @Path("id_post") id:Int,
    ):Call<CrudModel>

    @GET("admin/listyadnyanotonlaguanak/{id_post}")
    fun getDetailAllYadnyaNotOnLaguAnakAdmin(@Path("id_post") id:Int) : Call<YadnyaLaguAnakAdminModel>

    @FormUrlEncoded
    @POST("admin/addyadnyaonlaguanak/{id_post}")
    fun addDataYadnyaToLaguAnakAdmin (
        @Path("id_post") id:Int,
        @Field("id_sekar_agung") idSekarAgung:Int,
    ):Call<CrudModel>

    @POST("admin/deleteyadnyaonlaguanak/{id_post}")
    fun deleteDataYadnyaOnLaguAnakAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>


    //User
    //Yadnya
    @GET("listyadnya")
    fun getYadnyaMasterList(): Call<List<HomeModel>>
    @GET("yadnya/{nama_yadnya}")
    fun getYadnyaCardClickedList(@Path("nama_yadnya") nama_yadnya: String) : Call<ArrayList<YadnyaCardClickedModel>>
    @GET("listyadnyaterbaru")
    fun getYadnyaNewList(): Call<NewYadnyaModel>
    @GET("listkidungterbaru")
    fun getKidungNewList(): Call<NewKidungModel>
    @GET("listmantramterbaru")
    fun getMantramNewList(): Call<NewMantramModel>
    @GET("listallyadnya")
    fun getYadnyaAllList(): Call<ArrayList<AllYadnyaModel>>
    @GET("detailyadnya/{id_post}")
    fun getDetailYadnya(@Path("id_post") id: Int) : Call<DetailYadnyaModel>
    @GET("detailawal/{id_post}")
    fun getDetailAwal(@Path("id_post") id: Int) : Call<ProsesiAwalModel>
    @GET("detailpuncak/{id_post}")
    fun getDetailPuncak(@Path("id_post") id: Int) : Call<ProsesiPuncakModel>
    @GET("detailakhir/{id_post}")
    fun getDetailAkhir(@Path("id_post") id: Int) : Call<ProsesiAkhirModel>
    @GET("detailgamelanyadnya/{id_post}")
    fun getDetailGamelanYadnya(@Path("id_post") id: Int) : Call<GamelanYadnyaModel>
    @GET("detailtariyadnya/{id_post}")
    fun getDetailTariYadnya(@Path("id_post") id: Int) : Call<TariYadnyaModel>
    @GET("detailkidungyadnya/{id_post}")
    fun getDetailKidungYadnya(@Path("id_post") id: Int) : Call<KidungYadnyaModel>

    //Kidung
    @GET("listallkidung")
    fun getKidungMasterList() : Call<ArrayList<AllKidungModel>>
    @GET("detailkidung/{id_post}")
    fun getDetailKidung(@Path("id_post") id: Int) : Call<DetailKidungModel>
    @GET("detailbaitkidung/{id_post}")
    fun getDetailBaitKidung(@Path("id_post") id:Int) : Call<DetailBaitKidungModel>
    @GET("listvideokidung/{id_kidung}")
    fun getListVideoKidung(@Path("id_kidung") id:Int): Call<VideoKidungModel>
    @GET("listaudiokidung/{id_post}")
    fun getListAudioKidung(@Path("id_post") id:Int): Call<AudioKidungModel>
    @GET("yadnyakidung/{id_kidung}")
    fun getYadnyaKidung(@Path("id_kidung") id:Int): Call<YadnyaKidungModel>

    //Mantram
    @GET("listallmantram")
    fun getMantramMasterList() : Call<ArrayList<AllMantramModel>>
    @GET("detailmantram/{id_post}")
    fun getDetailMantram(@Path("id_post") id:Int) : Call<DetailMantramModel>

    //Tari
    @GET("listalltari")
    fun getTariMasterList() : Call<ArrayList<AllTariModel>>
    @GET("detailtari/{id_post}")
    fun getDetailTari(@Path("id_post") id:Int) : Call<DetailTariModel>
    @GET("detailtabuhtari/{id_post}")
    fun getDetailTabuhTari(@Path("id_post") id:Int) : Call<DetailTabuhTariModel>

    //Tabuh
    @GET("listalltabuh")
    fun getTabuhMasterList() : Call<ArrayList<AllTabuhModel>>
    @GET("detailtabuh/{id_post}")
    fun getDetailTabuh(@Path("id_post") id:Int) : Call<DetailTabuhModel>

    //Gamelan
    @GET("listallgamelan")
    fun getGamelanMasterList() : Call<ArrayList<AllGamelanModel>>
    @GET("detailgamelan/{id_post}")
    fun getDetailGamelan(@Path("id_post") id:Int) : Call<DetailGamelanModel>
    @GET("detailtabuhgamelan/{id_post}")
    fun getDetailTabuhGamelan(@Path("id_post") id:Int) : Call<DetailTabuhGamelanModel>

    //Prosesi
    @GET("listallprosesi")
    fun getProsesiMasterList() : Call<ArrayList<AllProsesiModel>>
    @GET("detailprosesi/{id_post}")
    fun getDetailProsesi(@Path("id_post") id:Int) : Call<DetailProsesiModel>
    @GET("detailgamelanprosesi/{id_post}")
    fun getDetailGamelanProsesi(@Path("id_post") id: Int) : Call<GamelanProsesiModel>
    @GET("detailtariprosesi/{id_post}")
    fun getDetailTariProsesi(@Path("id_post") id: Int) : Call<TariProsesiModel>
    @GET("detailkidungprosesi/{id_post}")
    fun getDetailKidungProsesi(@Path("id_post") id: Int) : Call<KidungProsesiModel>
    @GET("detailtabuhprosesi/{id_post}")
    fun getDetailTabuhProsesi(@Path("id_post") id: Int) : Call<TabuhProsesiModel>
    @GET("detailmantramprosesi/{id_post}")
    fun getDetailMantramProsesi(@Path("id_post") id: Int) : Call<MantramProsesiModel>

    @GET("prosesicr/{id_prosesi}/{id_yadnya}")
    fun getDetailProsesiKhusus(
        @Path("id_prosesi") idProsesi: Int,
        @Path("id_yadnya") idYadnya: Int,
    ) : Call<ProsesiKhususModel>

    //Dharmagita
    @GET("listalldharmagita")
    fun getDharmagitaMasterList(): Call<List<HomeModel>>
    @GET("listdharmagitaterbaru")
    fun getDharmagitaNewList(): Call<NewDharmagitaModel>
    @GET("listallgita")
    fun getGitaAllList(): Call<ArrayList<AllDharmagitaModel>>

    //Pupuh
    @GET("listallpupuh")
    fun getPupuhMasterList() : Call<ArrayList<AllPupuhModel>>
    @GET("listpupuhterbaru")
    fun getPupuhNewList(): Call<NewPupuhModel>
    @GET("listkategoripupuh/{id_pupuh}")
    fun getKategoriPupuh(@Path("id_pupuh") id: Int) : Call<ArrayList<KategoriPupuhModel>>
    @GET("listkategoripupuhuser/{id_pupuh}/{id_user}")
    fun getKategoriPupuhUser(@Path("id_pupuh") id: Int, @Path("id_user") id_user : Int) : Call<ArrayList<KategoriPupuhUserModel>>
    @GET("detailpupuh/{id_post}")
    fun getDetailPupuh(@Path("id_post") id: Int) : Call<DetailPupuhModel>
    @GET("detailbaitpupuh/{id_post}")
    fun getDetailBaitPupuh(@Path("id_post") id:Int) : Call<DetailBaitPupuhModel>
    @GET("listvideopupuh/{id_pupuh}")
    fun getListVideoPupuh(@Path("id_pupuh") id:Int): Call<VideoPupuhModel>
    @GET("listaudiopupuh/{id_post}")
    fun getListAudioPupuh(@Path("id_post") id:Int): Call<AudioPupuhModel>
    @GET("yadnyapupuh/{id_pupuh}")
    fun getYadnyaPupuh(@Path("id_pupuh") id:Int): Call<YadnyaPupuhModel>
    @FormUrlEncoded
    @POST("createpupuh")
    fun createDataPupuh (
        @Field("nama_post") namaPost:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
        @Field("id_pupuh") id_pupuh:Int,
    ):Call<CrudModel>
    @GET("showpupuh/{id_post}")
    fun getShowPupuh(@Path("id_post") id:Int) : Call<DetailPupuhModel>
    @FormUrlEncoded
    @POST("editpupuh/{id_post}")
    fun updatePupuh (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("deletepupuh/{id_post}")
    fun deletePupuh (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("listbaitpupuh/{id_post}")
    fun getAllLirikPupuh(@Path("id_post") id:Int) : Call<ArrayList<AllLirikPupuhModel>>

    @FormUrlEncoded
    @POST("addlirikpupuh/{id_post}")
    fun createDataLirikPupuh (
        @Path("id_post") id:Int,
        @Field("bait_pupuh") baitPupuh:String,
    ):Call<CrudModel>

    @GET("showlirikpupuh/{id_det_post}")
    fun getShowLirikPupuh(@Path("id_det_post") id:Int) : Call<DetailLirikPupuhModel>

    @FormUrlEncoded
    @POST("editlirikpupuh/{id_det_post}")
    fun updateDataLirikPupuh (
        @Path("id_det_post") id:Int,
        @Field("bait_pupuh") lirikPupuh:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("deletelirikpupuh/{id_post}")
    fun deleteDataLirikPupuh(
        @Path("id_post") id:Int,
        @Field("pupuh_id") idPupuh:Int,
    ):Call<CrudModel>

    @GET("showvideopupuh/{id_post}")
    fun getShowVideoPupuh(@Path("id_post") id:Int) : Call<DetailVideoPupuhModel>

    @FormUrlEncoded
    @POST("addvideoonpupuh/{id_post}")
    fun createDataVideoPupuh (
        @Path("id_post") id:Int,
        @Field("judul_video") judulVideo:String,
        @Field("gambar_video") gambarVideo:String,
        @Field("video") Video:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("editvideopupuh/{id_post}")
    fun updateDataVideoPupuh (
        @Path("id_post") id:Int,
        @Field("judul_video") judulVideo:String,
        @Field("gambar_video") gambarVideo:String,
        @Field("video") Video:String,
    ):Call<CrudModel>

    @POST("deletevideoonpupuh/{id_post}")
    fun deleteDataVideoPupuh (
        @Path("id_post") id:Int,
    ):Call<CrudModel>

    @GET("showaudiopupuh/{id_post}")
    fun getShowAudioPupuh(@Path("id_post") id:Int) : Call<DetailAudioPupuhModel>

    @FormUrlEncoded
    @POST("addaudioonpupuh/{id_post}")
    fun createDataAudioPupuh (
        @Path("id_post") id:Int,
        @Field("judul_audio") judulAudio:String,
        @Field("gambar_audio") gambarAudio:String,
        @Field("audio") Audio:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("editaudiopupuh/{id_post}")
    fun updateDataAudioPupuh (
        @Path("id_post") id:Int,
        @Field("judul_audio") judulAudio:String,
        @Field("gambar_audio") gambarAudio:String,
        @Field("audio") Audio:String,
    ):Call<CrudModel>

    @POST("deleteaudioonpupuh/{id_post}")
    fun deleteDataAudioPupuh (
        @Path("id_post") id:Int,
    ):Call<CrudModel>

    @GET("listyadnyanotonpupuh/{id_post}")
    fun getDetailAllYadnyaNotOnPupuh(@Path("id_post") id:Int) : Call<YadnyaPupuhModel>

    @FormUrlEncoded
    @POST("addyadnyaonpupuh/{id_post}")
    fun addDataYadnyaToPupuh (
        @Path("id_post") id:Int,
        @Field("id_pupuh") idPupuh:Int,
    ):Call<CrudModel>

    @POST("deleteyadnyaonpupuh/{id_post}")
    fun deleteDataYadnyaOnPupuh (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Lagu Anak
    @GET("listalllaguanak")
    fun getLaguAnakMasterList() : Call<ArrayList<AllLaguAnakModel>>
    @GET("listkategorilaguanak/{id_lagu_anak}")
    fun getKategoriLaguAnak(@Path("id_lagu_anak") id: Int) : Call<ArrayList<KategoriLaguAnakModel>>
    @GET("detaillaguanak/{id_post}")
    fun getDetailLaguAnak(@Path("id_post") id: Int) : Call<DetailLaguAnakModel>
    @GET("detailbaitlaguanak/{id_post}")
    fun getDetailBaitLaguAnak(@Path("id_post") id:Int) : Call<DetailBaitLaguAnakModel>
    @GET("listvideolaguanak/{id_lagu_anak}")
    fun getListVideoLaguAnak(@Path("id_lagu_anak") id:Int): Call<VideoLaguAnakModel>
    @GET("listaudiolaguanak/{id_post}")
    fun getListAudioLaguAnak(@Path("id_post") id:Int): Call<AudioLaguAnakModel>
    @GET("yadnyalaguanak/{id_lagu_anak}")
    fun getYadnyaLaguAnak(@Path("id_lagu_anak") id:Int): Call<YadnyaLaguAnakModel>

    //Kakawin
    @GET("listallkakawin")
    fun getKakawinMasterList() : Call<ArrayList<AllKakawinModel>>
    @GET("listkategorikakawin/{id_kakawin}")
    fun getKategoriKakawin(@Path("id_kakawin") id: Int) : Call<ArrayList<KategoriKakawinModel>>
    @GET("detailkakawin/{id_post}")
    fun getDetailKakawin(@Path("id_post") id: Int) : Call<DetailKakawinModel>
    @GET("detailbaitkakawin/{id_post}")
    fun getDetailBaitKakawin(@Path("id_post") id:Int) : Call<DetailBaitKakawinModel>
    @GET("listvideokakawin/{id_kakawin}")
    fun getListVideoKakawin(@Path("id_kakawin") id:Int): Call<VideoKakawinModel>
    @GET("listaudiokakawin/{id_post}")
    fun getListAudioKakawin(@Path("id_post") id:Int): Call<AudioKakawinModel>
    @GET("yadnyakakawin/{id_kakawin}")
    fun getYadnyaKakawin(@Path("id_kakawin") id:Int): Call<YadnyaKakawinModel>
}