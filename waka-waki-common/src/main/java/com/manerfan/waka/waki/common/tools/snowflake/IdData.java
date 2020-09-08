package com.manerfan.waka.waki.common.tools.snowflake;

import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

/**
 * IdData
 *
 * @author Maner.Fan
 * @date 2020/9/4
 */
@ToString
@EqualsAndHashCode
public class IdData {
    /**
     * 高64位
     */
    private final Long high;

    /**
     * 低64位
     */
    private final Long low;

    private transient String hex;
    private transient WorkerData workerData;

    private static final Random RANDOM = new Random();
    private static final String[] RANDOM_CHARS = IntStream.concat(
        IntStream.rangeClosed('0', '9'), IntStream.rangeClosed('a', 'z'))
        .mapToObj(c -> String.valueOf((char)c))
        .collect(Collectors.toList())
        .toArray(new String[] {});

    @Builder
    public IdData(Long high, Long low) {
        this.high = high;
        this.low = low;
    }

    /**
     * 转16进制字符串
     *
     * @return 16进制字符串
     */
    public String toHex() {
        if (StringUtils.isBlank(hex)) {
            hex = hex();
        }
        return hex;
    }

    /**
     * 转worker data，记录更详细信息
     *
     * @return {@link WorkerData}
     */
    public WorkerData toWorkerData() {
        if (Objects.isNull(workerData)) {
            workerData = workerData();
        }
        return workerData;
    }

    private String hex() {
        val highHex = StringUtils.leftPad(Long.toString(high, 16), IdWorker.HIGH_CHARS_NUM, '0');
        val lowHex = StringUtils.leftPad(Long.toString(low, 16), IdWorker.LOW_CHARS_NUM, '0');
        return String.format("1%s%s%s", highHex, lowHex, RANDOM_CHARS[RANDOM.nextInt(RANDOM_CHARS.length)]);
    }

    private WorkerData workerData() {
        long timestamp = high + IdWorker.TWEPOCH;
        long workerId = low >> IdWorker.WORKER_ID_SHIFT;
        long sequence = low & IdWorker.SEQUENCE_MASK;

        return WorkerData.builder().timestamp(timestamp).workId(workerId).sequence(sequence).build();
    }

    /**
     * 反解
     *
     * @param idHex 16进制格式的 Id Data
     * @return {@link IdData}
     */
    public static IdData parse(String idHex) {
        val sepIndex = idHex.length() - IdWorker.LOW_CHARS_NUM - 1;
        long high = Long.parseLong(idHex.substring(1, sepIndex), 16);
        long low = Long.parseLong(idHex.substring(sepIndex, idHex.length() - 1), 16);
        return IdData.builder().high(high).low(low).build();
    }
}
