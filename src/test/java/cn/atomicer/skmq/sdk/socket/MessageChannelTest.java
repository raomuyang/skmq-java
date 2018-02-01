package cn.atomicer.skmq.sdk.socket;

import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.functions.Action;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageParameterEnum;
import org.junit.*;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class MessageChannelTest {

    @Test
    public void testAction() throws IOException, InterruptedException {
        int port = 12345;

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
        Assert.assertEquals(OneTimeServiceThread.PONG.getType(), res.getType());

        Assert.assertEquals(true, thread.checked);
    }

    @Test
    public void testWrite() throws IOException {
        int port = 12346;
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
        Assert.assertEquals(MessageEncoder.encode(OneTimeServiceThread.PING).length + MessageParameterEnum.MSG_END.value().length(), w);

    }


}
