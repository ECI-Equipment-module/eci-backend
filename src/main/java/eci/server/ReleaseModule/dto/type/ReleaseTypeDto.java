package eci.server.ReleaseModule.dto.type;

import eci.server.ReleaseModule.entity.ReleaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseTypeDto {
    private Long id;
    private String name;

    public static ReleaseTypeDto toDto() {
        return new ReleaseTypeDto(
        );
    }

    public static ReleaseTypeDto toDto(ReleaseType releaseType){

        return new ReleaseTypeDto(
                releaseType.getId(),
                releaseType.getName()
        );
    }
}
