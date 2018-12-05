package niko.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import niko.dao.entry.StockDay;


/**
 * @Author: Niko Zhao
 * @Date: Create in 04/10/18
 * @Email:
 */
public interface StockDayRepository extends JpaRepository<StockDay, Integer> {

   /* @Query("select d from StockDay d ,StockMonitor m where d.stockNo = m.stockNo and m.status =1 and d.day>:start ")
    List<StockDay> getStockDay(@Param("start") Date start);*/

    @Query("select count(1) from StockDay d where d.stockNo = :stockNo")
    Integer countByStockNo(@Param("stockNo") String stockNo);

    List<StockDay> findByStockNoAndDay(@Param("stockNo") String stockNo, @Param("day") Date day);
}
