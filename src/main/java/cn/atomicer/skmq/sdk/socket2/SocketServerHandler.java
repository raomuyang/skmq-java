package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SocketServerHandler<I> extends AbstractHandler<I> {

    public SocketServerHandler(CodecCreator<Message2BufEncoder<I>> encoderCreator,
                               CodecCreator<Buf2MessageDecoder<I>> decoderCreator) {
        super(encoderCreator, decoderCreator);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {
        String msgType = message == null ? "null-msg" : String.valueOf(message);
        log.debug(String.format("read message, content: %s, ctx: %s", msgType, channelHandlerContext));

        if (message != null && MessageEncoder.PING.equals(message)) {
            channelHandlerContext.writeAndFlush(MessageEncoder.PONG);
        }
        onMessage.doAction(channelHandlerContext, (I) message);
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
