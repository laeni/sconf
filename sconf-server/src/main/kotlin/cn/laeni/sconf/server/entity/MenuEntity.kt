package cn.laeni.sconf.server.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * 应用配置界面的菜单列表.
 *
 * @author Laeni
 */
@Entity
@Table(name = "tbl_client_menu")
@EntityListeners(AuditingEntityListener::class)
data class MenuEntity(
    /**
     * 自增Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    /**
     * 菜单优先级.
     */
    @Column(name = "p_priority")
    var priority: Int = 0,

    /**
     * 菜单名称.
     */
    @Column(name = "p_title")
    var title: String? = null,

    /**
     * 父菜单id.
     *
     * ```null```表示顶级菜单.
     */
    @Column(name = "p_data")
    var parent: Int? = null,

    /**
     * 该菜单对应的配置内容.
     * 如果为空则表示此菜单为菜单组.
     */
    @OneToOne
    var confData: ConfDataEntity? = null,

    /**
     * 该配置所属的客户端.
     */
    @ManyToOne
    var client: ClientEntity? = null
)
