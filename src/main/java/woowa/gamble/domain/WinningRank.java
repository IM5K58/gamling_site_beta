package woowa.gamble.domain;

import java.util.Arrays;

public enum WinningRank {
    // 기존 코드와 동일한 로직 (1등~5등, 꽝)
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    MISS(0, false, 0);

    private final int matchCount;
    private final boolean matchBonus;
    private final int prize;

    WinningRank(int matchCount, boolean matchBonus, int prize) {
        this.matchCount = matchCount;
        this.matchBonus = matchBonus;
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }

    // 맞춘 개수와 보너스 번호 여부로 등수를 찾음
    public static WinningRank valueOf(int matchCount, boolean matchBonus) {
        return Arrays.stream(values())
                .filter(rank -> rank.matchCount == matchCount)
                .filter(rank -> !rank.matchBonus || matchBonus) // 2등 구분을 위한 로직
                .findFirst()
                .orElse(MISS);
    }
}