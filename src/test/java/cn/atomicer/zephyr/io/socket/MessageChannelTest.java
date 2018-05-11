package cn.atomicer.zephyr.io.socket;

import cn.atomicer.zephyr.io.OneTimeServiceThread;
import cn.atomicer.zephyr.io.coding.MessageEncoder;
import cn.atomicer.zephyr.io.functions.Action;
import cn.atomicer.zephyr.io.model.Message;
import org.junit.*;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class MessageChannelTest {
    private static MessageEncoder encoder = new MessageEncoder();
    @Test
    public void testAction() throws IOException, InterruptedException {
        int port = 10001;

        OneTimeServiceThread thread = new OneTimeServiceThread(port, new Action<Message>() {
            @Override
            public void doAction(Message key) {
                Assert.assertEquals(OneTimeServiceThread.PING.getType(), key.getType());
            }
        });
        thread.start();

        ClientChannel channel = new ClientChannel("127.0.0.1", port);
        while (!channel.finishConnect()) {
        }


        channel.getDealing().addOutputMessage(OneTimeServiceThread.PING);
        channel.doAction(SelectionKey.OP_WRITE);
        Thread.sleep(2000);

        channel.doAction(SelectionKey.OP_READ);
        Message res = channel.getDealing().poolInputMessage();
        Assert.assertEquals(OneTimeServiceThread.PONG, res);

        Assert.assertEquals(true, thread.isChecked());
        Assert.assertEquals(OneTimeServiceThread.PING, thread.getQueue().poll());
    }

    @Test
    public void testWrite() throws IOException {
        int port = 10010;
        OneTimeServiceThread thread = new OneTimeServiceThread(port, new Action<Message>() {
            @Override
            public void doAction(Message key) {
                Assert.assertEquals(OneTimeServiceThread.PING.getType(), key.getType());
            }
        });
        thread.start();

        ClientChannel channel = new ClientChannel("127.0.0.1", port);
        while (!channel.finishConnect()) {
        }

        int w = channel.write(OneTimeServiceThread.PING);
        Assert.assertEquals(encoder.encode(OneTimeServiceThread.PING).length, w);

    }


}
