package com.example.demo.src.interest;


import com.example.demo.src.interest.model.PostInterestReq;
import com.example.demo.src.interest.model.PostInterestRes;
import com.example.demo.src.itemPost.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface InterestDao {


    @Insert("insert into Interest(memberId, postId, interestType) values(#{memberId}, #{postId}, '중고거래')")
    @Options(useGeneratedKeys = true, keyProperty = "interestId")
    public int insertInterest(PostInterestReq req);

    @Select("select status from Interest where interestId = #{interestId}")
    public PostInterestRes getInterestStatus(Integer interestId);

    @Select("select count(*) from Interest where postId = #{postId} and interestType = '중고거래' and memberId = #{memberId}")
    public int getInterest(PostInterestReq req);

    @Select("select interestId from Interest where postId = #{postId} and interestType = '중고거래' and memberId = #{memberId}")
    public int getInterestId(PostInterestReq req);


    @Update("update Interest set status = 0 where postId = #{postId} and interestType = '중고거래' and memberId = #{memberId}")
    public int modifyToCancel(PostInterestReq req);

    @Update("update Interest set status = 1 where postId = #{postId} and interestType = '중고거래' and memberId = #{memberId}")
    public int modifyToInterest(PostInterestReq req);


    @Select("select count(*) cnt, postStatus from Itempost where itemPostId = #{postId}")
    public HashMap<String, String> checkPostExists(Integer postId);



}
