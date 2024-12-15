package com.mt.rest.controller;

import com.mt.entity.OrderDetail;
import com.mt.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/statistical")
public class StatisticalController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping
    public Map<String, Object> getStatistical(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate toDate) {

        // Lấy tất cả OrderDetails
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // Lọc theo ngày nếu có điều kiện
        if (fromDate != null || toDate != null) {
            orderDetails = orderDetails.stream()
                    .filter(od -> {
                        // Lấy createdAt từ Order và kiểm tra null
                        LocalDate createdAt = null;
                        if (od.getOrder().getCreatedAt() != null) {
                            createdAt = od.getOrder().getCreatedAt().toInstant()
                                    .atZone(TimeZone.getDefault().toZoneId())
                                    .toLocalDate();
                        }

                        // Kiểm tra nếu createdAt không null và áp dụng các điều kiện ngày
                        return (createdAt != null) &&
                               (fromDate == null || !createdAt.isBefore(fromDate)) &&
                               (toDate == null || !createdAt.isAfter(toDate));
                    })
                    .collect(Collectors.toList());
        }

        // Group theo ngày
        Map<LocalDate, List<OrderDetail>> groupedByDate = orderDetails.stream()
                .collect(Collectors.groupingBy(od -> {
                    // Lấy createdAt từ Order và kiểm tra null
                    LocalDate createdAt = null;
                    if (od.getOrder().getCreatedAt() != null) {
                        createdAt = od.getOrder().getCreatedAt().toInstant()
                                .atZone(TimeZone.getDefault().toZoneId())
                                .toLocalDate();
                    }
                    return createdAt;
                }));

        // Tính toán tổng giá trị
        List<Map<String, Object>> result = groupedByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<OrderDetail> details = entry.getValue();

                    double totalBuy = details.stream()
                            .mapToDouble(od -> od.getQuantity() * od.getProduct().getPrice())
                            .sum();
                    double totalSell = details.stream()
                            .mapToDouble(od -> od.getQuantity() * od.getPrice())
                            .sum();
                    double profit = totalSell - totalBuy;

                    Map<String, Object> stats = new HashMap<>();
                    stats.put("Date", date);
                    stats.put("TotalBuy", totalBuy);
                    stats.put("TotalSell", totalSell);
                    stats.put("Profit", profit);
                    return stats;
                })
                .collect(Collectors.toList());

        // Trả về kết quả
        Map<String, Object> response = new HashMap<>();
        response.put("Data", result);
        return response;
    }


}
