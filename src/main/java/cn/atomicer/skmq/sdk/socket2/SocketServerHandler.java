package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SocketServerHandler extends AbstractHandler {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if (message != null && MessageEncoder.PING.equals(message)) {
            channelHandlerContext.writeAndFlush(MessageEncoder.PONG);
        }
        super.channelRead0(channelHandlerContext, message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
