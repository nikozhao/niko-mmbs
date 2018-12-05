package niko.dao.entry;// default package

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
 * StockDay entity. @author MyEclipse Persistence Tools
 */

/**
 * the stock's transaction information . include  day,max,min
 */
@Entity
@Table(name="stock_day"
)
@Data
public class StockDay implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name="stock_no")
    private String stockNo;
    @Column(name="day")
    private Timestamp day;
    @Column(name="max")
    private BigDecimal max;
    @Column(name="min")
    private BigDecimal min;
    @Column(name="pe")
    private BigDecimal pe;
    @Column(name="pb")
    private BigDecimal pb;
    @Column(name="deal_account")
    private BigDecimal dealAccount;
    @Column(name="total_account")
    private BigDecimal totalAccount;
    @Column(name="total_tradable_account")
    private BigDecimal totalTradableAccount;
    @Column(name="rise_ratio")
    private BigDecimal riseRatio;
   /* @Column(name="number_ratio")
    private BigDecimal numberRatio;*/
    @Column(name="out_deal")
    private BigDecimal outDeal;
    @Column(name="in_deal")
    private BigDecimal inDeal;
    @Column(name="y_price")
    private BigDecimal yPrice;
    @Column(name="t_price_start")
    private BigDecimal tPriceStart;
    @Column(name="t_price_end")
    private BigDecimal tPriceEnd;
    @Column(name="t_price_pre")
    private BigDecimal tPricePre;
    @Column(name="sell_buy_reatio")
    private BigDecimal sellBuyReatio;
    @Column(name="max_min_price")
    private BigDecimal maxMinPrice;
}