package woowa.gamble.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class RaceResultDto {
    private List<String> winners;               // 최종 우승자 이름 목록
    private boolean isUserWin;                  // 사용자가 이겼는지 여부
    private long reward;                        // 획득한 상금
    private String userCarName;                 // 사용자가 선택한 차
}