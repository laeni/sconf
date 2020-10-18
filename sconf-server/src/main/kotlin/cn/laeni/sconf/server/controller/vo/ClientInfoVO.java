package cn.laeni.sconf.server.controller.vo;

import cn.laeni.sconf.server.entity.ClientEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 应用配置界面的菜单列表.
 *
 * @author Laeni
 */
@Data
@Builder
public class ClientInfoVO {
  /**
   * 自增Id.
   */
  private Integer id;

  /**
   * 客户端名称.
   */
  private String name;

  /**
   * 客户端描述说明.
   */
  private String desc;

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
   * 本客户端应用配置界面的所有菜单.
   */
  @JsonIgnoreProperties(value = {"client"})
  private Collection<ClientMenuVO> menus;

  /**
   * 本客户端应用所有配置.
   */
  @JsonIgnoreProperties(value = {"client"})
  private Collection<ConfDataVO> confDatas;

  @Nullable
  public static ClientInfoVO toVo(@Nullable ClientEntity entity) {
    if (entity == null) {
      return null;
    }
    return ClientInfoVO.builder()
        .id(entity.getId()).name(entity.getName()).desc(entity.getDesc())
        .menus(ClientMenuVO.toVo(entity.getMenus())).confDatas(ConfDataVO.toVo(entity.getConfDatas()))
        .createTime(entity.getCreateTime()).updateTime(entity.getUpdateTime())
        .build();
  }

  /**
   * @param entities 客户端列表
   * @return 客户端列表Vo形式
   * @deprecated 由于包含详细信息,所以不建议批量获取
   * @see ClientBaseVO
   */
  @NotNull
  @Deprecated
  public static Collection<ClientInfoVO> toVo(@NotNull Collection<ClientEntity> entities) {
    return entities.stream().map(ClientInfoVO::toVo).collect(Collectors.toList());
  }
}

