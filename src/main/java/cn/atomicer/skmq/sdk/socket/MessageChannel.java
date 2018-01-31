package cn.atomicer.skmq.sdk.socket;

import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.functions.Function0;
import cn.atomicer.skmq.sdk.model.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public interface MessageChannel {
    int read(MessageDecoder messageDecoder) throws IOException;
    int write(Message message) throws IOException;
    int write(ByteBuffer buffer) throws IOException;
    boolean finishConnect() throws IOException;
    void close();

    void doAction(int ops);
    MessageChannel setOnError(Function0<Throwable> onReadError, Function0<Throwable> onWriteError);

    Interaction getInteraction();
    SocketChannel getChannel();
}
