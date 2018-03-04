package cn.atomicer.skmq.sdk.functions;

/**
 * @author Rao-Mengnan
 *         on 2018/1/30.
 */
public interface Action<T> {
    void doAction(T key);
}
