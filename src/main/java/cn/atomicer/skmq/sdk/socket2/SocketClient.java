package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SocketClient {
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private int threads = 0;
    private String host;
    private int port;

    private Action2<ChannelHandlerContext, Message> onMessage;
    private Action2<ChannelHandlerContext, Throwable> onError;

    public static class Builder {
        private SocketClient skClient;
        public Builder(String host, int port) {
            skClient = new SocketClient(host, port);
        }

        public Builder setAction(Action2<ChannelHandlerContext, Message> onMessage,
                                 Action2<ChannelHandlerContext, Throwable> onError) {
            skClient.onMessage = onMessage;
            skClient.onError = onError;
            return this;
        }

        public Builder setThread(int threads) {
            if (threads >= 0) {
                skClient.threads = threads;
            }
            return this;
        }

        public SocketClient build() {
            skClient.init();
            return skClient;
        }

    }

    private SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void init() {
        group = new NioEventLoopGroup(threads);
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new SocketClientInitializer(onMessage, onError));
    }

    public ChannelFuture newConnect() {
        return bootstrap.connect(host, port);
    }

    public void shutdown() {
        group.shutdownGracefully();
    }
}
