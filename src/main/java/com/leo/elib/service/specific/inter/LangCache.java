package com.leo.elib.service.specific.inter;

public interface LangCache {
  String getLangName(byte langId);
  boolean isOtherLang(byte langId);
}
