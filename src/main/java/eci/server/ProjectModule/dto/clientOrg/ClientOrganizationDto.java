package eci.server.ProjectModule.dto.clientOrg;

import eci.server.ProjectModule.entity.project.ClientOrganization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrganizationDto {
    private Long id;
    private String name;

    public static ClientOrganizationDto toDto(){
        return new ClientOrganizationDto(-1L, "NONE");
    }
    public static ClientOrganizationDto toDto(ClientOrganization clientOrganization){

        return new ClientOrganizationDto(
                clientOrganization.getId(),
                clientOrganization.getName()
        );
    }
}
