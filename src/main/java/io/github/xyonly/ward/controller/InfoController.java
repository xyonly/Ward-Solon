package io.github.xyonly.ward.controller;


import io.github.xyonly.ward.dao.InfoDto;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import io.github.xyonly.ward.service.InfoService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

/**
 * InfoController displays responses from rest API, about server
 *
 * @author Rudolf Barbu
 * @version 1.0.1
 */
@Controller
@Mapping(value = "/api/info")
public class InfoController {
    /**
     * Autowired InfoService object
     * Used for getting information about server
     */
    @Inject
    private InfoService infoService;

    /**
     * Get request to display current usage information for processor, RAM and storage
     *
     * @return ResponseEntity to servlet
     */
    @Get
    @Mapping
    public Result<InfoDto> getInfo() throws ApplicationNotConfiguredException {
        return Result.succeed(infoService.getInfo());
    }
}