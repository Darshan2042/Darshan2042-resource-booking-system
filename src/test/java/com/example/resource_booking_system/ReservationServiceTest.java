package com.example.resource_booking_system;

import com.example.resource_booking_system.dto.reservation.ReservationRequest;
import com.example.resource_booking_system.dto.reservation.ReservationResponse;
import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.entity.Resource;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.enums.ReservationStatus;
import com.example.resource_booking_system.enums.Role;
import com.example.resource_booking_system.repository.ReservationRepository;
import com.example.resource_booking_system.repository.ResourceRepository;
import com.example.resource_booking_system.repository.UserRepository;
import com.example.resource_booking_system.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Resource resource;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        user = new User();

        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        resource = new Resource();

        resource.setId(1L);
        resource.setName("Meeting Room");
        resource.setType("ROOM");
        resource.setDescription("Test room");
        resource.setPricePerUnit(
                new BigDecimal("600.00")
        );
        resource.setAvailable(true);
    }


    // ==========================================
    // TEST 1: CREATE RESERVATION - 2 HOURS
    // ==========================================

    @Test
    void testCreateReservation() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusHours(2);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ReservationResponse response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                1L,
                response.getResourceId()
        );

        assertEquals(
                0,
                new BigDecimal("1200.00")
                        .compareTo(response.getTotalPrice())
        );

        assertEquals(
                ReservationStatus.PENDING,
                response.getStatus()
        );
    }


    // ==========================================
    // TEST 1A: 30 MINUTES = 1 BILLABLE HOUR
    // ==========================================

    @Test
    void testCreateReservationFor30Minutes() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusMinutes(30);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        // 30 minutes = 1 billable hour
        assertEquals(
                0,
                new BigDecimal("600.00")
                        .compareTo(response.getTotalPrice())
        );
    }


    // ==========================================
    // TEST 1B: 59 MINUTES = 1 BILLABLE HOUR
    // ==========================================

    @Test
    void testCreateReservationFor59Minutes() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusMinutes(59);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        // 59 minutes = 1 billable hour
        assertEquals(
                0,
                new BigDecimal("600.00")
                        .compareTo(response.getTotalPrice())
        );
    }


    // ==========================================
    // TEST 1C: 60 MINUTES = 1 BILLABLE HOUR
    // ==========================================

    @Test
    void testCreateReservationFor60Minutes() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusMinutes(60);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        // Exactly 60 minutes = 1 billable hour
        assertEquals(
                0,
                new BigDecimal("600.00")
                        .compareTo(response.getTotalPrice())
        );
    }


    // ==========================================
    // TEST 1D: 61 MINUTES = 2 BILLABLE HOURS
    // ==========================================

    @Test
    void testCreateReservationFor61Minutes() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusMinutes(61);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        // 61 minutes = 2 billable hours
        assertEquals(
                0,
                new BigDecimal("1200.00")
                        .compareTo(response.getTotalPrice())
        );
    }


    // ==========================================
    // TEST 2: UNAVAILABLE RESOURCE
    // ==========================================

    @Test
    void testCreateReservationWhenResourceUnavailable() {

        resource.setAvailable(false);

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusHours(2);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        assertThrows(
                RuntimeException.class,
                () -> reservationService.createReservation(
                        request,
                        "user"
                )
        );

        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // ==========================================
    // TEST 3: INVALID TIME
    // ==========================================

    @Test
    void testCreateReservationWithInvalidTime() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.minusHours(1);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        assertThrows(
                RuntimeException.class,
                () -> reservationService.createReservation(
                        request,
                        "user"
                )
        );

        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // ==========================================
    // TEST 4: OVERLAPPING RESERVATION
    // ==========================================

    @Test
    void testCreateReservationWhenTimeSlotOverlaps() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusHours(1);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        // Existing reservation overlaps with requested time
        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> reservationService.createReservation(
                        request,
                        "user"
                )
        );

        // Reservation must NOT be saved
        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // ==========================================
    // TEST 5: NON-OVERLAPPING RESERVATION
    // ==========================================

    @Test
    void testCreateReservationWhenTimeSlotDoesNotOverlap() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusHours(1);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        // No existing reservation overlaps
        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        verify(
                reservationRepository,
                times(1)
        ).save(any());
    }


    // ==========================================
    // TEST 6: CANCELLED RESERVATION DOES NOT BLOCK
    // ==========================================

    @Test
    void testCreateReservationWhenExistingReservationIsCancelled() {

        LocalDateTime start =
                LocalDateTime.now().plusDays(1);

        LocalDateTime end =
                start.plusHours(1);

        ReservationRequest request =
                new ReservationRequest(
                        1L,
                        start,
                        end
                );

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(resourceRepository.findById(1L))
                .thenReturn(Optional.of(resource));

        /*
         * Repository query excludes CANCELLED reservations.
         * Therefore, the result is false and the new booking is allowed.
         */
        when(reservationRepository.existsOverlappingReservation(
                eq(1L),
                eq(start),
                eq(end),
                eq(ReservationStatus.CANCELLED)
        )).thenReturn(false);

        when(reservationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response =
                reservationService.createReservation(
                        request,
                        "user"
                );

        assertNotNull(response);

        verify(
                reservationRepository,
                times(1)
        ).save(any());
    }
}