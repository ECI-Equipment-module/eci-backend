package eci.server.ProjectModule.dto.produceOrg;

import eci.server.ProjectModule.entity.project.ProduceOrganization;
import eci.server.ReleaseModule.entity.ReleaseOrgRelease;
import eci.server.ReleaseModule.entity.ReleaseOrganization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceOrganizationDto {
    private Long id;
    private String name;

    public static ProduceOrganizationDto toDto() {
        return new ProduceOrganizationDto(
        );
    }

    public static ProduceOrganizationDto toDto(ProduceOrganization produceOrganization){

        return new ProduceOrganizationDto(
                produceOrganization.getId(),
                produceOrganization.getName()
        );
    }

    public static ProduceOrganizationDto toDto(ReleaseOrganization produceOrganization){

        return new ProduceOrganizationDto(
                produceOrganization.getId(),
                produceOrganization.getName()
        );
    }

    public static List<ProduceOrganizationDto> toDtoList
            (Collection<ReleaseOrgRelease> produceOrganization){

        return produceOrganization.stream().map(
               ro -> new ProduceOrganizationDto(
                        ro.getReleaseOrganization().getId(),
                        ro.getReleaseOrganization().getName()
                )
        ).collect(Collectors.toList());
    }
}
