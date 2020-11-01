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
package cn.laeni.sconf.server;

/**
 * 描述配置项结构.
 * 设置配置项的目的是方便快速/准确对配置进行更改,适用于经常变更并且配置值相对固定的场景.
 * 复杂情况下,如果配置项不能满足请使用 yaml 或 properties 格式.
 *
 * @author Laeni
 */
public class ItemDataStruct {
  /**
   * 顺序.
   */
  Integer order;

  /**
   * 类型.
   */
  Type type;

  /**
   * key.
   * 配置对应的key.
   */
  String key;

  /**
   * value.
   * 配置对应的value.
   */
  String value;

  /**
   * 该配置项的说明.
   */
  String desc;

  /**
   * 可能值枚举(用于单选/多选场景).
   */
  String candidate;
  /**
   * 配置项所支持的类型.
   */
  public enum Type {
    /**
     * 单行文本输入框.
     */
    INPUT_TEXT,

    /**
     * 单行数字输入框.
     */
    INPUT_NUMBER,

    /**
     * 多行文本输入框(文本域).
     */
    TEXTAREA,

    /**
     * 开关.
     * 表示boolean.
     */
    SWITCH,

    /**
     * 单选框.
     * 最终值为字符串,与 INPUT_TEXT 一样的效果.
     */
    RADIO,

    /**
     * 多选框.
     * 最终值为数组格式的文本.
     */
    CHECKBOX;
  }
}
