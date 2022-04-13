package eci.server.ItemModule.service.member;

import eci.server.ItemModule.dto.member.MemberDto;
<<<<<<< HEAD:src/main/java/eci/server/ItemModule/service/member/MemberService.java
import eci.server.ItemModule.dto.member.MemberListDto;
import eci.server.ItemModule.dto.member.MemberReadCondition;
=======
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7:src/main/java/eci/server/service/member/MemberService.java
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
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

    public MemberListDto readAll(MemberReadCondition cond) {
        return MemberListDto.toDto(
                memberRepository.findAllByCondition(cond)
        );
    }

}