package cn.atomicer.skmq.sdk.coding;

import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageParameterEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public class MessageEncoderTest {
    @Test
    public void testEncode() {
        Message message = new Message();
        message.setAppId("app=id");

        byte[] bytes = MessageEncoder.encode(message);
        String appIdEncode = String.format("appid=%s\r\n", message.getAppId());
        Assert.assertEquals(appIdEncode + MessageParameterEnum.MSG_END.value(), new String(bytes));

        message.setMsgId("message=id");
        String msgIdEncode = String.format("msgid=%s\r\n", message.getMsgId());
        bytes = MessageEncoder.encode(message);
        Assert.assertEquals(true, new String(bytes).contains(msgIdEncode));
        Assert.assertEquals(true, new String(bytes).contains(appIdEncode));

    }
}
