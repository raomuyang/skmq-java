package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private Action2<ChannelHandlerContext, Message> onMessage;
    private Action2<ChannelHandlerContext, Throwable> onError;

    public SocketServerInitializer() {
    }

    public SocketServerInitializer(Action2<ChannelHandlerContext, Message> onMessage, Action2<ChannelHandlerContext, Throwable> onError) {
        this.onMessage = onMessage;
        this.onError = onError;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new Buf2MessageDecoder());
        pipeline.addLast(new Message2BufEncoder());

        SocketServerHandler handler = new SocketServerHandler();
        handler.setAction(onMessage, onError);
        pipeline.addLast(handler);
    }
}