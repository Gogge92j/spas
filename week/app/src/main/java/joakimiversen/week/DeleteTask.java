package joakimiversen.week;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;

public class DeleteTask extends DialogFragment {
    public interface DeleteTaskListener {
        void onDialogDeleteClick(int id, String task);
    }

    DeleteTaskListener mListener;

    int id;
    String task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        task = getArguments().getString("task");
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (DeleteTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("Delete task")
                .setMessage("Are you sure, that you wanna delete the task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogDeleteClick(id, task);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteTask.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
