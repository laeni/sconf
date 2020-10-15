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
import cn.laeni.sconf.server.controller.command.AddMenuCommand
import cn.laeni.sconf.server.controller.command.CreateClientCommand
import cn.laeni.sconf.server.controller.vo.ClientEntityBaseInfoVO
import cn.laeni.sconf.server.controller.vo.MenuVO
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
  fun allClientBaseInfo(): Result<Collection<ClientEntityBaseInfoVO?>> {
    return Result(ClientEntityBaseInfoVO.toVo(clientManageService.getAllClient()))
  }

  /**
   * 创建新客户端应用.
   * @return 返回新创建后的客户端实例的基本信息
   */
  @PutMapping("/client")
  fun createClient(@RequestBody @Validated command: CreateClientCommand): Result<ClientEntityBaseInfoVO?> {
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

  /**
   * 获取某个应用的配置分组(菜单)列表(仅列表的基本信息,不包好配置本身).
   *
   * @param id 客户端Id
   */
  @GetMapping("/client/conf_list")
  fun clientConfList(id: Int): Result<Collection<MenuVO?>> {
    return Result(MenuVO.toVo(clientManageService.getClientConfList(id)))
  }

  /**
   * 创建某个应用的配置或配置分组(菜单).
   *
   * @param addMenuCommand 待添加的配置或配置分组
   */
  @PutMapping("/client/conf")
  fun addMenu(@RequestBody @Validated addMenuCommand: AddMenuCommand): Result<MenuVO> {
    return Result(MenuVO.toVo(clientManageService.addMenu(addMenuCommand))!!)
  }

  /**
   * 删除某个应用的配置或配置分组(菜单).
   * 如果删除的是分组,并且分组下面有子菜单,则将子菜单移动到与该分组同级别下.
   *
   * @param clientId 待删除配置或分组的客户端
   * @param menuId   待删除的配置或分组Id
   * @return 返回被修改的配置或分组
   */
  @DeleteMapping("/client/conf")
  fun removeMenu(clientId: Int, menuId: Int): Result<Unit> {
    clientManageService.removeMenu(clientId, menuId)
    return Result()
  }

  // 修改应用配置(传入需修改的字段进行按需修改)
  // 删除应用的配置
  // 获取某个配置的具体内容(配置本身)
}
