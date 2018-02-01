package cn.atomicer.skmq.sdk.functions;

/**
 * Created by Rao-Mengnan
 * on 2018/1/30.
 */
public interface Action<T> {
    void doAction(T key);
}
