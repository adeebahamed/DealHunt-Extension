package com.dealhunt.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dealhunt.dto.NormalizedProduct;
import com.dealhunt.dto.ProductCandidate;
import com.dealhunt.dto.ProductMatchResult;

@Service
public class ProductMatchingService {

    private static final Set<String> STOP_WORDS =
            new HashSet<>(Arrays.asList(
                    "the",
                    "mens",
                    "men",
                    "womens",
                    "women",
                    "for",
                    "and",
                    "with",
                    "by",
                    "co",
                    "ltd",
                    "lifestyle"
            ));

    public ProductMatchResult match(
            NormalizedProduct original,
            ProductCandidate candidate) {

        if (original == null || candidate == null) {

            return new ProductMatchResult(
                    false,
                    0.0,
                    "Missing product information"
            );
        }

        String originalName =
                normalize(original.getName());

        String originalBrand =
                normalize(original.getBrand());

        String originalCategory =
                normalize(original.getCategory());

        String candidateTitle =
                normalize(candidate.getTitle());

        String candidateBrand =
                normalize(candidate.getBrand());

        if (candidateTitle.isBlank()) {

            return new ProductMatchResult(
                    false,
                    0.0,
                    "Candidate product title is missing."
            );
        }

        /*
         * ---------------------------------------------------------
         * 1. BRAND MATCH
         * ---------------------------------------------------------
         */

        boolean brandMatches = false;

        if (!originalBrand.isBlank() &&
                !candidateBrand.isBlank()) {

            brandMatches =
                    originalBrand.equals(candidateBrand);
        }

        /*
         * If candidate brand was not extracted correctly,
         * check whether the brand exists in the title.
         */

        if (!brandMatches &&
                !originalBrand.isBlank()) {

            brandMatches =
                    containsWord(
                            candidateTitle,
                            originalBrand
                    );
        }

        /*
         * ---------------------------------------------------------
         * 2. PRODUCT TYPE
         * ---------------------------------------------------------
         */

        boolean productTypeMatches =
                productTypeMatches(
                        originalName,
                        originalCategory,
                        candidateTitle
                );

        /*
         * ---------------------------------------------------------
         * 3. PRODUCT NAME SIMILARITY
         * ---------------------------------------------------------
         */

        int matchingTerms =
                countMatchingTerms(
                        originalName,
                        candidateTitle
                );

        int originalTermCount =
                meaningfulTerms(originalName).size();

        double nameSimilarity = 0.0;

        if (originalTermCount > 0) {

            nameSimilarity =
                    (matchingTerms * 100.0)
                            / originalTermCount;
        }

        /*
         * ---------------------------------------------------------
         * SCORE
         * ---------------------------------------------------------
         *
         * Brand       = 40
         * Product type = 20
         * Name        = 40
         *
         * Total = 100
         */

        double score = 0.0;

        StringBuilder reasons =
                new StringBuilder();

        if (brandMatches) {

            score += 40;

            reasons.append(
                    "Brand matches. "
            );

        } else if (!originalBrand.isBlank()) {

            reasons.append(
                    "Brand does not match. "
            );
        }

        if (productTypeMatches) {

            score += 20;

            reasons.append(
                    "Product type matches. "
            );

        } else {

            reasons.append(
                    "Product type does not match. "
            );
        }

        /*
         * Strong name matching.
         */

        if (nameSimilarity >= 80) {

            score += 40;

            reasons.append(
                    "Product name is an excellent match. "
            );

        } else if (nameSimilarity >= 40) {

            score += 25;

            reasons.append(
                    "Some product name terms match. "
            );
        }

        /*
         * ---------------------------------------------------------
         * 4. SPECIAL MISMATCH PENALTIES
         * ---------------------------------------------------------
         */

        boolean wrongProductType =
                isObviousTypeMismatch(
                        originalName,
                        originalCategory,
                        candidateTitle
                );

        if (wrongProductType) {

            score -= 25;

            reasons.append(
                    "Obvious product type mismatch. "
            );
        }

        /*
         * Women's result for a generic/men's product should not
         * outrank the correct gender.
         */

        if (isGenderMismatch(
                originalName,
                candidateTitle)) {

            score -= 20;

            reasons.append(
                    "Gender/target mismatch. "
            );
        }

        if (score < 0) {
            score = 0;
        }

        /*
         * A product needs both reasonable identity and type
         * compatibility.
         */

        boolean matched =
                brandMatches &&
                productTypeMatches &&
                score >= 60 &&
                !wrongProductType;

        if (reasons.length() == 0) {

            reasons.append(
                    "Insufficient matching information."
            );
        }

        return new ProductMatchResult(
                matched,
                score,
                reasons.toString().trim()
        );
    }

    /*
     * -------------------------------------------------------------
     * PRODUCT TYPE MATCHING
     * -------------------------------------------------------------
     */

    private boolean productTypeMatches(
            String originalName,
            String originalCategory,
            String candidateTitle) {

        String originalText =
                (originalName + " " + originalCategory)
                        .toLowerCase(Locale.ROOT);

        String candidate =
                candidateTitle.toLowerCase(Locale.ROOT);

        /*
         * Casual shoes / casual sneakers
         */

        if (containsAny(
                originalText,
                "casual shoes",
                "casual shoe",
                "casual sneaker",
                "casual sneakers")) {

            return containsAny(
                    candidate,
                    "casual shoe",
                    "casual shoes",
                    "casual sneaker",
                    "casual sneakers",
                    "lace up",
                    "lace-up",
                    "sneaker",
                    "sneakers"
            );
        }

        /*
         * Running shoes
         */

        if (containsAny(
                originalText,
                "running shoes",
                "running shoe")) {

            return containsAny(
                    candidate,
                    "running shoe",
                    "running shoes",
                    "runner",
                    "running sneaker"
            );
        }

        /*
         * General shoe category.
         */

        if (containsAny(
                originalText,
                "shoes",
                "shoe",
                "footwear")) {

            return containsAny(
                    candidate,
                    "shoe",
                    "shoes",
                    "sneaker",
                    "sneakers",
                    "footwear",
                    "loafer",
                    "loafers"
            );
        }

        /*
         * If category information is weak, use the name.
         */

        String[] words =
                originalText.split("\\s+");

        for (String word : words) {

            if (word.length() < 4) {
                continue;
            }

            if (candidate.contains(word)) {
                return true;
            }
        }

        return false;
    }

    /*
     * -------------------------------------------------------------
     * OBVIOUS PRODUCT TYPE MISMATCH
     * -------------------------------------------------------------
     */

    private boolean isObviousTypeMismatch(
            String originalName,
            String originalCategory,
            String candidateTitle) {

        String originalText =
                (originalName + " " + originalCategory)
                        .toLowerCase(Locale.ROOT);

        String candidate =
                candidateTitle.toLowerCase(Locale.ROOT);

        /*
         * Searching for casual shoes:
         * running shoes should not be treated as the same product.
         */

        if (containsAny(
                originalText,
                "casual shoe",
                "casual shoes",
                "casual sneaker",
                "casual sneakers")) {

            if (containsAny(
                    candidate,
                    "running shoe",
                    "running shoes",
                    "running sneaker",
                    "running sneakers")) {

                return true;
            }

            if (containsAny(
                    candidate,
                    "football shoe",
                    "football shoes",
                    "cricket shoe",
                    "cricket shoes")) {

                return true;
            }

            if (containsAny(
                    candidate,
                    "formal shoe",
                    "formal shoes")) {

                return true;
            }
        }

        return false;
    }

    /*
     * -------------------------------------------------------------
     * GENDER MATCHING
     * -------------------------------------------------------------
     */

    private boolean isGenderMismatch(
            String originalName,
            String candidateTitle) {

        String original =
                originalName.toLowerCase(Locale.ROOT);

        String candidate =
                candidateTitle.toLowerCase(Locale.ROOT);

        boolean originalMen =
                containsAny(
                        original,
                        "men",
                        "mens",
                        "men's"
                );

        boolean originalWomen =
                containsAny(
                        original,
                        "women",
                        "womens",
                        "women's"
                );

        boolean candidateMen =
                containsAny(
                        candidate,
                        "men",
                        "mens",
                        "men's"
                );

        boolean candidateWomen =
                containsAny(
                        candidate,
                        "women",
                        "womens",
                        "women's"
                );

        if (originalMen && candidateWomen) {
            return true;
        }

        if (originalWomen && candidateMen) {
            return true;
        }

        return false;
    }

    /*
     * -------------------------------------------------------------
     * NAME TERM MATCHING
     * -------------------------------------------------------------
     */

    private int countMatchingTerms(
            String originalName,
            String candidateTitle) {

        Set<String> originalTerms =
                meaningfulTerms(originalName);

        Set<String> candidateTerms =
                meaningfulTerms(candidateTitle);

        int count = 0;

        for (String term : originalTerms) {

            if (candidateTerms.contains(term)) {
                count++;
            }
        }

        return count;
    }

    private Set<String> meaningfulTerms(
            String text) {

        Set<String> terms =
                new HashSet<>();

        if (text == null || text.isBlank()) {
            return terms;
        }

        String cleaned =
                text.toLowerCase(Locale.ROOT)
                        .replaceAll(
                                "[^a-z0-9]+",
                                " "
                        )
                        .trim();

        if (cleaned.isBlank()) {
            return terms;
        }

        for (String word :
                cleaned.split("\\s+")) {

            if (word.length() < 3) {
                continue;
            }

            if (STOP_WORDS.contains(word)) {
                continue;
            }

            terms.add(word);
        }

        return terms;
    }

    private boolean containsWord(
            String text,
            String value) {

        if (text == null ||
                value == null ||
                value.isBlank()) {

            return false;
        }

        String normalizedText =
                " " + text + " ";

        String normalizedValue =
                " " + value + " ";

        return normalizedText.contains(
                normalizedValue
        );
    }

    private boolean containsAny(
            String text,
            String... values) {

        if (text == null) {
            return false;
        }

        for (String value : values) {

            if (value != null &&
                    text.contains(
                            value.toLowerCase(
                                    Locale.ROOT))) {

                return true;
            }
        }

        return false;
    }

    private String normalize(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .replaceAll(
                        "\\s+",
                        " "
                )
                .toLowerCase(
                        Locale.ROOT
                );
    }
}