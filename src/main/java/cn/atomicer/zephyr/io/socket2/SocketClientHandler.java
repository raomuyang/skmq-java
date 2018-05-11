package cn.atomicer.zephyr.io.socket2;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Rao-Mengnan
 *         on 2018/2/1.
 */
public class SocketClientHandler<I> extends AbstractHandler<I> {

    public SocketClientHandler(CodecCreator<Message2BufEncoder<I>> encoderCreator,
                               CodecCreator<Buf2MessageDecoder<I>> decoderCreator) {
        super(encoderCreator, decoderCreator);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {
        String msgType = message == null ? "null-msg" : String.valueOf(message);
        log.debug(String.format("read message, type: %s, ctx: %s", msgType, channelHandlerContext));

        onMessage.doAction(channelHandlerContext, (I) message);
    }
}
