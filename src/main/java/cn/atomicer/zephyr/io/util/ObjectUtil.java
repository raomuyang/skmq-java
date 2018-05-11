package cn.atomicer.zephyr.io.util;

/**
 * Created by Rao-Mengnan
 * on 2018/2/7.
 */
public class ObjectUtil {

    public static void ensureNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("argument is null");
        }
    }
}
