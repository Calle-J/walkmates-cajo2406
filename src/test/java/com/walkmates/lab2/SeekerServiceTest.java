package com.walkmates.lab2;

import com.walkmates.model.Seeker;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.NotificationService;
import com.walkmates.service.PaymentService;
import com.walkmates.service.SeekerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeekerServiceTest {

    @Mock
    private SeekerRepository seekers;

    @Mock
    private PaymentService payments;

    @Mock
    private NotificationService notifications;

    @InjectMocks
    private SeekerService seekerService;

    private Seeker seeker;

    @BeforeEach
    void setUp() {
        seeker = new Seeker("carl@example.com", "Carl", "0701234567");
    }

    @Test
    @DisplayName("topUp success: credits wallet and saves updated seeker")
    void topUpSuccessCreditsWalletAndSavesUpdatedSeeker() throws PaymentService.PaymentException {
        // When seekerService searches for seeker by ID, return seeker
        when(seekers.findById(seeker.getId())).thenReturn(Optional.of(seeker));

        // When payment is made, the gateway recalls with a transaction-ID
        when(payments.charge(seeker.getId(), "card-1", 200)).thenReturn("transaction-1");

        // When calling save, return the seeker object
        when(seekers.save(seeker)).thenReturn(seeker);

        // ACT
        Seeker result = seekerService.topUp(seeker.getId(), "card-1", 200);

        // ASSERT
        assertThat(result.getBalance()).isEqualTo(200);

        // VERIFY
        verify(payments).charge(seeker.getId(), "card-1", 200);
        verify(seekers).save(seeker);
    }

    @Test
    @DisplayName("topUp decline: throws PaymentException, does NOT credit wallet and does NOT save")
    void topUpDeclineDoesNotCreditWalletAndDoesNotSave() throws PaymentService.PaymentException {
        // When seekerService searches for seeker by ID, return seeker
        when(seekers.findById(seeker.getId())).thenReturn(Optional.of(seeker));

        // Decline payment and throws PaymentException
        when(payments.charge(seeker.getId(), "card-1", 200))
                .thenThrow(new PaymentService.PaymentException("Declined"));

        assertThatThrownBy(() -> seekerService.topUp(seeker.getId(), "card-1", 200))
                .isInstanceOf(PaymentService.PaymentException.class);

        assertThat(seeker.getBalance()).isEqualTo(0);
        verify(seekers, never()).save(any());
    }

    @Test
    @DisplayName("topUp timeout: throws PaymentTimeoutException, does NOT credit wallet and does NOT save")
    void topUpTimeoutDoesNotCreditWalletAndDoesNotSave() throws PaymentService.PaymentException {
        // When seekerService searches for seeker by ID, return seeker
        when(seekers.findById(seeker.getId())).thenReturn(Optional.of(seeker));

        // Gateway does not respond in time and throws PaymentTimeoutException
        when(payments.charge(seeker.getId(), "card-1", 200))
                .thenThrow(new PaymentService.PaymentTimeoutException("Gateway timeout"));

        assertThatThrownBy(() -> seekerService.topUp(seeker.getId(), "card-1", 200))
                .isInstanceOf(PaymentService.PaymentTimeoutException.class);

        assertThat(seeker.getBalance()).isEqualTo(0);
        verify(seekers, never()).save(any());
    }
}
