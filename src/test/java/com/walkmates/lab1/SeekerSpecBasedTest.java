package com.walkmates.lab1;

import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 1, Part B — specification-based tests for {@link Seeker}.
 *
 * <p>Design your tests on paper first (equivalence partitions, boundary values, decision table)
 * from {@code docs/REQUIREMENTS.md} FR-1.1 / FR-1.3 / FR-1.2, then implement them here. One
 * worked example is provided; the {@code TODO}s are yours.</p>
 */
class SeekerSpecBasedTest {

    @Test
    @DisplayName("Adding 250 SEK to a new seeker gives a 250.00 balance")
    void addingFundsWorks() {
        Seeker seeker = new Seeker("you@example.com", "You", "0701234567");  // Arrange
        seeker.addFunds(250.00);                                              // Act
        assertThat(seeker.getBalance()).isEqualTo(250.00);                   // Assert
    }

    // Email format/length tests
    @Test
    @DisplayName("Valid email with proper format is accepted")
    void validEmailIsAccepted() {
        Seeker seeker = new Seeker("example@example.com", "Adam", "0731231234");
        assertThat(seeker.getEmail()).isEqualTo("example@example.com");
    }

    @Test
    @DisplayName("Email blank rejected")
    void emailBlankIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("", "Adam", "0731231234"));
    }

    @Test
    @DisplayName("Email is null rejected")
    void emailIsNullIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker(null, "Adam", "0731231234"));
    }

    @Test
    @DisplayName("Email missing @ is rejected")
    void emailMissingAtIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("example.com", "Adam", "0731231234"));
    }

    @Test
    @DisplayName("Email with multiple @ is rejected")
    void emailWithMultipleAtIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("test@@example.com", "Adam", "0731231234"));
    }

    @Test
    @DisplayName("Email missing dot in domain is rejected")
    void emailMissingDotIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("test@examplecom", "Adam", "0731231234"));
    }

    @Test
    @DisplayName("Email missing local part is rejected")
    void emailMissingLocalPartIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("@example.com", "Adam", "0731231234"));
    }

    @Test
    @DisplayName("Email exceeding 254 characters is rejected")
    void emailTooLongIsRejected() {
        String longEmail = "a".repeat(243) + "@example.com"; // 255 chars total
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker(longEmail, "Adam", "0731231234"));
    }

    // Display name tests
    @Test
    @DisplayName("Valid display name with proper length and characters is accepted")
    void validDisplayNameIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam-Persson", "0731231234");
        assertThat(seeker.getDisplayName()).isEqualTo("Adam-Persson");
    }

    @Test
    @DisplayName("Display name too short (< 2 chars) is rejected")
    void displayNameTooShortIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "A", "0731231234"));
    }

    @Test
    @DisplayName("Display name too long (> 40 chars) is rejected")
    void displayNameTooLongIsRejected() {
        String longName = "A".repeat(41);
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", longName, "0731231234"));
    }

    @Test
    @DisplayName("Display name containing digits is rejected")
    void displayNameWithDigitsIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "Adam123", "0731231234"));
    }

    @Test
    @DisplayName("Display name containing special characters is rejected")
    void displayNameWithSpecialCharsIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "Adam:)", "0731231234"));
    }

    // Phone number tests
    @Test
    @DisplayName("Valid Swedish phone number format is accepted")
    void validSwedishPhoneIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        assertThat(seeker.getPhoneNumber()).isEqualTo("0731231234");
    }

    @Test
    @DisplayName("Valid international phone number format is accepted")
    void validInternationalPhoneIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "+46731231234");
        assertThat(seeker.getPhoneNumber()).isEqualTo("+46731231234");
    }

    @Test
    @DisplayName("Swedish phone number too long is rejected")
    void swedishPhoneTooLongIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "Adam", "07312312312"));
    }

    @Test
    @DisplayName("Swedish phone number too short is rejected")
    void swedishPhoneTooShortIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "Adam", "073123123"));
    }

    @Test
    @DisplayName("International phone number too long is rejected")
    void internationalPhoneTooLongIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "Adam", "+467312312345"));
    }

    @Test
    @DisplayName("International phone number too short is rejected")
    void internationalPhoneTooShortIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("adam@example.com", "Adam", "+4673123123"));
    }

    // Wallet top-up amount tests
    @Test
    @DisplayName("Wallet top-up below 10.00 SEK is rejected")
    void walletTopUpBelowMinimumIsRejected() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(9.99));
    }

    @Test
    @DisplayName("Valid wallet top-up at 10.00 is accepted")
    void walletTopUpAtMinimumIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(10.00);
        assertThat(seeker.getBalance()).isEqualTo(10.00);
    }

    @Test
    @DisplayName("Valid wallet top-up just above 10.00 is accepted")
    void walletTopUpJustAboveMinimumIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(10.01);
        assertThat(seeker.getBalance()).isEqualTo(10.01);
    }

    @Test
    @DisplayName("Valid wallet top-up between 10.00 and 5000.00 SEK is accepted")
    void walletTopUpIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(1000.00);
        assertThat(seeker.getBalance()).isEqualTo(1000.00);
    }

    @Test
    @DisplayName("Top-up just below maximum single transaction is accepted")
    void walletTopUpJustBelowSingleMaximumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");
        seeker.addFunds(4999.99);
        assertThat(seeker.getBalance()).isEqualTo(4999.99);
    }

    @Test
    @DisplayName("Top-up exactly at the 5000 SEK single-transaction maximum is accepted")
    void walletTopUpAtSingleMaximumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");
        seeker.addFunds(Seeker.MAX_SINGLE_TOP_UP); // 5000.00, the boundary value
        assertThat(seeker.getBalance()).isEqualTo(Seeker.MAX_SINGLE_TOP_UP);
    }

    @Test
    @DisplayName("Wallet top-up above 5000.00 SEK is rejected")
    void walletTopUpJustAboveMaximumIsRejected() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(5000.01));
    }

    @Test
    @DisplayName("Wallet top-up that results in balance <= 20000.00 SEK is accepted")
    void walletTopUpAtMaxBalanceIsAccepted() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00); // Total: 20000.00
        assertThat(seeker.getBalance()).isEqualTo(20000.00);
    }

    @Test
    @DisplayName("Wallet top-up that would exceed 20000.00 SEK balance is rejected")
    void walletTopUpExceedingMaxBalanceIsRejected() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(4991.00); // Balance: 19 991.00
        assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(10.00)); // Would make 20 001.00
    }

    @Test
    @DisplayName("Wallet balance that turns negative should be rejected")
    void walletBalanceTurningNegativeIsRejected() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(70.00);

        assertThrows(IllegalArgumentException.class,
                () -> seeker.charge(80.00));
    }

    @Test
    @DisplayName("Verify max concurrent bookings and platform fee for a NEW seeker")
    void verifyMaxConcurrentBookingsAndPlatformFeeForNewSeeker() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(1);
        assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.15);
    }

    @Test
    @DisplayName("Verify max concurrent bookings and platform fee for a VERIFIED seeker")
    void verifyMaxConcurrentBookingsAndPlatformFeeForVerifiedSeeker() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.setTrustTier(TrustTier.VERIFIED);
        assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(3);
        assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.12);
    }

    @Test
    @DisplayName("Verify max concurrent bookings and platform fee for a TRUSTED seeker")
    void verifyMaxConcurrentBookingsAndPlatformFeeForTrustedSeeker() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.setTrustTier(TrustTier.TRUSTED);
        assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(5);
        assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.08);
    }

    @Test
    @DisplayName("Verify max concurrent bookings and platform fee for a PRO_SITTER seeker")
    void verifyMaxConcurrentBookingsAndPlatformFeeForProSitterSeeker() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.setTrustTier(TrustTier.PRO_SITTER);
        assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(10);
        assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.05);
    }

    @Test
    @DisplayName("Verify that a Seeker gets an ID")
    void verifySeekerIdGenerated() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        assertFalse(seeker.getId().isEmpty());
    }
}
