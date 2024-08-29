package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

    @Test
    void User로_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();
        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // then
        assertEquals(myProfileResponse.getId(), 2L);
        assertEquals(myProfileResponse.getEmail(), "bht9011@gmail.com");
        assertEquals(myProfileResponse.getNickname(), "coen");
        assertEquals(myProfileResponse.getAddress(), "seoul");
        assertEquals(myProfileResponse.getStatus(), UserStatus.ACTIVE);
    }
}