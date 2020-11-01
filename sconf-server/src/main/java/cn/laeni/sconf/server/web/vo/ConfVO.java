package cn.laeni.sconf.server.web.vo;

import cn.laeni.sconf.server.entity.ConfEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 具体的配置数据基本信息,不包含具体配置数据.
 * 因为具体配置数据往往过于庞大并且可能是文件等.
 * 该对象常用于批量获取时使用,具体配置数据需要单独获取.
 *
 * @author Laeni
 */
@Data
@Builder
public class ConfVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 具体配置内容数据Id.
   * 可以为空(刚创建时).
   */
  private String contextId;

  /**
   * 示例配置数据Id.
   */
  private String exampleId;

  /**
   * 是否启用.
   */
  private Boolean enable;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  private ConfEntity.Type type;
  // 示例(纯文本配置时提供响应的示例很重要)

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  /**
   * 该配置所在客户端Id.
   */
  private Integer clientId;

  @Nullable
  public static ConfVO toVo(ConfEntity entity) {
    if (entity == null) {
      return null;
    }
    return ConfVO.builder()
        .id(entity.getId())
        .contextId(entity.getContextId())
        .exampleId(entity.getExampleId())
        .enable(entity.getEnable())
        .type(entity.getType())
        .clientId(entity.getClient().getId())
        .createTime(entity.getCreateTime()).updateTime(entity.getUpdateTime())
        .build();
  }

  public static Collection<ConfVO> toVo(Collection<ConfEntity> entities) {
    return entities.stream().map(ConfVO::toVo).collect(Collectors.toList());
  }
}
