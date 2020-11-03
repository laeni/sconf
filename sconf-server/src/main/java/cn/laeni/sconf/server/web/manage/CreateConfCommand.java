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

import cn.laeni.sconf.server.entity.ConfEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加配置.
 *
 * @author Laeni
 */
@Setter
@Getter
@ToString
public class CreateConfCommand {
  /**
   * 客户端Id.
   */
  @NotNull(message = "客户端Id不能为空")
  private Integer clientId;

  /**
   * 父菜单id,可以为空.
   * <p>
   * {@code null}表示顶级菜单.
   */
  private Integer parentId;

  /**
   * 菜单名称.
   */
  @NotBlank(message = "菜单名称不能为空")
  private String title;

  /**
   * 是否启用.
   */
  private Boolean enable;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  private ConfEntity.Type type;
}
