package cn.laeni.sconf.server.controller.vo;

import cn.laeni.sconf.server.entity.MenuEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 应用配置界面的菜单列表.
 *
 * @author Laeni
 */
@Data
@Builder
public class MenuVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 菜单优先级.
   */
  private Integer priority;

  /**
   * 菜单名称.
   */
  private String title;

  /**
   * 父菜单id.
   *
   * ```null```表示顶级菜单.
   */
  private Integer parentId;

  /**
   * 该菜单对应的配置内容.
   * 如果为空则表示此菜单为菜单组.
   */
  private ConfDataEntityVO confData;

  @Nullable
  public static MenuVO toVo(MenuEntity entity) {
    if (entity == null) {
      return null;
    }
    return MenuVO.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .parentId(entity.getParentId())
        .confData(ConfDataEntityVO.toVo(entity.getConfData()))
        .build();
  }

  public static Collection<MenuVO> toVo(Collection<MenuEntity> entities) {
    return entities.stream().map(MenuVO::toVo).collect(Collectors.toList());
  }
}

