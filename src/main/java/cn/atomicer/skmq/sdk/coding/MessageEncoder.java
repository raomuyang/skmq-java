package cn.atomicer.skmq.sdk.coding;

import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageParameterEnum;
import cn.atomicer.skmq.sdk.model.MessageTypeEnum;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The default message encoder, {@link Message} message entity can be encoded as their own array
 * <p>
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class MessageEncoder implements Encoder<Message> {

    public static final Message PING;
    public static final Message PONG;

    static {
        PING = new Message(MessageTypeEnum.PING.value());
        PONG = new Message(MessageTypeEnum.PONG.value());
    }

    @Override
    public byte[] encode(Message message) {
        if (MessageTypeEnum.PING.value().equals(message.getType()) ||
                MessageTypeEnum.PING.value().equals(message.getType())) {
            String str = message.getType() +
                    MessageParameterEnum.MSG_END.value();
            return str.getBytes();
        }
        Map<String, byte[]> kv = new HashMap<>();
        if (message.getMsgId() != null) {
            kv.put(MessageParameterEnum.MSG_ID.value(), message.getMsgId().getBytes());
        }
        if (message.getAppId() != null) {
            kv.put(MessageParameterEnum.APP_ID.value(), message.getAppId().getBytes());
        }
        if (message.getType() != null) {
            kv.put(MessageParameterEnum.TYPE.value(), message.getType().getBytes());
        }
        if (message.getContent() != null) {
            kv.put(MessageParameterEnum.CONTENT.value(), message.getContent());
        }

        List<byte[]> lines = new ArrayList<>();
        for (Map.Entry<String, byte[]> entry : kv.entrySet()) {
            byte[] sub1 = (entry.getKey() + MessageParameterEnum.PARAM_SEPARATOR.value()).getBytes();
            byte[] sub2 = entry.getValue();
            byte[] line = ArrayUtils.addAll(sub1, sub2);
            lines.add(line);
        }

        byte[] joinArray = null;
        for (byte[] line : lines) {
            joinArray = ArrayUtils.addAll(joinArray, line);
            joinArray = ArrayUtils.addAll(joinArray, MessageParameterEnum.LINE_DELIMITER.value().getBytes());
        }
        return ArrayUtils.addAll(joinArray, MessageParameterEnum.MSG_END.value().getBytes());
    }
}
