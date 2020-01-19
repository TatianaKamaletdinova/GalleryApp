package com.kamaltatyana.redgallery.view.about

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import com.kamaltatyana.redgallery.R

class AboutFragment : DialogFragment(), View.OnClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        /**
         * Разметка для диалога
         */
        val li = activity.layoutInflater
        val promptsView = li.inflate(R.layout.fragment_about, null)
        val mDialogBuilder = AlertDialog.Builder(activity)
        mDialogBuilder.setView(promptsView)
        /**
         * Работа с emailText: обработка нажатия, подчеркивание
         */
        val emailText = promptsView.findViewById<TextView>(R.id.email_send)
        emailText.setOnClickListener(this)
        emailText.text = Html.fromHtml("<u>kamaltatyana@gmail.com</u>")
        /**
         * Создания диалога
         */
        mDialogBuilder
                .setTitle(R.string.about)
                .setCancelable(false)
                .setPositiveButton(R.string.ok
                ) { dialog, id -> dialog.cancel() }
        return mDialogBuilder.create()
    }

    /**
     * Открытие почтового приложения по нажатию на emailText
     */
    override fun onClick(view: View) {
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        val uri = Uri.parse("mailto:kamaltatyana@gmail.com")
        sendIntent.data = uri
        startActivity(sendIntent)
    }
}