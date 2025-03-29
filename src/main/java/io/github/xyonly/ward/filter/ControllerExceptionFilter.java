package io.github.xyonly.ward.filter;


import io.github.xyonly.ward.component.UtilitiesComponent;
import io.github.xyonly.ward.dao.ErrorDto;
import io.github.xyonly.ward.exception.ApplicationAlreadyConfiguredException;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.*;
import org.noear.solon.validation.ValidatorException;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.common.exception.HttpException;

/**
 * 全局异常处理
 */

@Component
public class ControllerExceptionFilter implements Filter {
    /**
     * Inject UtilitiesComponent object
     * Used for various utility functions
     */
    @Inject
    private UtilitiesComponent utilitiesComponent;

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
        } catch (ApplicationNotConfiguredException | ApplicationAlreadyConfiguredException exception) {
            ctx.render(Result.failure(HttpStatus.BAD_REQUEST.value(), new ErrorDto(exception).toString()));
        } catch (ValidatorException exception) {
            ctx.render(Result.failure(HttpStatus.UNPROCESSABLE_ENTITY.value(), new ErrorDto(exception).toString()));
        } catch (Exception exception) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.put("theme", utilitiesComponent.getFromIniFile("theme"));
            if (exception instanceof HttpException) {
                HttpException httpException = (HttpException) exception;
                if (httpException.getHttpStatus() == HttpStatus.NOT_FOUND) {
                    modelAndView.view("error/404.html");
                }
            } else {
                modelAndView.view("error/500.html");
            }
            ctx.render(modelAndView);
        }
    }

}