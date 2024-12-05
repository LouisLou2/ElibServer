package com.leo.elib.comp_struct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FixedLengthList<T> {
  int maxLength;
  int toleratedLength;
  int maxCapacity;

  List<T> lis;

  public FixedLengthList(int maxLength, int toleratedLength) {
    assert maxLength > 0 && toleratedLength >= 0;
    this.maxLength = maxLength;
    this.toleratedLength = toleratedLength;
    this.maxCapacity = maxLength + toleratedLength;
    lis = new ArrayList<>();
  }

  // 注意这个需要最新的在前面
  public void flush(List<T> alis) {
    assert alis!=null;
    lis.clear();
    int begin = Math.min(maxLength - 1, alis.size() - 1);
    // reverse
    for (int i = begin; i >= 0; --i) {
      lis.add(alis.get(i));
    }
  }

  public void addNew(T t) {
    // 如果本次添加后将使得超过最大容量，则将数据重新变为maxLength
    if (lis.size() + 1 > maxCapacity) {
      lis = new ArrayList<>(lis.subList(0, maxLength));
    }
    lis.add(t);
  }

  public List<T> getNewest(int offset, int num) {
    int oldestInd = Math.max(lis.size() - maxLength, 0);
    int newestInd = lis.size() - 1;
    int startInd = Math.max(newestInd - offset, oldestInd - 1);
    if (startInd < oldestInd) {
      return new ArrayList<>();
    }
    int endNextInd = Math.max(startInd - num - 1, oldestInd -1 );
    List<T> res = new ArrayList<>();
    for (int i = startInd; i > endNextInd; --i) {
      res.add(lis.get(i));
    }
    return res;
  }

  public <R> FixedLengthList<R> getCopyMapped(Function<T, R> mapper) {
    FixedLengthList<R> res = new FixedLengthList<>(maxLength, toleratedLength);
    res.lis = new ArrayList<>(lis.size());
    for (T t : lis) {
      res.lis.add(mapper.apply(t));
    }
    return res;
  }
}
