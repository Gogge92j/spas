package joakimiversen.notitz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerDialogBox extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public interface TimeChangedListener {
        void onTimeChanged(int _id, int hour, int minute);
    }

    TimeChangedListener mListener;

    int id;
    int currentNotitzHour;
    int currentNotitzMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        currentNotitzHour = getArguments().getInt("notitzHour");
        currentNotitzMinute = getArguments().getInt("notitzMinute");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (TimeChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TimeChangedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedIntanceState) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, currentNotitzHour, currentNotitzMinute, DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        mListener.onTimeChanged(id, hourOfDay, minute);
    }
}
