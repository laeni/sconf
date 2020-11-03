import { BehaviorSubject, Subscriber, Subscription } from 'rxjs';

/**
 * 第一次订阅时执行初始化操作.
 */
export class InitSubject<T> extends BehaviorSubject<T> {
  /**
   * 是否为第一次
   */
  private first = true;

  constructor(initValue: T, private init: () => void = () => { }) {
    super(initValue);
  }

  /** @deprecated This is an internal implementation detail, do not use. */
  _subscribe(subscriber: Subscriber<T>): Subscription {
    if (this.first) {
      this.first = false;
      if (typeof this.init === 'function') {
        this.init();
      }
    }
    return super._subscribe(subscriber);
  }

}
