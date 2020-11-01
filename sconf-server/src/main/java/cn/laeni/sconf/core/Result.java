package cn.laeni.sconf.core;

import cn.laeni.personal.exception.ErrorInfo;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

/**
 * 统一封装接口返回数据格式
 *
 * @author laeni
 */
@NoArgsConstructor
public class Result<T> {
  /**
   * 状态码.
   */
  String code = "OK";

  /**
   * 状态描述.
   */
  String message;

  /**
   * 返回的具体数据.
   *
   * 只有成功时才可能会返回.
   * 某些成功的操作可能不会返回该结果.
   */
  T data;

  /**
   * 请求ID,只要api响应就一定会返回.
   */
  String requestId = MDC.get("transactionId");

  /**
   * 成功.并且包含业务返回数据.
   */
  public Result(T data) {
    this.data = data;
  }

  /**
   * 异常.
   *
   * @param errorInfo 具体异常描述
   */
  public Result(ErrorInfo errorInfo) {
    this.code = errorInfo.getErrorDesc();
    this.message = errorInfo.getErrorDesc();
  }
}
