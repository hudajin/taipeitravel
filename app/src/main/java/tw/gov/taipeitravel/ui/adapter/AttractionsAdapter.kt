package tw.gov.taipeitravel.ui.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.Travel
import tw.gov.taipeitravel.bean.AttractionsBean
import tw.gov.taipeitravel.utils.Utils

class AttractionsAdapter   (private val context: Travel, private val list: List<AttractionsBean.Data>) : BaseAdapter(){

    init{
        Utils.adjustFontScale(context)
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_attractions, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        if(list[position].images.isNotEmpty()){
            Glide.with(context).load(list[position].images[0].src).into(holder.imageView)
        }else
            holder.imageView.setImageBitmap(null)


        holder.name_title.text = list[position].name
        holder.introduction.text = list[position].introduction
        holder.open_time.text = list[position].open_time

        return view
    }

    inner class ViewHolder(val view: View) {
        val imageView : ImageView = view.findViewById(R.id.image) as ImageView
        val name_title : TextView = view.findViewById(R.id.name_title) as TextView
        val introduction : TextView = view.findViewById(R.id.introduction) as TextView
        val open_time: TextView = view.findViewById(R.id.open_time) as TextView
    }
}