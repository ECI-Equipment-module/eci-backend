package eci.server.NewItemModule.dto.maker;

import lombok.AllArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class CustomMakerResponse {
    private Long id;
    private String code;
    private String name;
}
