package io.github.pnoker.common.influx.entity.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Measurement(name = "dc3")
@Data
public class InfluxMapperDO {

    @Column(timestamp = true)
    private Instant time; //主键生成时间

    @Column(name = "deviceId", tag = true)
    private String deviceId;

    @Column(name = "pointId", tag = true)
    private String pointId;

    @Column(name = "originTime", tag = true)
    private String originTime;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "field")
    private String field;


}
