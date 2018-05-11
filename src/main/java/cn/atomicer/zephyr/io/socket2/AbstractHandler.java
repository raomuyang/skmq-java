package cn.atomicer.zephyr.io.socket2;

import cn.atomicer.zephyr.io.functions.Action2;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AbstractHandler extends the {@link SimpleChannelInboundHandler}, which automatically creates the corresponding
 * codec in the connection through the {@code encoderCreator} domain and the
 * {@code decoderCreator} domain. The {@code onMessage} domain is responsible
 * for processing the message as it is received. {@code onError} field is
 * responsible for completing the action when an error occurs
 *
 * @author Rao-Mengnan
 *         on 2018/2/1.
 */
public abstract class AbstractHandler<I> extends SimpleChannelInboundHandler {

    Log log = LogFactory.getLog(getClass());

    private CodecCreator<Message2BufEncoder<I>> encoderCreator;
    private CodecCreator<Buf2MessageDecoder<I>> decoderCreator;

    Action2<ChannelHandlerContext, I> onMessage;
    Action2<ChannelHandlerContext, Throwable> onError;

    AbstractHandler(CodecCreator<Message2BufEncoder<I>> encoderCreator,
                    CodecCreator<Buf2MessageDecoder<I>> decoderCreator) {
        this.encoderCreator = encoderCreator;
        this.decoderCreator = decoderCreator;
        setAction(null, null);
    }

    @SuppressWarnings("unchecked")
    public AbstractHandler<I> setAction(Action2<ChannelHandlerContext, I> onMessage,
                                        Action2<ChannelHandlerContext, Throwable> onError) {
        this.onMessage = onMessage != null ? onMessage : DEFAULT_ON_MESSAGE;
        this.onError = onError != null ? onError : DEFAULT_ON_ERROR;
        return this;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        onError.doAction(ctx, cause);
        super.exceptionCaught(ctx, cause);
    }

    private static final Action2<ChannelHandlerContext, Throwable> DEFAULT_ON_ERROR = new Action2<ChannelHandlerContext, Throwable>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

        }
    };

    private static final Action2 DEFAULT_ON_MESSAGE = new Action2<ChannelHandlerContext, Object>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Object t) throws Exception {

        }
    };

    public Message2BufEncoder<I> getEncoder() throws Exception {
        return encoderCreator.apply();
    }

    public Buf2MessageDecoder<I> getDecoder() throws Exception {
        return decoderCreator.apply();
    }
}
