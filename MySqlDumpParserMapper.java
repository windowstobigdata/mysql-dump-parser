import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.Configuration;

public class MySqlDumpParserMapper extends Mapper<LongWritable, Text, Text, Text> {
	private Text keyObject = new Text();
	private Text valueObject = new Text();

	@Override
	public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
	  
		String line = value.toString();
		int strIndex = 0;
		String keystr = "";
		String valuestr = "";
		if (line.startsWith("INSERT INTO `")) {
			strIndex = line.indexOf("` VALUES ");
			if (strIndex > 0) {
				line = line.substring(strIndex+9);
				line = line.replaceAll("'|;", "");
				line = line.replace("),(", ")\t(");
				String[] tuples = line.split("\\t");
				for (int i=0; i<tuples.length; i++)
				{
				String tuple = tuples[i];
				tuple = tuple.replaceAll("\\(|\\)", "");
				String[] records = tuple.split(",");
				keystr = records[0];
				keyObject.set(keystr);
				valuestr = "";
				for (int ii=1; ii<records.length; ii++)
					{				
					valuestr = valuestr + "\t" + records[ii];
					}
				valuestr = valuestr.	substring(1);
				valueObject.set(valuestr);
				context.write(keyObject, valueObject);	
				}
			}
		}
	}
}