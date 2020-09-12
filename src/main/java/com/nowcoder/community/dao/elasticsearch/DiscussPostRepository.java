package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @InterfaceName DiscussPostRepository
 * @Description es的帖子数据层接口
 * @Author cxc
 * @Date 2020/9/4 23:36
 * @Verseion 1.0
 **/
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

}
