package cn.atomicer.skmq.sdk.socket;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class ChannelSelector implements Runnable {
    private Selector selector;
    private IOException throwCause;

    public ChannelSelector() throws IOException {
        selector = Selector.open();
    }

    public SelectionKey channelRegister(SocketChannel channel, Object attachment) throws IOException {
        if (throwCause != null) throw throwCause;
        return channel.register(
                selector, SelectionKey.OP_CONNECT &
                        SelectionKey.OP_ACCEPT &
                        SelectionKey.OP_READ &
                        SelectionKey.OP_WRITE, attachment);
    }

    @Override
    public void run() {
        while (true) {
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

    public void iterate(Set<SelectionKey> keys) {
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            System.out.println(key);
            MessageChannel messageSocket = (MessageChannel) key.attachment();
            if (key.isAcceptable()) {
                messageSocket.doAction(SelectionKey.OP_ACCEPT);
            } else if (key.isConnectable()) {
                messageSocket.doAction(SelectionKey.OP_CONNECT);
            } else if (key.isReadable()) {
                messageSocket.doAction(SelectionKey.OP_READ);
            } else if (key.isWritable()) {
                messageSocket.doAction(SelectionKey.OP_WRITE);
            }
            iterator.remove();
        }
    }

    public Selector getSelector() {
        return selector;
    }
}
