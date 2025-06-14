package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calculator") // operation extension as given in the description
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    // main part of the task that does the summation operation, reads the operands sends to the operation (sum)
    @GetMapping("/add")
    public ResponseEntity<Map<String, BigDecimal>> sum(
            @RequestParam("operands") String csvNumbers
    ) {
        logger.info("ADD endpoint called with operands='{}'", csvNumbers);
        List<BigDecimal> nums = parseOperands(csvNumbers);
        BigDecimal sum = nums.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Computed sum={} from operands={}", sum, nums);
        return ResponseEntity.ok(Map.of("sum", sum));
    }

    @GetMapping("/multiply")
    public ResponseEntity<Map<String, BigDecimal>> multiply(
            @RequestParam("operands") String csvNumbers
    ) {
        logger.info("MULTIPLY endpoint called with operands='{}'", csvNumbers);
        List<BigDecimal> nums = parseOperands(csvNumbers);
        BigDecimal product = nums.stream()
                .reduce(BigDecimal.ONE, BigDecimal::multiply);
        logger.info("Computed product={} from operands={}", product, nums);
        return ResponseEntity.ok(Map.of("product", product));
    }

    @GetMapping("/average")
    public ResponseEntity<Map<String, BigDecimal>> average(
            @RequestParam("operands") String csvNumbers
    ) {
        logger.info("AVERAGE endpoint called with operands='{}'", csvNumbers);
        List<BigDecimal> nums = parseOperands(csvNumbers);
        BigDecimal sum = nums.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal count = new BigDecimal(nums.size());
        BigDecimal avg = sum.divide(count, MATH_CONTEXT);
        logger.info("Computed average={} (sum={} / count={})", avg, sum, nums.size());
        return ResponseEntity.ok(Map.of("average", avg));
    }

    @GetMapping("/stdev")
    public ResponseEntity<Map<String, BigDecimal>> stdev(
            @RequestParam("operands") String csvNumbers
    ) {
        logger.info("STDEV endpoint called with operands='{}'", csvNumbers);
        List<BigDecimal> nums = parseOperands(csvNumbers);
        BigDecimal count = new BigDecimal(nums.size());

        BigDecimal sum = nums.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal mean = sum.divide(count, MATH_CONTEXT);
        logger.debug("Mean computed as {} from sum={} and count={}", mean, sum, nums.size());

        BigDecimal varianceSum = nums.stream()
                .map(d -> d.subtract(mean, MATH_CONTEXT)
                        .pow(2, MATH_CONTEXT))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal variance = varianceSum.divide(count, MATH_CONTEXT);
        logger.debug("Variance computed as {} from varianceSum={} and count={}", variance, varianceSum, nums.size());

        // stdDev = sqrt(variance) (approximate via double)
        BigDecimal stdDev = new BigDecimal(
                Math.sqrt(variance.doubleValue()),
                MATH_CONTEXT
        );
        logger.info("Computed standard deviation={} from variance={}", stdDev, variance);

        return ResponseEntity.ok(Map.of("stdev", stdDev));
    }

    // the method that handles the input from operands. Does the comma separation and extracts the values into java format
    private List<BigDecimal> parseOperands(String csv) {
        logger.debug("Parsing operands string='{}'", csv);
        if (csv == null || csv.trim().isEmpty()) {
            logger.warn("No operands provided");
            throw new IllegalArgumentException("No operands provided");
        }

        String[] parts = csv.split(",", -1);
        List<BigDecimal> numbers = new ArrayList<>();
        List<String> badEntries = new ArrayList<>();

        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                logger.warn("Empty operand detected");
                badEntries.add(part);
            } else {
                try {
                    BigDecimal bd = new BigDecimal(trimmed, MATH_CONTEXT);
                    numbers.add(bd);
                    logger.debug("Parsed operand {}", bd);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid operand '{}'", part);
                    badEntries.add(part);
                }
            }
        }

        if (!badEntries.isEmpty()) {
            logger.error("Invalid operands encountered: {}", badEntries);
            throw new IllegalArgumentException("Invalid operands: " + badEntries);
        }

        logger.debug("Successfully parsed operands: {}", numbers);
        return numbers;
    }
}
