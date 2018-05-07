package gustavogr.iotdevicecontrol;

public class Node {
    public static final String NODE_NOME = "iotsensorcontrol.node.nome";
    public static final String NODE_DESCRICAO = "iotsensorcontrol.node.descricao";
    public static final String NODE_TOPICOMQTT = "iotsensorcontrol.node.topicomqtt";
    public static final String NODE_STATUS = "iotsensorcontrol.node.status";
    private String nome;
    private String descricao;
    private String topicomqtt;
    private String status;

    public Node(String nome, String descricao, String topicomqtt, String status) {
        this.nome = nome;
        this.descricao = descricao;
        this.topicomqtt = topicomqtt;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTopicomqtt() {
        return topicomqtt;
    }

    public void setTopicomqtt(String topicomqtt) {
        this.topicomqtt = topicomqtt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", topicomqtt='" + topicomqtt + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
