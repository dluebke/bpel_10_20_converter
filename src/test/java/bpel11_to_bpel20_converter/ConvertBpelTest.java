package bpel11_to_bpel20_converter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value=Parameterized.class)
public class ConvertBpelTest {

	@Parameters(name = "{index}: Conversion of {1} from BPEL 1.1 to BPEL 2.0")
    public static Iterable<Object[]> data() {
    	List<Object[]> result = new ArrayList<>();
    	
    	String testDataDirectory = "src/test/resources";
		for(File fileName : new File(testDataDirectory).listFiles()) {
    		result.add(new Object[] { fileName.getAbsoluteFile() });
    	}
		
		return result;
    }

	private File bpel11File;
	private File bpel20File;
	
    public ConvertBpelTest(File bpel11File) {
    	this.bpel11File = bpel11File;
    	File targetDir = new File("target/bpel");
    	if(!targetDir.exists()) {
    		targetDir.mkdirs();
    	}
		this.bpel20File = new File(targetDir, bpel11File.getName());
    }
    
	@Test
	public void testConversion() {
		ConvertBpel.main(new String[] { bpel11File.toString(), bpel20File.toString() });
	}

}
