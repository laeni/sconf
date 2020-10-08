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
package cn.laeni.sconf.server.db

import cn.laeni.personal.util.StringUtils
import cn.laeni.sconf.server.Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.streams.toList

/**
 * 基于数据库的配置存储实现.
 * 即所有应用配置将存放于本系统使用的数据库中.
 *
 * @author Laeni
 */
@Service
class DbStorage : Storage {
  @Autowired
  var confValueRepository: ConfValueRepository? = null

  @Transactional(rollbackOn = [Exception::class])
  override fun create(value: String?): String {
    val entity = ConfValueEntity(value = value)
    confValueRepository!!.save(entity)
    return entity.id!!.toString()
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun sava(id: String, value: String?) {
    val entity = confValueRepository!!.findById(id.toInt()).orElseThrow { RuntimeException("$id 对应的配置不存在") }
    entity!!.value = value
    confValueRepository!!.save(entity)
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun get(id: String): String {
    return confValueRepository!!.findById(id.toInt()).orElseThrow { RuntimeException("$id 对应的配置不存在") }!!.value ?: ""
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun delete(id: String) {
    confValueRepository!!.deleteById(id.toInt())
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun addLabel(id: String, label: String) {
    val entity = confValueRepository!!.findById(id.toInt()).orElseThrow { RuntimeException("$id 对应的配置不存在") }
    if (entity!!.labels == null) {
      entity.labels = HashSet()
    }
    entity.labels!!.add(label)
    confValueRepository!!.save(entity)
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun removeLabel(id: String, label: String) {
    val entity = confValueRepository!!.findById(id.toInt()).orElseThrow { RuntimeException("$id 对应的配置不存在") }
    if (entity!!.labels != null) {
      entity.labels!!.remove(label)
      confValueRepository!!.save(entity)
    }
  }

  @Transactional(rollbackOn = [Exception::class])
  override fun getAllGroupBy(label: String): Collection<String> {
    // 标签如果为空则使用默认值
    val v = if (StringUtils.isEmpty(label)) {
      "default"
    } else {
      label
    }

    return confValueRepository!!.findAll()
        .stream().filter {t -> t?.labels!!.contains(v) }
        .map { it!!.id.toString() }.toList()
  }
}
