package cn.laeni.sconf.server.web.command;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 具体的配置数据基本信息,不包含具体配置数据.
 * 因为具体配置数据往往过于庞大并且可能是文件等.
 * 该对象常用于批量获取时使用,具体配置数据需要单独获取.
 *
 * @author Laeni
 */
@Setter
@Getter
@ToString
public class PatchConfCommand {
  /**
   * 配置Id.
   * 用于指名需要修改的配置.
   */
  @NotNull(message = "配置Id不能为空")
  private Integer id;

  /**
   * 修改前的配置Id,用于实现乐观锁,防止多人同时修改.
   * 如果本身没有数据(刚刚创建)时可以为空.
   */
  private String oldContextId;

  /**
   * 修改前的示例配置Id,用于实现乐观锁,防止多人同时修改.
   * 如果本身没有数据(刚刚创建)时可以为空.
   */
  private String oldExampleId;

  // ---------------------------- 以下为需要修改的数据 ---------------------------

  /**
   * 具体配置内容数据.
   * 可以为空(刚创建时).
   */
  private String context;

  /**
   * 示例配置数据.
   */
  private String example;

  /**
   * 是否启用.
   */
  private Boolean enable;

}
