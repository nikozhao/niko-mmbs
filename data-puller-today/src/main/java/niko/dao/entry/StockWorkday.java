package niko.dao.entry;// default package

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * StockWorkday entity. @author MyEclipse Persistence Tools
 */

/**
 * save  the day  which can transaction the stock.
 */
@Entity
@Table(name = "stock_workday")
public class StockWorkday implements java.io.Serializable {

	// Fields

	private Integer id;
	private Timestamp day;

	// Constructors

	/** default constructor */
	public StockWorkday() {
	}

	/** full constructor */
	public StockWorkday(Timestamp day) {
		this.day = day;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "day", length = 19)
	public Timestamp getDay() {
		return this.day;
	}

	public void setDay(Timestamp day) {
		this.day = day;
	}

}