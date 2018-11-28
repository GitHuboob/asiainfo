package com.asiainfo.workbench.search.dao;

import com.asiainfo.workbench.search.domain.SendInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface SendInfoRepository extends ElasticsearchRepository<SendInfo,Long> {

}

