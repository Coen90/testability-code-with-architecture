package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져_보내지는지_테스트() {
        //given
        FakeMailSender mailSender = new FakeMailSender();
        CertificationServiceImpl certificationService = new CertificationServiceImpl(mailSender);

        //when
        certificationService.send("bht9011@gmail.com", 2L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        //then
        Assertions.assertThat(mailSender.email).isEqualTo("bht9011@gmail.com");
        Assertions.assertThat(mailSender.title).isEqualTo("Please certify your email address");
        Assertions.assertThat(mailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/2/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}