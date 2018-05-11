package cn.atomicer.zephyr.io.socket;

import cn.atomicer.zephyr.io.coding.MessageDecoder;
import cn.atomicer.zephyr.io.functions.Function;
import cn.atomicer.zephyr.io.functions.Action;
import cn.atomicer.zephyr.io.model.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class Dealing {
    private MessageDecoder decoder;
    private Queue<Message> outputMessages;

    private Action<Throwable> onReadError;
    private Action<Throwable> onWriteError;

    public Dealing() {
        decoder = new MessageDecoder();
        outputMessages = new ConcurrentLinkedQueue<>();
        onReadError = DEFAULT_ON_ERROR;
        onWriteError = DEFAULT_ON_ERROR;
    }

    public void onAccept() {

    }

    public void onConnect() {

    }

    public void onReadable(Function<MessageDecoder, Integer> func) {
        try {
            func.apply(decoder);
        } catch (Exception e) {
            onReadError.doAction(e);
        }
    }

    public void onWriteable(Function<Message, Integer> func) {
        Message message = outputMessages.poll();
        if (message == null) return;
        try {
            func.apply(message);
        } catch (Exception e) {
            onWriteError.doAction(e);
        }
    }

    public Message poolInputMessage() {
        return decoder.poolMessage();
    }

    public boolean addOutputMessage(Message message) {
        return outputMessages.add(message);
    }

    private static Action<Throwable> DEFAULT_ON_ERROR = new Action<Throwable>() {
        @Override
        public void doAction(Throwable key) {
        }
    };

    public void setOnReadError(Action<Throwable> onReadError) {
        this.onReadError = onReadError;
    }

    public void setOnWriteError(Action<Throwable> onWriteError) {
        this.onWriteError = onWriteError;
    }
}
