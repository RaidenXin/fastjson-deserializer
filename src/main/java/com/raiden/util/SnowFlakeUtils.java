package com.raiden.util;

/**
 * 分布式雪花ID算法
 * 
 * @author zhi
 * @since 2019年5月14日16:51:06
 *
 */
public final class SnowFlakeUtils {
    /**
     * 起始的时间戳
     */
    private final static long TWEPOCH = 1557825652094L;

    /**
     * 每一部分占用的位数
     */
    private final static long WORKER_ID_BITS = 5L;
    private final static long DATACENTER_ID_BITS = 5L;
    private final static long SEQUENCE_BITS = 12L;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);

    private final static long DATACENTER_ID; // 数据中心ID
    private final static String WORKER_ID; // 机器ID
    private static long SEQUENCE; // 序列号
    private static long LAST_TIMESTAMP;// 上一次时间戳

    static{
        DATACENTER_ID = 1L;//这是1号，因为我们只有一个
        SEQUENCE = 0L;
        WORKER_ID = MacUtils.getMaoId();
        LAST_TIMESTAMP = -1L;
        if (DATACENTER_ID > MAX_DATACENTER_ID || DATACENTER_ID < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
    }

    public static synchronized String nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < LAST_TIMESTAMP) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", LAST_TIMESTAMP - timestamp));
        }
        if (timestamp == LAST_TIMESTAMP) {
            SEQUENCE = (SEQUENCE + 1) & MAX_SEQUENCE;
            if (SEQUENCE == 0L) {
                timestamp = tilNextMillis();
            }
        } else {
            SEQUENCE = 0L;
        }
        LAST_TIMESTAMP = timestamp;

        return (timestamp - TWEPOCH) // 时间戳部分
                + String.valueOf(DATACENTER_ID) // 数据中心部分
                + WORKER_ID// 机器标识部分
                + SEQUENCE; // 序列号部分
    }

    private static long tilNextMillis() {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= LAST_TIMESTAMP) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}