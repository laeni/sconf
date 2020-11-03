export default class Page<T> {
  /**
   * 总数据量.
   */
  total?: number;

  /**
   * 当前切片的索引。
   * 索引从0开始
   */
  index?: number;

  /**
   * 切片的大小。
   */
  size?: number;

  /**
   * 当前在此切片上的元素数。
   */
  numberOfElements?: number;

  /**
   * 列表形式的切片内容。
   */
  content?: T[];

  /**
   * 当前的切片是否为第一个。
   */
  first?: boolean;

  /**
   * 当前切片是否为最后一个切片。
   */
  isLast?: boolean;

  /**
   * 是否有下一个切片。
   */
  next?: boolean;

  /**
   * 返回是否有上一个Slice。
   */
  previous?: boolean;
}
