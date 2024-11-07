package com.leo.elib.service.specific.inter.cache.static_type;

public interface LangCache {
  String getLangName(byte langId);
  boolean isOtherLang(byte langId);
}
