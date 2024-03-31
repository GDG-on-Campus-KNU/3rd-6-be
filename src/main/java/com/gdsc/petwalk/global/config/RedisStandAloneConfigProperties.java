package com.gdsc.petwalk.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisStandAloneConfigProperties {

    String host;

    int port;
}
