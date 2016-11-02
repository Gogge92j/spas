package joakimiversen.week;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class StateSelector extends DialogFragment {
    public interface StateListener {
        void onDialogStateClick(int _id, String _task, String _state);
    }

    StateListener mListener;

    // Possible states
    private static final String IN_PROGRESS = "In progress";
    private static final String DONE = "Done";
    private static final String PENDING = "Pending";

    int id;
    String task;
    String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        task = getArguments().getString("task");
        state = getArguments().getString("state");
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (StateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int defaultIndex = -1;

        if (state.equals(IN_PROGRESS)) {
            defaultIndex = 0;
        } else if (state.equals(DONE)) {
            defaultIndex = 1;
        } else {
            defaultIndex = 2;
        }

        final ArrayList<String> state = new ArrayList<>();

        builder.setTitle("Change state")
                .setSingleChoiceItems(R.array.state_choices, defaultIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        state.clear();
                        if (i == 0) {
                            state.add(IN_PROGRESS);
                        } else if (i == 1) {
                            state.add(DONE);
                        } else {
                            state.add(PENDING);
                        }
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogStateClick(id, task, state.get(0));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StateSelector.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
