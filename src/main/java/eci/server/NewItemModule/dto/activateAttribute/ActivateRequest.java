package eci.server.NewItemModule.dto.activateAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 두개 기준
 */
public class ActivateRequest {

    private Long c1;
    private Long c2;
    private Long c3;

}
