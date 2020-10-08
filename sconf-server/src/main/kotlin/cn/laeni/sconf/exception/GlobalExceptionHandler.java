/*
 * Copyright 2020-present m@laeni.cn
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.laeni.sconf.exception;

import cn.laeni.personal.exception.*;
import cn.laeni.sconf.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 全局统一异常捕获.
 * <p> 这里为了不影响原来的,所以将指定只捕获指定包下的Controller,其他将不受影响
 * <p>
 * Created by DaiWang on 2019/8/8.
 * </p>
 *
 * @author DaiWang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 以该前缀开头的表示为api调用,而非静态资源.
   */
  private static final String API_URL_PREFIX = "^/api/.*";

  /**
   * 200-业务逻辑异常.
   */
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(ServiceException.class)
  public Result<Void> serviceException(ServiceException e) {
    if (e.isPrintStackTrace()) {
      log.warn("业务逻辑异常: {} - {}", e.getMessage(), e.getErrorDesc(), e);
    } else {
      log.warn("业务逻辑异常: {} - {}", e.getMessage(), e.getErrorDesc());
    }
    return new Result<>(e);
  }

  /**
   * 400-参数解析失败.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Result<Void> mismatchedInputException() {
    log.warn("参数解析失败");
    ErrorInfo error = ClientErrorCode.PARAM_ERROR;
    return new Result<>(error);
  }

  /**
   * 400-客户端错误{@link ClientException}
   * <p>
   * 用户操作引发的异常不需要打印堆栈信息
   * </p>
   *
   * @param e 参数为捕获的异常,其中包含错误码即错误描述信息的异常对象
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ClientException.class)
  public Result<Void> userExceptionHandler(ClientException e) {
    if (e.isPrintStackTrace()) {
      log.warn("{} - {}", e.getMessage(), e.getErrorDesc(), e);
    } else {
      log.warn("{} - {}", e.getMessage(), e.getErrorDesc());
    }
    return new Result<>(e);
  }

  /**
   * 400-@Valid参数验证失败.
   * <p>
   * 如果是定义的Bean启用校验,并且不通过时会抛出{@link MethodArgumentNotValidException}异常
   * 如果Controller参数使用接口进行申明,并且使用@Validated进行验证时可能引发{@link BindException}异常.
   * </p>
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public Result<Void> methodArgumentNotValidExceptionHandler(Exception e) {
    String errorMessage = null;

    if (e instanceof MethodArgumentNotValidException) {
      BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
      for (FieldError error : bindingResult.getFieldErrors()) {
        errorMessage = error.getDefaultMessage();
        break;
      }
    } else {
      BindingResult errors = ((BindException) e).getBindingResult();
      for (ObjectError error : errors.getAllErrors()) {
        errorMessage = error.getDefaultMessage();
        break;
      }
    }

    ClientErrorCodeMark error = ClientErrorCode.PARAM_ERROR
        .as(Optional.ofNullable(errorMessage).orElse(ClientErrorCode.PARAM_ERROR.getErrorDesc()));
    return new Result<>(error);
  }

  /**
   * 400-@RequestParam参数验证失败.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> missingServletRequestParameterException() {
    ErrorInfo error = ClientErrorCode.PARAM_ERROR.as("必要参数不能为空");
    return new Result<>(error);
  }

  // 401-未授权

  // 403-无权访问.

  /**
   * 404-资源不存在.
   * 默认如果有"/api/"前缀则当做api请求处理,否则返回"index.html"
   * 需要开启: spring.mvc.throw-exception-if-no-handler-found: true
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Result<Void>> noHandlerFoundException(HttpServletRequest request, HttpServletResponse response) {
    log.warn("404: {}", request.getRequestURI());
    if (request.getServletPath().matches(API_URL_PREFIX)) {
      response.setContentType("application/json;charset=UTF-8");
      ErrorInfo error = SystemErrorCode.API_NOT_FOND;
      return new ResponseEntity<>(new Result<>(error), HttpStatus.NOT_FOUND);
    } else {
      try {
        request.getRequestDispatcher("/index.html").forward(request, response);
        return null;
      } catch (Exception e) {
        return new ResponseEntity<>(exceptionHandler(e), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  /**
   * 405-请求方法不支持.
   */
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Result<Void> httpExceptionHandler() {
    log.warn("Http请求类型不支持异常");
    ErrorInfo error = SystemErrorCode.HTTP_NOT_SUPPORTED;
    return new Result<>(error);
  }

  /**
   * 500-系统错误(未知).
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public Result<Void> exceptionHandler(Exception e) {
    log.error("系统异常", e);
    ErrorInfo error = SystemErrorCodeMark.Code.INTERNAL_ERROR;
    return new Result<>(error);
  }

  /**
   * 500-系统错误(已知).
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(SystemException.class)
  public Result<Void> systemExceptionHandler(SystemException e) {
    log.error(e.getMessage(), e);
    return new Result<>(e);
  }

  /**
   * 500-系统错误(已知).
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = MultipartException.class)
  public Result<Void> exceptionHandler(MultipartException e) {
    log.error("系统异常", e);
    SystemErrorCodeMark error = SystemErrorCodeMark.Code.INTERNAL_ERROR.as("请求失败，请检查参数是否正确（如上传的文件大小是否符合要求等）");

    return new Result<>(error);
  }

}
