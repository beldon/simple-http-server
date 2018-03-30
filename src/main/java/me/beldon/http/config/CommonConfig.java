package me.beldon.http.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Beldon
 * @create 2018-03-30 下午3:53
 */
@Component
@ConfigurationProperties(prefix = "http")
@Data
public class CommonConfig {
    private int port = 8080;
}
