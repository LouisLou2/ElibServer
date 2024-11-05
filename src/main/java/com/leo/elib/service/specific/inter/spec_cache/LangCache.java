package com.leo.elib.service.specific.inter.spec_cache;

public interface LangCache {
  String getLangName(byte langId);
  boolean isOtherLang(byte langId);
}
