package niko.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import niko.dao.entry.StockDay;
import niko.dao.entry.StockMaster;


/**
 * @Author: Niko Zhao
 * @Date: Create in 04/10/18
 * @Email:
 */
public interface StockMasterRepository extends JpaRepository<StockMaster, Integer> {

    List<StockMaster> findByStockNoAndDay( String stockNo, Date today);
}
