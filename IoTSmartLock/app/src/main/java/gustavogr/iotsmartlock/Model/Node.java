package gustavogr.iotsmartlock.Model;

public class Node {
    public static final String NODE_ID = "iotsmartlock.node.id";
    public static final String NODE_USERID = "iotsmartlock.node.userid";
    public static final String NODE_NOME = "iotsmartlock.node.nome";
    public static final String NODE_DESCRICAO = "iotsmartlock.node.descricao";
    public static final String NODE_MQTTID = "iotsmartlock.node.mqttid";
    public static final String NODE_LOCKSTATUS = "iotsmartlock.node.lockstatus";
    public static final String NODE_ALARMSTATUS = "iotsmartlock.node.alarmstatus";
    public static final String NODE_INSTALLATIONSTATUS = "iotsmartlock.node.installationstatus";
    public static final String NODE_DTATUALIZACAO = "iotsmartlock.node.dtatualizacao";

    private String id;
    private String userid;
    private String nome;
    private String descricao;
    private String mqttid;
    private String lockstatus;
    private String alarmstatus;
    private String installationstatus;
    private String dtatualizacao;

    public Node(String id, String userid, String nome, String descricao, String mqttid, String lockstatus, String alarmstatus, String installationstatus, String dtatualizacao) {
        this.id = id;
        this.userid = userid;
        this.nome = nome;
        this.descricao = descricao;
        this.mqttid = mqttid;
        this.lockstatus = lockstatus;
        this.alarmstatus = alarmstatus;
        this.installationstatus = installationstatus;
        this.dtatualizacao = dtatualizacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getMqttid() {
        return mqttid;
    }

    public void setMqttid(String mqttid) {
        this.mqttid = mqttid;
    }

    public String getLockstatus() {
        return lockstatus;
    }

    public void setLockstatus(String lockstatus) {
        this.lockstatus = lockstatus;
    }

    public String getAlarmstatus() {
        return alarmstatus;
    }

    public void setAlarmstatus(String alarmstatus) { this.alarmstatus = alarmstatus; }

    public String getInstallationstatus() {
        return installationstatus;
    }

    public void setInstallationstatus(String installationstatus) { this.installationstatus = installationstatus; }

    public String getDtatualizacao() {
        return dtatualizacao;
    }

    public void setDtatualizacao(String dtatualizacao) {
        this.dtatualizacao = dtatualizacao;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", mqttid='" + mqttid + '\'' +
                ", lockstatus='" + lockstatus + '\'' +
                ", alarmstatus='" + alarmstatus + '\'' +
                ", installationstatus='" + installationstatus + '\'' +
                ", dtatualizacao='" + dtatualizacao + '\'' +
                '}';
    }
}
