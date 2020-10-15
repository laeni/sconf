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
import cn.laeni.sconf.server.controller.command.AddMenuCommand;
import cn.laeni.sconf.server.controller.command.CreateClientCommand;
import cn.laeni.sconf.server.entity.ClientEntity;
import cn.laeni.sconf.server.entity.ConfDataEntity;
import cn.laeni.sconf.server.entity.MenuEntity;
import cn.laeni.sconf.server.repository.ClientEntityRepository;
import cn.laeni.sconf.server.repository.MenuEntityRepository;
import cn.laeni.sconf.server.service.ClientManageService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * @author Laeni
 */
@Service
public class ClientManageServiceImpl implements ClientManageService {
  private final ClientEntityRepository clientEntityRepository;
  private final MenuEntityRepository menuEntityRepository;

  public ClientManageServiceImpl(ClientEntityRepository clientEntityRepository, MenuEntityRepository menuEntityRepository) {
    this.clientEntityRepository = clientEntityRepository;
    this.menuEntityRepository = menuEntityRepository;
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
  public Collection<MenuEntity> getClientConfList(Integer id) {
    return clientEntityRepository.findById(id).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND)).getMenus();
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public MenuEntity addMenu(AddMenuCommand addMenuCommand) {
    ClientEntity clientEntity = clientEntityRepository.findById(addMenuCommand.getClientId()).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND));

    // 可能有配置数据
    val conf = addMenuCommand.getConfData();
    val confData = conf == null ? null : ConfDataEntity.builder().name(conf.getName()).enable(conf.getEnable()).priority(conf.getPriority()).type(conf.getType()).build();
    val menu = MenuEntity.builder().parent(addMenuCommand.getParent()).title(addMenuCommand.getTitle()).confData(confData).client(clientEntity).build();
    menuEntityRepository.save(menu);
    clientEntity.getMenus().add(menu);
    return menu;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void removeMenu(Integer clientId, Integer menuId) {
    val clientEntity = clientEntityRepository.findById(clientId).orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOND));
    val menu = clientEntity.getMenus().stream().filter(it -> it.getId().equals(menuId)).findFirst().orElse(null);
    // 如果该分组有子配置则不删除
    System.out.println(menu);
    if (menu == null) {
      return;
    }
    if (menu.getConfData() == null && clientEntity.getMenus().stream().allMatch(it -> it.getParent().equals(menu.getId()))) {
      throw new ClientException(ClientErrorCode.PARAM_ERROR);
    }

    menuEntityRepository.delete(menu);
  }
}
