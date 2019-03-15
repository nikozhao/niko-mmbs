package niko.dao.entry;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2018/12/6
 */
@Entity
@Table(name="stock_master"
)
@Data
public class StockMaster {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name="stock_no")
    private String stockNo;
    @Column(name="big_in")
    private BigDecimal bigIn;
    @Column(name="big_out")
    private BigDecimal bigOut;
    @Column(name="big")
    private BigDecimal big;
    @Column(name="small_in")
    private BigDecimal smallIn;
    @Column(name="small_out")
    private BigDecimal smallOut;
    @Column(name="small")
    private BigDecimal small;
    @Column(name="day")
    private Timestamp day;

}
