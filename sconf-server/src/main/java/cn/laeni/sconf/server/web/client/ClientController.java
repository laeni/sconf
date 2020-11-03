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

import cn.laeni.personal.exception.SystemErrorCodeMark;
import cn.laeni.personal.exception.SystemException;
import cn.laeni.sconf.core.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * 客户端接入Controller.
 *
 * @author Laeni
 */
@RestController
@RequestMapping("/api/client")
public class ClientController {
  int i = 0;
  // 获取client配置
  @GetMapping("/info")
  public DeferredResult<Result<ClientInfo>> getClientInfo(String clientId, Boolean straightway) {
    final DeferredResult<Result<ClientInfo>> result = new DeferredResult<>(200000L, ClientController::get1);
    ClientCacheManage.subscribe(clientId, result);
    System.out.println(++i);
    return result;
  }

  /**
   * 获取配置数据.
   *
   * @param dataIds 逗号分隔的一个或多个配置数据Id
   * @return 返回配置数据
   */
  public Result<List<Object>> getData(String dataIds) {
    return null;
  }

  public static Result<ClientInfo> get1() {
    return new Result<>(new SystemException(SystemErrorCodeMark.Code.INTERNAL_ERROR));
  }
}
