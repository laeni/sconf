package cn.laeni.sconf.server.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * 具体的配置数据,将于客户端进行关联.
 * 并且同一个客户端可能同时存在多个配置.
 *
 * @author Laeni
 */
@Entity
@Table(name = "tbl_conf_data")
@EntityListeners(AuditingEntityListener::class)
data class ConfDataEntity(
    /**
     * 自增Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    /**
     * 配置名.
     */
    @Column(name = "p_name")
    var name: String? = null,

    /**
     * 具体的配置内容. TODO 由于内容可能过大,所有可能不适合直接保存到关系数据库中
     */
    @Column(name = "p_data")
    var data: String? = null,

    /**
     * 是否启用.
     */
    @Column(name = "p_enable")
    var enable: Boolean? = null,

    /**
     * 优先级.
     */
    @Column(name = "p_priority")
    var priority: Int? = 0,

    // 配置类型(properties | yml | 配置项)
    var type: Type? = null
    // 示例(纯文本配置时提供响应的示例很重要)
) {
  enum class Type {
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
     * @see
     */
    ITEM,
    /**
     * 文件.
     * 如证书等.
     */
    FILE;
  }
}
