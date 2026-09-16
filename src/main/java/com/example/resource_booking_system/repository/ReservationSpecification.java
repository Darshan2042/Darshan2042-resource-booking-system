package com.example.resource_booking_system.repository;

import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.enums.ReservationStatus;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ReservationSpecification {

    private ReservationSpecification() {
        // Utility class
    }

    public static Specification<Reservation> hasStatus(
            ReservationStatus status) {

        if (status == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Reservation> hasMinPrice(
            BigDecimal minPrice) {

        if (minPrice == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("totalPrice"),
                        minPrice
                );
    }

    public static Specification<Reservation> hasMaxPrice(
            BigDecimal maxPrice) {

        if (maxPrice == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("totalPrice"),
                        maxPrice
                );
    }
}