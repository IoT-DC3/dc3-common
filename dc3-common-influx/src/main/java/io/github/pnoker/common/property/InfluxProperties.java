package io.github.pnoker.common.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "influx")
public class InfluxProperties {

    @NotNull
    private String url;

    @NotNull
    private String organization;

    @NotNull
    private String bucket;

    private String token;

    private String username;

    private String password;

}
