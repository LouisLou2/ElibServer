package com.leo.elib.service.impl;

import com.leo.elib.service.inter.PathManager;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "path")
@Setter
public class PathManagerImpl implements PathManager {
  
  private List<String> filterExcludes;

  @Override
  public boolean permit(String path) {
    for (String exclude : filterExcludes) {
      if (path.startsWith(exclude))
        return true;
    }
    return false;
  }
}