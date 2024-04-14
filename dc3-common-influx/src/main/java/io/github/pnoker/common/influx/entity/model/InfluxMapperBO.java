package io.github.pnoker.common.influx.entity.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class InfluxMapperBO {

    private Instant time; //主键生成时间

    private String deviceId;

    private String pointId;

    private String originTime;

    private BigDecimal value;

    private BigDecimal rawValue;


    public InfluxMapperBO(Instant time, String deviceId, String pointId, String originTime, BigDecimal value, BigDecimal rawValue) {
        this.time = time;
        this.deviceId = deviceId;
        this.pointId = pointId;
        this.originTime = originTime;
        this.value = value;
        this.rawValue = rawValue;
    }
}
