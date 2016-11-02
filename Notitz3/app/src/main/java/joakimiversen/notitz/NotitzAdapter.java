package joakimiversen.notitz;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;

import static joakimiversen.notitz.MainActivity.INTENT_EXTRA_ID;
import static joakimiversen.notitz.MainActivity.INTENT_EXTRA_TEXT;
import static joakimiversen.notitz.MainActivity.INTENT_EXTRA_TITLE;
import static joakimiversen.notitz.MainActivity.NOTITZ;

public class NotitzAdapter extends RecyclerView.Adapter<NotitzAdapter.ViewHolder> {
    public ArrayList<Notitz> mNotitzCollection;
    private Context mContext;

    private static final String CARD_CLICK = "Card click";
    private static final String SWITCH_CLICK = "Button click";
    private static final String DELETE_CLICK = "Delete click";
    private static final String REPEAT_CLICK = "Repeat click";
    private static final String TIME_CLICK = "Time click";
    private static final String MONDAY_CLICK = "Monday click";
    private static final String TUESDAY_CLICK = "Tuesday click";
    private static final String WEDNESDAY_CLICK = "Wednesday click";
    private static final String THURSDAY_CLICK = "Thursday click";
    private static final String FRIDAY_CLICK = "Friday click";
    private static final String SATURDAY_CLICK = "Saturday click";
    private static final String SUNDAY_CLICK = "Sunday click";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        public TextView notitzTitle;
        public TextView notitzText;
        public TextView notitzTime;
        public Switch notitzState;
        public ImageButton notitzDelete;
        public CheckBox notitzRepeat;
        public CheckBox notitzMonday;
        public CheckBox notitzTuesday;
        public CheckBox notitzWednesday;
        public CheckBox notitzThursday;
        public CheckBox notitzFriday;
        public CheckBox notitzSaturday;
        public CheckBox notitzSunday;

        public int position;

        public ViewHolderClicks mListener;

        public ViewHolder(View v, ViewHolderClicks listener) {
            super(v);
            mListener = listener;
            cardView = (CardView) v.findViewById(R.id.cv);
            notitzTitle = (TextView) v.findViewById(R.id.notitz_title);
            notitzText = (TextView) v.findViewById(R.id.notitz_text);
            notitzTime = (TextView) v.findViewById(R.id.notitz_time);

            v.setOnClickListener(this);

            notitzTime.setOnClickListener(this);
            notitzState = (Switch) v.findViewById(R.id.notitz_change_state);
            notitzState.setOnClickListener(this);
            notitzDelete = (ImageButton) v.findViewById(R.id.notitz_delete);
            notitzDelete.setOnClickListener(this);
            notitzRepeat = (CheckBox) v.findViewById(R.id.notitz_repeat);
            notitzRepeat.setOnClickListener(this);
            notitzMonday = (CheckBox) v.findViewById(R.id.notitz_monday);
            notitzMonday.setOnClickListener(this);
            notitzTuesday = (CheckBox) v.findViewById(R.id.notitz_tuesday);
            notitzTuesday.setOnClickListener(this);
            notitzWednesday = (CheckBox) v.findViewById(R.id.notitz_wednesday);
            notitzWednesday.setOnClickListener(this);
            notitzThursday = (CheckBox) v.findViewById(R.id.notitz_thursday);
            notitzThursday.setOnClickListener(this);
            notitzFriday = (CheckBox) v.findViewById(R.id.notitz_friday);
            notitzFriday.setOnClickListener(this);
            notitzSaturday = (CheckBox) v.findViewById(R.id.notitz_saturday);
            notitzSaturday.setOnClickListener(this);
            notitzSunday = (CheckBox) v.findViewById(R.id.notitz_sunday);
            notitzSunday.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == notitzState.getId()) {
                mListener.onClick(view, position, SWITCH_CLICK);
            } else if (view.getId() == notitzDelete.getId()) {
                mListener.onClick(view, position, DELETE_CLICK);
            } else if (view.getId() == notitzRepeat.getId()) {
                mListener.onClick(view, position, REPEAT_CLICK);
            } else if (view.getId() == notitzTime.getId()) {
                mListener.onClick(view, position, TIME_CLICK);
            } else if (view.getId() == notitzMonday.getId()) {
                mListener.onClick(view, position, MONDAY_CLICK);
            } else if (view.getId() == notitzTuesday.getId()) {
                mListener.onClick(view, position, TUESDAY_CLICK);
            } else if (view.getId() == notitzWednesday.getId()) {
                mListener.onClick(view, position, WEDNESDAY_CLICK);
            } else if (view.getId() == notitzThursday.getId()) {
                mListener.onClick(view, position, THURSDAY_CLICK);
            } else if (view.getId() == notitzFriday.getId()) {
                mListener.onClick(view, position, FRIDAY_CLICK);
            } else if (view.getId() == notitzSaturday.getId()) {
                mListener.onClick(view, position, SATURDAY_CLICK);
            } else if (view.getId() == notitzSunday.getId()) {
                mListener.onClick(view, position, SUNDAY_CLICK);
            } else {
                mListener.onClick(view, position, CARD_CLICK);
            }
        }

        public interface ViewHolderClicks {
            void onClick(View view, int position, String state);
        }
    }

    public NotitzAdapter(Context context, ArrayList<Notitz> notitzs) {
        mContext = context;
        mNotitzCollection = notitzs;

        try {
            toggleListener = (OnToggleStateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnToggleStateListener");
        }

        /*databaseClass = new DatabaseClass(mContext);
        try {
            databaseClass.open();
        } catch (SQLException e) {
            System.out.println("SQLException!");
        }*/
    }

    public interface OnToggleStateListener {
        void onToggleStateChanged(int _id, String property, boolean newState);
    }

    OnToggleStateListener toggleListener;

    @Override
    public NotitzAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notitz_view, parent, false);
        final ViewHolder vh = new ViewHolder(v, new NotitzAdapter.ViewHolder.ViewHolderClicks() {
            public void onClick(View view, int position, String state) {
                Notitz notitz = mNotitzCollection.get(position);

                if (state.equals(SWITCH_CLICK)) {
                    if (notitz.activated) {
                        mNotitzCollection.get(position).activated = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_ACTIVATED, false);
                    } else {
                        mNotitzCollection.get(position).activated = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_ACTIVATED, true);
                    }
                    scheduleAlarm(v, notitz.id, notitz.title, notitz.text, notitz.hour, notitz.minute, notitz.activated);
                } else if (state.equals(REPEAT_CLICK)) {
                    if (notitz.repeat) {
                        mNotitzCollection.get(position).repeat = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_REPEAT, false);
                    } else {
                        mNotitzCollection.get(position).repeat = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_REPEAT, true);
                    }
                } else if (state.equals(MONDAY_CLICK)) {
                    if (notitz.monday) {
                        mNotitzCollection.get(position).monday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_MONDAY, false);
                    } else {
                        mNotitzCollection.get(position).monday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_MONDAY, true);
                    }
                } else if (state.equals(TUESDAY_CLICK)) {
                    if (notitz.tuesday) {
                        mNotitzCollection.get(position).tuesday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_TUESDAY, false);
                    } else {
                        mNotitzCollection.get(position).tuesday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_TUESDAY, true);
                    }
                } else if (state.equals(WEDNESDAY_CLICK)) {
                    if (notitz.wednesday) {
                        mNotitzCollection.get(position).wednesday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_WEDNESDAY, false);
                    } else {
                        mNotitzCollection.get(position).wednesday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_WEDNESDAY, true);
                    }
                } else if (state.equals(THURSDAY_CLICK)) {
                    if (notitz.thursday) {
                        mNotitzCollection.get(position).thursday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_THURSDAY, false);
                    } else {
                        mNotitzCollection.get(position).thursday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_THURSDAY, true);
                    }
                } else if (state.equals(FRIDAY_CLICK)) {
                    if (notitz.friday) {
                        mNotitzCollection.get(position).friday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_FRIDAY, false);
                    } else {
                        mNotitzCollection.get(position).friday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_FRIDAY, true);
                    }
                } else if (state.equals(SATURDAY_CLICK)) {
                    if (notitz.saturday) {
                        mNotitzCollection.get(position).saturday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_SATURDAY, false);
                    } else {
                        mNotitzCollection.get(position).saturday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_SATURDAY, true);
                    }
                } else if (state.equals(SUNDAY_CLICK)) {
                    if (notitz.sunday) {
                        mNotitzCollection.get(position).sunday = false;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_SUNDAY, false);
                    } else {
                        mNotitzCollection.get(position).sunday = true;
                        toggleListener.onToggleStateChanged(notitz.id, DatabaseClass.NOTITZ_SUNDAY, true);
                    }
                } else if (state.equals(TIME_CLICK)) {
                    Bundle args = new Bundle();
                    args.putInt("id", notitz.id);
                    args.putInt("notitzHour", notitz.hour);
                    args.putInt("notitzMinute", notitz.minute);

                    TimePickerDialogBox timePicker = new TimePickerDialogBox();
                    timePicker.setArguments(args);

                    FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    timePicker.show(fragmentManager, "TimePicker");
                } else if (state.equals(DELETE_CLICK)) {
                    Bundle args = new Bundle();
                    args.putInt("id", notitz.id);
                    args.putString("title", notitz.title);

                    DeleteNotitzDialog deleteNotitzDialog = new DeleteNotitzDialog();
                    deleteNotitzDialog.setArguments(args);

                    FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    deleteNotitzDialog.show(fragmentManager, "Delete");
                } else if (state.equals(CARD_CLICK)) {
                    Bundle args = new Bundle();
                    args.putInt("id", notitz.id);
                    args.putString("title", notitz.title);
                    args.putString("text", notitz.text);

                    EditNotitz editNotitz = new EditNotitz();
                    editNotitz.setArguments(args);

                    FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    editNotitz.show(fragmentManager, "Edit Notitz");
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notitz notitz = mNotitzCollection.get(position);

        holder.notitzTitle.setText(notitz.title);
        holder.notitzText.setText(notitz.text);
        String t;
        if (notitz.minute < 10) {
            t = notitz.hour + ":0" + notitz.minute;
        } else {
            t = notitz.hour + ":" + notitz.minute;
        }
        holder.notitzTime.setText(t);
        holder.notitzState.setChecked(notitz.activated);
        holder.notitzRepeat.setChecked(notitz.repeat);
        holder.notitzMonday.setChecked(notitz.monday);
        holder.notitzTuesday.setChecked(notitz.tuesday);
        holder.notitzWednesday.setChecked(notitz.wednesday);
        holder.notitzThursday.setChecked(notitz.thursday);
        holder.notitzFriday.setChecked(notitz.friday);
        holder.notitzSaturday.setChecked(notitz.saturday);
        holder.notitzSunday.setChecked(notitz.sunday);

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mNotitzCollection.size();
    }

    public int getNotitzPositionById(int id) {
        int i = 0;
        for (Notitz notitz : mNotitzCollection) {
            if (notitz.id == id) {
                return i;
            }
            i++;
        }
        return 999;
    }

    public void scheduleAlarm(View v, int id, String title, String text, int hour, int minute, boolean activated) {
        // todo setup for consecutive days
        // todo cancel alarm already set
        // todo Display the time for when the alarm should call

        // did updates here. 
        if (activated) {
            Time timeNow = new Time(System.currentTimeMillis());
            Time timeLater = new Time(System.currentTimeMillis());
            timeLater.setHours(hour);
            timeLater.setMinutes(minute);
            timeLater.setSeconds(0);


            if (timeNow.getHours() < timeLater.getHours() && timeNow.getMinutes() < timeLater.getMinutes()) {
                long currentLater = timeLater.getTime();
                long timeForDay = 24 * 60 * 60 * 1000;
                timeLater.setTime(currentLater + timeForDay);
            }

            Long time = timeLater.getTime();

            Intent intentAlarm = new Intent(mContext, AlarmReciever.class);
            intentAlarm.putExtra(INTENT_EXTRA_ID, id);
            intentAlarm.putExtra(INTENT_EXTRA_TITLE, title);
            intentAlarm.putExtra(INTENT_EXTRA_TEXT, text);

            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(mContext, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

            String minuteText = minute + "";
            if (minute < 10) {
                minuteText = "0" + minute;
            }
            Snackbar.make(v, title + " scheduled for " + hour + ":" + minuteText, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTITZ, id + 41092);
        }
    }
}
