package eci.server.ProjectModule.dto.produceOrg;

import eci.server.ProjectModule.entity.project.ProduceOrganization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceOrganizationDto {
    private Long id;
    private String name;

    public static ProduceOrganizationDto toDto(ProduceOrganization produceOrganization){

        return new ProduceOrganizationDto(
                produceOrganization.getId(),
                produceOrganization.getName()
        );
    }
}
