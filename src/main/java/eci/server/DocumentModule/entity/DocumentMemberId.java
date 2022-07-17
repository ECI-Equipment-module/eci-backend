package eci.server.DocumentModule.entity;

import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DocumentMemberId implements Serializable {
    private Document document;
    private Member member;
}

