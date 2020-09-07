package com.manerfan.waka.waki.common.tools.snowflake;

/**
 * IdGenerator
 *
 * @author Maner.Fan
 * @date 2020/9/4
 */
public interface IdGenerator {
    /**
     * 生成
     *
     * @return {@link IdData}
     */
    IdData nextId();
}
