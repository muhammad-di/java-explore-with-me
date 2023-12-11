package ru.practicum.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "email should be more then 5 and less than 255 symbols")
    private String email;

    @NotBlank
    @Size(min = 2, max = 250, message = "name should be more then 1 and less than 251 symbols")
    private String name;
}
