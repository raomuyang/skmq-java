package cn.atomicer.skmq.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * SKMQ recipient's properties
 *
 * @author Rao-Mengnan
 *         on 2018/1/29.
 */
public class Recipient {
    @SerializedName("id")
    private String recipientId;
    @SerializedName("app_id")
    private String applicationId;
    private String host;
    private int port;
    private int weight;

    public Recipient() {
    }

    public Recipient(String recipientId, String applicationId, String host, int port, int weight) {
        this.recipientId = recipientId;
        this.applicationId = applicationId;
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
