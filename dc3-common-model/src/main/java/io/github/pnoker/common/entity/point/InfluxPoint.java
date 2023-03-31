package io.github.pnoker.common.entity.point;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

@Measurement(name = "point_value")
@Data
public class InfluxPoint {

    @Column(tag = true)
    private String deviceId;

    @Column(tag = true)
    private String pointId;

    @Column
    private String pointValue;

    @Column(timestamp = true)
    private Instant time;

    public InfluxPoint(PointValue pointValue) {
        this.deviceId = pointValue.getDeviceId();
        this.pointId = pointValue.getPointId();
        this.pointValue = pointValue.getValue();
        this.time = pointValue.getCreateTime().toInstant();
    }
}
