package cn.atomicer.skmq.sdk.socket;

import cn.atomicer.skmq.sdk.functions.Function0;
import cn.atomicer.skmq.sdk.model.Message;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Rao-Mengnan
 * on 2018/1/31.
 */
public class ChannelSelectorTest {
    @Test
    public void testSelectReadAndWrite() throws IOException, InterruptedException {
        int port = 12345;
        OneTimeServiceThread thread = new OneTimeServiceThread(port, new Function0<Message>() {
            @Override
            public void apply(Message key) {
                Assert.assertEquals(OneTimeServiceThread.PING.getType(), key.getType());
            }
        });

        thread.start();

        ChannelSelector channelSelector = new ChannelSelector();

        ClientChannel clientChannel = new ClientChannel("127.0.0.1", port);
        channelSelector.channelRegister(clientChannel.getChannel(), ChannelSelector.clientOps(), clientChannel);
        clientChannel.getInteraction().addOutputMessage(OneTimeServiceThread.PING);

        new Thread(channelSelector).start();
        Thread.sleep(1000);

        Assert.assertEquals(true, thread.checked);
        Message msg = clientChannel.getInteraction().poolInputMessage();
        Assert.assertNotNull(msg);
        Assert.assertEquals(OneTimeServiceThread.PONG.getType(), msg.getType());
    }
}
