package com.manerfan.waka.waki.auth.support.dao.common.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

/**
 * BaseEntity
 *
 * @author Maner.Fan
 * @date 2020/9/8
 */
@Data
@MappedSuperclass
public class BaseEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @Column
    private LocalDateTime gmtModified;
}
