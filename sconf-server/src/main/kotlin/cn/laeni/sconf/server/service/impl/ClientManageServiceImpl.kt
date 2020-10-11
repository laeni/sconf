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
package cn.laeni.sconf.server.service.impl

import cn.laeni.personal.exception.ClientException
import cn.laeni.sconf.exception.ClientErrorCode
import cn.laeni.sconf.server.controller.command.AddMenuCommand
import cn.laeni.sconf.server.controller.command.CreateClientCommand
import cn.laeni.sconf.server.entity.ClientEntity
import cn.laeni.sconf.server.entity.ConfDataEntity
import cn.laeni.sconf.server.entity.MenuEntity
import cn.laeni.sconf.server.repository.ClientEntityRepository
import cn.laeni.sconf.server.repository.MenuEntityRepository
import cn.laeni.sconf.server.service.ClientManageService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author Laeni
 */
@Service
class ClientManageServiceImpl(
    private val clientEntityRepository: ClientEntityRepository,
    private val menuEntityRepository: MenuEntityRepository) : ClientManageService {

  @Transactional(rollbackOn = [Exception::class])
  override fun getAllClient(): Collection<ClientEntity> {
    return clientEntityRepository.findAll()
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun createClient(newClient: CreateClientCommand): ClientEntity {
    val clientEntity = ClientEntity(name = newClient.name, desc = newClient.desc)
    clientEntityRepository.save(clientEntity)
    return clientEntity
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun deleteClient(id: Int) {
    clientEntityRepository.delete(clientEntityRepository.findById(id).orElseThrow { ClientException(ClientErrorCode.CLIENT_NOT_FOND) })
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun getClientConfList(id: Int): Collection<MenuEntity> {
    val clientEntity = clientEntityRepository.findById(id).orElseThrow { ClientException(ClientErrorCode.CLIENT_NOT_FOND) }
    return clientEntity.menus!!
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun addMenu(addMenuCommand: AddMenuCommand): MenuEntity {
    val clientEntity = clientEntityRepository.findById(addMenuCommand.clientId!!).orElseThrow { ClientException(ClientErrorCode.CLIENT_NOT_FOND) }
    // 可能有配置数据
    val confData: ConfDataEntity? = when (val conf = addMenuCommand.confData) {
      null -> null
      else -> ConfDataEntity(name = conf.name, enable = conf.enable, priority = conf.priority, type = conf.type)
    }
    val menu = MenuEntity(parent = addMenuCommand.parent, title = addMenuCommand.title, confData = confData, client = clientEntity)
    menuEntityRepository.save(menu)
    clientEntity.menus!!.plus(menu)
    return menu
  }
}
