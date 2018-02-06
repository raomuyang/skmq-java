package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class RecipientServer {
    private int port;
    private int bossThreads = 1;
    private int workerThreads = 1;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Action2<ChannelHandlerContext, Message> onMessage;
    private Action2<ChannelHandlerContext, Throwable> onError;

    public static class Builder {
        private RecipientServer server;

        public Builder(int port) {
            this.server = new RecipientServer(port);
        }

        public Builder setAction(Action2<ChannelHandlerContext, Message> onMessage,
                                 Action2<ChannelHandlerContext, Throwable> onError) {
            server.onMessage = onMessage;
            server.onError = onError;
            return this;
        }

        public Builder setBossThread(int threads) {
            if (threads >= 0) {
                server.bossThreads = threads;
            }
            return this;
        }

        public Builder setWorkerThread(int threads) {
            if (threads >= 0) {
                server.workerThreads = threads;
            }
            return this;
        }

        public RecipientServer build() {
            server.init();
            return server;
        }
    }

    private RecipientServer(int port) {
        this.port = port;
    }

    private void init() {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(bossThreads);
        workerGroup = new NioEventLoopGroup(workerThreads);
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RecipientServerInitializer(onMessage, onError));
    }

    public ChannelFuture startUp() {
        return bootstrap.bind(port);
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


}
