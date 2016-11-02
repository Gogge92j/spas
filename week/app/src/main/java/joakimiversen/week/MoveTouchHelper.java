package joakimiversen.week;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class MoveTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TaskAdapter mTaskAdapter;

    public MoveTouchHelper(TaskAdapter moveAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mTaskAdapter = moveAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mTaskAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e("right ", "" + direction);
        mTaskAdapter.swipeBasedOnState(viewHolder.getAdapterPosition());
    }
}