package cn.atomicer.skmq.sdk.coding;

/**
 * Created by Rao-Mengnan
 * on 2018/2/12.
 */
public interface Encoder<T> {
    byte[] encode(T t);
}
