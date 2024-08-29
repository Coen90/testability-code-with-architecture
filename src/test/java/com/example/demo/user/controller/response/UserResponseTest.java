package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void User_로_UserResponse_응답을_만들_수_있다() {
        //given
        User user = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();
        //when
        UserResponse userResponse = UserResponse.from(user);
        //then
        assertThat(userResponse.getId()).isEqualTo(2L);
        assertThat(userResponse.getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(userResponse.getNickname()).isEqualTo("coen");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}