package com.example.demo.medium.controller.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;

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

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void userCreateDto_를_이용해서_유저를_수정한다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("경기")
                .nickname("cooo")
                .build();

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

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
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);

//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
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

//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

}