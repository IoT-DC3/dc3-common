package io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo;

import lombok.Data;

@Data
public class TopicVO {
    private String topic;
    private String deviceName;
    private String pointName;
}
