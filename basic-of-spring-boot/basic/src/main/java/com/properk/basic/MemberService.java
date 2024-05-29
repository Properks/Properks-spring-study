package com.properk.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    public List<Member> test() {
        // Create
        memberRepository.save(new Member(1L, "John"));

        //Search
        Optional<Member> member = memberRepository.findById(1L);
        List<Member> memberList = memberRepository.findAll();

        //Remove
        memberRepository.deleteById(1L);
        return memberList;
    }
}
