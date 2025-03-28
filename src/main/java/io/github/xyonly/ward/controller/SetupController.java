package io.github.xyonly.ward.controller;


import io.github.xyonly.ward.dao.ResponseDto;
import io.github.xyonly.ward.dao.SetupDto;
import io.github.xyonly.ward.exception.ApplicationAlreadyConfiguredException;
import io.github.xyonly.ward.service.SetupService;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;

import java.io.IOException;

/**
 * SetupController displays responses from rest API
 *
 * @author Rudolf Barbu
 * @version 1.0.1
 */
@Valid
@Controller
@Mapping(value = "/api/setup")
public class SetupController {
    /**
     * Autowired SetupService object
     * Used for posting settings information in ini file
     */
    @Inject
    private SetupService setupService;

    /**
     * Posting setup info in database
     *
     * @param setupDto dto with data
     * @return ResponseEntity to servlet
     */
    @Post
    @Mapping
    public Result<ResponseDto> postSetup(@Body @Validated final SetupDto setupDto) throws IOException, ApplicationAlreadyConfiguredException {
        return Result.succeed(setupService.postSetup(setupDto));
    }
}