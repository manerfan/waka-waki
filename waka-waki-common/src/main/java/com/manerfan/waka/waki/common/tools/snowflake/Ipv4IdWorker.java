package com.manerfan.waka.waki.common.tools.snowflake;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.vavr.control.Try;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

/**
 * Ipv4IdGenerator
 *
 * @author Maner.Fan
 * @date 2020/9/6
 */
public class Ipv4IdWorker implements IdGenerator {
    private static final int IPV4_SEG_LEN = 4;
    private static final int IPV4_SEG_BITS = 8;
    private static final String IPV4_DELIMITER = ".";
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(\\d){1,3}(\\.(\\d){1,3}){3}$");

    private final IdWorker idWorker;

    private static final Ipv4IdWorker INSTANCE = new Ipv4IdWorker();

    public static Ipv4IdWorker singleInstance() {
        return INSTANCE;
    }

    private Ipv4IdWorker() {
        idWorker = ipv4Address()
            .flatMap(Ipv4IdWorker::ipv4ToLong)
            .map(IdWorker::new)
            .orElseThrow(() -> new IllegalStateException("Cannot Found a valid IPv4 HostAddress!"));
    }

    @Override
    public IdData nextId() {
        return idWorker.nextId();
    }

    /**
     * 获取首个非回环ip
     */
    private Optional<String> ipv4Address() {
        return Try.of(NetworkInterface::getNetworkInterfaces)
            .map(this::enumeration2Stream)
            .map(networkInterfaceStream ->
                networkInterfaceStream.map(NetworkInterface::getInetAddresses)
                    .map(this::firstUnLoopbackAddress)
                    .filter(StringUtils::isNotBlank)
                    .findFirst()
            )
            .getOrElse(Optional.empty());
    }

    /**
     * 将 {@link Enumeration} 转为 {@link Stream}
     *
     * @param enumeration {@link Enumeration}
     * @param <T>         {@link T}
     * @return Stream
     */
    private <T> Stream<T> enumeration2Stream(Enumeration<T> enumeration) {
        if (Objects.isNull(enumeration)) {
            return Stream.empty();
        }

        val iterable = Spliterators.spliteratorUnknownSize(enumeration.asIterator(), Spliterator.ORDERED);
        return StreamSupport.stream(iterable, false);
    }

    /**
     * 在地址组内返回首个非回环（且IPv4）地址
     *
     * @param inetAddresses 地址组
     * @return 首个非回环地址 or null
     */
    private String firstUnLoopbackAddress(Enumeration<InetAddress> inetAddresses) {
        return enumeration2Stream(inetAddresses)
            .map(inetAddress -> {
                // 非回环地址 && IPv4格式
                val unLoopbackIPv4Address = !inetAddress.isLoopbackAddress()
                    && IPV4_PATTERN.matcher(inetAddress.getHostAddress()).matches();
                return unLoopbackIPv4Address ? inetAddress.getHostAddress() : null;
            })
            .filter(StringUtils::isNotBlank)
            .findFirst()
            .orElse(null);
    }

    /**
     * 字符型IPV4（127.0.0.1）转为Long
     *
     * @param ipv4 字符型IPv4地址
     * @return Optional Long
     * @see #longToIPv4(Long)
     */
    static Optional<Long> ipv4ToLong(String ipv4) {
        if (StringUtils.isBlank(ipv4)) {
            return Optional.empty();
        }

        val segments = StringUtils.split(ipv4, IPV4_DELIMITER, IPV4_SEG_LEN);
        if (segments.length < IPV4_SEG_LEN) {
            return Optional.empty();

        }

        return Try.of(() -> Optional.of(
            IntStream.rangeClosed(0, IPV4_SEG_LEN - 1)
                // 左移
                .mapToObj(i ->
                    Long.parseLong(segments[i]) << ((IPV4_SEG_LEN - 1 - i) * IPV4_SEG_BITS)
                )
                // 或运算
                .reduce(0L, (pre, post) ->
                    pre | post
                )
            )
        ).getOrElse(Optional.empty());
    }

    /**
     * Long型IPV4转为字符（127.0.0.1）
     *
     * @param ipv4 Long型IPV4
     * @return Optional String
     * @see #ipv4ToLong(String)
     */
    static Optional<String> longToIPv4(Long ipv4) {
        if (Objects.isNull(ipv4) || ipv4 < 0) {
            return Optional.empty();
        }

        // 0000_0000 . 0000_0000 . 0000_0000 . 1111_1111
        val mask = 0x00_00_00_FFL;
        return Try.of(() -> Optional.of(
            IntStream.rangeClosed(0, IPV4_SEG_LEN - 1)
                .boxed()
                .sorted(Collections.reverseOrder())
                // 计算各段
                .mapToLong(i -> (ipv4 >> (i * IPV4_SEG_BITS)) & mask)
                .mapToObj(Objects::toString)
                // 拼接
                .collect(Collectors.joining(IPV4_DELIMITER))
            )
        ).getOrElse(Optional.empty());
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(0, IPV4_SEG_LEN - 1).forEach(System.out::println);
    }
}
