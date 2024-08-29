package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(12381923L))
                .userRepository(fakeUserRepository)
                .build();
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("서울시 광진구")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());
        fakeUserRepository.save(User.builder()
                .id(3L)
                .email("bht9012@gmail.com")
                .nickname("coen")
                .address("서울시 광진구")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail_은_ACTIVE_유저데이터를_찾아올_수_있다() {
        //given
        String email = "bht9011@gmail.com";

        //when
        User result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("coen");
    }

    @Test
    void getByEmail_은_PENDING_유저데이터를_찾아올_수_없다() {
        //given
        String email = "bht9012@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById_은_ACTIVE_유저데이터를_찾아올_수_있다() {
        //given
        //when
        User result = userService.getById(2L);

        //then
        assertThat(result.getNickname()).isEqualTo("coen");
        assertThat(result.getEmail()).isEqualTo("bht9011@gmail.com");
    }

    @Test
    void getById_은_PENDING_유저데이터를_찾아올_수_없다() {
        //given
        String email = "bht9012@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getById(3L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용해서_유저를_생성한다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("bht9011@gmail.com")
                .address("경기")
                .nickname("cooo")
                .build();

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    void userCreateDto_를_이용해서_유저를_수정한다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("경기")
                .nickname("cooo")
                .build();

        //when
        User result = userService.update(2L, userUpdate);

        //then
        User byId = userService.getById(2L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getAddress()).isEqualTo("경기");
        assertThat(result.getNickname()).isEqualTo("cooo");

//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }


    @Test
    void login_를_이용해서_로그인_시키면_마지막_로그인_시간이_업데이트된다() {
        //given
        //when
        userService.login(2L);

        //then
        User result = userService.getById(2L);
        assertThat(result.getLastLoginAt()).isEqualTo(12381923L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        //given
        //when
        userService.verifyEmail(3L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        //then
        User result = userService.getById(3L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);

//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> userService.verifyEmail(3L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}