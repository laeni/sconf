/**
 * [一维数组]从原始数组中删除满足条件的元素.
 *
 * @param sources 原始数组
 * @param equals  判断条件
 * @param whole   [可选]是否全部遍历,默认情况主要找到第一个就将其中断,如果为true,则将会删除所有满足条件的值
 */
export function deleteArrayChild<T>(sources: T[], equals: (target: T) => boolean, whole: boolean = false): boolean {
  let mark = false;
  for (let i = 0; i < sources.length; i++) {
    if (equals(sources[i])) {
      sources.splice(i, 1);
      mark = true;
      if (!whole) {
        break;
      }
      i--;
    }
  }
  return mark;
}

/**
 * [嵌套数组,多维]删除当前数组中满足条件的元素,如果没有找到则递归删除器子元素(必须使用children属性表示子元素)中满足条件的元素.
 * @param sources 待删除的元素组成的数组
 * @param bo 判断条件,即条件成立的将被删除
 */
export function deleteChildrenItem<T extends { children?: T[]; }>(sources: T[], bo: (o: T) => boolean): boolean {
  // 在当前层中删除目标元素
  if (deleteArrayChild(sources, o => bo(o))) {
    return true;
  } else {
    // 如果当前层中没有删除目标元素则依次遍历子元素进行删除
    for (const o2 of sources) {
      if (o2.children && deleteChildrenItem(o2.children, bo)) {
        return true;
      }
    }
  }
  return false;
}
