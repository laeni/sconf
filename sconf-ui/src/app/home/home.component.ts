import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientInfo, ClientService } from '../client.service';
import { deleteArrayChild } from '../../utlis';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less']
})
export class HomeComponent implements OnInit {
  title = '首页';

  /**
   * 缓存所有客户端数据.
   */
  clients: ClientInfo[];
  // 分组展示的客户端
  clientGroup: ClientInfo[][] = [];

  /// region 添加客户端
  // 添加客户端Modal框开启状态
  addClientModalVisible: boolean;
  // 添加客户端表单
  addClientForm: FormGroup;
  // 添加客户端按钮加载中状态
  addClientButtonLoading = false;

  /// endregion

  constructor(
      private fb: FormBuilder,
      private clientService: ClientService
  ) {}

  ngOnInit(): void {
    this.clientService.getAllClient().then(clients => {
      this.clients = clients;
      this.groupClient();
    });
  }

  /// region 客户端相关
  // 打开"添加客户端"弹窗
  openAddClientModal(): void {
    // 初始化表单对象
    if (!this.addClientForm) {
      this.addClientForm = this.fb.group({ name: [null, [Validators.required]], desc: [null, [Validators.required]] });
    }
    this.addClientModalVisible = true;
  }
  // 添加客户端
  addClientSubmit(): void {
    this.addClientButtonLoading = true;
    const name = this.addClientForm.controls.name.value;
    const desc = this.addClientForm.controls.desc.value;
    this.clientService.addClient({ name, desc })
        .then(value => {
          this.clients.push(value);
          // 重新进行分组显示
          this.groupClient();
          // 关闭Modal框
          this.addClientModalVisible = false;
          // 重置表单
          this.addClientForm.reset();
        })
        .finally(() => this.addClientButtonLoading = false);
  }
  // 删除客户端应用
  deleteClient(client: ClientInfo): void {
    this.clientService.deleteClient(client).then(() => {
      // 将服务器端删除的元素从本地缓存中移出
      deleteArrayChild(this.clients, target => target.id === client.id);
      // 重新进行分组显示
      this.groupClient();
    });
  }
  /// endregion

  // 拖拽排序
  drop(event: CdkDragDrop<ClientInfo[]>): void {
    console.log();
    // 找到两个元素的位置
    const previousIndex = this.findIndex(event.previousContainer.data[event.previousIndex]);
    const currentIndex = this.findIndex(event.container.data[event.currentIndex]);

    // 对原始数据进行重新排序
    moveItemInArray(this.clients, previousIndex, currentIndex);

    // 重新进行分组显示
    this.groupClient();
  }

  // 将所有客户端列表进行每n个进行一个分组
  private groupClient(n: number = 3): void {
    if (!this.clients) {
      return;
    }

    const groups: ClientInfo[][] = [];

    for (let i = 0; i < this.clients.length; i++) {
      // 找出本元素对应的分组
      let group: ClientInfo[];
      const index = Math.floor(i / n);
      if (groups.length <= index) {
        group = [];
        groups.push(group);
      } else {
        group = groups[index];
      }

      group.push(this.clients[i]);
    }

    this.clientGroup = groups;
  }

  // 从原始缓存集合中找到该元素所有位置的索引
  private findIndex(item: ClientInfo): number {
    for (let i = 0; i < this.clients.length; i++) {
      if (this.clients[i] === item) {
        return i;
      }
    }
    return 0;
  }
}
