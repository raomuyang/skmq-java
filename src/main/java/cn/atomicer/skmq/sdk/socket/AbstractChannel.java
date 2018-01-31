package cn.atomicer.skmq.sdk.socket;

import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.functions.Function;
import cn.atomicer.skmq.sdk.functions.Function0;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageParameterEnum;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public abstract class AbstractChannel implements MessageChannel {
    private Interaction interaction = new Interaction();
    SocketChannel channel;

    @Override
    public int read(MessageDecoder messageDecoder) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int count = 0;
        while (true) {
            int read = channel.read(buffer);
            if (read <= 0) {
                break;
            }
            count += read;
            messageDecoder.write(buffer.array(), read);
            buffer.clear();
        }
        return count;
    }

    @Override
    public int write(Message message) throws IOException {
        byte[] bytes = MessageEncoder.encode(message);
        byte[] end = MessageParameterEnum.MSG_END.value().getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length + end.length);
        buffer.put(bytes, 0, bytes.length);
        buffer.put(end, 0, end.length);
        buffer.flip();
        return write(buffer);
    }

    @Override
    public int write(ByteBuffer buffer) throws IOException {
        int count = 0;
        while (buffer.hasRemaining()) {
            count += channel.write(buffer);
        }
        return count;
    }

    @Override
    public boolean finishConnect() throws IOException {
        return channel.finishConnect();
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SocketChannel getChannel() {
        return this.channel;
    }

    @Override
    public void doAction(int ops) {

        switch (ops) {
            case SelectionKey.OP_ACCEPT:
                interaction.onAccept();
                break;
            case SelectionKey.OP_CONNECT:
                interaction.onConnect();
                break;
            case SelectionKey.OP_READ:
                interaction.onReadable(new Function<MessageDecoder, Integer>() {
                    @Override
                    public Integer apply(MessageDecoder decoder) throws Exception {
                        return read(decoder);
                    }
                });
                break;
            case SelectionKey.OP_WRITE:
                interaction.onWriteable(new Function<Message, Integer>() {
                    @Override
                    public Integer apply(Message message) throws Exception {
                        return write(message);
                    }
                });
                break;
            default:
        }
    }

    @Override
    public MessageChannel setOnError(Function0<Throwable> onReadError, Function0<Throwable> onWriteError) {
        this.interaction.setOnReadError(onReadError);
        this.interaction.setOnWriteError(onWriteError);
        return this;
    }

    @Override
    public Interaction getInteraction() {
        return this.interaction;
    }

}
