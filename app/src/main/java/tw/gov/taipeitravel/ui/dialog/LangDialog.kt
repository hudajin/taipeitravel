package tw.gov.taipeitravel.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.databinding.DialogLangBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter

typealias OnLangChangedListener = (lang: String) -> Unit

class LangDialog ( private val onLangChanged:OnLangChangedListener) :
                   JDialogFramgnet() , View.OnClickListener{

    private var _binding: DialogLangBinding? = null
    private val binding get() = _binding!!

    private var selected_pos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogLangBinding.inflate(inflater, container, false)
        val view = binding.root


        var langAdapter = ArrayAdapter.createFromResource(requireActivity()
            ,R.array.lang,R.layout.spinner_lang)
        langAdapter.setDropDownViewResource(R.layout.spinner_lang)
        binding.langSpinner.apply {
            adapter = langAdapter
            isSelected = false
            setSelection(0,false)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selected_pos = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        binding.also {
            it.btnConfirm.setOnClickListener(this)
            it.btnCancel.setOnClickListener(this)
        }

        return  view
    }


    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_confirm  -> {
                dismiss()
                val lang_array: Array<String> = resources.getStringArray(R.array.lang)
                onLangChanged(lang_array[selected_pos])
            }
            R.id.btn_cancel  -> {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}