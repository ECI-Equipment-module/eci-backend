package eci.server.BomModule.dto.compare;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompareRequestDto {
    Long compareId;
    Long againstId;
}
