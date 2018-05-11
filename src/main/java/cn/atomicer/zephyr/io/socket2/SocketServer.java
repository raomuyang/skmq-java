package cn.atomicer.zephyr.io.socket2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Build a socket server launcher quickly with {@link SocketServer.Builder}
 *
 * @author Rao-Mengnan
 *         on 2018/2/1.
 */
public class SocketServer {
    private int port;
    private int bossThreads = 1;
    private int workerThreads = 1;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private SocketServerInitializer<?> handlerInitializer;

    public static class Builder<T> {
        private SocketServer server;
        private HandlerCreator<T> handlerCreator;

        public Builder(int port) {
            this.server = new SocketServer(port);
        }

        public Builder<T> setBossThread(int threads) {
            if (threads >= 0) {
                server.bossThreads = threads;
            }
            return this;
        }

        public Builder<T> setWorkerThread(int threads) {
            if (threads >= 0) {
                server.workerThreads = threads;
            }
            return this;
        }

        public Builder<T> setHandlerCreator(HandlerCreator<T> handlerCreator) {
            this.handlerCreator = handlerCreator;
            return this;
        }

        public SocketServer build() {
            if (handlerCreator == null) {
                throw new IllegalArgumentException("server handler creator must be not null");
            }
            server.handlerInitializer = new SocketServerInitializer<>(handlerCreator);
            server.init();
            return server;
        }
    }

    private SocketServer(int port) {
        this.port = port;
    }

    private void init() {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(bossThreads);
        workerGroup = new NioEventLoopGroup(workerThreads);
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(handlerInitializer);
    }

    /**
     * Start a service asynchronously
     *
     * @return get {@link ChannelFuture} did not call {@code sync()}
     */
    public ChannelFuture startUp() {
        return bootstrap.bind(port);
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


}
