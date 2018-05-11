package cn.atomicer.zephyr.io.socket;

import java.io.IOException;
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
