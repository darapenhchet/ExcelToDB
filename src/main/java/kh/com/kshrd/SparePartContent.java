package kh.com.kshrd;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.TransactionScoped;

import org.apache.poi.hssf.usermodel.HSSFPicture;

@Entity
@Table(name="models")
public class SparePartContent implements Serializable{

    @Id
/*	@SequenceGenerator(name="pk_sequence",sequenceName="spare_part_contents_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")*/
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="model_id")
	private Long id;
	private String logoBrand;
	private String koreanTitle;
	private String englishTitle;
	private String date;
	private String code;
	private Long parentId;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the logoBrand
	 */
	public String getLogoBrand() {
		return logoBrand;
	}
	/**
	 * @param logoBrand the logoBrand to set
	 */
	public void setLogoBrand(String logoBrand) {
		this.logoBrand = logoBrand;
	}
	/**
	 * @return the koreanTitle
	 */
	public String getKoreanTitle() {
		return koreanTitle;
	}
	/**
	 * @param koreanTitle the koreanTitle to set
	 */
	public void setKoreanTitle(String koreanTitle) {
		this.koreanTitle = koreanTitle;
	}
	/**
	 * @return the englishTitle
	 */
	public String getEnglishTitle() {
		return englishTitle;
	}
	/**
	 * @param englishTitle the englishTitle to set
	 */
	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
