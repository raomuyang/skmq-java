package cn.atomicer.zephyr.io.socket;

import cn.atomicer.zephyr.io.coding.MessageDecoder;
import cn.atomicer.zephyr.io.functions.Action;
import cn.atomicer.zephyr.io.model.Message;

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
    MessageChannel setOnError(Action<Throwable> onReadError, Action<Throwable> onWriteError);

    Dealing getDealing();
    SocketChannel getChannel();
}
