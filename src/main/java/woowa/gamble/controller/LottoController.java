package woowa.gamble.controller;

import woowa.gamble.domain.User;
import woowa.gamble.repository.UserRepository;
import woowa.gamble.service.LottoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @Autowired
    private UserRepository userRepository;

    // 1. 로또 게임 화면 보여주기
    @GetMapping("/game/lotto")
    public String lottoPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("myUserId");
        if (userId == null) return "redirect:/"; // 로그인 안했으면 쫓아냄

        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        return "game/lotto"; // templates/game/lotto.html 파일을 보여줌
    }

    // 2. 로또 구매 및 결과 처리
    @PostMapping("/game/lotto/buy")
    public String buyLotto(@RequestParam("quantity") int quantity,
                           Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("myUserId");
        if (userId == null) return "redirect:/";

        try {
            // 서비스에게 게임 진행 시킴
            Map<String, Object> result = lottoService.playLotto(userId, quantity);

            // 결과를 화면에 전달
            model.addAttribute("result", result);
            model.addAttribute("message", quantity + "장을 구매했습니다!");
        } catch (IllegalArgumentException e) {
            // 돈이 부족하거나 에러가 나면 메시지 전달
            model.addAttribute("error", e.getMessage());
        }

        // 갱신된 사용자 정보 다시 불러오기
        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);

        return "game/lotto"; // 결과와 함께 다시 로또 페이지 표시
    }
}