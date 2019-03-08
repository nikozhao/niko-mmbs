package niko.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

import niko.dao.entry.StockWorkday;


/**
 * @Author: Niko Zhao
 * @Date: Create in 04/13/18
 * @Email:
 */
public interface StockWorkdayRepository extends JpaRepository<StockWorkday, String> {
    @Query(value = "select w.* from stock_workday w ORDER BY w.day desc limit 0,5", nativeQuery = true)
    List<StockWorkday> getLastFive();

    List<StockWorkday> findByDay(Date date);
}
