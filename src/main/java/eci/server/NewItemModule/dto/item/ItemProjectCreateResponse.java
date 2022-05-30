package eci.server.NewItemModule.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemProjectCreateResponse {
    private Long id;
    private String name;
}
