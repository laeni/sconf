/*
 * Copyright 2020-present m@laeni.cn
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.laeni.sconf.server.db;

import cn.laeni.personal.util.StringUtils;
import cn.laeni.sconf.core.Storage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 基于数据库的配置存储实现.
 * 即所有应用配置将存放于本系统使用的数据库中.
 *
 * @author Laeni
 */
@Service
public class DbStorage implements Storage {
  private final ConfValueRepository confValueRepository;

  public DbStorage(ConfValueRepository confValueRepository) {
    this.confValueRepository = confValueRepository;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public String create(String value) {
    ConfValueEntity entity = ConfValueEntity.builder().value(value).build();
    confValueRepository.save(entity);
    return entity.getId().toString();
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public String get(String id) {
    final String value = confValueRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new RuntimeException(id + " 对应的配置不存在")).getValue();
    return value == null ? "" : value;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void delete(String id) {
    confValueRepository.deleteById(Integer.valueOf(id));
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void addLabel(String id, String label) {
    ConfValueEntity entity = confValueRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new RuntimeException(id + " 对应的配置不存在"));
    if (entity.getLabels() == null) {
      entity.setLabels(new HashSet<>());
    }
    entity.getLabels().add(label);
    confValueRepository.save(entity);
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void removeLabel(String id, String label) {
    ConfValueEntity entity = confValueRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new RuntimeException(id + " 对应的配置不存在"));
    if (entity.getLabels() != null) {
      entity.getLabels().remove(label);
      confValueRepository.save(entity);
    }
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public Collection<String> getAllGroupBy(String label) {
    // 标签如果为空则使用默认值
    String v = StringUtils.isEmpty(label) ? "default" : label;

    return confValueRepository.findAll()
        .stream()
        .filter(t -> t.getLabels().contains(v))
        .map(it -> it.getId().toString())
        .collect(Collectors.toList());
  }
}
