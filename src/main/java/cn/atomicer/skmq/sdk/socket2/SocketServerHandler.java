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
        String msgType = message == null ? "null-msg" : message.getType();
        log.debug(String.format("read message, type: %s, ctx: %s", msgType, channelHandlerContext));

        if (message != null && MessageEncoder.PING.equals(message)) {
            channelHandlerContext.writeAndFlush(MessageEncoder.PONG);
        }
        onMessage.doAction(channelHandlerContext, message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug(String.format("channel active: %s", ctx));
        ctx.fireChannelActive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.debug(String.format("channel read complete: %s", ctx));
        ctx.flush();
    }
}
