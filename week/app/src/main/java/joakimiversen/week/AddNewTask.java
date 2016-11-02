package joakimiversen.week;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewTask extends DialogFragment {
    public interface AddNewListener {
        void onDialogPositiveClick(String _task, String _note);
    }

    AddNewListener mListener;

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (AddNewListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_task_layout, null);
        final EditText text = (EditText) view.findViewById(R.id.newTaskText);
        final EditText note = (EditText) view.findViewById(R.id.newTaskNote);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add new task")
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
                        if (text.getText().toString().equals("")) {
                            text.setError("Task name cannot be empty!");
                        } else {
                            mListener.onDialogPositiveClick(text.getText().toString(), note.getText().toString());
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