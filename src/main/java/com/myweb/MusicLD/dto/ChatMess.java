package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.utility.enumUtils.ChatStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMess {
    BigInteger id;
    String senderName;
    String receiverName;
    String message;
    String avatar;
    String date;
    ChatStatus status;
}
