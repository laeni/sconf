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
package cn.laeni.sconf.server.service

import cn.laeni.sconf.server.controller.command.CreateClientCommand
import cn.laeni.sconf.server.entity.ClientEntity
import cn.laeni.sconf.server.entity.ConfDataEntity

/**
 * 应用管理.
 *
 * @author Laeni
 */
interface ClientManageService {
  /**
   * 获取所有客户端.
   * @return 所有客户端的基本信息
   */
  fun getAllClient(): Collection<ClientEntity>

  /**
   * @param newClient 新创建客户端时初始化数据
   * @return 新创建的客户端信息
   */
  fun createClient(newClient: CreateClientCommand): ClientEntity

  /**
   * 删除Id对应的客户端应用.
   * @param id 即将删除的客户端应用的Id
   */
  fun deleteClient(id: Int)

  // --------------------------------------

  /**
   * 获取客户端的所有配置列表(不包含具体的配置内容,由于陪孩子内容过大,需要单独获取).
   */
  fun getClientConfList(id: Int): Collection<ConfDataEntity>
}
