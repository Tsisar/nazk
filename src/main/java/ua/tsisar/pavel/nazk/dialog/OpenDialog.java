package ua.tsisar.pavel.nazk.dialog;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;

import com.github.mrengineer13.snackbar.SnackBar;

import ua.tsisar.pavel.nazk.R;


public class OpenDialog extends DialogFragment {

    private static final String LINK_PDF = "LinkPDF";
    private static final String LINK_HTML = "LinkHTML";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_open, null);

        ImageButton pdf = view.findViewById(R.id.pdf_imageButton);
        pdf.setOnClickListener((View v) -> openUri(LINK_PDF));

        ImageButton html = view.findViewById(R.id.html_imageButton);
        html.setOnClickListener((View v) -> openUri(LINK_HTML));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton(getString(R.string.cancel), null);
        return builder.create();
    }

    private void openUri(String key){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString(key)));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
            new SnackBar.Builder(getActivity())
                    .withMessage(e.getMessage())
                    .withStyle(SnackBar.Style.ALERT)
                    .show();
        }
        dismiss();
    }
}