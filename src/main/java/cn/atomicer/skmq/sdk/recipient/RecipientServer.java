package cn.atomicer.skmq.sdk.recipient;

import cn.atomicer.skmq.sdk.functions.Action2;
import cn.atomicer.skmq.sdk.model.Message;
import cn.atomicer.skmq.sdk.model.MessageTypeEnum;
import cn.atomicer.skmq.sdk.model.Recipient;
import cn.atomicer.skmq.sdk.socket2.SocketClient;
import cn.atomicer.skmq.sdk.socket2.SocketServer;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static cn.atomicer.skmq.sdk.util.ObjectUtil.ensureNotNull;

/**
 * Created by Rao-Mengnan
 * on 2018/2/6.
 */
public class RecipientServer {
    private Log log = LogFactory.getLog(getClass());

    private SocketServer server;
    private Recipient recipient;

    public RecipientServer(SocketServer server, Recipient recipient) {
        ensureNotNull(recipient);
        ensureNotNull(server);

        this.recipient = recipient;
        this.server = server;
    }


    public void registerRecipient(String host, int port) throws InterruptedException {
        SocketClient client = new SocketClient
                .Builder(host, port)
                .setAction(
                        new Action2<ChannelHandlerContext, Message>() {
                            @Override
                            public void doAction(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
                                log.info(String.format("Recipient register finished, %s", message));
                                channelHandlerContext.close();
                            }
                        },
                        new Action2<ChannelHandlerContext, Throwable>() {
                            @Override
                            public void doAction(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                                log.warn("Recipient register failed", throwable);
                                channelHandlerContext.close();
                            }
                        }).build();
        Message message = new Message(MessageTypeEnum.RECIPIENT_REGISTER.value());
        message.setContent(new Gson().toJson(recipient).getBytes());
        client.newConnect()
                .sync().channel().writeAndFlush(message)
                .channel().closeFuture().sync();

    }

    public SocketServer getServer() {
        return server;
    }
}
