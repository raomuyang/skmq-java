package cn.atomicer.skmq.sdk.model;

/**
 * Created by Rao-Mengnan
 * on 2018/1/29.
 */
public enum  MessageParameterEnum {

    MSG_ID("msgid", ""),
    APP_ID("appid", ""),
    TYPE("type", ""),
    CONTENT("content", ""),
    PARAM_SEPARATOR("=", ""),
    LINE_DELIMITER("\r\n", ""),
    MSG_END(LINE_DELIMITER.value() + LINE_DELIMITER.value(), "");

    String name;
    String desc;

    MessageParameterEnum(String name, String desc) {
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
