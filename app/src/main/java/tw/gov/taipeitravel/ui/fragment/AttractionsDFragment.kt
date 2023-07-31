package tw.gov.taipeitravel.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import com.bumptech.glide.Glide
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.databinding.FragmentAttractionsDBinding
import tw.gov.taipeitravel.framework.JFragment
import tw.gov.taipeitravel.viewmodel.AttractionDViewModel
import tw.gov.taipeitravel.viewmodel.ViewModelFactory


class AttractionsDFragment : JFragment(), View.OnClickListener {

    private var _binding: FragmentAttractionsDBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttractionDViewModel by viewModels{
        ViewModelFactory(
            mCtx, SavedStateHandle(
                mapOf(
                    "data" to arguments?.getSerializable("data")
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAttractionsDBinding.inflate(inflater, container, false)
        val view = binding.root
        view.isClickable = true
        binding.also {
            it.introduction.isFocusable = false
            it.introduction.isSelected = false
            it.btnBack.setOnClickListener(this)

        }

        viewModel.data.observe(viewLifecycleOwner){
            binding.name.text = it.name
            binding.introduction.setText(it.introduction)
            binding.address.text = it.address
            binding.modified.text = it.modified
            binding.officialSite.text = it.official_site
            binding.officialSite.paintFlags = binding.officialSite.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            binding.officialSite.setOnClickListener(View.OnClickListener { view->
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(it.official_site)
                startActivity(i)
            })
            if(it.images.isNotEmpty()){
                Glide.with(requireActivity()).load(it.images[0].src).into(binding.pic)
           }

        }

        return view
    }


    override fun onClick(v: View) {
       when(v.id){
           R.id.btn_back -> {
             popBackStack()
           }
       }
    }

}