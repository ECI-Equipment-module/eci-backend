package eci.server.NewItemModule.dto.maker;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakerDto {
    private Long id;
    private String code;
    private String name;
}
