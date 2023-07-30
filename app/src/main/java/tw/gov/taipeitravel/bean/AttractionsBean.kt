package tw.gov.taipeitravel.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttractionsBean(
    @Expose
    @SerializedName("data")
    var data: List<Data>,
    var total: Int
) :BaseBean() , Serializable {
    data class Data(
        var address: String,
        @Expose
        @SerializedName("category")
        var category: List<Category>,
        var distric: String,
        var elong: Double,
        var email: String,
        var facebook: String,
        var fax: String,
        @Expose
        @SerializedName("files")
        var files: List<File>,
        @Expose
        @SerializedName("friendly")
        var friendly: List<Friendly>,
        var id: Int,
        @Expose
        @SerializedName("images")
        var images: List<Image>,
        var introduction: String,
        @Expose
        @SerializedName("links")
        var links: List<Link>,
        var modified: String,
        var months: String,
        var name: String,
        var name_zh: String,
        var nlat: Double,
        var official_site: String,
        var open_status: Int,
        var open_time: String,
        var remind: String,
        @Expose
        @SerializedName("service")
        var service: List<Service>,
        var staytime: String,
        @Expose
        @SerializedName("target")
        var target: List<Target>,
        var tel: String,
        var ticket: String,
        var url: String,
        var zipcode: String
    ) :BaseBean() , Serializable{
        data class Category(
            var id: Int,
            var name: String
        )

        data class File(
            var ext: String,
            var src: String,
            var subject: String
        )

        data class Friendly(
            var id: Int,
            var name: String
        )

        data class Image(
            var ext: String,
            var src: String,
            var subject: String
        )

        data class Link(
            var src: String,
            var subject: String
        )

        data class Service(
            var id: Int,
            var name: String
        )

        data class Target(
            var id: Int,
            var name: String
        )
    }
}