package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class Message2BufEncoder<T> extends MessageToByteEncoder {
    private Encoder<T> encoder;

    public Message2BufEncoder(Encoder<T> encoder) {
        this.encoder = encoder;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object message, ByteBuf out) throws Exception {
        byte[] bytes = encoder.encode((T) message);
        out.writeBytes(bytes);
    }
}
