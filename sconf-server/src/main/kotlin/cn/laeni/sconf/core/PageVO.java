/*
 * Copyright 2020-present m@laeni.cn
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.laeni.sconf.core;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 封装分页查询结果数据.
 * <p>
 * Created by DaiWang on 2020/7/6.
 * </p>
 *
 * @param <T> VO数据类型
 * @author DaiWang
 */
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {
  /**
   * 总数据量.
   */
  private Long total;

  /**
   * 当前切片的索引。
   * 索引从0开始
   */
  private Integer index;

  /**
   * 切片的大小。
   */
  private Integer size;

  /**
   * 当前在此切片上的元素数。
   */
  private Integer numberOfElements;

  /**
   * 列表形式的切片内容。
   */
  private Collection<T> content;

  /**
   * 当前的切片是否为第一个。
   */
  private Boolean first;

  /**
   * 当前切片是否为最后一个切片。
   */
  private Boolean isLast;

  /**
   * 是否有下一个切片。
   */
  private Boolean next;

  /**
   * 返回是否有上一个Slice。
   */
  private Boolean previous;

  /**
   * 转为VO发送给前端.
   * 转换器使用参考{@link Stream#map(Function)}.
   *
   * @param <R>    原始数据类型
   * @param page   分页数据
   * @param mapper 从原始类型转为VO类型的转换器
   */
  public <R> PageVO(Page<R> page, Function<R, T> mapper) {
    this.total = page.getTotalElements();
    this.index = page.getNumber();
    this.size = page.getSize();
    this.numberOfElements = page.getNumberOfElements();
    this.content = page.getContent().stream().map(mapper).collect(Collectors.toList());
    this.first = page.isFirst();
    this.isLast = page.isLast();
    this.next = page.hasNext();
    this.previous = page.hasPrevious();
  }
}
