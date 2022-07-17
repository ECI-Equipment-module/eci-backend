package eci.server.DocumentModule.entity;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.co.CoMemberId;
import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(DocumentMemberId.class)
public class DocumentMember{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Document document;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

