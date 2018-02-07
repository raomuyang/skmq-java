package cn.atomicer.skmq.sdk.example;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.model.Recipient;
import cn.atomicer.skmq.sdk.recipient.RecipientServer;
import cn.atomicer.skmq.sdk.socket2.SocketServer;
import io.netty.channel.ChannelFuture;

/**
 * Created by Rao-Mengnan
 * on 2018/2/7.
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        Recipient recipient = new Recipient(
                "recipientId", "applicationId", "host", port, 0);

        SocketServer server = new SocketServer
                .Builder(port)
                .setBossThread(1)
                .setWorkerThread(4)
                .setAction(
                        (channelHandlerContext, message) -> {
                            // do something
                            // 消息到达时，耗时的业务应另起线程进行处理
                            System.out.println(message);
                            channelHandlerContext.writeAndFlush(MessageEncoder.PONG);
                        },
                        (channelHandlerContext, throwable) -> {
                            // do something
                            throwable.printStackTrace();
                        })
                .build();

        RecipientServer recipientServer = new RecipientServer(server, recipient);

        String skmqServerHost = "";
        int skmqServerPort = 1734;
        recipientServer.registerRecipient(skmqServerHost, skmqServerPort);

        try {

            // server.startUp()
            ChannelFuture future = recipientServer.getServer().startUp();
            future.sync().channel().closeFuture().sync();
        } finally {
            recipientServer.getServer().shutdown();
        }
    }
}
