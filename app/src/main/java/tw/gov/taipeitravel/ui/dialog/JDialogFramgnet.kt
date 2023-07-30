package tw.gov.taipeitravel.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import tw.gov.taipeitravel.Travel
import tw.gov.taipeitravel.R

open class JDialogFramgnet: DialogFragment() {

    lateinit var mCtx: Travel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCtx = activity?.applicationContext  as Travel
        setStyle(STYLE_NORMAL, R.style.custom_dialog_style)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    //DialogFragment dismiss重新觸發onResume()
    override fun dismiss() {
        var intent = Intent(activity,activity?.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
        super.dismiss()
    }

}