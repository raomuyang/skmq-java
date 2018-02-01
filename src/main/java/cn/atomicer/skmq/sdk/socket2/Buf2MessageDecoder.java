package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class Buf2MessageDecoder extends ByteToMessageDecoder{
    private MessageDecoder decoder;

    public Buf2MessageDecoder() {
        decoder = new MessageDecoder();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < 8) {
            return;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        decoder.write(bytes, bytes.length);

        while (true) {
            Message message = decoder.poolMessage();
            if (message == null) break;
            out.add(message);
        }
    }
}
