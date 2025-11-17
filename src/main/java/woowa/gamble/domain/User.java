package woowa.gamble.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 사이트를 벗어나면 새 유저가 되므로 닉네임 중복 허용 (unique 제거)
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "money", nullable = false)
    private Long money;

    public User(String userName) {
        this.userName = userName;
        this.money = 100000L; // 초기 자금 10만원
    }
}