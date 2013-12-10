package test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.bloodtesting.TSVFileHeaderName;
import model.bloodtesting.UploadTTIResultConstant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import repository.bloodtesting.BloodTestingRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:**/v2v-servlet.xml" })
@WebAppConfiguration
@TransactionConfiguration(defaultRollback=false)
@Transactional
public class TTIControllerTest {
	
	@Autowired
	public BloodTestingRepository bloodTestingRepository;

	@Test
	public void testSaveTestResultsToDatabase() {
		List<TSVFileHeaderName> tSVFileHeaderNameList = new ArrayList<TSVFileHeaderName>();
		TSVFileHeaderName tSVFileHeaderNameObj;
		try{
			SimpleDateFormat formatter = new SimpleDateFormat(
					UploadTTIResultConstant.DATE_FORMAT);
			tSVFileHeaderNameObj = new TSVFileHeaderName();
			tSVFileHeaderNameObj.setSID("C1213000001");
			tSVFileHeaderNameObj.setAssayNumber(20);
			tSVFileHeaderNameObj.setResult(BigDecimal.valueOf(480.02));
			tSVFileHeaderNameObj.setInterpretation("0");
			tSVFileHeaderNameObj.setCompleted(formatter.parse("16.10.2013 17:03"));
			tSVFileHeaderNameObj.setOperatorID(1);
			tSVFileHeaderNameObj.setReagentLotNumber("29582LF00");
			
			tSVFileHeaderNameList.add(tSVFileHeaderNameObj);
			bloodTestingRepository.saveTestResultsToDatabase(tSVFileHeaderNameList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
