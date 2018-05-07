package appceiot.ceiot.com.br.appceiot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by leona on 11/03/2018.
 */

/**
 * TODO (5) RecyclerView.Adapter
 * Criação da classe Adapter, que deve extender RecyclerView.Adapter<NodeAdapter.ViewHolder>
 */
public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {
    private List<Node> mDataset;

    /**
     * TODO (10) ListItemClickListener
     * Definição de um handler para interfacear o RecyclerView com a Activity
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    /**
     * Construtora do NodeAdapter
     *
     * TODO (11) Adicionar como parâmetro da contrutora o ClickListener.
     * @param nodeList
     * @param listener
     */
    public NodeAdapter(List<Node> nodeList, ListItemClickListener listener) {
        mDataset = nodeList;
        mOnClickListener = listener;
    }

    /**
     * Este método é chamado pelo RecyclerView para exibir os dados na posição especificada.
     * O conteúdo do ViewHolder é atualizado para exibir os índices corretos na lista
     * de acordo com o parâmetro position.
     * @param holder O View Holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position));
    }

    /**
     * Retorna o tamanho da lista a ser apresentada.
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Create new views (invoked by the layout manager)

    /**
     * Este método é chamado quando cada ViewHolder é criada. Ou seja, quando o RecyclerView também
     * for criado. Será criada a quantidade de ViewHolders suficiente para preencher a tela.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public NodeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_node;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        // Cria uma nova view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * TODO (7) ViewHolder
     * Apresenta uma refeência para a exibição para os itens no RecyclerView.
     * Fonte: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html
     * TODO (13) Implementar OnClickListener
     * Interface para tratar o evento de click
     */
    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //Declaração dos elementos  em tela a serem apresentados para cada item.
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_node_nome);
            itemView.setOnClickListener(this);
        }

        /**
         * Criação de um método para único para preencher o ViewHolder
         * @param node
         */
        void bind(Node node) {
            this.mTextView.setText(node.getNome());
        }

        /**
         * TODO (14) onCLick
         * Implementação do método que é chamado ao quando um item for clicado.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    /**
     * Método para reiniciar a lista
     */
    public void resetList(List<Node> listNodes)
    {
        final int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);

        for (Node node:listNodes) {
            adicionarItem(node);
        }
    }

    /**
     * Método para adicionar um novo item.
     * @param node
     */
    private void adicionarItem(Node node) {
        mDataset.add(node);
        notifyItemInserted(getItemCount());
    }

    /**
     * Método para remover um item de uma determinada posição.
     * @param position
     */
    private void removerItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }

    /**
     * Método que recupera item de uma determinada posição
     * @param position
     * @return
     */
    private Node getItem(int position) {
        return mDataset.get(position);
    }

    /**
     * Método que atualiza item a partir de uma determinada posição.
     * @param position
     * @param node
     */
    private void atualizarItem(int position,Node node) {
        mDataset.set(position, node);
        notifyItemChanged(position);
    }


    /**
     * TODO (12) Criação da interface;
     * Interface que receberá o evento onClick.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

}
