package cn.laeni.sconf.server.web.manage;

import cn.laeni.sconf.server.entity.ClientMenuEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 应用菜单.此菜单为树形结构,可能是一个配置,或者是某些配置或某些分组的父组.
 *
 * @author Laeni
 */
@Data
@Builder
public class ClientMenuVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 父菜单id.
   * <p>
   * ```null```表示顶级菜单.
   */
  private Integer parentId;

  /**
   * 菜单名称.
   */
  private String title;

  /**
   * 菜单优先级.
   */
  private Integer priority;

  /**
   * 该菜单对应的配置内容Id.
   */
  private Integer confId;

  /**
   * 该配置所属的客户端Id.
   */
  private Integer clientId;

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

  @Nullable
  public static ClientMenuVO toVo(@Nullable ClientMenuEntity entity) {
    if (entity == null) {
      return null;
    }
    return ClientMenuVO.builder()
        .id(entity.getId()).title(entity.getTitle()).parentId(entity.getParentId()).priority(entity.getPriority())
        // 当菜单为菜单组时不存在confId
        .confId(entity.getConfData() != null ? entity.getConfData().getId() : null)
        .clientId(entity.getClient().getId())
        .createTime(entity.getCreateTime()).updateTime(entity.getUpdateTime())
        .build();
  }

  @NonNull
  public static Collection<ClientMenuVO> toVo(@NonNull Collection<ClientMenuEntity> entities) {
    return entities.stream().map(ClientMenuVO::toVo).collect(Collectors.toList());
  }
}
