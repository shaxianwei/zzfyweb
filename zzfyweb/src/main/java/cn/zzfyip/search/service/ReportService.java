package cn.zzfyip.search.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentStatisticJob;
import cn.zzfyip.search.dal.common.vo.PatentFawenVo;
import cn.zzfyip.search.utils.DateUtils;

@Service
public class ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	GlobalConstant globalConstant;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	PatentDao patentDao;
	
	public void generateFawenReport(PatentStatisticJob job){
		HSSFWorkbook workbook = new HSSFWorkbook();
		List<PatentFawenVo> list = patentDao.selectPatentFawenVoListByFawenUpdateDate(job.getFawenUpdateDate());
		createFawenSheet(workbook,list);
		
		String fileName = String.format("(%s)_fawen_report.xls", DateUtils.formatDate(job.getFawenUpdateDate()));
		writeExcelToFile(workbook, fileName);
		
		mailService.sendMail(DateUtils.formatDate(job.getFawenUpdateDate())+"更新日发文数据导出", "见附件", fileName);
		
		job.setJobStatus("FINISH");
		patentDao.updatePatentStatisticJobRecord(job);
	}
	
	private File writeExcelToFile(HSSFWorkbook workbook, String fileName) {
        File reportFile = null;
        FileOutputStream fos = null;
        try {
            reportFile = new File(globalConstant.getBasePath() + "temp/" + fileName);
            fos = new FileOutputStream(reportFile);
            workbook.write(fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            logger.error("error when saving file " + fileName + ",cause file is not found", e);
        } catch (IOException e) {
            logger.error("error when saving file " + fileName, e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("error when saving file " + fileName, e);
                }
            }
        }
        return reportFile;
    }
	
	private HSSFSheet createFawenSheet(HSSFWorkbook workbook,List<PatentFawenVo> list) {
        HSSFSheet sheet = workbook.createSheet("发文检索结果");
        
        int r = 0;
        int c = 0;
        
        HSSFRow header = sheet.createRow(r++);
        
        HSSFCell cell = header.createCell(c++);
        cell.setCellValue("专利号");

        cell = header.createCell(c++);
        cell.setCellValue("发文通知内容");
        
        cell = header.createCell(c++);
        cell.setCellValue("申请日");
        
        cell = header.createCell(c++);
        cell.setCellValue("名称");

        cell = header.createCell(c++);
        cell.setCellValue("公开(公告)号");
        
        cell = header.createCell(c++);
        cell.setCellValue("公开(公告)日");

        cell = header.createCell(c++);
        cell.setCellValue("主分类号");
        
        cell = header.createCell(c++);
        cell.setCellValue("分案原申请号");
        
        cell = header.createCell(c++);
        cell.setCellValue("分类号");
        
        cell = header.createCell(c++);
        cell.setCellValue("颁证日");
        
        cell = header.createCell(c++);
        cell.setCellValue("优先权");
        cell = header.createCell(c++);
        cell.setCellValue("申请(专利权)人");
        cell = header.createCell(c++);
        cell.setCellValue("地址");
        cell = header.createCell(c++);
        cell.setCellValue("发明(设计)人");
        cell = header.createCell(c++);
        cell.setCellValue("国际申请");
        cell = header.createCell(c++);
        cell.setCellValue("国际公布");
        cell = header.createCell(c++);
        cell.setCellValue("进入国家日期");
        cell = header.createCell(c++);
        cell.setCellValue("专利代理机构");
        cell = header.createCell(c++);
        cell.setCellValue("代理人");
        cell = header.createCell(c++);
        cell.setCellValue("摘要");


        for(PatentFawenVo vo:list){
            c = 0;
            HSSFRow bodyRow = sheet.createRow(r++);
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getPatentNo());

            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getFawenInfo());

            cell = bodyRow.createCell(c++);
            cell.setCellValue(DateUtils.formatDate(vo.getApplyDate()));

            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getPatentName());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getPublicNo());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(DateUtils.formatDate(vo.getPublicDate()));

            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getMainCategoryNo());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getPreApplyNo());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getSecCategoryNo());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(DateUtils.formatDate(vo.getCertificateDate()));
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getPreRight());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getApplier());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getAddress());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getInventor());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getGlobalPatent());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getGlobalPublic());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(DateUtils.formatDate(vo.getEntryCountryDate()));
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getAgency());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getAgent());
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getSummary());
        }
        
        return sheet;
    }
}
