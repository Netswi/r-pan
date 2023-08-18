package com.heisenberg.pan.swagger2;

import com.heisenberg.pan.core.constants.RPanConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2ConfigProperties {
    private boolean show = true;

    private String basePackge = RPanConstants.BASE_COMPONENT_SCAN_PATH;

    private String title = "server";

    private String description = "server";

    private String termsOfServiceUrl = "http://127.0.0.1:${server.port}";

    private String contactName = "heisenberg";

    private String version = "1.0";
}
