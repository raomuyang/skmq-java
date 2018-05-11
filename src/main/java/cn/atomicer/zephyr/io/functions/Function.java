package cn.atomicer.zephyr.io.functions;

/**
 * @author Rao-Mengnan
 *         on 2018/1/30.
 */
public interface Function<T, R> {
    R apply(T key) throws Exception;
}
