package com.manerfan.waka.waki.common.tools.snowflake;

import lombok.Getter;
import lombok.ToString;

/**
 * Ipv4WorkerData
 *
 * @author Maner.Fan
 * @date 2020/9/7
 */
@ToString(callSuper = true)
public class Ipv4WorkerData extends WorkerData {
    @Getter
    private final String host;

    public static Ipv4WorkerData from(WorkerData workerData) {
        return new Ipv4WorkerData(workerData);
    }

    private Ipv4WorkerData(WorkerData workerData) {
        super(workerData.getTimestamp(), workerData.getWorkId(), workerData.getSequence());
        this.host = Ipv4IdWorker.longToIPv4(workerData.getWorkId())
            .orElseThrow(() -> new IllegalStateException("Cannot Parse a valid IPv4 HostAddress!"));
    }
}
