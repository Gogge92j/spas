package joakimiversen.notitz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewNotitz extends DialogFragment {
    public interface AddNewNotitzListener {
        void onAcceptNewNotitz(String _title, String _text);
    }

    AddNewNotitzListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddNewNotitzListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddNewNotitzListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_notitz_layout, null);
        final EditText title = (EditText) view.findViewById(R.id.newNotitzTitle);
        final EditText text = (EditText) view.findViewById(R.id.newNotitzText);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add new Notitz")
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
                        if (title.getText().toString().equals("")) {
                            title.setError("Notitz title cannot be empty!");
                        } else {
                            mListener.onAcceptNewNotitz(title.getText().toString(), text.getText().toString());
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
