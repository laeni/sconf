package cn.laeni.sconf.server.controller.vo;

import cn.laeni.sconf.server.entity.ConfDataEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 具体的配置数据,将于客户端进行关联.
 * 并且同一个客户端可能同时存在多个配置.
 *
 * @author Laeni
 */
@Data
@Builder
public class ConfDataEntityVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 配置名.
   */
  private String name;

  /**
   * 是否启用.
   */
  private Boolean enable;

  /**
   * 优先级.
   */
  private Integer priority;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  private ConfDataEntity.Type type;
  // 示例(纯文本配置时提供响应的示例很重要)

  @Nullable
  public static ConfDataEntityVO toVo(ConfDataEntity entity) {
    if (entity == null) {
      return null;
    }
    return ConfDataEntityVO.builder()
        .id(entity.getId())
        .name(entity.getName())
        .enable(entity.getEnable())
        .priority(entity.getPriority())
        .type(entity.getType())
        .build();
  }

  public static Collection<ConfDataEntityVO> toVo(Collection<ConfDataEntity> entities) {
    return entities.stream().map(ConfDataEntityVO::toVo).collect(Collectors.toList());
  }
}
