import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanDeactivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Title } from '@angular/platform-browser';
import { NgProgress } from 'ngx-progressbar';

@Injectable({
  providedIn: 'root'
})
export class ClientGuard implements  CanActivate, CanDeactivate<unknown> {

  // 原始页面标题
  sourceTitle: string;

  constructor(private title: Title, private readonly: NgProgress) {
    this.sourceTitle = title.getTitle();
  }

  canActivate(
      next: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // 进入组件前结束进度条
    this.readonly.ref().start();
    return true;
  }

  canDeactivate(
      component: unknown,
      currentRoute: ActivatedRouteSnapshot,
      currentState: RouterStateSnapshot,
      nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.title.setTitle(this.sourceTitle);
    return true;
  }

  /**
   * 设置标题.
   *
   * @param title 指定组件/页面特有的标题
   */
  setTitle(title: string): void {
    this.title.setTitle(`${title} - ${this.sourceTitle}`);
  }
}
