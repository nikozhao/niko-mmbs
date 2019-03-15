package niko.dao.entry;// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Stock entity. @author MyEclipse Persistence Tools
 */

/**
 * stock info
 */
@Entity
@Table(name = "stock")
public class Stock implements java.io.Serializable {

	// Fields

	private String stockNo;
	private String stockName;

	// Constructors

	/** default constructor */
	public Stock() {
	}

	/** minimal constructor */
	public Stock(String stockNo) {
		this.stockNo = stockNo;
	}

	/** full constructor */
	public Stock(String stockNo, String stockName) {
		this.stockNo = stockNo;
		this.stockName = stockName;
	}

	// Property accessors
	@Id
	@Column(name = "stock_no", unique = true, nullable = false)
	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	@Column(name = "stock_name")
	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

}