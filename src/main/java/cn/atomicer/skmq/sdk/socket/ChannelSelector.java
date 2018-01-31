package cn.atomicer.skmq.sdk.socket;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class ChannelSelector implements Runnable {
    private Selector selector;
    private IOException throwCause;
    private AtomicBoolean closed;

    public ChannelSelector() throws IOException {
        selector = Selector.open();
        closed = new AtomicBoolean();
    }

    public SelectionKey channelRegister(SocketChannel channel, int ops, Object attachment) throws IOException {
        if (throwCause != null) throw throwCause;
        this.selector.wakeup();
        return channel.register(
                selector, ops, attachment);
    }

    public static int clientOps() {
        return SelectionKey.OP_CONNECT |
                SelectionKey.OP_READ |
                SelectionKey.OP_WRITE;
    }

    public static int serverOps() {
        return SelectionKey.OP_ACCEPT |
                SelectionKey.OP_READ |
                SelectionKey.OP_WRITE;
    }

    @Override
    public void run() {
        while (!closed.get()) {
            try {
                int keyNum = selector.select();
                if (keyNum > 0) {
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    iterate(keySet);
                }
            } catch (IOException e) {
                throwCause = e;
                break;
            }
        }
    }

    public void iterate(Set<SelectionKey> keys) throws IOException {
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            if (key.isAcceptable()) {
                MessageChannel messageChannel = new ServerChannel((SocketChannel) key.channel());
                key.attach(messageChannel);
                messageChannel.doAction(SelectionKey.OP_ACCEPT);
            } else {
                MessageChannel messageChannel = (MessageChannel) key.attachment();
                if (key.isConnectable()) {
                    messageChannel.finishConnect();
                    messageChannel.doAction(SelectionKey.OP_CONNECT);
                } else if (key.isReadable()) {
                    messageChannel.doAction(SelectionKey.OP_READ);
                } else if (key.isWritable()) {
                    messageChannel.doAction(SelectionKey.OP_WRITE);
                }
            }
            iterator.remove();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public void close() throws IOException {
        this.closed.set(true);
        this.selector.close();

    }
}
