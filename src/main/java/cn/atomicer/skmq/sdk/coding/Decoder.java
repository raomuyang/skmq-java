package cn.atomicer.skmq.sdk.coding;

/**
 * Created by Rao-Mengnan
 * on 2018/2/12.
 */
public interface Decoder <T> {
    void write(byte[] bytes, int offset);
    T poolMessage();
}
