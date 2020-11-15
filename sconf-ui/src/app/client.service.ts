import { Injectable } from '@angular/core';
import { EMPTY, Observable } from 'rxjs';
import { classToClass } from 'class-transformer';
import url, { ResponseHandle } from '../url';
import { HttpClient } from '@angular/common/http';
import { NzMessageService } from 'ng-zorro-antd/message';
import CommonResult from '../declare/response';
import { deleteArrayChild } from '../utlis';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { NgProgress } from 'ngx-progressbar';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  /**
   * 缓存所有客户端数据.
   */
  private clients: ClientInfo[];
  /**
   * 缓存所有数据.
   */
  private datas: Array<{ id: string; value: string; }> = [];

  constructor(
      private http: HttpClient,
      private message: NzMessageService
  ) { }

  /**
   * 获取全部客户端基本数据.
   */
  async getAllClient(): Promise<ClientInfo[]> {
    if (this.clients) {
      return this.clients.map(client => classToClass(client));
    } else {
      await this.loadAllClient();
      return this.clients.map(client => classToClass(client));
    }
  }

  /**
   * 获取Id对应的客户端数据.
   * @param id 客户端id
   */
  async getClientFull(id: number): Promise<ClientInfo> {
    // 如果基本客户端列表不存在则优先加载客户端列表
    if (!this.clients) {
      await this.loadAllClient();
    }
    const client = this.clients.find(value => value.id === id);

    // 如果详细数据没有加载则加载详细数据
    if (client && (!client.menus || !client.confs)) {
      // tslint:disable-next-line:max-line-length
      const clientInfo = await ResponseHandle.handle1(this.http.get<CommonResult<ClientInfo>>(`${ url.clientInfo }?id=${ client.id }`), this.message);
      client.menus = clientInfo.menus;
      client.confs = clientInfo.confs;
    }

    return classToClass(client);
  }

  /**
   * 添加新客户端.
   *
   * @param newClient 添加新客户端所必须的属性
   * @return 返回添加成功后的客户端
   */
  async addClient(newClient: NewClient): Promise<ClientInfo> {
    const value = await ResponseHandle.handle1(this.http.put<CommonResult<ClientInfo>>(url.client, newClient), this.message);
    this.clients.push(value);
    return classToClass(value);
  }

  /**
   * 请求服务端删除指定的客户端应用.
   *
   * @param client 即将删除的客户端应用实例
   */
  async deleteClient(client: ClientInfo): Promise<void> {
    await ResponseHandle.handle1(this.http.delete<CommonResult<void>>(url.client + ('?id=' + client.id)), this.message);
    // 将服务器端删除的元素从本地缓存中移出
    deleteArrayChild(this.clients, target => target.id === client.id);
  }

  /**
   * 创建菜单组(配置集分组)或菜单(包含该菜单关联的配置集).
   *
   * @param clientId 客户端Id
   * @param menu     新菜单
   * @param conf 菜单关联的配置集
   */
  async createMenuOrconf(clientId: number, menu: { title: string; parentId: number; }, conf?: { type: ConfType; }): Promise<ClientMenu> {
    const body: { [key: string]: any } = { clientId, ... menu };
    if (conf) {
      body.conf = conf;
    }
    // tslint:disable-next-line:max-line-length
    const newMenu = await ResponseHandle.handle1(this.http.post<CommonResult<ClientMenu>>(url.clientMenu, body), this.message);
    // 缓存新数据
    const clientInfo = this.clients.find(value => value.id === clientId);
    if (!clientInfo.menus) {
      clientInfo.menus = [];
    }
    clientInfo.menus.push(newMenu);
    // 如果创建的是配置数据,则需要把对应的配置数据拉取回来
    if (newMenu.confId) {
      if (!clientInfo.confs) {
        clientInfo.confs = [];
      }
      clientInfo.confs.push(await this.getClientConf(newMenu.confId));
    }
    return classToClass(newMenu);
  }

  async deleteMenu(clientId, menuId): Promise<void> {
    // tslint:disable-next-line:max-line-length
    await ResponseHandle.handle1(this.http.delete<CommonResult<void>>(`${ url.clientMenu }?clientId=${ clientId }&menuId=${ menuId }`), this.message);

    // 删除缓存新数据
    const clientInfo = this.clients.find(value => value.id === clientId);
    for (let i = 0; i < clientInfo.menus.length; i++) {
      if (clientInfo.menus[i].id === menuId) {
        clientInfo.menus.splice(i, 1);
        break;
      }
    }
  }

  /**
   * 获取数据.
   * 该数据可以是真实的配置数据,也可能是示例数据.
   *
   * @param id 数据Id
   */
  async getData(id: string): Promise<string> {
    let data = this.datas.find(value => value.id === id);
    if (!data) {
      // 异步加载并缓存
      data = { id, value: await ResponseHandle.handle1(this.http.get<CommonResult<string>>(url.data, { params: { id } }), this.message) };
      this.datas.push(data);
    }
    return data.value;
  }

  /**
   * 局部修改配置.
   */
  async patchClientConf(command: PatchConfCommand): Promise<Conf> {
    return await ResponseHandle.handle1(this.http.patch<CommonResult<Conf>>(url.clientconf, command), this.message);
  }

  /**
   * 从服务端获取客户端配置数据.
   * 获取的配置数据并未写入与本地内存缓存中.
   * @param confId 配置数据Id
   * @private 获取的配置数据并未写入与本地内存缓存中,如果需要公开,则需要考虑缓存的问题
   */
  private async getClientConf(confId: number): Promise<Conf> {
    return await ResponseHandle.handle1(this.http.get<CommonResult<Conf>>(`${ url.clientconf }?confId=${ confId }`), this.message);
  }

  // 加载所有客户端
  private async loadAllClient(): Promise<void> {
    this.clients = await ResponseHandle.handle1(this.http.get<CommonResult<ClientInfo[]>>(url.clientAllBase), this.message);
  }
}

/**
 * 客户端.
 */
export class ClientInfo {
  /**
   * 自增Id.
   */
  id: number;

  /**
   * 客户端名称.
   */
  name: string;

  /**
   * 客户端描述说明.
   */
  desc: string;

  /**
   * 创建时间
   */
  createTime: string;

  /**
   * 更新时间
   */
  updateTime: string;

  /**
   * 本客户端应用下的所有配置集合.
   */
  menus?: ClientMenu[];

  /**
   * 本客户端应用所有配置.
   */
  confs?: Conf[];
}

/**
 * 配置页面左侧菜单.
 */
export class ClientMenu {
  /**
   * 自增Id.
   */
  id: number;

  /**
   * 父菜单id.
   *
   * ```null```表示顶级菜单.
   */
  parentId: number;

  /**
   * 菜单名称.
   */
  title: string;

  /**
   * 菜单优先级.
   */
  priority: number;

  /**
   * 该菜单对应的配置内容Id(如果该菜单不表示配置时则为空).
   */
  confId: number;

  /**
   * 该配置所属的客户端Id.
   */
  clientId: number;

  /**
   * 创建时间
   */
  createTime: string;

  /**
   * 更新时间
   */
  updateTime: string;
}

/**
 * 一个配置集合.
 * 可是一个yaml文件
 */
export class Conf {
  /**
   * 自增Id.
   */
  id: number;

  /**
   * 具体的配置内容Id.
   */
  contextId: string;

  /**
   * 示例配置数据Id.
   */
  exampleId: string;

  /**
   * 是否启用.
   */
  enable: boolean;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  type: ConfType;

  /**
   * 该配置所在客户端Id.
   */
  clientId: number;

  /**
   * 创建时间
   */
  createTime: string;

  /**
   * 更新时间
   */
  updateTime: string;
}

/**
 * 可能的配置类型.
 */
export type ConfType = 'PROP' | 'YAML' | 'ENUM' | 'FILE';

/**
 * 添加的新客户端.
 */
export interface NewClient {
  /**
   * 客户端名称.
   */
  name: string;

  /**
   * 客户端描述说明.
   */
  desc: string;
}

/**
 * 局部修改配置.
 */
export interface PatchConfCommand {
  /**
   * 配置Id.
   * 用于指名需要修改的配置.
   */
  id: number;

  /**
   * 修改前的配置Id,用于实现乐观锁,防止多人同时修改.
   * 如果本身没有数据(刚刚创建)时可以为空.
   */
  oldContextId?: string;

  /**
   * 修改前的示例配置Id,用于实现乐观锁,防止多人同时修改.
   * 如果本身没有数据(刚刚创建)时可以为空.
   */
  oldExampleId?: string;

  // ---------------------------- 以下为需要修改的数据 ---------------------------

  /**
   * 具体配置内容数据.
   * 可以为空(刚创建时).
   */
  context?: string;

  /**
   * 示例配置数据.
   */
  example?: string;

  /**
   * 是否启用.
   */
  enable?: boolean;
}

// ------------------------------------------------------------

/**
 * 进入组件前加载组件id的基本数据.
 */
@Injectable({
  providedIn: 'root',
})
export class CrisisDetailResolverService implements Resolve<ClientInfo> {
  constructor(private clientService: ClientService, private router: Router, private readonly: NgProgress) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ClientInfo> | Promise<ClientInfo> | ClientInfo {
    const id = route.paramMap.get('id');
    // 如果id为空则转到首页
    if (!id) {
      this.router.navigate(['/']).then();
      return EMPTY;
    }

    // 获取客户端详细数据
    return this.clientService.getClientFull(Number(id)).then(crisis => {
      if (crisis) {
        return crisis;
      } else { // id not found
        this.router.navigate(['/']).then();
        return null;
      }
    })
        // 数据加载完成后结束进度条
        .finally(() => this.readonly.ref().complete());
  }
}
