package eci.server.ProjectModule.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequest {
    @Null
    private Long memberId;
}
