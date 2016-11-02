package joakimiversen.week;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTask extends DialogFragment {
    public interface EditListener {
        void onDialogAcceptClick(int _id, String _task, String _note);
    }

    EditListener mListener;

    int id;
    String task;
    String _progress = "";
    String _note;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        task = getArguments().getString("task");
        _note = getArguments().getString("note");
        _progress = getArguments().getString("progress");
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (EditListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_task_layout, null);
        final EditText text = (EditText) view.findViewById(R.id.editTaskText);
        text.setText(task);
        final EditText note = (EditText) view.findViewById(R.id.editTaskNote);
        note.setText(_note);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Edit task")
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
                            mListener.onDialogAcceptClick(id, text.getText().toString(), note.getText().toString());
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
