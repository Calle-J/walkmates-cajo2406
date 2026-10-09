package com.walkmates.lab3;

import com.walkmates.model.Listing;
import com.walkmates.model.ListingType;
import com.walkmates.model.Seeker;
import com.walkmates.repository.ListingRepository;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.ai.MatchExplanationService;
import com.walkmates.web.MatchController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lab 3, Part A (interface rung) — testing the AI feature through its HTTP boundary with
 * {@code MockMvc}, with the service/repositories mocked. This is the "test the interface, not a
 * live model" example. One worked test is provided (the 404 path); extend it to the success and
 * fallback paths.
 */
@WebMvcTest(MatchController.class)
class MatchControllerWebTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SeekerRepository seekers;
    @MockitoBean
    private ListingRepository listings;
    @MockitoBean
    private MatchExplanationService matchExplanation;

    @Test
    @DisplayName("GET explain returns 404 when the seeker does not exist")
    void explainReturns404WhenSeekerMissing() throws Exception {
        when(seekers.findById("missing")).thenReturn(Optional.empty());
        when(listings.findById("l1")).thenReturn(Optional.empty());

        mvc.perform(get("/api/match/missing/explain").param("listingId", "l1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET explain returns 200 OK and JSON object when seeker and listing exist")
    void explainReturns200WhenSeekerAndListingExist() throws Exception {
        String seekerId = "seeker-1";
        String listingId = "listing-1";
        String cannedExplanation = "This listing is great for you!";

        Seeker seeker = new Seeker("carl@example.com", "Carl", "0701234567");
        Listing listing = new Listing("provider-1", "Walk Rex",
                "Short walk", ListingType.DOG_WALK);

        when(seekers.findById(seekerId)).thenReturn(Optional.of(seeker));
        when(listings.findById(listingId)).thenReturn(Optional.of(listing));
        when(matchExplanation.explainMatch(seeker, listing)).thenReturn(cannedExplanation);

        mvc.perform(get("/api/match/" + seekerId + "/explain")
                        .param("listingId", listingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seekerId").value(seekerId))
                .andExpect(jsonPath("$.listingId").value(listingId))
                .andExpect(jsonPath("$.explanation").value(cannedExplanation));
    }

    // OPTIONAL EXTENSION: make the mocked service return the fallback text and assert the
    // endpoint still returns 200; also cover the listing-missing 404 path separately.
}
