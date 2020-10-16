package cn.laeni.sconf.server.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 应用配置界面的菜单列表.
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
public class MenuEntity {
  /**
   * 自增Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 菜单优先级.
   */
  @Column(name = "p_priority")
  private Integer priority;

  /**
   * 菜单名称.
   */
  @Column(name = "p_title")
  private String title;

  /**
   * 父菜单id.
   * <p>
   * ```null```表示顶级菜单.
   */
  @Column(name = "p_data")
  private Integer parentId;

  /**
   * 该菜单对应的配置内容.
   * 如果为空则表示此菜单为菜单组.
   */
  @OneToOne
  private ConfDataEntity confData;

  /**
   * 该配置所属的客户端.
   */
  @ManyToOne
  private ClientEntity client;
}
