package cn.laeni.sconf.core

import cn.laeni.personal.exception.ErrorInfo
import org.slf4j.MDC

/**
 * 统一封装接口返回数据格式
 *
 * @author laeni
 */
data class Result<T>(
    /**
     * 状态码.
     */
    val code: String? = "OK",

    /**
     * 状态描述.
     */
    val message: String? = null,

    /**
     * 返回的具体数据.
     *
     *
     * 只有成功时才可能会返回.
     * 某些成功的操作可能不会返回该结果.
     *
     */
    val data: T? = null,

    /**
     * 请求ID,只要api响应就一定会返回.
     */
    val requestId: String? = MDC.get("transactionId")
) {
  /**
   * 成功.并且包含业务返回数据.
   */
  constructor(d: T): this(data = d)

  /**
   * 异常.
   *
   * @param errorInfo 具体异常描述
   */
  constructor(errorInfo: ErrorInfo): this(code = errorInfo.errorCode, message = errorInfo.errorDesc)
}
