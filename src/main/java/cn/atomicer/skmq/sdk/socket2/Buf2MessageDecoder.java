package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.Decoder;
import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * The message decode handler of the Netty handling process,
 * depending on the type of decoder, different message entities
 * are decoded
 *
 * @author Rao-Mengnan
 *         on 2018/2/1.
 */
public class Buf2MessageDecoder<T> extends ByteToMessageDecoder {
    private Decoder<T> decoder;

    public Buf2MessageDecoder(Decoder<T> decoder) {

        this.decoder = decoder;
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
            T message = decoder.poolMessage();
            if (message == null) break;
            out.add(message);
        }
    }

    public static Buf2MessageDecoder<Message> getDefault() {
        return new Buf2MessageDecoder<>(new MessageDecoder());
    }
}
