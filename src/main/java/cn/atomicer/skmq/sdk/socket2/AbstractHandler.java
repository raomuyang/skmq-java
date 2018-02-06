package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public abstract class AbstractHandler extends SimpleChannelInboundHandler<Message> {

    private Action2<ChannelHandlerContext, Message> onMessage = DEFAULT_ON_MESSAGE;
    private Action2<ChannelHandlerContext, Throwable> onError = DEFAULT_ON_ERROR;

    public void setAction(Action2<ChannelHandlerContext, Message> onMessage,
                                     Action2<ChannelHandlerContext, Throwable> onError) {
        this.onMessage = onMessage != null ? onMessage : DEFAULT_ON_MESSAGE;
        this.onError = onError != null ? onError : DEFAULT_ON_ERROR;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        onMessage.doAction(channelHandlerContext, message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        onError.doAction(ctx, cause);
        super.exceptionCaught(ctx, cause);
    }

    private static final Action2<ChannelHandlerContext, Message> DEFAULT_ON_MESSAGE = new Action2<ChannelHandlerContext, Message>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

        }
    };

    private static final Action2<ChannelHandlerContext, Throwable> DEFAULT_ON_ERROR = new Action2<ChannelHandlerContext, Throwable>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

        }
    };
}
