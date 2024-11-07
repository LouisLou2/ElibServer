package com.leo.elib.service.specific.impl.cache.static_type;

import com.leo.elib.service.specific.inter.cache.static_type.LangCache;
import org.springframework.stereotype.Service;

@Service
public class LangCacheImpl implements LangCache {
  static private final String[] langNames = {
    "Other", // 0 其他小众语言
    "Chinese", // 1
    "English", // 2
    "Japanese",
    "Korean",
    "French",
    "German",
    "Russian",
    "Spanish",
    "Italian",
    "Portuguese",
    "Arabic",
    "Hindi",
    "Bengali",
    "Urdu",
    "Turkish",
    "Vietnamese",
    "Thai",
    "Indonesian",
    "Malay",
    "Filipino",
    "Dutch",
    "Swedish",
    "Danish",
    "Norwegian",
    "Finnish",
    "Polish",
    "Czech",
    "Slovak",
    "Hungarian",
    "Romanian",
    "Bulgarian",
    "Greek",
    "Serbian",
    "Croatian"
  };
  @Override
  public String getLangName(byte langId) {
    assert langId >= 0 && langId < langNames.length;
    return langNames[langId];
  }

  @Override
  public boolean isOtherLang(byte langId) {
    return langId == 0;
  }
}
