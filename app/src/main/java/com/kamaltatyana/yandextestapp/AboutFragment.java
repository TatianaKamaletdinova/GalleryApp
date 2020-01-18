package com.kamaltatyana.yandextestapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class AboutFragment extends DialogFragment implements View.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        /**
         *  Разметка для диалога
         **/
        LayoutInflater li =getActivity().getLayoutInflater();
        View promptsView = li.inflate(R.layout.fragment_about, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setView(promptsView);

        /**
         * Работа с emailText: обработка нажатия, подчеркивание
         **/
        TextView emailText = promptsView.findViewById(R.id.email_send);
        emailText.setOnClickListener(this);
        emailText.setText(Html.fromHtml("<u>kamaltatyana@gmail.com</u>"));

        /**
         * Создания диалога
         **/
        mDialogBuilder
                .setTitle(R.string.about)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        return mDialogBuilder.create();
    }

    /**
     *  Открытие почтового приложения по нажатию на emailText
     **/
    @Override
    public void onClick(View view) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        Uri uri = Uri.parse("mailto:kamaltatyana@gmail.com");
        sendIntent.setData(uri);
        startActivity(sendIntent);
    }
}
