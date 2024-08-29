package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_개인정보_없이_전달_받을_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build());
        //when
        UserController userController = testContainer.userController;
        ResponseEntity<UserResponse> result = userController.getUserById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("coen");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 존재하지_않는_유저의_아이디로_api_호출을_할_경우_404_응답을_받는다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        UserController userController = testContainer.userController;
        //when
        //then
        assertThatThrownBy(() -> {
            userController.getUserById(11241278);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build());
        //when
        UserController userController = testContainer.userController;
        ResponseEntity<Void> result = userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(result.getBody()).isNull();

        testContainer.userRepository.findById(1L).ifPresent(user -> {
            assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        });
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(124912894L))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build());
        //when
        UserController userController = testContainer.userController;
        ResponseEntity<MyProfileResponse> result = userController.getMyInfo("bht9011@gmail.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("coen");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("seoul");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(124912894L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build());
        //when
        UserController userController = testContainer.userController;
        ResponseEntity<MyProfileResponse> result = userController.updateMyInfo("bht9011@gmail.com", UserUpdate.builder()
                .address("경기")
                .nickname("cooo")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("cooo");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("경기");
    }

}