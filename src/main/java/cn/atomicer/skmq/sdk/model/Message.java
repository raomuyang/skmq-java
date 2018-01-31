package cn.atomicer.skmq.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Rao-Mengnan
 * on 2018/1/29.
 */
public class Message {
    @SerializedName("msgid")
    private String msgId;
    @SerializedName("appid")
    private String appId;
    private String type;
    private byte[] content;

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
}
