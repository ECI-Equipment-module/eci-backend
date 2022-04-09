package eci.server.ItemModule.repository.member;

import eci.server.ItemModule.dto.member.MemberReadCondition;
import eci.server.ItemModule.dto.member.MemberSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomMemberRepository {
    Page<MemberSimpleDto> findAllByCondition(MemberReadCondition cond);
}
