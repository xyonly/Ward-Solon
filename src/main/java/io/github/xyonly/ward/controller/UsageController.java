package io.github.xyonly.ward.controller;


import io.github.xyonly.ward.dao.UsageDto;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import io.github.xyonly.ward.service.UsageService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;


/**
 * UsageController displays responses from rest API
 *
 * @author Rudolf Barbu
 * @version 1.0.1
 */
@Controller
@Mapping(value = "/api/usage")
public class UsageController {
    /**
     * Autowired UsageService object
     * Used for getting usage information
     */
    @Inject
    private UsageService usageService;

    /**
     * Get request to display current usage information for processor, RAM and storage
     *
     * @return ResponseEntity to servlet
     */
    @Get
    @Mapping
    public Result<UsageDto> getUsage() throws ApplicationNotConfiguredException {
        return Result.succeed(usageService.getUsage());
    }
}