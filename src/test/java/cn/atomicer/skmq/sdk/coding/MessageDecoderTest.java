package cn.atomicer.skmq.sdk.coding;

import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.model.Message;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class MessageDecoderTest {

    @Test
    public void testDecodeMessage() {
        MessageDecoder heap = new MessageDecoder();
        String str = "msgid=xxx\r\nappid=a=a=a\r\ncontent=123456\r\ntype=queue\r\n\r\n\r\nping\r\n\r\n";
        heap.write(str.getBytes(), str.getBytes().length);

        Message msg = heap.poolMessage();
        Assert.assertEquals("a=a=a", msg.getAppId());
        Assert.assertEquals("xxx", msg.getMsgId());
        Assert.assertEquals("123456", new String(msg.getContent()));
        Assert.assertEquals("queue", msg.getType());

        msg = heap.poolMessage();
        Assert.assertEquals("ping", msg.getType());
        Assert.assertNull(msg.getMsgId());

        msg = heap.poolMessage();
        Assert.assertNull(msg);
    }

}
