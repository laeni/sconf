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
package cn.laeni.sconf.server.exception;

import cn.laeni.personal.exception.ClientErrorCodeMark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Laeni
 */
@Getter
@AllArgsConstructor
public enum ClientErrorCode implements ClientErrorCodeMark {
  /**
   * 参数错误.
   */
  PARAM_ERROR("ParamError", "参数错误"),

  /**
   * 客户端应用不存在.
   */
  CLIENT_NOT_FOND("ClientNotFond", "客户端应用不存在"),
  ;

  /**
   * 错误码.
   */
  private final String errorCode;
  /**
   * 错误码对应的外部描述信息,该信息是通过错误码自动获取,并且直接返回给调用方.
   */
  private final String errorDesc;
}
