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
package cn.laeni.sconf.server.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * 客户端列表.
 *
 * @author Laeni
 */
@Entity
@Table(name = "tbl_conf_client")
@EntityListeners(AuditingEntityListener::class)
data class ClientEntity(
    /**
     * 自增Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    /**
     * 客户端名称.
     */
    @Column(name = "p_name")
    var name: String? = null,

    /**
     * 客户端描述说明.
     */
    @Column(name = "p_desc")
    var desc: String? = null,

    /**
     * 本客户端应用下的所有配置集合.
     */
    @OneToMany(mappedBy = "id", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var datas: List<ConfDataEntity>? = null
)
