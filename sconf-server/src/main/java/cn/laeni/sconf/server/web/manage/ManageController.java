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
package cn.laeni.sconf.server.web.manage;

import cn.laeni.sconf.core.Result;
import cn.laeni.sconf.core.Storage;
import cn.laeni.sconf.server.service.ClientManageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Web管理页相关接口.
 *
 * @author Laeni
 */
@RestController
@RequestMapping("/api/manage")
public class ManageController {
  private final ClientManageService clientManageService;
  private final Storage storage;

  public ManageController(ClientManageService clientManageService, Storage storage) {
    this.clientManageService = clientManageService;
    this.storage = storage;
  }

  /**
   * 获取所有应用列表的基本信息.
   *
   * @return 所有应用的基本信息.
   */
  @GetMapping("/client/all_base")
  public Result<Collection<ClientBaseVO>> getAllClientBase() {
    return new Result<>(ClientBaseVO.toVo(clientManageService.getAllClient()));
  }

  /**
   * 获取某个应用的详细信息(包含配置列表和菜单列表).
   *
   * @param id 客户端Id
   */
  @GetMapping("/client/info")
  public Result<ClientInfoVO> getClientInfo(Integer id) {
    return new Result<>(ClientInfoVO.toVo(clientManageService.getClient(id)));
  }

  /**
   * 创建新客户端应用.
   *
   * @return 返回新创建后的客户端实例的基本信息
   */
  @PutMapping("/client")
  public Result<ClientBaseVO> createClient(@RequestBody @Validated CreateClientCommand command) {
    return new Result<>(ClientBaseVO.toVo(clientManageService.createClient(command)));
  }

  /**
   * 删除应用.
   */
  @DeleteMapping("/client")
  public Result<Void> deleteClient(Integer id) {
    clientManageService.deleteClient(id);
    return new Result<>();
  }
  // 修改应用

  /**
   * 创建客户端菜单(菜单可能是一个菜单组,也可能是一个拥有配置数据的菜单项).
   *
   * @param command 待添加的配置或配置分组
   */
  @PostMapping("/client/menu")
  public Result<ClientMenuVO> createMenu(@RequestBody @Validated CreateMenuCommand command) {
    return new Result<>(ClientMenuVO.toVo(clientManageService.createMenu(command)));
  }

  /**
   * 删除某个应用的配置或配置分组(菜单).
   * 如果删除的是分组,并且分组下面有子菜单,则将子菜单移动到与该分组同级别下.
   *
   * @param clientId 待删除配置或分组的客户端
   * @param menuId   待删除的配置或分组Id
   * @return 返回被修改的配置或分组
   */
  @DeleteMapping("/client/menu")
  public Result<Void> removeMenu(Integer clientId, Integer menuId) {
    clientManageService.removeMenu(clientId, menuId);
    return new Result<>();
  }

  /**
   * 获根据id获取详细的配置.
   *
   * @param confId 配置Id
   */
  @GetMapping("/client/conf_data")
  public Result<ConfVO> getConfData(Integer confId) {
    return new Result<>(ConfVO.toVo(clientManageService.getConf(confId)));
  }

  /**
   * 修改应用配置(传入需修改的字段进行按需修改).
   */
  @PatchMapping("/client/conf_data")
  public Result<ConfVO> patchConfData(@Validated @RequestBody PatchConfCommand command) {
    return new Result<>(ConfVO.toVo(clientManageService.patchConfData(command)));
  }

  // 删除应用的配置
  /**
   * 获取数据.
   * 该数据可能是真实的配置数据,也可能是示例.
   */
  @GetMapping("/client/data")
  public Result<String> getData(String id) {
    return new Result<>(this.storage.get(id));
  }
}
