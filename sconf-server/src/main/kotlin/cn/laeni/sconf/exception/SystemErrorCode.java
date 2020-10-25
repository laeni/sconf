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
package cn.laeni.sconf.exception;

import cn.laeni.personal.exception.SystemErrorCodeMark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Laeni
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements SystemErrorCodeMark {
  /**
   * 访问的API不存在.
   */
  API_NOT_FOND("ApiNotFond", "访问的API不存在"),
  /**
   * HTTP方法不支持.
   */
  HTTP_NOT_SUPPORTED("HttpNotSupported", "HTTP方法不支持"),
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
