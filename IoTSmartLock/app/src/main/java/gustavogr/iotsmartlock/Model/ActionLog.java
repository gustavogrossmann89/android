package gustavogr.iotsmartlock.Model;

public class ActionLog {
    public static final String LOG_NODE = "iotsmartlock.log.node";
    public static final String LOG_TOPIC = "iotsmartlock.log.topic";
    public static final String LOG_DATE = "iotsmartlock.log.date";
    public static final String LOG_TIME = "iotsmartlock.log.time";
    public static final String LOG_MSG = "iotsmartlock.log.msg";

    private String node;
    private String topic;
    private String date;
    private String time;
    private String msg;

    public ActionLog(String node, String topic, String date, String time, String msg) {
        this.node = node;
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.msg = msg;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ActionLog{" +
                "node='" + node + '\'' +
                ", topic='" + topic + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
