package cn.atomicer.skmq.sdk.socket2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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

    private SocketClientInitializer<?> handlerInitializer;

    public static class Builder<T> {
        private SocketClient skClient;
        private HandlerCreator<T> handlerCreator;

        public Builder(String host, int port) {
            skClient = new SocketClient(host, port);
        }

        public Builder<T> setThread(int threads) {
            if (threads >= 0) {
                skClient.threads = threads;
            }
            return this;
        }

        public Builder<T> setHandlerCreator(HandlerCreator<T> handlerCreator) {
            this.handlerCreator = handlerCreator;
            return this;
        }

        public SocketClient build() {
            if (handlerCreator == null) {
                throw new IllegalArgumentException("client handler creator must be not null");
            }
            skClient.handlerInitializer = new SocketClientInitializer<>(handlerCreator);
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
                .handler(handlerInitializer);
    }

    public ChannelFuture newConnect() {
        return bootstrap.connect(host, port);
    }

    public void shutdown() {
        group.shutdownGracefully();
    }
}
