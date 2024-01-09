//package swm.hkcc.LGTM.app.modules.suggestion.repository;
//
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import swm.hkcc.LGTM.app.modules.suggestion.domain.Suggestion;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class SuggestionRepositoryTest {
//
//    @Autowired
//    private SuggestionRepository suggestionRepository;
//
//    void test() throws InterruptedException {
//        // given
//        Suggestion suggestion = Suggestion.builder()
//                .title("title")
//                .description("description")
//                .build();
//
//        suggestionRepository.save(suggestion);
//
//        CountDownLatch countDownLatch = new CountDownLatch(10);
//        List<LikeWorker> workers = Stream
//                .generate(() -> new LikeWorker(countDownLatch, suggestion.getSuggestionId()))
//                .limit(10)
//                .toList();
//
//        // when
//        workers.forEach(worker -> new Thread(worker).start());
//
//        countDownLatch.await();
//
//        // then
//        Suggestion resultSuggestion = suggestionRepository.findById(suggestion.getSuggestionId()).orElseThrow(RuntimeException::new);
//        assertThat(resultSuggestion.getLikeNum()).isEqualTo(10);
//    }
//
//    private class LikeWorker implements Runnable {
//        private CountDownLatch countDownLatch;
//        private final Long suggestionId;
//
//        public LikeWorker(CountDownLatch countDownLatch, Long suggestionId) {
//            this.countDownLatch = countDownLatch;
//            this.suggestionId = suggestionId;
//        }
//
//        @Override
//        public void run() {
//            plusLike(0);
//            countDownLatch.countDown();
//        }
//
//        @Transactional
//        private void plusLike(int depth) {
//            Suggestion suggestion = suggestionRepository.findById(suggestionId).orElseThrow(RuntimeException::new);
//            suggestion.setLikeNum(suggestion.getLikeNum() + 1);
//            suggestionRepository.save(suggestion);
//        }
//    }
//}
