package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.model.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SocketClientHandler extends AbstractHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        String msgType = message == null ? "null-msg" : message.getType();
        log.debug(String.format("read message, type: %s, ctx: %s", msgType, channelHandlerContext));

        onMessage.doAction(channelHandlerContext, message);
    }
}
