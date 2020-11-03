export default class CommonResult<T> {
  /**
   * 状态码.
   * 成功返回 "OK".
   */
  code: string;

  /**
   * 状态描述.
   */
  message: string;

  /**
   * 可选的返回数据.
   */
  data: T;

  /**
   * 请求Id.
   */
  requestId: string;
}
