package com.manerfan.waka.waki.common.tools.snowflake;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Worker
 *
 * @author Maner.Fan
 * @date 2020/9/4
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class WorkerData {
    /**
     * 时间序列
     */
    private final Long timestamp;

    /**
     * 机器码
     */
    private final Long workId;

    /**
     * 并发序列
     */
    private final Long sequence;
}
