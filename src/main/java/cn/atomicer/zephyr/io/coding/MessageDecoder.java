package cn.atomicer.zephyr.io.coding;

import cn.atomicer.zephyr.io.model.Message;
import cn.atomicer.zephyr.io.model.MessageParameterEnum;
import cn.atomicer.zephyr.io.model.MessageTypeEnum;
import com.google.gson.Gson;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The default message decoder can decode a
 * stream of bytes into a series of {@link Message} entities
 *
 * @author Rao-Mengnan
 *         on 2018/1/29.
 */
public class MessageDecoder implements Decoder<Message> {
    private byte[] buffer;
    private Queue<Message> messages;
    private Map<String, Object> kv;
    private Gson gson;

    public MessageDecoder() {
        messages = new ConcurrentLinkedQueue<>();
        kv = new HashMap<>();
        gson = new Gson();
    }

    @Override
    public synchronized void write(byte[] bytes, int offset) {
        if (buffer == null) {
            buffer = new byte[offset];
            System.arraycopy(bytes, 0, buffer, 0, offset);
        } else {
            int len = bytes.length + offset;
            byte[] appendArray = new byte[len];
            System.arraycopy(buffer, 0, appendArray, 0, buffer.length);
            System.arraycopy(bytes, 0, appendArray, 0, offset);
            buffer = appendArray;
        }
        decode();
    }

    /**
     * @return the head  message of this queue, or {@code null} if this queue is empty
     */
    @Override
    public Message poolMessage() {
        return messages.poll();
    }

    private void decode() {
        List<byte[]> lines = new ArrayList<>();
        int start = 0;
        int offset = 0;
        for (; offset < buffer.length; offset += 1) {
            if (offset + 1 >= buffer.length) {
                break;
            }
            if (buffer[offset] == '\r' && buffer[offset + 1] == '\n') {
                byte[] sub = ArrayUtils.subarray(buffer, start, offset);
                lines.add(sub);
                offset += 1;
                start = offset + 1;
            }
        }

        buffer = ArrayUtils.subarray(buffer, start, buffer.length);
        for (byte[] line : lines) {

            if (line.length == 4) {
                String str = new String(line);
                if (MessageTypeEnum.PING.value().equals(str) ||
                        MessageTypeEnum.PONG.value().equals(str)) {
                    kv.put(MessageParameterEnum.TYPE.value(), str);
                    continue;
                }
            }

            if (line.length == 0 && kv.size() > 0) {
                Message message = gson.fromJson(gson.toJson(kv), Message.class);
                messages.add(message);
                kv = new HashMap<>();
                continue;
            }

            if (line.length == 0) {
                continue;
            }

            int index = ArrayUtils.indexOf(line, MessageParameterEnum.PARAM_SEPARATOR.value().getBytes()[0]);
            if (index < 0) {
                throw new RuntimeException("Separator not found.");
            }

            String key = new String(ArrayUtils.subarray(line, 0, index));
            byte[] value = ArrayUtils.subarray(line, index + 1, line.length);
            if (!key.equals(MessageParameterEnum.CONTENT.value())) {
                kv.put(key, new String(value));
            } else {
                kv.put(key, value);
            }
        }

    }

    public boolean interruptible() {
        return buffer.length == 0 || buffer.length == 4 && MessageParameterEnum.MSG_END.value().equals(new String(buffer));
    }

}
