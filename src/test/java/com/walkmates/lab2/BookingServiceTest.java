package com.walkmates.lab2;

import com.walkmates.model.Booking;
import com.walkmates.model.BookingStatus;
import com.walkmates.model.Listing;
import com.walkmates.model.ListingType;
import com.walkmates.model.Provider;
import com.walkmates.model.Seeker;
import com.walkmates.repository.BookingRepository;
import com.walkmates.repository.ListingRepository;
import com.walkmates.repository.ProviderRepository;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.BookingService;
import com.walkmates.service.NotificationService;
import com.walkmates.service.PricingCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    SeekerRepository seekers;

    @Mock
    ListingRepository listings;

    @Mock
    ProviderRepository providers;

    @Mock
    BookingRepository bookings;

    @Mock
    PricingCalculator pricing;

    @Mock
    NotificationService notifications;

    @InjectMocks
    BookingService service;

    private Seeker seeker;
    private Provider provider;
    private Listing listing;

    @BeforeEach
    void setUp() {
        seeker = new Seeker("adam@example.se", "Adam", "0701234567");
        provider = new Provider("A provider", 63.18, 14.64);
        listing = new Listing(provider.getId(), "A listing", "desc", ListingType.DOG_WALK);
    }

    @Test
    @DisplayName("Verify the confirmation notification is sent on a successful booking")
    void confirmationNotificationSentOnSuccessfulBooking() {
        when(seekers.findById(seeker.getId())).thenReturn(Optional.of(seeker));
        when(listings.findById(listing.getId())).thenReturn(Optional.of(listing));
        when(providers.findById(provider.getId())).thenReturn(Optional.of(provider));
        when(bookings.findBySeekerId(seeker.getId())).thenReturn(List.of());
        when(listings.findByProviderId(provider.getId())).thenReturn(List.of());
        when(pricing.priceFor(any(Booking.class), same(listing), same(seeker)))
                .thenReturn(100.0);
        seeker.addFunds(100.0);

        Booking result = service.createBooking(seeker.getId(), listing.getId(), 60);

        assertEquals(seeker.getId(), result.getSeekerId());
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        verify(notifications).sendBookingConfirmed(seeker, result);
    }

}
