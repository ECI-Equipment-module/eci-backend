package eci.server.NewItemModule.dto.responsibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsibleDto {
    private Long id;
    private String name;
    private String range;

    public ResponsibleDto toDto(
            String name,
            String range

    ) {
        return new ResponsibleDto(
                id,
                name,
                range
        );
    }
}
