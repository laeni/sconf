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
package cn.laeni.sconf.server.controller.vo

import cn.laeni.sconf.server.entity.ClientEntity
import java.util.stream.Collectors

/**
 * @see ClientEntity
 * @author Laeni
 */
data class ClientEntityBaseInfoVO(
    /**
     * 自增Id.
     */
    var id: Int? = null,

    /**
     * 客户端名称.
     */
    var name: String? = null,

    /**
     * 客户端描述说明.
     */
    var desc: String? = null
) {
  companion object {
    fun toVo(entity: ClientEntity?): ClientEntityBaseInfoVO? {
      if (entity == null) {
        return null
      }
      return ClientEntityBaseInfoVO(
          id = entity.id,
          name = entity.name,
          desc = entity.desc
      )
    }

    fun toVo(entities: Collection<ClientEntity>): Collection<ClientEntityBaseInfoVO?> {
      return entities.stream().map { entity -> toVo(entity) }.collect(Collectors.toList())
    }
  }
}
