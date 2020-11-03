import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
// 这里导入 switchMap 操作符是因为你稍后将会处理路由参数的可观察对象 Observable
import { ClientInfo, ClientMenu, ClientService, Conf, ConfType, PatchConfCommand } from '../client.service';
import { ClientGuard } from './client.guard';
import { NzResizeEvent } from 'ng-zorro-antd/resizable';
import { NzContextMenuService, NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { classToClass, plainToClass } from 'class-transformer';
import { NzCascaderOption } from 'ng-zorro-antd/cascader/typings';
import { deleteArrayChild, deleteChildrenItem } from '../../utlis';
import { JoinedEditorOptions } from 'ng-zorro-antd/code-editor/typings';
import { editor } from 'monaco-editor';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.less']
})
export class ClientComponent implements OnInit {

  constructor(
      private route: ActivatedRoute,
      private clientGuard: ClientGuard,
      private nzContextMenuService: NzContextMenuService,
      private clientService: ClientService,
      private fb: FormBuilder
  ) { }
  client: ClientInfoEx;
  // 当前右键菜单点击的菜单
  selectMenu: MenuEx;

  // 代码编辑器实例,初始化后获得
  editorInstall: editor.IStandaloneCodeEditor;
  // 侧边栏宽度(可调整)
  siderWidth = 210;
  // 添加分组Modal是否打开
  createMenuGroupVisible = false;

  // 创建配置Modal是否打开
  createConModalVisible = false;
  // 创建配置表单
  createConForm?: FormGroup;
  // 编辑器配置选项
  createConEditorOption: JoinedEditorOptions = {
    language: 'dart',
    lineNumbers: 'off',           // off: 取消行号
    minimap: { enabled: false }, // 关闭缩略图
    showFoldingControls: 'always'// 总是显示折叠空控件
  };

  /**
   * 根据类型反序列化数据.
   * @param data 字符串数据
   * @param type 类型
   */
  private static parseData(data: string, type: ConfType): any {
    if (type === 'YAML' || type === 'PROP') {
      return data;
    } else {
      return JSON.parse(data);
    }
  }

  ngOnInit(): void {
    this.route.data.subscribe((data: { clientInfo: ClientInfo }) => {
      this.client = ClientInfoEx.to(data.clientInfo);
      // 更新/设置标题
      this.clientGuard.setTitle(this.client.name);
    });
  }

  // 调整侧边栏大小
  onSideResize({ width, mouseEvent }: NzResizeEvent): void {
    cancelAnimationFrame(-1);
    requestAnimationFrame(() => {
      this.siderWidth = width;
      // 宽度过小直接隐藏
      if ((mouseEvent as MouseEvent).clientX < 40) {
        this.siderWidth = 0;
      }
    });
  }

  // 显示右键菜单
  contextMenu($event: MouseEvent, menuComponent: NzDropdownMenuComponent, menu: MenuEx = null): void {
    $event.stopPropagation();
    this.selectMenu = menu;
    this.nzContextMenuService.create($event, menuComponent);
  }

  // 添加分组提交按钮处理
  createMenuFormSubmit(menus: MenuEx[], title: string): void {
    this.clientService.createMenuOrconf(this.client.id, { title, parentId: menus?.length > 0 ? menus[menus.length - 1].id : null })
        .subscribe(value => {
          this.client.addMenu(value);
          this.createMenuGroupVisible = false;
        });
  }

  /**
   * 删除分组.
   */
  async deleteGroup(): Promise<void> {
    const selectMenu = this.selectMenu;
    await this.clientService.deleteMenu(this.client.id, selectMenu.id);
    // 删除原始数据
    deleteArrayChild(this.client.menus, target => target.id === selectMenu.id);
    // 删除生成的NZ菜单
    deleteChildrenItem(this.client.nzCascaderOption, o => o.value.id === selectMenu.id);
    // 删除递归渲染的UI菜单
    deleteChildrenItem(this.client.menusEx, target => target.id === selectMenu.id);
  }

  /// region 创建配置
  /**
   * 打开创建配置Modal框.
   * 第一次打开时进行初始化表单.
   */
  openCreateConModal(): void {
    if (!this.createConForm) {
      this.createConForm = this.fb.group({
        name: [null, [Validators.required]],
        group: [null],
        type: ['YAML'],
      });
    } else {
      // 非第一次则重置表单
      this.createConForm.reset();
    }
    this.createConModalVisible = true;
  }

  // 添加配置提交按钮处理
  createConfig(): void {
    const title = this.createConForm.controls.name.value;
    const group = this.createConForm.controls.group.value;
    const type = this.createConForm.controls.type.value;
    this.clientService.createMenuOrconf(this.client.id,
        { title, parentId: group?.length > 0 ? group[group.length - 1].id : null },
        { type }
    ).subscribe(value => {
      this.client.addMenu(value);
      // 成功后关闭表单
      this.createConModalVisible = false;
    });
  }

  /// endregion

  // 代码编辑器初始化完成事件
  editorInitialized(editorInstall: editor.IStandaloneCodeEditor): void {
    this.editorInstall = editorInstall;
  }

  /**
   * 配置菜单点击事件.
   *
   * @param menu 点击的菜单对象
   */
  async clickMenuHandle(menu: MenuEx): Promise<void> {
    // 从缓存中找到该菜单对应的配置数据
    let conf = this.client.confEx.find(v => v.id === menu.confId);
    if (!conf) {
      this.client.confEx.push(conf = await this.toConfEx(this.client.confs.find(data => data.id === menu.confId)));
    }

    // 配置编辑器语言
    if ('language' in this.createConEditorOption) {
      if (conf.type === 'YAML') {
        this.createConEditorOption.language = 'yaml';
      } else if (conf.type === 'PROP') {
        this.createConEditorOption.language = 'dart';
      }
    }

    // 设置选择标记
    this.client.confEx.forEach(v => v.selected = v === conf);
  }

  /**
   * 保存已经修改的内容.
   */
  async saveEditor(conf: ConfEx): Promise<void> {
    conf.submitting = true;

    const command: PatchConfCommand = { id: conf.id, enable: conf.enableEditor };
    // 实际配置内容
    if (conf.contextEditor !== conf.context) {
      command.oldContextId = conf.contextId;
      command.context = conf.contextEditor;
    }
    // TODO 实例配置内容
    const newConf = await this.toConfEx(await this.clientService.patchClientConf(command));

    // 将新增属性复制到原有属性中
    if (newConf.contextId) {
      conf.contextId = newConf.contextId;
      conf.context = newConf.context;
    }
    conf.enable = newConf.enable;

    conf.submitting = false;
  }

  private toConfEx(conf: Conf): Promise<ConfEx> {
    return new Promise(async resolve => {
      const confEx = new ConfEx(conf);
      if (conf.contextId) {
        confEx.context = ClientComponent.parseData(await this.clientService.getData(conf.contextId), conf.type);
        confEx.contextEditor = classToClass(confEx.context);
      }
      confEx.enableEditor = confEx.enable;
      if (conf.exampleId) {
        confEx.example = ClientComponent.parseData(await this.clientService.getData(conf.exampleId), conf.type);
      }
      resolve(confEx);
    });
  }
}

class ClientInfoEx extends ClientInfo {
  /**
   * 将平行的菜单结构转换为层级关系结构.
   */
  menusEx?: MenuEx[];
  /**
   * 用于缓存扩展后的配置实例.
   * 这里并不会一次性把所有的Conf转为ConfEx后添加到这里,
   * 而是需要的时候再进行转换并将结果作为一个缓存放在这里.
   */
  confEx: ConfEx[] = [];
  /**
   * Ant 级联选择所需的数据格式.
   */
  nzCascaderOption: NzCascaderOption[];

  public static to(item: ClientInfo): ClientInfoEx {
    const ex = plainToClass(ClientInfoEx, classToClass(item));
    // 将 MenuEx[] 结构转换为 NzCascaderOption[] 结构
    if (ex.menus) {
      ex.menusEx = MenuEx.tos(ex.menus);
      ex.nzCascaderOption = ClientInfoEx.toNzCascaderOption(ex.menusEx);
    }
    return ex;
  }

  public static tos(items: ClientInfo[]): ClientInfoEx[] {
    return items.map(v => ClientInfoEx.to(v));
  }

  // 将菜单转为NZ所需格式
  private static toNzCascaderOption(ms: MenuEx[]): NzCascaderOption[] {
    const options: NzCascaderOption[] = [];
    for (const m of ms) {
      // 如果不存在contextId则表示该菜单为一个菜单组(配置分组),即可以柏选项
      if (!m.confId) {
        options.push({
          label: m.title,
          value: m,
          children: m.children?.length > 0 ? this.toNzCascaderOption(m.children) : [],
          // 如果没有子元素或者子元素全部为配置数据则认为该节点是最后一个可选择元素
          isLeaf: m.children?.filter(v => !v.confId).length === 0
        });
      }
    }
    return options;
  }

  /**
   * 添加新菜单.
   * @param menu 新菜单
   */
  public addMenu(menu: ClientMenu): void {
    this.menus.push(menu);
    this.menusEx = MenuEx.tos(this.menus);
    this.nzCascaderOption = ClientInfoEx.toNzCascaderOption(this.menusEx);
  }
}

class MenuEx extends ClientMenu {
  /**
   * 树结构的菜单.
   */
  children: MenuEx[];

  public static tos(items: ClientMenu[]): MenuEx[] {
    // 创建一个副本并按照优先级进行排序
    const copyItem = classToClass(items).sort((a, b) => a.priority - b.priority);
    const toMenuEx = (ms: ClientMenu[]): MenuEx[] => {
      if (ms.length === 0) {
        return [];
      } else {
        return ms.map(m => {
          const ex = plainToClass(MenuEx, m);
          ex.children = toMenuEx(copyItem.filter(value => value.parentId === m.id));
          return ex;
        });
      }
    };
    // 过滤处父级菜单并转换为 MenuEx
    return toMenuEx(copyItem.filter(value => !value.parentId));
  }
}

/**
 * 根据配置Id获取具体具体数据并转为对应的对象类型.
 */
class ConfEx extends Conf {
  constructor(item: Conf) {
    super();
    Object.keys(item).forEach(key => this[key] = item[key]);
  }

  /**
   * 标识当前配置是否被选中.
   * 如果选中则会进行展示.
   */
  selected = false;

  /**
   * 真实配置数据.
   */
  context?: any;

  /**
   * 正在编辑中,但尚未保存的数据.
   */
  contextEditor?: any;

  /**
   * 是否启用,该值与开关进行双向绑定,开关变更时不会马上生效,需要点击"保存"后才生效.
   */
  enableEditor: boolean;

  /**
   * 数据提交中.
   */
  submitting = false;

  /**
   * 示例配置.
   */
  example?: any;

  /**
   * 检测是否已修改.
   * 编辑器内容与原始内容不一致或者该配置相关的开关等状态与原始不一致视为已修改.
   */
  checkEditor(): boolean {
    // 检测开关修改状态
    if (this.enableEditor !== this.enable) {
      return true;
    }
    // 如果没有和数据关联(刚刚创建),但是编辑器有数据即视为已经修改
    if (!this.contextId && this.contextEditor) {
      return true;
    }
    // 比较内容
    return this.contextId && this.context !== this.contextEditor;
  }
}
