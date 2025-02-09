//package com.myweb.MusicLD.repository.elasticSearch;
//
//
//import com.myweb.MusicLD.entity.UserEntity;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.annotations.Query;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
//import java.math.BigInteger;
//import java.util.List;
//
//@Repository("userSearchRepository")
//public interface UserSearchRepository extends ElasticsearchRepository<UserEntity, BigInteger> {
//    // Tìm kiếm gần giống (fuzzy) với độ sai số tối đa 2 ký tự
//    @Query("{\"match\": {\"nickname\": {\"query\": \"?0\", \"fuzziness\": 2}}}")
//    List<UserEntity> searchByNickname(String searchString, Pageable pageable);
//}
//
