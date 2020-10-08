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
package cn.laeni.sconf.server.controller

import cn.laeni.sconf.core.Result
import cn.laeni.sconf.server.controller.command.CreateClientCommand
import cn.laeni.sconf.server.controller.vo.ClientEntityBaseInfoVO
import cn.laeni.sconf.server.controller.vo.ConfDataEntityVO
import cn.laeni.sconf.server.service.ClientManageService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Web管理页相关接口.
 *
 * @author Laeni
 */
@RestController
@RequestMapping("/api/manage")
class WebManageController(
    private val clientManageService: ClientManageService
) {
  /**
   * 获取所有应用列表的基本信息.
   * @return 所有应用的基本信息.
   */
  @GetMapping("/client")
  fun allClientBaseInfo(): Result<Collection<ClientEntityBaseInfoVO>> {
    return Result(ClientEntityBaseInfoVO.toVo(clientManageService.getAllClient()))
  }

  /**
   * 创建新客户端应用.
   * @return 返回新创建后的客户端实例的基本信息
   */
  @PutMapping("/client")
  fun createClient(@RequestBody @Validated command: CreateClientCommand): Result<ClientEntityBaseInfoVO> {
    return Result(ClientEntityBaseInfoVO.toVo(clientManageService.createClient(command)))
  }

  /**
   * 删除应用.
   */
  @DeleteMapping("/client")
  fun deleteClient(id: Int): Result<Unit> {
    clientManageService.deleteClient(id)
    return Result()
  }
  // 修改应用

  // 获取某个应用的配置列表(仅列表的基本信息,不包好配置本身)
  @GetMapping("/client/conf_list")
  fun clientConfList(id: Int): Result<Collection<ConfDataEntityVO>> {
    return Result(ConfDataEntityVO.toVo(clientManageService.getClientConfList(id)))
  }

  // 创建某个应用的配置
  // 修改应用配置(传入需修改的字段进行按需修改)
  // 删除应用的配置
  // 获取某个配置的具体内容(配置本身)
}
