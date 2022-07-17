package eci.server.ProjectModule.entity.project;

import eci.server.ItemModule.entity.member.Member;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectMemberId implements Serializable {
    private Project project;
    private Member member;
}

