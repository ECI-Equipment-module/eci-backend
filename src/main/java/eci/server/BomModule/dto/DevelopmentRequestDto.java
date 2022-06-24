package eci.server.BomModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentRequestDto {
    Long parentId;
    Long childId;
}
