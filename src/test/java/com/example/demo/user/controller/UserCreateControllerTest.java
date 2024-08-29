package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    void 사용자는_회원가입을_할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-bbbb-aaaaaaaaaaaa"))
                .build();

        //when
        UserCreateController userCreateController = testContainer.userCreateController;
        ResponseEntity<UserResponse> result = userCreateController.createUser(UserCreate.builder()
                .address("seoul")
                .email("bht9011@gmail.com")
                .nickname("coen")
                .build());
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("coen");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(testContainer.userRepository.getById(1L).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-bbbb-aaaaaaaaaaaa");
    }

}