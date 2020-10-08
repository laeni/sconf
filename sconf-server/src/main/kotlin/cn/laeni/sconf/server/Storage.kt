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
package cn.laeni.sconf.server

/**
 * 配置存储抽象.
 * 由于配置文件可能很大,所以可能不适合放数据库,因此可以根据需要选择对应的存储即可.
 * 支持多种存储介质,如: MySQL/file/COS/OSS.
 * 具体类型的存储需要专门针对特定的存储进行实现.
 *
 * @author Laeni
 */
interface Storage {
  /**
   * 创建新配置.
   *
   * @param value 配置内容
   * @return 该配置对应的Id
   */
  fun create(value: String?): String

  /**
   * 保存配置.
   *
   * @param id    该配置对应的Id
   * @param value 配置内容
   */
  fun sava(id: String, value: String?)

  /**
   * 获取配置ID对应的配置内容.
   *
   * @param id 配置Id
   * @return 配置对应的内容
   */
  operator fun get(id: String): String

  /**
   * 删除Id对应的配置.
   *
   * @param id 配置Id
   */
  fun delete(id: String)

  /**
   * 给某个配置添加标签.
   *
   * @param id    配置id
   * @param label 标签名
   */
  fun addLabel(id: String, label: String)

  /**
   * 删除某个配置的指定标签.
   *
   * @param id    配置id
   * @param label 标签名
   */
  fun removeLabel(id: String, label: String)

  /**
   * 根据分组获取该标签下的所有配置.
   * 如果尚未指定分组,则为default.
   * 分组一般标识同一个应用或者同一个环境的所有配置.
   * 一个配置理论上可以允许有多个标签.
   *
   * @param label 标签名
   * @return 该标签所属的全部配置
   */
  fun getAllGroupBy(label: String): Collection<String>
}