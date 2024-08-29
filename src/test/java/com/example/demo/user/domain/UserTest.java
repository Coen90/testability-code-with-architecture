package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class UserTest {

    @Test
    void UserCreate_객체로_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("bht9011@gmail.com")
                .address("경기")
                .nickname("cooo")
                .build();
        //when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(user.getAddress()).isEqualTo("경기");
        assertThat(user.getNickname()).isEqualTo("cooo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    void UserUpdate_객체로_업데이트할_수_있다() {
        //given
        User user = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("코엔")
                .address("서울")
                .build();
        //when
        user = user.update(userUpdate);
        //then
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(user.getAddress()).isEqualTo("서울");
        assertThat(user.getNickname()).isEqualTo("코엔");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
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
        user = user.login(new TestClockHolder(1000L));
        //then
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getLastLoginAt()).isEqualTo(1000L);
    }

    @Test
    void 유효한_인증코드로_계정을_활성화_할_수_있다() {
        //given
        User user = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .build();
        //when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        //then
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
        //given
        User user = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .build();
        //when
        //then
        assertThatThrownBy(() -> {
            user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}