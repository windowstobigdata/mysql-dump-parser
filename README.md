# MySqlDumpParser #


---------
Map only MapReduce Application to convert mysqldump generated SQL files into tab delimted, new line terminated flat files in HDFS.

For use when sqoop is not available or not an option, eg ingesting historical mysql dumps.

Usage:


    \# dump table to SQL
    mysqldump --host=127.0.0.1 -u root -p <dbname> <tablename> > C:\mysqldumps\tablename.sql 
 
    \# ingest raw SQL file into HDFS 
    hadoop fs -put tablename.sql tabledata_raw/
  
    \# complile source  
    javac -classpath \`hadoop classpath\` *.java  
    jar cvf mysqldumpparser.jar *.class
  
    \# run program
    hadoop jar mysqldumpparser.jar MySqlDumpParser tabledata_raw tabledata

*Windows to Big Data, April 2014* 