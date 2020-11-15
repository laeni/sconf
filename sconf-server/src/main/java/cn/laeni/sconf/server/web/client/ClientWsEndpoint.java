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

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Laeni
 */
@Component
@ServerEndpoint("/myWs")
public class ClientWsEndpoint {
  /**
   * 连接成功.
   */
  @OnOpen
  public void onOpen(Session session) {
    System.out.println("连接成功");
  }

  /**
   * 连接关闭.
   */
  @OnClose
  public void onClose(Session session) {
    System.out.println("连接关闭");
  }

  /**
   * 接收到消息.
   */
  @OnMessage
  public String onMsg(String text) {
    return "servet 发送：" + text;
  }
}
