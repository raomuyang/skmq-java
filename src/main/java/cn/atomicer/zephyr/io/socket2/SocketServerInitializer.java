package cn.atomicer.zephyr.io.socket2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Initialize the processing of the server pipeline through {@link HandlerCreator}
 *
 * @author Rao-Mengnan
 *         on 2018/2/1.
 */
public class SocketServerInitializer<I> extends ChannelInitializer<SocketChannel> {

    private HandlerCreator<I> handlerCreator;

    public SocketServerInitializer(HandlerCreator<I> handlerCreator) {
        this.handlerCreator = handlerCreator;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        if (handlerCreator == null) {
            throw new IllegalArgumentException("server handler must be not null");
        }
        ChannelPipeline pipeline = channel.pipeline();
        AbstractHandler<I> serverHandler = handlerCreator.createServerHandler();
        pipeline.addLast(serverHandler.getDecoder());
        pipeline.addLast(serverHandler.getEncoder());
        pipeline.addLast(serverHandler);
    }

}
