package tw.gov.taipeitravel.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import tw.gov.taipeitravel.databinding.FragmentAttractionsBinding
import tw.gov.taipeitravel.framework.JFragment
import tw.gov.taipeitravel.viewmodel.AttractionViewModel
import tw.gov.taipeitravel.viewmodel.ViewModelFactory
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.bean.AttractionsBean
import tw.gov.taipeitravel.ui.adapter.AttractionsAdapter
import tw.gov.taipeitravel.ui.dialog.LangDialog
import tw.gov.taipeitravel.ui.dialog.OnLangChangedListener
import tw.gov.taipeitravel.utils.JDialog
import tw.gov.taipeitravel.viewmodel.ApiResult
import java.io.Serializable

class AttractionsFragment : JFragment(), View.OnClickListener {

    private var _binding: FragmentAttractionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttractionViewModel by viewModels { ViewModelFactory(
        mCtx, SavedStateHandle()
    ) }

    private var current_lang = "zh-tw"
    private lateinit var langDialog:LangDialog
    private lateinit var attractionsBean: AttractionsBean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAttractionsBinding.inflate(inflater, container, false)
        val view = binding.root
        view.isClickable = true
        binding.also {
            it.btnTranslate.setOnClickListener(this)
        }

        viewModel.AttractionsBean.observe(viewLifecycleOwner){
            when (it) {
                ApiResult.Loading -> JDialog.showLoading(requireActivity())
                is ApiResult.Success -> {
                    JDialog.cancelLoading()
                    attractionsBean = it.data
                    var attractionsAdapter = AttractionsAdapter(mCtx,it.data.data)
                    binding.attractionsList.adapter = attractionsAdapter
                    attractionsAdapter.notifyDataSetChanged()
                }
                else -> {
                    JDialog.cancelLoading()
                }
            }
        }

        viewModel.doAttractionsBean(current_lang)

        binding.attractionsList?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, postion, l ->
            var attractionsDFragment = AttractionsDFragment()
            var bundle = Bundle()
            bundle.putSerializable("data",attractionsBean.data[postion] as Serializable)
            attractionsDFragment.arguments = bundle
            addFragment(R.id.frameLayout,attractionsDFragment)


        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.doAttractionsBean(current_lang)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_translate -> {
                langDialog =
                    LangDialog(onLangChangedListener).also { it.show(requireActivity().supportFragmentManager, "LangDialog") }
            }
        }
    }

    private val onLangChangedListener = object:OnLangChangedListener{
        override fun invoke(lang: String) {
            current_lang = lang
            onResume()
        }
    }
}