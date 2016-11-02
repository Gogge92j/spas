package joakimiversen.notitz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNotitz extends DialogFragment{
    public interface EditListener {
        void onDialogEditClick(int _id, String _title, String _text);
    }

    EditListener mListener;

    int id;
    String title;
    String text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        title = getArguments().getString("title");
        text = getArguments().getString("text");
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (EditListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_notitz_layout, null);
        final EditText titleBox = (EditText) view.findViewById(R.id.editNotitzTitle);
        titleBox.setText(title);
        final EditText textBox = (EditText) view.findViewById(R.id.editNotitzText);
        textBox.setText(text);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Edit Notitz")
                .setPositiveButton("Accept", null)
                .setNegativeButton("Reject", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (titleBox.getText().toString().equals("")) {
                            titleBox.setError("Task name cannot be empty!");
                        } else {
                            mListener.onDialogEditClick(id, titleBox.getText().toString(), textBox.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });

                Button negative = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

        return dialog;
    }
}
