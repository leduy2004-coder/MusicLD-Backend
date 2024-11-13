package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.utility.enumUtils.ChatStatus;
import lombok.*;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMess {
    private BigInteger id;
    private String senderName;
    private String receiverName;
    private String message;
    private String avatar;
    private String date;
    private ChatStatus status;
}
