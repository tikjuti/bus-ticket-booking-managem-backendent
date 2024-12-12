package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Ticket;
import com.tikjuti.bus_ticket_booking.repository.CustomTicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CustomTicketRepositoryImpl implements CustomTicketRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkIsEmployeeBooking(String employeeId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("checkIsEmployeeBooking");
        query.registerStoredProcedureParameter("employeeId", String.class, IN);
        query.setParameter("employeeId", employeeId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

    @Override
    public List<Object[]> findTripsByUser(String departureLocation, String arrivalLocation, String departureDate) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("FindTripsUser");
        query.registerStoredProcedureParameter("departureLocation", String.class, IN);
        query.registerStoredProcedureParameter("arrivalLocation", String.class, IN);
        query.registerStoredProcedureParameter("departureDate", String.class, IN);
        query.setParameter("departureLocation", departureLocation);
        query.setParameter("arrivalLocation", arrivalLocation);
        query.setParameter("departureDate", departureDate);
        query.execute();
        return query.getResultList();
    }

    @Override
    public int findAvailableSeatsByVehicleId(String vehicleId) {
        String sql = "SELECT GetAvailableSeats(:vehicleId)";
        return ((Number) entityManager.createNativeQuery(sql)
                .setParameter("vehicleId", vehicleId)
                .getSingleResult()).intValue();
    }

    @Override
    public int findTicketPrice(String vehicleId, String routeId) {
        String sql = "SELECT GetTicketPrice(:vehicleId, :routeId)";
        return ((Number) entityManager.createNativeQuery(sql)
                .setParameter("vehicleId", vehicleId)
                .setParameter("routeId", routeId)
                .getSingleResult()).intValue();
    }

    @Override
    public List<Ticket> findByTripId(String tripId) {
        String jpql = "SELECT t FROM Ticket t WHERE t.trip.id = :tripId";
        return entityManager.createQuery(jpql, Ticket.class)
                .setParameter("tripId", tripId)
                .getResultList();
    }

    @Override
    public Optional<Ticket> findTicketByTicketIdAndPhone(String ticketId, String phone) {
        String jpql = "SELECT t FROM Ticket t WHERE t.id = :ticketId AND t.customer.phone = :phone";
        return entityManager.createQuery(jpql, Ticket.class)
                .setParameter("ticketId", ticketId)
                .setParameter("phone", phone)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Ticket> findByTripIdsAndEmailNotSent(List<String> tripIds) {
        String jpql = "SELECT t FROM Ticket t WHERE t.trip.id IN :tripIds AND t.isEmailSent = false";
        return entityManager.createQuery(jpql, Ticket.class)
                .setParameter("tripIds", tripIds)
                .getResultList();
    }

    @Override
    public List<Object[]> countTicketsByDay(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(t.id) AS count, FUNCTION('DATE', t.paymentDate) AS date " +
                "FROM Ticket t ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY FUNCTION('DATE', t.paymentDate) " +
                "ORDER BY date ASC");

        log.warn(jpql.toString());

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    @Override
    public List<Object[]> countTicketsByMonth(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT FUNCTION('YEAR', t.paymentDate) AS year, " +
                        "FUNCTION('MONTH', t.paymentDate) AS month, " +
                        "COUNT(t.id) AS count " +
                        "FROM Ticket t ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY FUNCTION('YEAR', t.paymentDate), FUNCTION('MONTH', t.paymentDate) " +
                "ORDER BY year ASC, month ASC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }


    @Override
    public List<Object[]> countTicketsByYear(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT COUNT(t.id) AS count, FUNCTION('YEAR', t.paymentDate) AS year " +
                        "FROM Ticket t ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY FUNCTION('YEAR', t.paymentDate) " +
                "ORDER BY year ASC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }


    @Override
    public List<Object[]> countTicketsByRoute(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT COUNT(t.id) AS count, " +
                        "CONCAT(r.departureLocation, ' - ', r.arrivalLocation) AS routeName " +
                        "FROM Ticket t " +
                        "JOIN t.trip trip " +
                        "JOIN trip.route r ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY r.departureLocation, r.arrivalLocation " +
                "ORDER BY count DESC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }


    @Override
    public List<Object[]> countRevenueByDay(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT FUNCTION('DATE', t.paymentDate) AS paymentDate, " +
                        "SUM(t.actualTicketPrice) AS totalRevenue " +
                        "FROM Ticket t ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY FUNCTION('DATE', t.paymentDate) " +
                "ORDER BY paymentDate ASC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    @Override
    public List<Object[]> countRevenueByMonth(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT FUNCTION('YEAR', t.paymentDate) AS year, " +
                        "FUNCTION('MONTH', t.paymentDate) AS month, " +
                        "SUM(t.actualTicketPrice) AS totalRevenue " +
                        "FROM Ticket t ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY FUNCTION('YEAR', t.paymentDate), FUNCTION('MONTH', t.paymentDate) " +
                "ORDER BY year ASC, month ASC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    @Override
    public List<Object[]> countRevenueByYear(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT FUNCTION('YEAR', t.paymentDate) AS year, " +
                        "SUM(t.actualTicketPrice) AS totalRevenue " +
                        "FROM Ticket t ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY FUNCTION('YEAR', t.paymentDate) " +
                "ORDER BY year ASC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }


    @Override
    public List<Object[]> countRevenueByRoute(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT CONCAT(r.departureLocation, ' - ', r.arrivalLocation) AS routeName, " +
                        "SUM(t.actualTicketPrice) AS totalRevenue " +
                        "FROM Ticket t " +
                        "JOIN t.trip trip " +
                        "JOIN trip.route r ");

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY r.departureLocation, r.arrivalLocation, trip.id " +
                "ORDER BY totalRevenue DESC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }


    @Override
    public List<Object[]> countRevenueByVehicleType(LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder(
                "SELECT vt.vehicleTypeName AS vehicleTypeName, " +
                        "SUM(t.actualTicketPrice) AS totalRevenue " +
                        "FROM Ticket t " +
                        "JOIN t.seat s " +
                        "JOIN s.vehicle v " +
                        "JOIN v.vehicleType vt "
        );

        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;

        if (hasStartDate || hasEndDate) {
            jpql.append("WHERE ");
            if (hasStartDate) {
                jpql.append("FUNCTION('DATE', t.paymentDate) >= :startDate ");
            }
            if (hasEndDate) {
                if (hasStartDate) {
                    jpql.append("AND ");
                }
                jpql.append("FUNCTION('DATE', t.paymentDate) <= :endDate ");
            }
        }

        jpql.append("GROUP BY vt.vehicleTypeName " +
                "ORDER BY totalRevenue DESC");

        var query = entityManager.createQuery(jpql.toString(), Object[].class);

        if (hasStartDate) {
            query.setParameter("startDate", startDate);
        }
        if (hasEndDate) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }



}
