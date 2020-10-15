package cn.laeni.sconf.server.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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
public class ConfDataEntity {
  /**
   * 自增Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 配置名.
   */
  @Column(name = "p_name")
  private String name;

  /**
   * 具体的配置内容. TODO 由于内容可能过大,所有可能不适合直接保存到关系数据库中
   */
  @Column(name = "p_data")
  private String data;

  /**
   * 是否启用.
   */
  @Column(name = "p_enable")
  private Boolean enable;

  /**
   * 优先级.
   */
  @Column(name = "p_priority")
  private Integer priority;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  private Type type;
  // 示例(纯文本配置时提供响应的示例很重要)

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
    ITEM,

    /**
     * 文件.
     * 如证书等.
     */
    FILE;
  }
}
