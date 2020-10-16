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
package cn.laeni.sconf.server.controller.vo;

import cn.laeni.sconf.server.entity.ClientEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Laeni
 * @see ClientEntity
 */
@Data
@Builder
public class ClientEntityBaseInfoVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 客户端名称.
   */
  private String name;

  /**
   * 客户端描述说明.
   */
  private String desc;

  @Nullable
  public static ClientEntityBaseInfoVO toVo(@Nullable ClientEntity entity) {
    if (entity == null) {
      return null;
    }
    return ClientEntityBaseInfoVO.builder()
        .id(entity.getId()).name(entity.getName()).desc(entity.getDesc())
        .build();
  }

  @Nullable
  public static Collection<ClientEntityBaseInfoVO> toVo(Collection<ClientEntity> entities) {
    return entities.stream().map(ClientEntityBaseInfoVO::toVo).collect(Collectors.toList());
  }
}
