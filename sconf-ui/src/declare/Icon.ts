/**
 * 应用图标.
 * @author laeni
 */
export class Icon {
  /**
   * 图标类型.
   * <p>
   *   IMAGES: 图片
   *   ANT_DESIGN: Ant Design 图标名
   *   ICONFONT: 阿里妈妈矢量图标库名
   *   SVG: 直接存放相应的svg代码
   * </p>
   */
  type: 'IMAGES' | 'ANT_DESIGN' | 'ICONFONT' | 'SVG';
  /**
   * 具体的图标内容(根据{@link #type}的值,可能为url/svg代码/图标名等).
   */
  value: string;
}
