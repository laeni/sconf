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

import cn.laeni.personal.util.JacksonUtils
import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * @author Laeni
 */
@Entity
@Table(name = "tbl_conf_value")
@EntityListeners(AuditingEntityListener::class)
data class ConfValueEntity(
    /**
     * 自增Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    /**
     * 配置内容.
     */
    @Column(name = "p_value")
    @Basic(fetch = FetchType.LAZY)
    var value: String? = null,

    /**
     * 标签.
     */
    @Convert(converter = LabelConverter::class)
    var labels: MutableSet<String>? = null
) {
  @Converter
  private class LabelConverter : AttributeConverter<Set<String>?, String?> {
    /**
     * 入库编码.
     */
    override fun convertToDatabaseColumn(attr: Set<String>?): String? {
      if (attr == null) {
        return null
      }
      return JacksonUtils.objectToString(attr)
    }

    /**
     * 出库解码.
     */
    override fun convertToEntityAttribute(col: String?): Set<String>? {
      if (col == null) {
        return null;
      }
      return JacksonUtils.stringToObject(col, object : TypeReference<HashSet<String>>() {})
    }
  }
}
