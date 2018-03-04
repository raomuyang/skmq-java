package cn.atomicer.skmq.sdk.socket2;

import cn.atomicer.skmq.sdk.coding.MessageDecoder;
import cn.atomicer.skmq.sdk.coding.MessageEncoder;
import cn.atomicer.skmq.sdk.functions.Function0;
import cn.atomicer.skmq.sdk.model.Message;

/**
 * Created by Rao-Mengnan
 * on 2018/3/4.
 */
public interface CodecCreator<R> extends Function0<R> {

    CodecCreator<Message2BufEncoder<Message>> DEFAULT_ENCODER_CREATOR = new CodecCreator<Message2BufEncoder<Message>>() {
        @Override
        public Message2BufEncoder<Message> apply() throws Exception {
            return new Message2BufEncoder<>(new MessageEncoder());
        }
    };

    CodecCreator<Buf2MessageDecoder<Message>> DEFAULT_DECODER_CREATOR = new CodecCreator<Buf2MessageDecoder<Message>>() {
        @Override
        public Buf2MessageDecoder<Message> apply() throws Exception {
            return new Buf2MessageDecoder<>(new MessageDecoder());
        }
    };

}
