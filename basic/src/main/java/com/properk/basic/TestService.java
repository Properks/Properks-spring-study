package com.properk.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired // Inject bean
    MemberRepository memberRepository;

    public List<Member> getAllMember() {
        return memberRepository.findAll();
    }
}
