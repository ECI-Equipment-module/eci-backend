package eci.server.BomModule.dto.dev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentRequestDto {
    List<Long> parentId;
    List<Long> childId;
    Long devId;
}
