package org.example.expert.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource(value = "classpath:myInfo.yml", factory = YamlPropertySourceFactory.class)
public class MyInfoConfig {

    @Value("${db.user}")
    private String user;

    @Value("${db.name}")
    private String name;

    @Value("${db.pw}")
    private String pw;

    @Value("${jwt.secret.key}")
    private String secretKey;
}

