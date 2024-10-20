//package com.myweb.MusicLD.entity.elasticSearch;
//
//import com.myweb.MusicLD.utility.enumUtils.AuthenticationType;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.Id;
//import lombok.*;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.util.Date;
//import java.util.List;
//
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Document(indexName = "user-search")
//public class UserSearchEntity{
//
//    @Id
//    @Field(type = FieldType.Long)
//    private String id;
//
//    @Field(type = FieldType.Text)
//    private String username;
//
//    @Field(type = FieldType.Text)
//    private String nickName;
//
//    @Field(type = FieldType.Boolean)
//    private Boolean status;
//
//    @Field(type = FieldType.Date)
//    private Date dateOfBirth;
//
//    @Field(type = FieldType.Boolean)
//    private Boolean gender;
//
//    @Enumerated(EnumType.STRING)
//    @Field(type = FieldType.Keyword)
//    private AuthenticationType authType;
//
//    @Field(type = FieldType.Keyword)
//    private List<Long> paymentIds;
//
//    @Field(type = FieldType.Keyword)
//    private List<Long> avatarIds;
//
//    @Field(type = FieldType.Keyword)
//    private List<Long> roleIds;
//
//    @Field(type = FieldType.Keyword)
//    private List<Long> followingIds;
//
//    @Field(type = FieldType.Keyword)
//    private List<Long> followerIds;
//
//    @Field(type = FieldType.Keyword)
//    private List<Long> musicIds;
//}
