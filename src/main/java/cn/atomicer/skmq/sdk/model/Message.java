package cn.atomicer.skmq.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * The default message format for SKMQ
 *
 * @author Rao-Mengnan
 *         on 2018/1/29.
 */
public class Message {
    @SerializedName("msgid")
    private String msgId;
    @SerializedName("appid")
    private String appId;
    private String type;
    private byte[] content;

    public Message() {
    }

    public Message(String type) {
        this.type = type;
    }

    public Message(String msgId, String appId, String type, byte[] content) {
        this.msgId = msgId;
        this.appId = appId;
        this.type = type;
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId='" + msgId + '\'' +
                ", appId='" + appId + '\'' +
                ", type='" + type + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (msgId != null ? !msgId.equals(message.msgId) : message.msgId != null) return false;
        if (appId != null ? !appId.equals(message.appId) : message.appId != null) return false;
        if (type != null ? !type.equals(message.type) : message.type != null) return false;
        return Arrays.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        int result = msgId != null ? msgId.hashCode() : 0;
        result = 31 * result + (appId != null ? appId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
