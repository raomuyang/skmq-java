package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class Message2BufEncoder extends MessageToByteEncoder<Message>{
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        byte[] bytes = MessageEncoder.encode(message);
        out.writeBytes(bytes);
    }
}
