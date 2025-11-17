package woowa.gamble.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        Set<Integer> uniqueNumbers = new HashSet<>(numbers);
        if (uniqueNumbers.size() != numbers.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호에 중복된 숫자가 있습니다.");
        }
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    // 당첨 번호와 비교해서 몇 개 맞았는지 반환
    public int matchCount(Lotto winningLotto) {
        return (int) numbers.stream()
                .filter(winningLotto.getNumbers()::contains)
                .count();
    }

    // 보너스 번호를 포함하고 있는지 확인
    public boolean contains(int number) {
        return numbers.contains(number);
    }
}