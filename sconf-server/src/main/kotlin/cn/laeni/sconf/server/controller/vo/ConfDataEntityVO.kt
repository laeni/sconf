package cn.laeni.sconf.server.controller.vo

import cn.laeni.sconf.server.entity.ConfDataEntity
import java.util.stream.Collectors

/**
 * 具体的配置数据,将于客户端进行关联.
 * 并且同一个客户端可能同时存在多个配置.
 *
 * @author Laeni
 */
data class ConfDataEntityVO(
    /**
     * 自增Id.
     */
    var id: Int? = null,

    /**
     * 配置名.
     */
    var name: String? = null,

    /**
     * 是否启用.
     */
    var enable: Boolean? = null,

    /**
     * 优先级.
     */
    var priority: Int? = 0,

    // 配置类型(properties | yml | 配置项)
    var type: ConfDataEntity.Type? = null
    // 示例(纯文本配置时提供响应的示例很重要)
) {
  companion object {
    fun toVo(entity: ConfDataEntity?): ConfDataEntityVO? {
      if (entity == null) {
        return null
      }
      return ConfDataEntityVO(
          id = entity.id,
          name = entity.name,
          enable = entity.enable,
          priority = entity.priority,
          type = entity.type
      )
    }

    fun toVo(entities: Collection<ConfDataEntity>): Collection<ConfDataEntityVO?> {
      return entities.stream().map { entity -> toVo(entity) }.collect(Collectors.toList())
    }
  }
}
