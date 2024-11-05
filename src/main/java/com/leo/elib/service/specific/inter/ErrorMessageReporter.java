package com.leo.elib.service.specific.inter;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;

// 一般来说ResCodeEnum就足够标记错误，但是有些情况需要报告更加详细的错误信息
public interface ErrorMessageReporter {
  String getSpecificMsg(ResCodeEnum resCode);
  NullablePair<ResCodeEnum,String> getSpecificMsgWithCode(ResCodeEnum resCode);
}
