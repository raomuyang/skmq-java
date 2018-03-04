package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.functions.Action2;
import io.netty.channel.ChannelHandlerContext;

/**
 * In the netty messaging, the pipeline's handle should not be shared, otherwise the
 * user can not rest assured that some of the concurrent jobs are completed. Provided
 * to {@link SocketClientInitializer} or {@link SocketServerInitializer} via the
 * {@code HandlerCreator} instance so that the handlers in each message pipe are not
 * shared and do not cause some problems due to concurrency. Similarly, users can
 * inherit it and override {@link HandlerCreator#createClientHandler()} and
 * {@link HandlerCreator#createServerHandler()} to customize HandlerCreator
 *
 * @author Rao-Mengnan
 *         on 2018/3/4.
 */
public class HandlerCreator<I> {

    private final CodecCreator<Message2BufEncoder<I>> encoderCreator;
    private final CodecCreator<Buf2MessageDecoder<I>> decoderCreator;

    private Action2<ChannelHandlerContext, I> onMessage;
    private Action2<ChannelHandlerContext, Throwable> onError;

    public HandlerCreator(CodecCreator<Message2BufEncoder<I>> encoderCreator, CodecCreator<Buf2MessageDecoder<I>> decoderCreator) {
        this.encoderCreator = encoderCreator;
        this.decoderCreator = decoderCreator;
    }

    public HandlerCreator<I> setAction(Action2<ChannelHandlerContext, I> onMessage,
                                       Action2<ChannelHandlerContext, Throwable> onError) {
        this.onMessage = onMessage;
        this.onError = onError;
        return this;
    }

    public SocketClientHandler<I> createClientHandler() {
        SocketClientHandler<I> handler = new SocketClientHandler<>(encoderCreator, decoderCreator);
        handler.setAction(onMessage, onError);
        return handler;
    }

    public SocketServerHandler<I> createServerHandler() {
        SocketServerHandler<I> handler = new SocketServerHandler<>(encoderCreator, decoderCreator);
        handler.setAction(onMessage, onError);
        return handler;
    }

}
