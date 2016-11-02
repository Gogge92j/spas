package joakimiversen.week;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> mDataSet;
    private Context mContext;
    private DatabaseClass databaseClass;

    // Possible states
    private static final String IN_PROGRESS = "In progress";
    private static final String DONE = "Done";
    private static final String PENDING = "Pending";

    private static final String CARD_CLICK = "Card click";
    private static final String BUTTON_CLICK = "Button click";
    private static final String DELETE_CLICK = "Delete click";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        public TextView taskTask;
        public TextView taskNote;
        public TextView taskProgress;
        public ImageButton activityDone;
        public ImageButton activityDelete;
        public int position;

        public ViewHolderClicks mListener;

        public ViewHolder(View v, ViewHolderClicks listener) {
            super(v);
            mListener = listener;
            cardView = (CardView) v.findViewById(R.id.cv);
            taskTask = (TextView) v.findViewById(R.id.task_task);
            taskNote = (TextView) v.findViewById(R.id.task_note);
            //taskProgress = (TextView) v.findViewById(R.id.task_progress);
            v.setOnClickListener(this);

            activityDone = (ImageButton) v.findViewById(R.id.task_change_state);
            activityDone.setOnClickListener(this);
            activityDelete = (ImageButton) v.findViewById(R.id.task_delete);
            activityDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == activityDone.getId()) {
                mListener.onClick(view, position, BUTTON_CLICK);
            } else if (view.getId() == activityDelete.getId()) {
                mListener.onClick(view, position, DELETE_CLICK);
            } else {
                mListener.onClick(view, position, CARD_CLICK);
            }
        }

        public interface ViewHolderClicks {
            void onClick(View view, int position, String state);
        }
    }

    public TaskAdapter(Context context, ArrayList<Task> dataSet) {
        mContext = context;
        mDataSet = dataSet;

        databaseClass = new DatabaseClass(mContext);
        try {
            databaseClass.open();
        } catch (SQLException e) {
            System.out.println("SQLException!");
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        final ViewHolder vh = new ViewHolder(v, new TaskAdapter.ViewHolder.ViewHolderClicks() {
            public void onClick(View view, int position, String state) {
                if (state.equals(CARD_CLICK)) {
                    Task task = mDataSet.get(position);

                    Bundle args = new Bundle();
                    args.putInt("id", task.id);
                    args.putString("task", task.task);
                    args.putString("note", task.note);
                    args.putString("progress", task.progress);

                    EditTask editTask = new EditTask();
                    editTask.setArguments(args);

                    FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    editTask.show(fragmentManager, "edit");
                } else if (state.equals(BUTTON_CLICK)) {
                    Task task = mDataSet.get(position);

                    Bundle args = new Bundle();
                    args.putInt("id", task.id);
                    args.putString("task", task.task);
                    args.putString("state", task.state);

                    StateSelector stateSelector = new StateSelector();
                    stateSelector.setArguments(args);

                    FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    stateSelector.show(fragmentManager, "state");
                } else if (state.equals(DELETE_CLICK)) {
                    Task task = mDataSet.get(position);

                    Bundle args = new Bundle();
                    args.putInt("id", task.id);
                    args.putString("task", task.task);

                    DeleteTask deleteTask = new DeleteTask();
                    deleteTask.setArguments(args);

                    FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    deleteTask.show(fragmentManager, "delete");
                }
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.taskTask.setText(mDataSet.get(position).task);
        holder.taskNote.setText(mDataSet.get(position).note);
        //holder.taskProgress.setText(mDataSet.get(position).progress);
        holder.position = position;
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void remove(int position) {
        databaseClass.deleteTask(mDataSet.get(position));

        Toast.makeText(mContext, "\"" + mDataSet.get(position).task + "\"" + " removed", Toast.LENGTH_SHORT).show();

        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void swipeBasedOnState(int position) {

        if (mDataSet.get(position).state.equals(IN_PROGRESS)) {
            databaseClass.changeTaskState(mDataSet.get(position), DONE);
            Toast.makeText(mContext, "\"" + mDataSet.get(position).task + "\"" + " moved to \"" + DONE + "\"", Toast.LENGTH_SHORT).show();
        } else if (mDataSet.get(position).state.equals(PENDING)) {
            databaseClass.changeTaskState(mDataSet.get(position), IN_PROGRESS);
            Toast.makeText(mContext, "\"" + mDataSet.get(position).task + "\"" + " moved to \"" + IN_PROGRESS + "\"", Toast.LENGTH_SHORT).show();
        } else if (mDataSet.get(position).state.equals(DONE)) {
            databaseClass.deleteTask(mDataSet.get(position));
            Toast.makeText(mContext, "\"" + mDataSet.get(position).task + "\"" + " removed", Toast.LENGTH_SHORT).show();
        }

        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void swap(int firstPosition, int secondPosition) {
        Collections.swap(mDataSet, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);

        int position = 0;
        for (Task task: mDataSet) {
            databaseClass.updatePriority(task.id, position);
            position++;
        }
    }
}
