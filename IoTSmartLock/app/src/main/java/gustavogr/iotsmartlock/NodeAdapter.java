package gustavogr.iotsmartlock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {
    private List<Node> mDataset;

    final private ListItemClickListener mOnClickListener;

    public NodeAdapter(List<Node> nodeList, ListItemClickListener listener) {
        mDataset = nodeList;
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
    public NodeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_node;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView nameTextView;
        public TextView installationStatusTextView;
        public TextView lockAlarmStatusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.item_node_name);
            installationStatusTextView = (TextView) itemView.findViewById(R.id.item_node_installation_status);
            lockAlarmStatusTextView = (TextView) itemView.findViewById(R.id.item_node_lock_alarm_status);
            itemView.setOnClickListener(this);
        }

        void bind(Node node) {
            this.nameTextView.setText(node.getNome());
            if(node.getInstallationstatus().equals("0")) {
                this.installationStatusTextView.setText("Status: ABERTA");
            } else{
                this.installationStatusTextView.setText("Status: FECHADA");
            }

            String lockAlarmText = "";
            if(node.getLockstatus().equals("0")) {
                lockAlarmText = "Trava: OFF";
            } else{
                lockAlarmText = "Trava: ON";
            }

            if(node.getAlarmstatus().equals("0")) {
                lockAlarmText = lockAlarmText + " / Alarme: OFF";
            } else{
                lockAlarmText = lockAlarmText + " / Alarme: ON";
            }

            this.lockAlarmStatusTextView.setText(lockAlarmText);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void resetList(List<Node> listNodes)
    {
        final int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);

        for (Node node:listNodes) {
            adicionarItem(node);
        }
    }

    private void adicionarItem(Node node) {
        mDataset.add(node);
        notifyItemInserted(getItemCount());
    }

    public Node getItem(int position) {
        return mDataset.get(position);
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
