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
package cn.laeni.sconf.server.service;

import cn.laeni.sconf.server.web.manage.CreateClientCommand;
import cn.laeni.sconf.server.web.manage.CreateMenuCommand;
import cn.laeni.sconf.server.web.manage.PatchConfCommand;
import cn.laeni.sconf.server.entity.ClientEntity;
import cn.laeni.sconf.server.entity.ClientMenuEntity;
import cn.laeni.sconf.server.entity.ConfEntity;

import java.util.Collection;

/**
 * 应用管理.
 *
 * @author Laeni
 */
public interface ClientManageService {
  /**
   * 获取所有客户端.
   *
   * @return 所有客户端的基本信息
   */
  Collection<ClientEntity> getAllClient();

  /**
   * 获取id对应的客户端.
   *
   * @param clientId 客户端Id
   * @return id对应的客户端
   */
  ClientEntity getClient(Integer clientId);

  /**
   * 获根据id获取详细的配置.
   *
   * @param confId 配置Id
   * @return id对应的配置数据详情.
   */
  ConfEntity getConf(Integer confId);

  /**
   * 创建一个新客户端应用.
   *
   * @param newClient 新创建客户端时初始化数据
   * @return 新创建的客户端信息
   */
  ClientEntity createClient(CreateClientCommand newClient);

  /**
   * 删除Id对应的客户端应用.
   *
   * @param id 即将删除的客户端应用的Id
   */
  void deleteClient(Integer id);

  /**
   * 添加配置或配置分组(菜单).
   *
   * @param createMenuCommand 新菜单信息
   * @return 已经创建的菜单对象
   */
  ClientMenuEntity createMenu(CreateMenuCommand createMenuCommand);

  /**
   * 删除配置或分组.
   * 如果是分组,并且子
   *
   * @param clientId 菜单所属的客户端Id
   * @param menuId   需要删除的客户端Id
   */
  void removeMenu(Integer clientId, Integer menuId);

  /**
   * 修改配置.
   *
   * @param command 新配置数据
   * @return 修改完成的配置
   */
  ConfEntity patchConfData(PatchConfCommand command);
}
