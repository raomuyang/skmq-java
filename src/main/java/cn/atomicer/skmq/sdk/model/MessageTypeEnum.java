package cn.atomicer.skmq.sdk.model;

/**
 * Created by Rao-Mengnan
 * on 2018/1/29.
 */
public enum  MessageTypeEnum {
    RECIPIENT_REGISTER("register", "注册类型消息"),
    PUSH("push", ""),
    PING("ping", ""),
    PONG("pong", ""),
    TOPIC("topic", ""),
    QUEUE("queue", ""),
    RESPONSE("response", ""),
    ACK("ack", ""),
    REJECT("reject", ""),
    ERROR("error", ""),
    MSG_ARRIVED("arrived", "");

    String name;
    String desc;

    MessageTypeEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String value() {
        return this.name;
    }

    public String desc() {
        return this.desc;
    }
}
