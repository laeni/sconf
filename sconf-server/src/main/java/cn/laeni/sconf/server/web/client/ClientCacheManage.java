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

import cn.laeni.sconf.core.Result;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Laeni
 */
public class ClientCacheManage {
  private static final Set<ClientDeferredResult> DEFERRED_RESULTS = new HashSet<>();

  /**
   * 注册监听器.
   *
   * @param clientId 客户端Id
   * @param result   监听器(返回对象)
   */
  public static void subscribe(String clientId, DeferredResult<Result<ClientInfo>> result) {
    DEFERRED_RESULTS.add(ClientDeferredResult.builder()
        .clientId(clientId)
        .result(result)
        .build());
  }

  /**
   * 发布.
   *
   * @param info 响应内容
   */
  public static void publish(ClientInfo info){
    DEFERRED_RESULTS.stream().filter(v -> v.getClientId().equals(info.getClientId())).forEach(clientDeferredResult -> {
      DEFERRED_RESULTS.remove(clientDeferredResult);
      final DeferredResult<Result<ClientInfo>> result = clientDeferredResult.getResult();
      if (!result.isSetOrExpired()) {
        result.setResult(new Result<>(info));
      }
    });
  }

  @Getter
  @Builder
  @EqualsAndHashCode
  private static class ClientDeferredResult {
    private final String clientId;
    private final DeferredResult<Result<ClientInfo>> result;
  }
}
