package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.OneTimeServiceThread;
import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Rao-Mengnan
 * on 2018/2/1.
 */
public class SocketClientTest {

    private static final Action2<ChannelHandlerContext, Throwable> ON_ERROR = new Action2<ChannelHandlerContext, Throwable>() {
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            throwable.printStackTrace();
        }
    };


    @Test
    public void testClient() throws IOException, InterruptedException {
        int port = 1234;

        OneTimeServiceThread thread = new OneTimeServiceThread(port, null);
        thread.start();

        HandlerCreator<Message> handlerCreator = new HandlerCreator<>(
                CodecCreator.DEFAULT_ENCODER_CREATOR, CodecCreator.DEFAULT_DECODER_CREATOR
        );
        MessageAction2 checkMessage = new MessageAction2();
        handlerCreator.setAction(checkMessage, ON_ERROR);

        SocketClient client = new SocketClient.Builder<Message>("127.0.0.1", port)
                .setHandlerCreator(handlerCreator)
                .setThread(2)
                .build();

        ChannelFuture future = client.newConnect();
        Channel channel = future.sync().channel();

        channel.writeAndFlush(OneTimeServiceThread.PING);
        Thread.sleep(200);
        channel.close().sync();
        client.shutdown();

        Assert.assertEquals(MessageEncoder.PING, thread.getQueue().poll());
        Assert.assertEquals(true, thread.getQueue().isEmpty());

        Assert.assertEquals(false, checkMessage.queue.isEmpty());
        Assert.assertEquals(MessageEncoder.PONG, checkMessage.queue.poll());
        Assert.assertEquals(true, checkMessage.queue.isEmpty());
    }

    class MessageAction2 implements Action2<ChannelHandlerContext, Message> {

        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();

        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
            queue.add(message);
        }
    }

}
