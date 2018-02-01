package cn.atomicer.skmq.sdk.socket;

import cn.atomicer.skmq.sdk.functions.Action;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageTypeEnum;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/1/31.
 */
public class OneTimeServiceThread extends Thread {

    final static Message PING;
    final static Message PONG;

    static {
        PING = new Message();
        PING.setType(MessageTypeEnum.PING.value());
        PONG = new Message();
        PONG.setType(MessageTypeEnum.PONG.value());
    }

    boolean checked;
    private ServerSocketChannel channel;
    private Action<Message> assertMessage;

    OneTimeServiceThread(int port, Action<Message> assertMessage) throws IOException {
        this.channel = ServerSocketChannel.open();
        this.channel.bind(new InetSocketAddress(port));
        this.assertMessage = assertMessage;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = channel.accept();
            ServerChannel serverChannel = new ServerChannel(socketChannel);
            Thread.sleep(100);
            serverChannel.doAction(SelectionKey.OP_READ);
            Message msg = serverChannel.getDealing().poolInputMessage();
            assertMessage.doAction(msg);
            checked = true;
            serverChannel.write(PONG);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
