package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageTypeEnum;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Rao-Mengnan
 * on 2018/2/6.
 */
public class RecipientServerTest {

    static final Message DEFAULT_MSG;
    static {
        DEFAULT_MSG = new Message(
                "msg-id", "app-id",
                MessageTypeEnum.ACK.value(), "content".getBytes());

    }

    @Test
    public void testServer() throws InterruptedException {
        int port = 10011;

        OnMessage onMessage = new OnMessage();
        OnError onError = new OnError();
        RecipientServer server = new RecipientServer
                .Builder(port)
                .setBossThread(0)
                .setAction(onMessage, onError)
                .build();
        ChannelFuture future = server.startUp();

        ClientOnMessage clientOnMessage = new ClientOnMessage();
        SKClient client = new SKClient.Builder("127.0.0.1", port)
                .setAction(clientOnMessage, null)
                .build();

        client.newConnect().sync().channel().writeAndFlush(DEFAULT_MSG);
        Thread.sleep(500);

        Assert.assertEquals(DEFAULT_MSG, onMessage.queue.poll());
        Assert.assertEquals(null, onMessage.queue.poll());

        Assert.assertEquals(DEFAULT_MSG, clientOnMessage.queue.poll());
        Assert.assertEquals(null, clientOnMessage.queue.poll());

        client.newConnect().sync().channel().writeAndFlush(MessageEncoder.PING);
        Thread.sleep(500);

        Assert.assertEquals(MessageEncoder.PING, onMessage.queue.poll());
        Assert.assertEquals(null, onMessage.queue.poll());

        Assert.assertEquals(MessageEncoder.PONG, clientOnMessage.queue.poll());
        Assert.assertEquals(null, clientOnMessage.queue.poll());

        server.shutdown();
        future.sync().channel().closeFuture().sync();
    }

    class OnMessage implements Action2<ChannelHandlerContext, Message> {
        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
            queue.add(message);
            System.out.println(message);
            if (MessageEncoder.PING.equals(message)) return;
            channelHandlerContext.writeAndFlush(DEFAULT_MSG);
        }
    }

    class ClientOnMessage implements Action2<ChannelHandlerContext, Message> {
        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();

        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
            queue.add(message);
            System.out.println("client: " + message);
            channelHandlerContext.close();
        }
    }

    class OnError implements Action2<ChannelHandlerContext, Throwable> {
        Throwable throwable;

        @Override
        public void doAction(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            this.throwable = throwable;
        }
    }


}
