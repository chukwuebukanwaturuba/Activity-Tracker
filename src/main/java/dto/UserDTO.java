package dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @NotNull(message = "Not blank")
    private String name;
    private String email;
    private String password;
}
