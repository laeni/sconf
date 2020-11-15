package cn.laeni.sconf.server.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 具体的配置数据,将于客户端进行关联.
 * 并且同一个客户端可能同时存在多个配置.
 *
 * @author Laeni
 */
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tbl_conf_data")
@EntityListeners(AuditingEntityListener.class)
public class ConfEntity {
  /**
   * 自增Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 具体配置内容数据Id.
   * 可以为空(刚创建时).
   */
  @Column(name = "data_id")
  private String contextId;

  /**
   * 示例配置数据Id.
   */
  @Column(name = "example_id")
  private String exampleId;

  /**
   * 是否启用.
   * {@link #contextId}不为空时才可以启用.
   */
  @Column(name = "p_enable")
  @org.hibernate.annotations.Type(type="yes_no")
  private Boolean enable;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  @Enumerated(EnumType.STRING)
  private Type type;
  // 示例(纯文本配置时提供响应的示例很重要)

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
   * 该配置所属的客户端.
   */
  @ManyToOne
  private ClientEntity client;

  /**
   * 数据类型.
   */
  public enum Type {
    /**
     * properties 格式的字符串配置.
     */
    PROP,

    /**
     * yml 格式的字符串配置.
     */
    YAML,

    /**
     * json 格式的字符串配置.
     */
    ENUM,

    /**
     * 文件.
     * 如证书等.
     */
    FILE;
  }
}
