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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 客户端基本信息.
 * 不包含该客户端下的菜单和配置数据.
 *
 * @author Laeni
 * @see ClientEntity
 */
@Data
@Builder
public class ClientBaseVO {
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

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  @Nullable
  public static ClientBaseVO toVo(@Nullable ClientEntity entity) {
    if (entity == null) {
      return null;
    }
    return ClientBaseVO.builder()
        .id(entity.getId()).name(entity.getName()).desc(entity.getDesc())
        .createTime(entity.getCreateTime()).updateTime(entity.getUpdateTime())
        .build();
  }

  @NonNull
  public static Collection<ClientBaseVO> toVo(@NonNull Collection<ClientEntity> entities) {
    return entities.stream().map(ClientBaseVO::toVo).collect(Collectors.toList());
  }
}
