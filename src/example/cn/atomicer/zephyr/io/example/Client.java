package cn.atomicer.zephyr.io.example;

import cn.atomicer.zephyr.io.coding.MessageEncoder;
import cn.atomicer.zephyr.io.model.Message;
import cn.atomicer.zephyr.io.socket2.CodecCreator;
import cn.atomicer.zephyr.io.socket2.HandlerCreator;
import cn.atomicer.zephyr.io.socket2.SocketClient;
import io.netty.channel.ChannelFuture;

/**
 * Created by Rao-Mengnan
 * on 2018/2/7.
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        String skmqServerHost = "";
        int skmqServerPort = 1734;

        HandlerCreator<Message> clientHandler = new HandlerCreator<>(
                CodecCreator.DEFAULT_ENCODER_CREATOR,
                CodecCreator.DEFAULT_DECODER_CREATOR)
                .setAction((channelHandlerContext, message) -> {
                            // do something
                            System.out.println(message);
                            channelHandlerContext.close();
                        },
                        (channelHandlerContext, throwable) -> {
                            // do something
                            throwable.printStackTrace();
                        });

        SocketClient client = new SocketClient.Builder<Message>(skmqServerHost, skmqServerPort)
                .setHandlerCreator(clientHandler)
                .build();

        ChannelFuture channelFuture = client.newConnect();
        channelFuture
                .sync().channel()
                .writeAndFlush(MessageEncoder.PING)
                .channel().closeFuture().sync();
        client.shutdown();
    }
}
