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
package cn.laeni.sconf.server.controller.command;

import cn.laeni.sconf.server.entity.ConfDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 添加客户端的菜单(配置分组).
 *
 * @author Laeni
 */
@Setter
@Getter
@ToString
public class AddConfDataCommand {
  /**
   * 配置名.
   */
  private String name;

  /**
   * 是否启用.
   */
  private Boolean enable;

  /**
   * 优先级.
   */
  private Integer priority;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  private ConfDataEntity.Type type;
}
