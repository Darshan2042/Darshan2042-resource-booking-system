package com.example.resource_booking_system.service;

import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.entity.Resource;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.enums.Role;
import com.example.resource_booking_system.repository.ReservationRepository;
import com.example.resource_booking_system.repository.ResourceRepository;
import com.example.resource_booking_system.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ReservationAccessTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User admin;
    private User user;
    private User anotherUser;

    private Resource resource;

    private Reservation reservation;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        // ==========================
        // ADMIN USER
        // ==========================

        admin = new User();

        admin.setId(1L);
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);


        // ==========================
        // NORMAL USER
        // ==========================

        user = new User();

        user.setId(2L);
        user.setUsername("user");
        user.setRole(Role.USER);


        // ==========================
        // ANOTHER USER
        // ==========================

        anotherUser = new User();

        anotherUser.setId(3L);
        anotherUser.setUsername("anotherUser");
        anotherUser.setRole(Role.USER);


        // ==========================
        // RESOURCE
        // ==========================

        resource = new Resource();

        resource.setId(10L);
        resource.setName("Meeting Room");
        resource.setType("ROOM");
        resource.setDescription("Test meeting room");
        resource.setPricePerUnit(
                new BigDecimal("600.00")
        );
        resource.setAvailable(true);


        // ==========================
        // RESERVATION
        // ==========================

        reservation = new Reservation();

        reservation.setId(100L);

        // Reservation belongs to USER
        reservation.setUser(user);

        // Reservation uses RESOURCE
        reservation.setResource(resource);
    }


    // ==========================================
    // TEST 1: ADMIN CAN ACCESS ANY RESERVATION
    // ==========================================

    @Test
    void adminCanAccessAnyReservation() {

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(admin));

        when(reservationRepository.findById(100L))
                .thenReturn(Optional.of(reservation));

        assertDoesNotThrow(() ->
                reservationService.getReservationById(
                        100L,
                        "admin"
                )
        );
    }


    // ==========================================
    // TEST 2: USER CAN ACCESS OWN RESERVATION
    // ==========================================

    @Test
    void userCanAccessOwnReservation() {

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(reservationRepository.findById(100L))
                .thenReturn(Optional.of(reservation));

        assertDoesNotThrow(() ->
                reservationService.getReservationById(
                        100L,
                        "user"
                )
        );
    }


    // ==========================================
    // TEST 3: USER CANNOT ACCESS
    // ANOTHER USER'S RESERVATION
    // ==========================================

    @Test
    void userCannotAccessAnotherUsersReservation() {

        when(userRepository.findByUsername("anotherUser"))
                .thenReturn(Optional.of(anotherUser));

        when(reservationRepository.findById(100L))
                .thenReturn(Optional.of(reservation));

        assertThrows(
                RuntimeException.class,
                () ->
                        reservationService.getReservationById(
                                100L,
                                "anotherUser"
                        )
        );
    }
}