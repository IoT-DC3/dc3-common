package io.github.pnoker.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据统计DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataStatisticsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long driverCount;

    private Long profileCount;

    private Long pointCount;

    private Long deviceCount;

    private Long dataCount;
}
