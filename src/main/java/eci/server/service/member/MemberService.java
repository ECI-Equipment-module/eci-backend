package eci.server.service.member;

import eci.server.dto.member.MemberDto;
<<<<<<< HEAD
import eci.server.exception.member.sign.MemberNotFoundException;
=======
import eci.server.exception.member.MemberNotFoundException;
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
import eci.server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto read(Long id) {
        return MemberDto.toDto(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public void delete(Long id) {
        if(notExistsMember(id)) throw new MemberNotFoundException();
        memberRepository.deleteById(id);
    }

    private boolean notExistsMember(Long id) {
        return !memberRepository.existsById(id);
    }

}