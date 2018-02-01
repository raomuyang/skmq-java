package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.functions.Action;
import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.OneTimeServiceThread;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SKClientTest {
    private static final Action2<ChannelHandlerContext, Message> ON_MESSAGE = new Action2<ChannelHandlerContext, Message>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
            System.out.println(message);
        }
    };

    private static final Action2<ChannelHandlerContext, Throwable> ON_ERROR = new Action2<ChannelHandlerContext, Throwable>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            throwable.printStackTrace();
        }
    };


    @Test
    public void testClient() throws IOException, InterruptedException {
        int port = 1234;
        OneTimeServiceThread thread = new OneTimeServiceThread(port, new Action<Message>() {

            @Override
            public void doAction(Message key) {
                Assert.assertEquals(OneTimeServiceThread.PING.getType(), key.getType());
            }
        });
        thread.start();

        Action2<ChannelHandlerContext, Message> checkMessage = new Action2<ChannelHandlerContext, Message>(){
            @Override
            public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
                Assert.assertEquals(OneTimeServiceThread.PONG.getType(), message.getType());
            }
        };
        SKClient client = new SKClient.Builder("127.0.0.1", port)
                .setAction(checkMessage, ON_ERROR)
                .setThread(2)
                .build();

        ChannelFuture future = client.newConnect();
        Channel channel = future.sync().channel();

        channel.writeAndFlush(OneTimeServiceThread.PING);
        Thread.sleep(200);
        client.shutdown();

    }
}
