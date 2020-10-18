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
package cn.laeni.sconf.server.service.impl;

import cn.laeni.personal.exception.ClientException;
import cn.laeni.sconf.exception.ClientErrorCode;
import cn.laeni.sconf.server.controller.command.CreateClientCommand;
import cn.laeni.sconf.server.controller.command.CreateConfDataCommand;
import cn.laeni.sconf.server.controller.command.CreateMenuCommand;
import cn.laeni.sconf.server.entity.ClientEntity;
import cn.laeni.sconf.server.entity.ClientMenuEntity;
import cn.laeni.sconf.server.entity.ConfDataEntity;
import cn.laeni.sconf.server.repository.ClientEntityRepository;
import cn.laeni.sconf.server.repository.ConfDataEntityRepository;
import cn.laeni.sconf.server.repository.MenuEntityRepository;
import cn.laeni.sconf.server.service.ClientManageService;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Laeni
 */
@Service
public class ClientManageServiceImpl implements ClientManageService {

  private final ClientEntityRepository clientEntityRepository;
  private final MenuEntityRepository menuEntityRepository;
  private final ConfDataEntityRepository confDataEntityRepository;

  public ClientManageServiceImpl(ClientEntityRepository clientEntityRepository,
                                 MenuEntityRepository menuEntityRepository,
                                 ConfDataEntityRepository confDataEntityRepository
  ) {
    this.clientEntityRepository = clientEntityRepository;
    this.menuEntityRepository = menuEntityRepository;
    this.confDataEntityRepository = confDataEntityRepository;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public Collection<ClientEntity> getAllClient() {
    return clientEntityRepository.findAll();
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public ClientEntity createClient(CreateClientCommand newClient) {
    final ClientEntity clientEntity = ClientEntity.builder().name(newClient.getName()).desc(newClient.getDesc()).build();
    clientEntityRepository.save(clientEntity);
    return clientEntity;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void deleteClient(Integer id) {
    clientEntityRepository.delete(clientEntityRepository.findById(id).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND)));
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public ClientEntity getClient(Integer clientId) {
    final ClientEntity client = clientEntityRepository.findById(clientId).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND));
    // 触发懒加载查询数据
    client.getMenus();
    client.getConfDatas();
    return client;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public ConfDataEntity getConfData(Integer confDataId) {
    return confDataEntityRepository.findById(confDataId).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND));
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public ClientMenuEntity createMenu(CreateMenuCommand createMenuCommand) {
    ClientEntity clientEntity = clientEntityRepository.findById(createMenuCommand.getClientId()).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND));

    // 可能有配置数据
    final CreateConfDataCommand conf = createMenuCommand.getConfData();
    final ConfDataEntity confData = conf == null ? null : ConfDataEntity.builder().data("")
        .enable(Optional.ofNullable(conf.getEnable()).orElse(false))
        .type(conf.getType()).client(clientEntity).build();
    // 创建菜单
    final ClientMenuEntity menu = ClientMenuEntity.builder()
        .parentId(createMenuCommand.getParentId()).title(createMenuCommand.getTitle())
        // 优先级为已知最大优先级 + 1
        .priority(this.getMenuMaxPriority(clientEntity.getMenus()) + 1)
        .confData(confData).client(clientEntity).build();
    menuEntityRepository.save(menu);
    if (confData != null) {
      confDataEntityRepository.save(confData);
    }
    return menu;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void removeMenu(Integer clientId, Integer menuId) {
    val client = clientEntityRepository.findById(clientId).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND));
    val menu = client.getMenus().stream().filter(it -> it.getId().equals(menuId)).findFirst().orElse(null);
    if (menu == null) {
      return;
    }
    // 如果该分组有子配置则不删除
    if (menu.getConfData() == null && client.getMenus().stream().anyMatch(it -> menu.getId().equals(it.getParentId()))) {
      throw new ClientException(ClientErrorCode.PARAM_ERROR);
    }

    menuEntityRepository.delete(menu);
  }

  /**
   * 获取所有元素中的最大优先级.
   *
   * @param menus 所有菜单
   * @return 最大优先级
   */
  private int getMenuMaxPriority(List<ClientMenuEntity> menus) {
    int priority = 0;
    for (ClientMenuEntity menu : menus) {
      if (menu.getPriority() != null && menu.getPriority() > priority) {
        priority = menu.getPriority();
      }
    }
    return priority;
  }
}
