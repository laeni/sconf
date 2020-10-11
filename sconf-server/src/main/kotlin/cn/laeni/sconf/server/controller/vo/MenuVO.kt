package cn.laeni.sconf.server.controller.vo

import cn.laeni.sconf.server.entity.MenuEntity
import java.util.stream.Collectors

/**
 * 应用配置界面的菜单列表.
 *
 * @author Laeni
 */
data class MenuVO(
    /**
     * 自增Id.
     */
    var id: Int? = null,

    /**
     * 菜单优先级.
     */
    var priority: Int = 0,

    /**
     * 菜单名称.
     */
    var title: String? = null,

    /**
     * 父菜单id.
     *
     * ```null```表示顶级菜单.
     */
    var parent: Int? = null,

    /**
     * 该菜单对应的配置内容.
     * 如果为空则表示此菜单为菜单组.
     */
    var confData: ConfDataEntityVO? = null
) {
    companion object {
        fun toVo(entity: MenuEntity?): MenuVO? {
            if (entity == null) {
                return null
            }
            return MenuVO(
                id = entity.id,
                title = entity.title,
                parent = entity.parent,
                confData = ConfDataEntityVO.toVo(entity.confData)
            )
        }

        fun toVo(entities: Collection<MenuEntity>): Collection<MenuVO?> {
            return entities.stream().map { entity -> toVo(entity) }.collect(Collectors.toList())
        }
    }
}

