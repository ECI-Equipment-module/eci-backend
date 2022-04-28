package eci.server.ProjectModule.dto.produceOrg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProduceOrganizationReadResponse{
        private Long id;
        private String name;
    }