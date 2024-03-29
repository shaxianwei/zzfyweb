package cn.zzfyip.search.dal.common.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PatentFee {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.id
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.patent_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private String patentNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.fee_amount
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private BigDecimal feeAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.fee_type
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private String feeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.register_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private String registerNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.receipt_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private String receiptNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.fee_person
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private String feePerson;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.status
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.fee_date
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private Date feeDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.patent_fee.add_date
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    private Date addDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.id
     *
     * @return the value of public.patent_fee.id
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.id
     *
     * @param id the value for public.patent_fee.id
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.patent_no
     *
     * @return the value of public.patent_fee.patent_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public String getPatentNo() {
        return patentNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.patent_no
     *
     * @param patentNo the value for public.patent_fee.patent_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setPatentNo(String patentNo) {
        this.patentNo = patentNo == null ? null : patentNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.fee_amount
     *
     * @return the value of public.patent_fee.fee_amount
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.fee_amount
     *
     * @param feeAmount the value for public.patent_fee.fee_amount
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.fee_type
     *
     * @return the value of public.patent_fee.fee_type
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.fee_type
     *
     * @param feeType the value for public.patent_fee.fee_type
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.register_no
     *
     * @return the value of public.patent_fee.register_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public String getRegisterNo() {
        return registerNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.register_no
     *
     * @param registerNo the value for public.patent_fee.register_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo == null ? null : registerNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.receipt_no
     *
     * @return the value of public.patent_fee.receipt_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public String getReceiptNo() {
        return receiptNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.receipt_no
     *
     * @param receiptNo the value for public.patent_fee.receipt_no
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo == null ? null : receiptNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.fee_person
     *
     * @return the value of public.patent_fee.fee_person
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public String getFeePerson() {
        return feePerson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.fee_person
     *
     * @param feePerson the value for public.patent_fee.fee_person
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setFeePerson(String feePerson) {
        this.feePerson = feePerson == null ? null : feePerson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.status
     *
     * @return the value of public.patent_fee.status
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.status
     *
     * @param status the value for public.patent_fee.status
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.fee_date
     *
     * @return the value of public.patent_fee.fee_date
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public Date getFeeDate() {
        return feeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.fee_date
     *
     * @param feeDate the value for public.patent_fee.fee_date
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setFeeDate(Date feeDate) {
        this.feeDate = feeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.patent_fee.add_date
     *
     * @return the value of public.patent_fee.add_date
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public Date getAddDate() {
        return addDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.patent_fee.add_date
     *
     * @param addDate the value for public.patent_fee.add_date
     *
     * @mbggenerated Fri Feb 21 15:35:45 CST 2014
     */
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
}