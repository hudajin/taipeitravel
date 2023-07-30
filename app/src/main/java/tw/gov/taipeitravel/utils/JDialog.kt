package tw.gov.taipeitravel.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.Window
import android.widget.Button
import android.widget.TextView
import tw.gov.taipeitravel.R

object JDialog {

    var loading: Dialog?= null

    fun showLoading(context: Activity, loadingMsg: String = "請稍等..."): Boolean {
        if (loading == null) {

            loading = Dialog(context)
            loading?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            loading?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            loading?.setCancelable(false)
            loading?.show()
            loading?.setContentView(R.layout.progress_dialog)
            val hit = loading?.findViewById(R.id.progressing_hit) as TextView
            hit.text = loadingMsg

            return true
        }
        return false
    }

    fun cancelLoading() : Boolean?{
        if(loading !=null && loading?.isShowing!=null){
            try{
                loading?.dismiss()
                loading =null
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        return true
    }


    fun showMessage(
        context: Context,
        title: String = "提示訊息",
        msg: String?
    ): Dialog {
        return createDialog(context, title, msg!!, null, false, null)
    }

    fun showDialog(
        context: Context,
        title: String = "提示訊息",
        msg: String,
        listener: DialogInterface.OnClickListener?
    ): Dialog {
        return createDialog(context, title, msg, listener, false, null)
    }

    fun showDialog(
        context: Context,
        title: String = "提示訊息",
        msg: String,
        confirmStr: String,
        listener: DialogInterface.OnClickListener
    ): Dialog {
        return createDialog(
            context,
            title,
            msg,
            confirmStr,
            listener
        )
    }

    fun showDialog(
        context: Context,
        title: String,
        msg: String,
        confirmAction: DialogInterface.OnClickListener?,
        cancelAction: DialogInterface.OnClickListener?
    ): Dialog {

        return createDialog(context, title, msg, confirmAction, true, cancelAction)
    }

    fun showDialog(
        context: Context,
        title: String,
        msg: String,
        confirmStr: String,
        cancelStr: String,
        confirmAction: DialogInterface.OnClickListener?,
        cancelAction: DialogInterface.OnClickListener?
    ): Dialog {

        return createDialog(
            context,
            title,
            msg,
            confirmStr,
            cancelStr,
            confirmAction,
            cancelAction
        )
    }

    fun showDialog(
        context: Context,
        title: String,
        msg: String,
        confirmStr: String,
        infoStr: String,
        cancelStr: String,
        confirmAction: DialogInterface.OnClickListener,
        infoAction: DialogInterface.OnClickListener,
        cancelAction: DialogInterface.OnClickListener
    ): Dialog {
        return createDialog(
            context,
            title,
            msg,
            confirmStr,
            infoStr,
            cancelStr,
            confirmAction,
            infoAction,
            cancelAction
        )
    }


    private fun createDialog(
        context: Context,
        title: String,
        msg: String,
        confirmAction: DialogInterface.OnClickListener?,
        showCancel: Boolean,
        cancelAction: DialogInterface.OnClickListener?
    ): Dialog {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        if(showCancel){
            builder.setPositiveButton("確認", confirmAction)
            builder.setNegativeButton("取消", cancelAction)
        }else
            builder.setPositiveButton("確認", confirmAction)
        var dialog = avoidDismiss(builder.create())
        dialog.show()
        setStyle(context, dialog)
        return dialog
    }

    private fun createDialog(
        context: Context,
        title: String,
        msg: String,
        confirmStr: String,
        cancelStr: String,
        confirmAction: DialogInterface.OnClickListener?,
        cancelAction: DialogInterface.OnClickListener?
    ): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(confirmStr, confirmAction)
        builder.setNegativeButton(cancelStr, cancelAction)
        val dialog: Dialog = avoidDismiss(builder.create())
        dialog.show()
        setStyle(context, dialog)
        return dialog
    }

    private fun createDialog(
        context: Context,
        title: String,
        msg: String,
        confirmStr: String,
        confirmAction: DialogInterface.OnClickListener,
    ): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(confirmStr, confirmAction)
        val dialog: Dialog = avoidDismiss(builder.create())
        dialog.show()
        setStyle(context, dialog)
        return dialog
    }

    private fun createDialog(
        context: Context,
        title: String,
        msg: String,
        confirmStr: String,
        infoStr: String,
        cancelStr: String,
        confirmAction: DialogInterface.OnClickListener,
        infoAction: DialogInterface.OnClickListener,
        cancelAction: DialogInterface.OnClickListener
    ): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(confirmStr, confirmAction)
        builder.setNegativeButton(cancelStr, cancelAction)
        builder.setNeutralButton(infoStr, infoAction)
        val dialog: Dialog = avoidDismiss(builder.create())
        dialog.show()
        setStyle(context, dialog)
        return dialog
    }


    fun <T : Dialog> avoidDismiss(t: T): T {
        t.setCancelable(false)
        t.setOnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_SEARCH && event.repeatCount == 0
        }
        return t
    }

    fun setStyle(context: Context, dialog: Dialog){
        try {
            val alertTitle = dialog.findViewById(
                context.resources.getIdentifier(
                    "alertTitle", "id", "android"
                )
            ) as TextView
            val msgView = dialog.findViewById(
                context.resources.getIdentifier(
                    "message", "id", "android"
                )
            ) as TextView
            var button1 =  dialog.findViewById(android.R.id.button1) as Button
            var button2 =  dialog.findViewById(android.R.id.button2) as Button
            var button3 =  dialog.findViewById(android.R.id.button3) as Button

            alertTitle.textSize = context.resources.getDimension(R.dimen.dialog_title)
            msgView.textSize = context.resources.getDimension(R.dimen.dialog_content)
            button1.textSize = context.resources.getDimension(R.dimen.dialog_content)
            button2.textSize = context.resources.getDimension(R.dimen.dialog_content)
            button3.textSize = context.resources.getDimension(R.dimen.dialog_content)
        } catch (e: Exception) {
        }
    }

}