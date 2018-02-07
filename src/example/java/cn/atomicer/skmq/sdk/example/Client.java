package cn.atomicer.skmq.sdk.example;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.socket2.SocketClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Rao-Mengnan
 * on 2018/2/7.
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        String skmqServerHost = "";
        int skmqServerPort = 1734;
        SocketClient client = new SocketClient.Builder(skmqServerHost, skmqServerPort)
                .setAction(
                        (channelHandlerContext, message) -> {
                            // do something
                            System.out.println(message);
                            channelHandlerContext.close();
                        },
                        (channelHandlerContext, throwable) -> {
                            // do something
                            throwable.printStackTrace();
                        })
                .build();

        ChannelFuture channelFuture = client.newConnect();
        channelFuture
                .sync().channel()
                .writeAndFlush(MessageEncoder.PING)
                .channel().closeFuture().sync();
    }
}
