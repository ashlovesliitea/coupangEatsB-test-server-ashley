package com.example.demo.src.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSocialOAuthRes {
    private int userIdx;
    private String jwtToken;
}
