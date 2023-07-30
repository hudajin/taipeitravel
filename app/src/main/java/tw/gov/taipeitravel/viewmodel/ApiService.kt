package tw.gov.taipeitravel.viewmodel

import retrofit2.http.*
import tw.gov.taipeitravel.bean.AttractionsBean

interface ApiService {

    //取得最新消息
//    @GET("{lang}/Attractions/All")
//    suspend fun doGetAttraction( @Header("Accept") Accept:String,
//                                 @Path("lang") lang:String,
//                                 @Query("nlat") nlat:Double,
//                                 @Query("elong") elong:Double  )
//            : Attractions
    @GET("{lang}/Attractions/All")
    suspend fun doGetAttraction( @Header("Accept") Accept:String,
                                 @Path("lang") lang:String)
            : AttractionsBean
}