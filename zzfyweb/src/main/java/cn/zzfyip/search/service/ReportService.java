package cn.zzfyip.search.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import cn.zzfyip.search.dal.common.vo.PatentStatisticVo;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.ZipUtils;

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
		List<PatentStatisticVo> list = patentDao.selectPatentFawenStatisticVoListByFawenUpdateDate(job.getFawenUpdateDate());
		
		//XSL file
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		createFawenSheet(workbook,list,"发文检索结果");
//		String fileName = String.format("(%s)_fawen_report.xls", DateUtils.formatDate(job.getFawenUpdateDate()));
//		writeExcelToFile(workbook, fileName);
		
		//TXT file
		String fileName2 = String.format("(%s)_fawen_report.txt", DateUtils.formatDate(job.getFawenUpdateDate()));
		writeTxtToFile(list, fileName2);
		
		//ZIP these files
		zipFile(fileName2);
		
		mailService.sendMail(DateUtils.formatDate(job.getFawenUpdateDate())+"更新日发文数据导出", "见附件", fileName2+".zip");
		
		job.setJobStatus("FINISH");
		patentDao.updatePatentStatisticJobRecord(job);
	}
	
	public void generateFeeReport(Date fromDay, Date endDay){
		List<PatentStatisticVo> list = patentDao.selectPatentFeeStatisticVoListByFromDateAndEndDate(fromDay,endDay);
		
		//XSL file
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		createFawenSheet(workbook,list,"无效收费检索结果");
//		String fileName = String.format("(%s)to(%s)_wuxiao_report.xls", DateUtils.formatDate(fromDay), DateUtils.formatDate(endDay));
//		writeExcelToFile(workbook, fileName);
		
		//TXT file
		String fileName2 = String.format("(%s)to(%s)_wuxiao_report.txt", DateUtils.formatDate(fromDay), DateUtils.formatDate(endDay));
		writeTxtToFile(list, fileName2);
		
		//ZIP these files
		zipFile(fileName2);
		
//		mailService.sendMail("从"+DateUtils.formatDate(fromDay)+"到"+DateUtils.formatDate(endDay)+"无效收费数据导出", "见附件", fileName2+".zip");
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
	
	private HSSFSheet createFawenSheet(HSSFWorkbook workbook,List<PatentStatisticVo> list,String sheetName) {
        HSSFSheet sheet = workbook.createSheet(sheetName);
        
        int r = 0;
        int c = 0;
        
        HSSFRow header = sheet.createRow(r++);
        
        HSSFCell cell = header.createCell(c++);
        cell.setCellValue("专利号");

        cell = header.createCell(c++);
        cell.setCellValue("通知或收费内容");
        
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


        for(PatentStatisticVo vo:list){
            c = 0;
            HSSFRow bodyRow = sheet.createRow(r++);
            
            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getPatentNo());

            cell = bodyRow.createCell(c++);
            cell.setCellValue(vo.getExtInfo());

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
	
	private void writeTxtToFile(List<PatentStatisticVo> list,String fileName){
		try {
			PrintWriter pw = new PrintWriter( new FileWriter( globalConstant.getBasePath() + "temp/" +fileName) );
			String lineSeparators ="\r\n----------------------------------------------------------------\r\n";
			pw.println(fileName);
			pw.println(lineSeparators);
			for(PatentStatisticVo vo:list){
				pw.println("专利号："+vo.getPatentNo());
				pw.println("内容：");
				if(StringUtils.isNotBlank(vo.getExtInfo())){
					String[] fawenLines = StringUtils.split(vo.getExtInfo(), ";");
					for(String fawenLine:fawenLines){
						pw.println(fawenLine+";");
					}
				}
				pw.println("申请日："+DateUtils.formatDate(vo.getApplyDate()));
				pw.println("名称："+vo.getPatentName());
				pw.println("公开(公告)号："+vo.getPublicNo());
				pw.println("公开(公告)日："+DateUtils.formatDate(vo.getPublicDate()));
				pw.println("主分类号："+vo.getMainCategoryNo());
				pw.println("分案原申请号："+vo.getPreApplyNo());
				pw.println("分类号："+vo.getSecCategoryNo());
				pw.println("颁证日："+DateUtils.formatDate(vo.getCertificateDate()));
				pw.println("优先权："+vo.getPreRight());
				pw.println("申请(专利权)人："+vo.getApplier());
				pw.println("地址："+vo.getAddress());
				pw.println("发明(设计)人："+vo.getInventor());
				pw.println("国际申请："+vo.getGlobalPatent());
				pw.println("国际公布："+vo.getGlobalPublic());
				pw.println("进入国家日期："+DateUtils.formatDate(vo.getEntryCountryDate()));
				pw.println("专利代理机构："+vo.getAgency());
				pw.println("代理人："+vo.getAgent());
				pw.println("摘要："+vo.getSummary());
				pw.println(lineSeparators);
			}
			pw.close();
		} catch (IOException e) {
			logger.error("IOException",e);
		}
	}
	
	private void zipFile(String ...fileNames){
		for(String fileName:fileNames){
			try {
				ZipUtils.compress(globalConstant.getBasePath() + "temp/" +fileName);
			} catch (Exception e) {
				logger.error("Compress File error!",e);
			}
		}
	}
}
