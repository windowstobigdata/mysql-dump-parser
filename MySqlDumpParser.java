import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
* 	MySqlDumpParser
* 	MapReduce (Map-Only) Application to convert mysql dump file(s) in HDFS to TSV files for use with Pig or Hive
*		Designed for self contained table dumps from mysql
*		
*		Written by Jeffrey Aven
*		Windows to Big Data, Apr 2014 
*/

public class MySqlDumpParser extends Configured implements Tool {

  @Override
  public int run(String[] args) throws Exception {

	Configuration conf = this.getConf();

	FileSystem fs = FileSystem.get(conf);
	Path outputPath = new Path(args[1]) ;
	if (fs.exists(outputPath)) {
		System.out.println("Deleting output path - " + args[1]);
		fs.delete(outputPath, true);
	} else {
		System.out.println("New output path to be used - " + args[1]);
	}

	String jobName =  "MySQL Dump To TSV Files";
	
     Job job = new Job(conf, jobName);
   
    job.setJarByClass(MySqlDumpParser.class);
    
	FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, outputPath);
		
    job.setMapperClass(MySqlDumpParserMapper.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);	
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

	job.setNumReduceTasks(0);
	
	return job.waitForCompletion(true) ? 0 : 1;
  }
  
  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new Configuration(), new MySqlDumpParser(), args);
    System.exit(exitCode);
  } 
 
}
