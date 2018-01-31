package cn.atomicer.skmq.sdk.socket;

import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.functions.Function;
import cn.atomicer.skmq.sdk.functions.Function0;
import cn.atomicer.skmq.sdk.model.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class Interaction {
    private MessageDecoder decoder;
    private Queue<Message> outputMessages;

    private Function0<Throwable> onReadError;
    private Function0<Throwable> onWriteError;

    public Interaction() {
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
            onReadError.apply(e);
        }
    }

    public void onWriteable(Function<Message, Integer> func) {
        Message message = outputMessages.poll();
        if (message == null) return;
        try {
            func.apply(message);
        } catch (Exception e) {
            onWriteError.apply(e);
        }
    }

    public Message poolInputMessage() {
        return decoder.poolMessage();
    }

    public boolean addOutputMessage(Message message) {
        return outputMessages.add(message);
    }

    private static Function0<Throwable> DEFAULT_ON_ERROR = new Function0<Throwable>() {
        @Override
        public void apply(Throwable key) {
        }
    };

    public void setOnReadError(Function0<Throwable> onReadError) {
        this.onReadError = onReadError;
    }

    public void setOnWriteError(Function0<Throwable> onWriteError) {
        this.onWriteError = onWriteError;
    }
}
