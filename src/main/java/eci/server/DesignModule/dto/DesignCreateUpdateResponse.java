package eci.server.DesignModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DesignCreateUpdateResponse {
        private Long id;
        private Long routeId;
}
