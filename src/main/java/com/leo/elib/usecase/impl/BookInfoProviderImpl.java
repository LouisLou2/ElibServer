package com.leo.elib.usecase.impl;

import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BookInfoProviderImpl implements BookInfoProvider {
  @Resource
  private BookInfoMapper bookInfoMapper;
}
