package eci.server.CRCOModule.dto.co;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.CRCOModule.dto.featuresdtos.CrReasonDto;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoSearchDto {
    private Long coId;
    private String coNumber;
    private String coType;
    private CrReasonDto coReason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static CoSearchDto toDto(){
        return  new CoSearchDto();
    }


    public static CoSearchDto toDto(
            ChangeOrder co
    ) {

        return new CoSearchDto(
                co.getId(),
                co.getCoNumber(),
                "FULL TRACK",
                CrReasonDto.toDto(co.getCoReason()),
                co.getCreatedAt()
        );
    }
}
