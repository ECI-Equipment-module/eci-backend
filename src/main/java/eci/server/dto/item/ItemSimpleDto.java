package eci.server.dto.item;


import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSimpleDto {
    private Long id;
    private String name;
    private String type;
    private Long width;
    private Long height;
    private Long weight;
    private String username;
    private List<ImageDto> thumbnail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}