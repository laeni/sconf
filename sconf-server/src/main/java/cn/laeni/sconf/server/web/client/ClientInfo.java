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
package cn.laeni.sconf.server.web.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Laeni
 */
@Data
@Builder
public class ClientInfo {
  /**
   * 客户端Id;
   */
  String clientId;

  /**
   * 该客户端下所有配置Id.
   */
  private List<String> all;

  /**
   * 已更新的配置Id.
   */
  private List<String> updated;
}
