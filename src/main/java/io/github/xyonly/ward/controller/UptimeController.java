package io.github.xyonly.ward.controller;

import io.github.xyonly.ward.dao.UptimeDto;
import io.github.xyonly.ward.service.UptimeService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;


/**
 * SetupController displays responses from rest API
 *
 * @author Rudolf Barbu
 * @version 1.0.0
 */
@Controller
@Mapping(value = "/api/uptime")
public class UptimeController {
    /**
     * Autowired UptimeService object
     * Used for getting uptime information
     */
    @Inject
    private UptimeService uptimeService;

    /**
     * Get request to display uptime information
     *
     * @return ResponseEntity to servlet
     */
    @Get
    @Mapping
    public Result<UptimeDto> getUptime() {
        return Result.succeed(uptimeService.getUptime());
    }
}