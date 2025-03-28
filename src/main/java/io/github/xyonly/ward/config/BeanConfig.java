package io.github.xyonly.ward.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.view.thymeleaf.ThymeleafRender;
import oshi.SystemInfo;

/**
 * @author 倚栏听风
 * @date 2025-03-28 16:21
 */
@Configuration
public class BeanConfig {

    @Bean
    public void configure(ThymeleafRender render) {
        render.getProvider(); //:TemplateEngine
    }

    /**
     * @return SystemInfo object
     */
    @Bean
    public SystemInfo systemInfo() {
        return new SystemInfo();
    }
}
