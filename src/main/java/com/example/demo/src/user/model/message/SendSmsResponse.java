package com.example.demo.src.user.model.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class SendSmsResponse {
    private String statusCode;
    private String statusName;
    private String requestId;
    private Timestamp requestTime;
}
