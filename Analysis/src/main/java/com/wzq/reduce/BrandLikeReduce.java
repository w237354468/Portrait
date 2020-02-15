package com.wzq.reduce;

import com.wzq.entity.BrandLike;
import org.apache.flink.api.common.functions.ReduceFunction;

public class BrandLikeReduce implements ReduceFunction<BrandLike> {

  @Override
  public BrandLike reduce(BrandLike value1, BrandLike value2) throws Exception {
    Integer brand = value1.getBrand();

    long count1 = value1.getCount();
    long count2 = value2.getCount();

    BrandLike resultBrandLike = new BrandLike();
    resultBrandLike.setBrand(brand);
    resultBrandLike.setCount(count1 + count2);
    return resultBrandLike;
  }
}
