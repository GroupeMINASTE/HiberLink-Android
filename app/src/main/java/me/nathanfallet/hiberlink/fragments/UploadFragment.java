package me.nathanfallet.hiberlink.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import me.nathanfallet.hiberlink.R;
import me.nathanfallet.hiberlink.api.APIRequest;
import me.nathanfallet.hiberlink.api.APIResponseStatus;

public class UploadFragment extends Fragment implements View.OnClickListener {

    private EditText input;
    private Button generate;
    private EditText output;
    private Button copy;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        // Set views
        input = view.findViewById(R.id.upload_input);
        generate = view.findViewById(R.id.upload_generate);
        output = view.findViewById(R.id.upload_output);
        copy = view.findViewById(R.id.upload_copy);

        // Add listeners
        input.requestFocus();
        generate.setOnClickListener(this);
        output.setInputType(InputType.TYPE_NULL);
        copy.setOnClickListener(this);

        // Return the view
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == generate && !input.getText().toString().trim().isEmpty()) {
            String url = input.getText().toString().trim();

            // Disable
            generate.setEnabled(false);
            generate.setText(R.string.upload_generating);
            input.endBatchEdit();

            // Generate a link
            new APIRequest("POST", "/link.php", new APIRequest.CompletionHandler() {
                @Override
                public void completionHandler(String object, APIResponseStatus status) {
                    // Check if request was sent
                    if (object != null) {
                        // Show generated link
                        output.setText(object);

                        // Add it to database
                        // TODO: DB

                        // Notify delegate
                        // TODO: Delegates

                        // Select it
                        output.requestFocus();
                        output.selectAll();
                    } else {
                        // An error occurred
                        output.setText(R.string.upload_error);
                    }

                    // Enable it again
                    generate.setEnabled(true);
                    generate.setText(R.string.upload_generate);
                }
            }).withBody("link=" + url).execute();
        } else if (view == copy && !output.getText().toString().trim().isEmpty()) {
            // Select it
            output.requestFocus();
            output.selectAll();

            // Copy link
            ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("link", output.getText().toString());
                clipboard.setPrimaryClip(clip);

                // Show confirmation
                new AlertDialog.Builder(view.getContext()).setTitle(R.string.copied_title).setPositiveButton(R.string.copied_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        }
    }

}
