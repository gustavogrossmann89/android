package gustavogr.iotdevicecontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

public class NodeInfoFragment extends Fragment {

    TextView nomeTextView;
    TextView topicoTextView;
    TextView descricaoTextView;
    Switch switchRadioBtn;

    String nome;
    String descricao;
    String topicomqtt;
    String status;

    public NodeInfoFragment() {}

    public static NodeInfoFragment newInstance(String nome, String descricao, String topicomqtt, String status) {
        NodeInfoFragment fragment = new NodeInfoFragment();
        Bundle args = new Bundle();
        args.putString(Node.NODE_NOME, nome);
        args.putString(Node.NODE_DESCRICAO, descricao);
        args.putString(Node.NODE_TOPICOMQTT, topicomqtt);
        args.putString(Node.NODE_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nome = getArguments().getString(Node.NODE_NOME);
            descricao = getArguments().getString(Node.NODE_DESCRICAO);
            topicomqtt = getArguments().getString(Node.NODE_TOPICOMQTT);
            status = getArguments().getString(Node.NODE_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node_info, container, false);
        updateValues(view,nome,descricao,topicomqtt, status);
        return view;
    }

    public void updateValues(View view, String nome, String descricao, String topicomqtt, String status)
    {
        nomeTextView = (TextView) view.findViewById(R.id.node_nome);
        nomeTextView.setText("Nome: " + nome);
        topicoTextView = (TextView) view.findViewById(R.id.node_topicomqtt);
        topicoTextView.setText("Tópico MQTT: " + topicomqtt);
        descricaoTextView = (TextView) view.findViewById(R.id.node_descricao);
        descricaoTextView.setText("Descrição: " + descricao);
        switchRadioBtn = (Switch) view.findViewById(R.id.node_switchBtn);
        if(status.equals("0")){
            switchRadioBtn.setChecked(false);
        } else {
            switchRadioBtn.setChecked(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}