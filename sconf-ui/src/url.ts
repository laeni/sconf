import { Observable } from 'rxjs';
import CommonResult from './declare/response';
import { NzMessageService } from 'ng-zorro-antd/message';

const contextPath = '/';

/**
 * 枚举所有url.
 */
export default {
  // [GET]获取全部客户端基本信息.
  clientAllBase: contextPath + 'api/manage/client/all_base',
  // [GET]获取客户端的详细数据
  clientInfo: contextPath + 'api/manage/client/info',
  // [PUT|DELETE|POST]客户端应用Api.
  client: contextPath + 'api/manage/client',
  // [PUT]添加客户端的配置或配置分组(菜单)
  clientMenu: contextPath + 'api/manage/client/menu',
  // [GET|PATCH]添加客户端的配置或配置分组(菜单)
  clientconf: contextPath + 'api/manage/client/conf_data',
  // [GET]添加客户端的配置或配置分组(菜单)
  data: contextPath + 'api/manage/client/data',
};

export class ResponseHandle {
  /**
   * 处理响应结果,如果有异常则自动进行提示.
   * <code><pre>
   *   return new Observable<T>(subscriber => {
   *      ResponseHandle.handle1(this.http.get<CommonResult<T>>(url.getLinksType), this.message).subscribe({
   *        next: value => {subscriber.next(value);subscriber.complete();},
   *        error: err => subscriber.error(err)
   *      });
   *    });
   * </pre></code>
   * @param observable 响应结果
   * @param message    NzMessageService
   */
  public static handle1<T>(observable: Observable<CommonResult<T>>, message: NzMessageService): Promise<T> {
    return new Promise<T>((resolve, reject) => observable.subscribe({
      next: value => {
        if (value.code === 'OK') {
          resolve(value.data);
        } else {
          if (value.message) {
            message.error(value.message);
          }
          reject(value);
        }
      },
      error: err => {
        message.error('网络异常,请稍候重试');
        console.error(err);
        reject(err);
      }
    }));
  }

  /**
   * 处理响应结果,如果有异常则自动进行提示.
   * @param observable 响应结果
   * @param message    NzMessageService
   */
  public static handle2(observable: Observable<CommonResult<void>>, message: NzMessageService): Promise<void> {
    return new Promise<void>((resolve, reject) => observable.subscribe({
      next: value => {
        if (value.code === 'OK') {
          resolve();
        } else {
          if (value.message) {
            message.error(value.message);
          }
          reject(value);
        }
      },
      error: err => {
        message.error('网络异常,请稍候重试');
        console.error(err);
        reject(err);
      }
    }));
  }
}
