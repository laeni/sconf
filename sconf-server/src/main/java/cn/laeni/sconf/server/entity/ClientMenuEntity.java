package cn.laeni.sconf.server.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 应用菜单.此菜单为树形结构,可能是一个配置,或者是某些配置或某些分组的父组.
 *
 * @author Laeni
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"client"})
@Entity
@Table(name = "tbl_client_menu")
@EntityListeners(AuditingEntityListener.class)
public class ClientMenuEntity {
  /**
   * 自增Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 父菜单id.
   * <p>
   * ```null```表示顶级菜单.
   */
  @Column(name = "parent_id")
  private Integer parentId;

  /**
   * 菜单名称.
   */
  @Column(name = "p_title")
  private String title;

  /**
   * 菜单优先级.
   */
  @Column(name = "p_priority")
  private Integer priority;

  /**
   * 该菜单对应的配置内容.
   * 如果为空则表示此菜单为菜单组.
   */
  @OneToOne
  private ConfEntity confData;

  /**
   * 该配置所属的客户端.
   */
  @ManyToOne
  private ClientEntity client;

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
}
