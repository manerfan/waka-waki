package com.manerfan.waka.waki.common.tools.snowflake;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Ipv4IdWorkerTest
 *
 * @author Maner.Fan
 * @date 2020/9/7
 */
@DisplayName("基于IPv4的SnowFlake测试")
public class Ipv4IdWorkerTest {
    @Test
    public void ipv4IdWorkerTest() {
        val idData = Ipv4IdWorker.singleInstance().nextId();
        val hex = idData.toHex();
        val workerData = idData.toWorkerData();

        System.out.println(hex);
        System.out.println(workerData);

        val idDataParsed = IdData.parse(hex);
        val workerDataParsed = idDataParsed.toWorkerData();

        assertThat(idData).isEqualTo(idDataParsed);
        assertThat(workerData).isEqualTo(workerDataParsed);

        val ipv4Worker = Ipv4WorkerData.from(workerData);
        System.out.println(ipv4Worker);
    }

    @Test
    public void simplePerformanceTest() {
        int count = 10000;

        // 预热
        while (count-- > 0) {
            Ipv4IdWorker.singleInstance().nextId();
        }

        count = 50000;
        long start = System.currentTimeMillis();
        while (count-- > 0) {
            Ipv4IdWorker.singleInstance().nextId();
        }
        long end = System.currentTimeMillis();

        System.out.printf("=== Run %d times, total cost %dms, tps: %fms ===", 50000, end - start, (end - start) / 50000.0);

        count = 100;
        while (count-- > 0) {
            System.out.println(Ipv4IdWorker.singleInstance().nextId().toHex());
        }
    }
}
