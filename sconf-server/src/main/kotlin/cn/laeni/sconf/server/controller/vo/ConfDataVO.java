package cn.laeni.sconf.server.controller.vo;

import cn.laeni.sconf.server.entity.ConfDataEntity;
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
public class ConfDataVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 具体的配置内容声明.
   * 由于内容可能过大,所有可能不适合直接保存到关系数据库中
   */
  private String data;

  /**
   * 是否启用.
   */
  private Boolean enable;

  /**
   * 配置类型(properties | yml | 配置项).
   */
  private ConfDataEntity.Type type;
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
  public static ConfDataVO toVo(ConfDataEntity entity) {
    if (entity == null) {
      return null;
    }
    return ConfDataVO.builder()
        .id(entity.getId())
        .data(entity.getData())
        .enable(entity.getEnable())
        .type(entity.getType())
        .clientId(entity.getClient().getId())
        .createTime(entity.getCreateTime()).updateTime(entity.getUpdateTime())
        .build();
  }

  public static Collection<ConfDataVO> toVo(Collection<ConfDataEntity> entities) {
    return entities.stream().map(ConfDataVO::toVo).collect(Collectors.toList());
  }
}
