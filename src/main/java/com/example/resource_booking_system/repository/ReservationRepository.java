package com.example.resource_booking_system.repository;

import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation> {

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reservation r
            WHERE r.resource.id = :resourceId
              AND r.startTime < :endTime
              AND r.endTime > :startTime
              AND r.status <> :cancelledStatus
            """)
    boolean existsOverlappingReservation(
            @Param("resourceId") Long resourceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reservation r
        WHERE r.resource.id = :resourceId
          AND r.id <> :reservationId
          AND r.startTime < :endTime
          AND r.endTime > :startTime
          AND r.status <> :cancelledStatus
        """)
    boolean existsOverlappingReservationForUpdate(
            @Param("resourceId") Long resourceId,
            @Param("reservationId") Long reservationId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );
}