package org.example.expert.domain.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaPerformanceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private static final int WARMUP_COUNT = 5;
    private static final int TEST_COUNT = 10;

    @Test
    @Order(1)
    @DisplayName("다양한 검색 방식의 성능 비교")
    void compareSearchPerformance() {
        // 테스트할 닉네임 패턴
        String exactNickname = "행복한토끼500000";
        String prefixKeyword = "행복한";
        String containsKeyword = "토끼";
        String suffixKeyword = "500000";

        System.out.println("\n=== 검색 성능 테스트 시작 ===");

        // 1. 정확한 매칭 검색 (인덱스 직접 활용)
        measureExactMatchPerformance(exactNickname);

        // 2. 접두어 검색 (인덱스 활용도 높음)
        measurePrefixSearchPerformance(prefixKeyword);

        // 3. 중간 검색 (인덱스 활용도 낮음)
        measureContainsSearchPerformance(containsKeyword);

        // 4. 접미어 검색 (인덱스 활용도 낮음)
        measureSuffixSearchPerformance(suffixKeyword);
    }

    private void measureExactMatchPerformance(String nickname) {
        System.out.println("\n=== 정확한 매칭 검색 성능 ===");

        // 워밍업
        for (int i = 0; i < WARMUP_COUNT; i++) {
            userRepository.findByNickname(nickname);
        }

        List<Long> executionTimes = new ArrayList<>();

        // 실제 테스트
        for (int i = 0; i < TEST_COUNT; i++) {
            entityManager.clear(); // 캐시 초기화

            long startTime = System.nanoTime();
            Optional<User> result = userRepository.findByNickname(nickname);
            long endTime = System.nanoTime();

            executionTimes.add(endTime - startTime);

            // 첫 실행에서만 결과 존재 여부 출력
            if (i == 0) {
                System.out.printf("결과 존재 여부: %s%n", result.isPresent());
            }
        }

        printPerformanceStats("정확한 매칭", executionTimes);
    }

    private void measurePrefixSearchPerformance(String prefix) {
        System.out.println("\n=== 접두어 검색 성능 ===");

        // 워밍업
        for (int i = 0; i < WARMUP_COUNT; i++) {
            userRepository.findByNicknameStartsWith(prefix);
        }

        List<Long> executionTimes = new ArrayList<>();

        // 실제 테스트
        for (int i = 0; i < TEST_COUNT; i++) {
            entityManager.clear();

            long startTime = System.nanoTime();
            List<User> results = userRepository.findByNicknameStartsWith(prefix);
            long endTime = System.nanoTime();

            executionTimes.add(endTime - startTime);

            // 첫 실행에서만 결과 수 출력
            if (i == 0) {
                System.out.printf("검색된 결과 수: %d%n", results.size());
            }
        }

        printPerformanceStats("접두어 검색", executionTimes);
    }

    private void measureContainsSearchPerformance(String keyword) {
        System.out.println("\n=== 중간 검색 성능 ===");

        // 워밍업
        for (int i = 0; i < WARMUP_COUNT; i++) {
            userRepository.findByNicknameContaining(keyword);
        }

        List<Long> executionTimes = new ArrayList<>();

        // 실제 테스트
        for (int i = 0; i < TEST_COUNT; i++) {
            entityManager.clear();

            long startTime = System.nanoTime();
            List<User> results = userRepository.findByNicknameContaining(keyword);
            long endTime = System.nanoTime();

            executionTimes.add(endTime - startTime);

            // 첫 실행에서만 결과 수 출력
            if (i == 0) {
                System.out.printf("검색된 결과 수: %d%n", results.size());
            }
        }

        printPerformanceStats("중간 검색", executionTimes);
    }

    private void measureSuffixSearchPerformance(String suffix) {
        System.out.println("\n=== 접미어 검색 성능 ===");

        // 워밍업
        for (int i = 0; i < WARMUP_COUNT; i++) {
            userRepository.findByNicknameEndsWith(suffix);
        }

        List<Long> executionTimes = new ArrayList<>();

        // 실제 테스트
        for (int i = 0; i < TEST_COUNT; i++) {
            entityManager.clear();

            long startTime = System.nanoTime();
            List<User> results = userRepository.findByNicknameEndsWith(suffix);
            long endTime = System.nanoTime();

            executionTimes.add(endTime - startTime);

            // 첫 실행에서만 결과 수 출력
            if (i == 0) {
                System.out.printf("검색된 결과 수: %d%n", results.size());
            }
        }

        printPerformanceStats("접미어 검색", executionTimes);
    }

    private void printPerformanceStats(String testName, List<Long> executionTimes) {
        DoubleSummaryStatistics stats = executionTimes.stream()
                .mapToDouble(time -> time / 1_000_000.0) // 나노초를 밀리초로 변환
                .summaryStatistics();

        System.out.println("\n" + testName + " 성능 통계:");
        System.out.printf("평균 실행 시간: %.2f ms%n", stats.getAverage());
        System.out.printf("최소 실행 시간: %.2f ms%n", stats.getMin());
        System.out.printf("최대 실행 시간: %.2f ms%n", stats.getMax());
        System.out.printf("표준 편차: %.2f ms%n", calculateStandardDeviation(executionTimes));
    }

    private double calculateStandardDeviation(List<Long> times) {
        double mean = times.stream().mapToDouble(time -> time / 1_000_000.0).average().orElse(0.0);
        double variance = times.stream()
                .mapToDouble(time -> time / 1_000_000.0)
                .map(time -> Math.pow(time - mean, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

}
