package com.walkmates.lab2;

import com.walkmates.model.Booking;
import com.walkmates.model.Listing;
import com.walkmates.model.ListingType;
import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;
import com.walkmates.service.PricingCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lab 2, Part A — structural testing for {@link PricingCalculator} (FR-4.3).
 *
 * <p>Run coverage with {@code mvn clean test jacoco:report} and open
 * {@code target/site/jacoco/index.html}. Find the uncovered branches and add tests to reach
 * them — then look hard at the <em>overnight surcharge boundary</em>: there is a path that your
 * happy-path test "covers" but does not actually check (coverage ≠ correctness).</p>
 */
class PricingCalculatorStructuralTest {

    private final PricingCalculator pricing = new PricingCalculator();

    private Seeker seeker(TrustTier tier) {
        Seeker s = new Seeker("p@example.com", "Pat", "0701112233");
        s.setTrustTier(tier);
        return s;
    }

    private Listing listing(ListingType type) {
        return new Listing("provider-1", "A listing",
                "desc", type);
    }

    // ---- Worked example: a short standard walk, no overnight surcharge ----
    @Test
    @DisplayName("60 min DOG_WALK for a VERIFIED seeker = 80 base + 12% fee = 89.60")
    void shortWalkPrice() {
        Booking booking = new Booking("seeker-1", "listing-1", 60);

        double price = pricing.priceFor(booking, listing(ListingType.DOG_WALK), seeker(TrustTier.VERIFIED));

        assertThat(price).isEqualTo(89.60);
    }

    @ParameterizedTest
    @DisplayName("free SHELTER_VOLUNTEER listing always cost 0.00")
    @ValueSource(ints = {30, 80, 120, 480, 481})
    void freeShelterVolunteerListing(int duration) {
        Booking booking = new Booking("seeker-1", "listing-1", duration);
        Listing listing = listing(ListingType.SHELTER_VOLUNTEER);
        Seeker seeker = seeker(TrustTier.NEW);

        double price = pricing.priceFor(booking, listing, seeker);

        assertThat(price).isEqualTo(0.00);
    }

    @Test
    @DisplayName("Clearly overnight booking (600 min)")
    void clearlyOverNightBooking() {
        Booking booking = new Booking("seeker-1", "listing-1", 600);

        double price = pricing.priceFor(booking, listing(ListingType.DOG_WALK), seeker(TrustTier.VERIFIED));

        double expected = 80 * 10 * 1.12 * 1.2; // Base * duration * 12% fee * 20% surcharge
        double expectedRound = BigDecimal.valueOf(expected).setScale(2, RoundingMode.HALF_UP).doubleValue();

        assertThat(price).isEqualTo(expectedRound);
    }

    @Test
    @DisplayName("Booking of exactly 480 minutes must NOT be surcharged")
    void surchargeBoundaryVerification() {
        Booking booking = new Booking("seeker-1", "listing-1", 480);

        double price = pricing.priceFor(booking, listing(ListingType.DOG_WALK), seeker(TrustTier.VERIFIED));

        double expected = 80 * 8 * 1.12; // Base * duration * 12% fee
        double expectedRound = BigDecimal.valueOf(expected).setScale(2, RoundingMode.HALF_UP).doubleValue();

        assertThat(price).isEqualTo(expectedRound);
    }
}
