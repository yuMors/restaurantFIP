package com.aurora.mapper;

import com.aurora.domain.dto.ArticleSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author
 * elasticsearch
 */
@Repository
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchDTO,Integer> {

}
