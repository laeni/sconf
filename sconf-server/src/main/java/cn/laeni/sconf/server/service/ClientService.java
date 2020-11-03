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

import cn.laeni.sconf.server.entity.ClientEntity;
import cn.laeni.sconf.server.entity.ClientMenuEntity;
import cn.laeni.sconf.server.entity.ConfEntity;
import cn.laeni.sconf.server.web.manage.CreateClientCommand;
import cn.laeni.sconf.server.web.manage.CreateMenuCommand;
import cn.laeni.sconf.server.web.manage.PatchConfCommand;

import java.util.Collection;

/**
 * 应用管理.
 *
 * @author Laeni
 */
public interface ClientService {
  /**
   * 获取所有客户端.
   *
   * @return 所有客户端的基本信息
   */
  Collection<ClientEntity> getAllConfId();

}
