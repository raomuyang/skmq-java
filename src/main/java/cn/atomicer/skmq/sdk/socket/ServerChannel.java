package cn.atomicer.skmq.sdk.socket;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class ServerChannel extends AbstractChannel {
    public ServerChannel(SocketChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false);
    }
}
