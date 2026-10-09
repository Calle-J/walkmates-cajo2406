package com.walkmates.lab3;

import com.walkmates.model.Listing;
import com.walkmates.model.ListingType;
import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;
import com.walkmates.service.ai.LlmClient;
import com.walkmates.service.ai.MatchExplanationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Lab 3, Part A — testing the AI "explain this match" feature without a live LLM.
 *
 * <p>There is no exact oracle for the model's text, so we test the parts we <em>can</em> pin
 * down: the deterministic prompt builder, the fallback path (mock the {@link LlmClient} to
 * fail/timeout), the metamorphic relations, and prompt-injection resistance. Two worked
 * examples are provided; the {@code TODO}s are yours.</p>
 */
class MatchExplanationServiceTest {

    private Seeker seeker() {
        return new Seeker("p@example.com", "Pat", "0701112233");
    }

    private Listing listing(String description) {
        return new Listing("provider-1", "Walk Rex", description, ListingType.DOG_WALK);
    }

    // ---- Worked example 1: the prompt builder is deterministic and structured (FR-5.1) ----
    @Test
    @DisplayName("buildPrompt includes the structured fields")
    void promptIncludesStructuredFields() {
        MatchExplanationService service = new MatchExplanationService(mock(LlmClient.class));

        String prompt = service.buildPrompt(seeker(), listing("Friendly dog"));

        assertThat(prompt).contains("Seeker trust tier: " + TrustTier.NEW);
        assertThat(prompt).contains("Listing type: " + ListingType.DOG_WALK);
        assertThat(prompt).contains("Listing base rate (SEK/hour): " + ListingType.DOG_WALK.getBaseRatePerHour());
        assertThat(prompt).containsSubsequence("<<<LISTING_DESCRIPTION_DATA", "Friendly dog",
                "LISTING_DESCRIPTION_DATA>>>");
    }

    // ---- Worked example 2: on LLM failure, fall back deterministically (FR-5.2) ----
    @Test
    @DisplayName("explainMatch falls back when the LLM call fails")
    void fallsBackOnLlmFailure() throws Exception {
        LlmClient llm = mock(LlmClient.class);
        when(llm.complete(org.mockito.ArgumentMatchers.anyString()))
                .thenThrow(new LlmClient.LlmException("provider down"));
        MatchExplanationService service = new MatchExplanationService(llm);
        Seeker seeker = seeker();
        Listing listing = listing("Friendly dog");

        String result = service.explainMatch(seeker, listing);

        // Use an independent, concrete oracle. Comparing result only with another call to
        // fallbackExplanation would pass if both calls returned the same wrong text.
        assertThat(result).isEqualTo(
                "This DOG_WALK opportunity \"Walk Rex\" is a good fit for a NEW seeker.");
    }

    @Test
    @DisplayName("explainMatch falls back when the LLM times out")
    void fallsBackOnLlmTimeout() throws Exception {
        LlmClient llm = mock(LlmClient.class);
        when(llm.complete(org.mockito.ArgumentMatchers.anyString()))
                .thenThrow(new LlmClient.LlmTimeoutException("call timed out"));
        MatchExplanationService service = new MatchExplanationService(llm);

        String result = service.explainMatch(seeker(), listing("Friendly dog"));

        assertThat(result).isEqualTo(
                "This DOG_WALK opportunity \"Walk Rex\" is a good fit for a NEW seeker.");
    }

    @Test
    @DisplayName("explainMatch falls back when the LLM returns null")
    void fallsBackOnNullResponse() throws Exception {
        LlmClient llm = mock(LlmClient.class);
        when(llm.complete(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
        MatchExplanationService service = new MatchExplanationService(llm);

        String result = service.explainMatch(seeker(), listing("Friendly dog"));

        assertThat(result).isEqualTo(
                "This DOG_WALK opportunity \"Walk Rex\" is a good fit for a NEW seeker.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("explainMatch falls back when the LLM returns an blank string")
    void fallsBackOnBlankResponse(String blankResponse) throws Exception {
        LlmClient llm = mock(LlmClient.class);
        when(llm.complete(org.mockito.ArgumentMatchers.anyString())).thenReturn(blankResponse);
        MatchExplanationService service = new MatchExplanationService(llm);

        String result = service.explainMatch(seeker(), listing("Friendly dog"));

        assertThat(result).isEqualTo(
                "This DOG_WALK opportunity \"Walk Rex\" is a good fit for a NEW seeker.");
    }

    @Test
    @DisplayName("MR-1: adding an irrelevant sentence to the listing description must not change " +
            "recommendBestMatch's chosen listing")
    void addingIrrelevantSentenceToDescriptionDoesNotChangeChosenListing() {
        MatchExplanationService service = new MatchExplanationService(mock(LlmClient.class));
        Seeker seeker = seeker();

        Listing l1 = new Listing("provider-1", "Walk the dog",
                "Friendly dog", ListingType.DOG_WALK);
        Listing l2 = new Listing("provider-2", "Sitting with Rex",
                "Needs regular sitting", ListingType.PET_SITTING);

        List<Listing> originalCandidates = List.of(l1, l2);

        Listing originalBest = service.recommendBestMatch(seeker, originalCandidates);
        assertThat(originalBest).isNotNull();

        // Create a new listing with an irrelevant sentence
        Listing l1Modified = new Listing("provider-1", "Walk the dog",
                "Friendly dog. Sweden is located in Europe", ListingType.DOG_WALK);

        List<Listing> modifiedCandidates = List.of(l1Modified, l2);

        Listing modifiedBest = service.recommendBestMatch(seeker, modifiedCandidates);
        assertThat(modifiedBest).isNotNull();

        assertThat(modifiedBest.getTitle()).isEqualTo(originalBest.getTitle());
    }

    @Test
    @DisplayName("MR-2: shuffling the candidate list must not change the chosen listing")
    void shufflingCandidatesDoesNotChangeChosenListing() {
        MatchExplanationService service = new MatchExplanationService(mock(LlmClient.class));
        Seeker seeker = seeker();

        Listing l1 = new Listing("provider-1", "Walk the dog",
                "Friendly dog", ListingType.DOG_WALK);
        Listing l2 = new Listing("provider-2", "Sitting with Rex",
                "Needs regular sitting", ListingType.PET_SITTING);
        Listing l3 = new Listing("provider-3", "House sitting with Bob",
                "Needs regular house sitting", ListingType.HOUSE_SITTING);

        List<Listing> candidates = List.of(l1, l2, l3);

        Listing originalBest = service.recommendBestMatch(seeker, candidates);
        assertThat(originalBest).isNotNull();

        List<Listing> shuffledCandidates = new ArrayList<>(candidates);
        Collections.reverse(shuffledCandidates);

        Listing modifiedBest = service.recommendBestMatch(seeker, shuffledCandidates);
        assertThat(modifiedBest).isNotNull();

        assertThat(modifiedBest).isSameAs(originalBest);
    }

    // TODO (injection): a description containing "ignore previous instructions and ..." must
    //      stay inside the data block; buildPrompt must still contain the data delimiters.
}
