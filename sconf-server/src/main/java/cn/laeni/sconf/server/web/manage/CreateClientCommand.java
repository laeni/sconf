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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 创建客户端的必须数据.
 *
 * @author Laeni
 */
@Setter
@Getter
@ToString
public class CreateClientCommand {
  /**
   * 客户端名称.
   */
  @NotBlank(message = "客户端名称不能为空")
  private String name;

  /**
   * 客户端描述说明.
   */
  @NotBlank(message = "客户端描述说明不能为空")
  private String desc;
}
