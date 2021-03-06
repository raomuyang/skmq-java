package cn.atomicer.zephyr.io.socket;

import cn.atomicer.zephyr.io.coding.MessageDecoder;
import cn.atomicer.zephyr.io.coding.MessageEncoder;
import cn.atomicer.zephyr.io.functions.Function;
import cn.atomicer.zephyr.io.functions.Action;
import cn.atomicer.zephyr.io.model.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public abstract class AbstractChannel implements MessageChannel {
    private Dealing dealing = new Dealing();
    private MessageEncoder encoder = new MessageEncoder();
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
        byte[] bytes = encoder.encode(message);
        return write(ByteBuffer.wrap(bytes));
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
                dealing.onAccept();
                break;
            case SelectionKey.OP_CONNECT:
                dealing.onConnect();
                break;
            case SelectionKey.OP_READ:
                dealing.onReadable(new Function<MessageDecoder, Integer>() {
                    @Override
                    public Integer apply(MessageDecoder decoder) throws Exception {
                        return read(decoder);
                    }
                });
                break;
            case SelectionKey.OP_WRITE:
                dealing.onWriteable(new Function<Message, Integer>() {
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
    public MessageChannel setOnError(Action<Throwable> onReadError, Action<Throwable> onWriteError) {
        this.dealing.setOnReadError(onReadError);
        this.dealing.setOnWriteError(onWriteError);
        return this;
    }

    @Override
    public Dealing getDealing() {
        return this.dealing;
    }

}
