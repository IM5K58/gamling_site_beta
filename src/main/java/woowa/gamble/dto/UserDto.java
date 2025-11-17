package woowa.gamble.dto;

import woowa.gamble.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String userName;
    private Long money;

    public UserDto(User user) {
        this.userName = user.getUserName();
        this.money = user.getMoney();
    }
}