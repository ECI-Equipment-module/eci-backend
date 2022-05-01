package eci.server.ProjectModule.dto.clientOrg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrganizationReadResponse {
    private Long id;
    private String name;
}