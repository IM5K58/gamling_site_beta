package woowa.gamble.service;

import woowa.gamble.domain.Lotto;
import woowa.gamble.domain.User;
import woowa.gamble.domain.WinningRank;
import woowa.gamble.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LottoService {

    @Autowired
    private UserRepository userRepository;

    private static final int TICKET_PRICE = 2000; // 1장당 2000원

    // 로또 게임을 진행하고 결과를 반환하는 핵심 함수
    @Transactional // DB 정보(돈)가 바뀌므로 트랜잭션 처리
    public Map<String, Object> playLotto(Long userId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();

        // 1. 돈이 충분한지 확인
        long totalCost = (long) quantity * TICKET_PRICE;
        if (user.getMoney() < totalCost) {
            throw new IllegalArgumentException("돈이 부족합니다!");
        }

        // 2. 돈 차감
        user.setMoney(user.getMoney() - totalCost);

        // 3. 사용자 로또 구매 (자동 생성)
        List<Lotto> userLottos = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            userLottos.add(generateRandomLotto());
        }

        // 4. 당첨 번호 및 보너스 번호 생성 (시스템 랜덤)
        Lotto winningLotto = generateRandomLotto();
        int bonusNumber = generateBonusNumber(winningLotto);

        // 5. 당첨 결과 확인 및 상금 계산
        Map<WinningRank, Integer> resultStats = new HashMap<>();
        long totalPrize = 0;

        for (Lotto userLotto : userLottos) {
            int matchCount = userLotto.matchCount(winningLotto);
            boolean matchBonus = userLotto.contains(bonusNumber);

            WinningRank rank = WinningRank.valueOf(matchCount, matchBonus);

            // 통계에 추가
            resultStats.put(rank, resultStats.getOrDefault(rank, 0) + 1);
            totalPrize += rank.getPrize();
        }

        // 6. 당첨금 지급
        user.setMoney(user.getMoney() + totalPrize);
        userRepository.save(user); // 변경된 돈 저장

        // 7. 결과를 모아서 컨트롤러로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("userLottos", userLottos); // 사용자가 산 로또들
        result.put("winningLotto", winningLotto); // 이번주 당첨 번호
        result.put("bonusNumber", bonusNumber); // 보너스 번호
        result.put("stats", resultStats); // 등수별 당첨 횟수
        result.put("totalPrize", totalPrize); // 총 당첨금
        result.put("earningRate", (double) totalPrize / totalCost * 100); // 수익률

        return result;
    }

    // 1~45 사이 랜덤 로또 번호 생성기
    private Lotto generateRandomLotto() {
        List<Integer> numbers = IntStream.rangeClosed(1, 45)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(numbers); // 섞기
        List<Integer> lottoNumbers = numbers.subList(0, 6); // 앞에서 6개 뽑기
        lottoNumbers.sort(Comparator.naturalOrder()); // 정렬
        return new Lotto(lottoNumbers);
    }

    // 보너스 번호 생성 (당첨 번호와 안 겹치게)
    private int generateBonusNumber(Lotto winningLotto) {
        while (true) {
            int bonus = (int) (Math.random() * 45) + 1;
            if (!winningLotto.contains(bonus)) {
                return bonus;
            }
        }
    }
}