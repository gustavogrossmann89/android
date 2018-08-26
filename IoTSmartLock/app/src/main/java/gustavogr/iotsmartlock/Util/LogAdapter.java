package gustavogr.iotsmartlock.Util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gustavogr.iotsmartlock.Model.ActionLog;
import gustavogr.iotsmartlock.R;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Adapter para listagem de Logs
 */
public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private List<ActionLog> mDataset;

    final private ListItemClickListener mOnClickListener;

    public LogAdapter(List<ActionLog> actionLogList, ListItemClickListener listener) {
        mDataset = actionLogList;
        mOnClickListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_log;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.item_description);
            itemView.setOnClickListener(this);
        }

        void bind(ActionLog log) {
            this.descriptionTextView.setText(log.getDate() + " " + log.getTime());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void resetList(List<ActionLog> actionLogList) {
        final int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);

        Collections.sort(actionLogList, new Comparator<ActionLog>() {
            @Override
            public int compare(ActionLog o1, ActionLog o2) {
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });

        for (ActionLog log: actionLogList) {
            adicionarItem(log);
        }
    }

    private void adicionarItem(ActionLog log) {
        mDataset.add(log);
        notifyItemInserted(getItemCount());
    }

    public ActionLog getItem(int position) {
        return mDataset.get(position);
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
