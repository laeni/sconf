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
package cn.laeni.sconf.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户端列表.
 *
 * @author Laeni
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"menus"})
@Entity
@Table(name = "tbl_conf_client")
@EntityListeners(AuditingEntityListener.class)
public class ClientEntity {
  /**
   * 自增Id.
   */
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 客户端名称.
   */
  @Column(name = "p_name")
  private String name;

  /**
   * 客户端描述说明.
   */
  @Column(name = "p_desc")
  private String desc;

  /**
   * 创建时间
   */
  @Column(name = "create_time")
  @CreatedDate
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  @Column(name = "update_time")
  @LastModifiedDate
  private LocalDateTime updateTime;

  /**
   * 本客户端应用配置界面的所有菜单.
   */
  @JsonIgnoreProperties(value = {"client"})
  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  private List<ClientMenuEntity> menus;

  /**
   * 本客户端应用所有配置.
   */
  @JsonIgnoreProperties(value = {"client"})
  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  private List<ConfDataEntity> confDatas;
}
