# MySqlDumpParser (Java MR/PigLatin)#


---------
Map only Java MapReduce Application and alternative Apache Pig script to convert mysqldump generated SQL files into tab delimted, new line terminated flat files in HDFS.

For use when sqoop is not available or not an option, eg ingesting historical mysql dumps.

Java MR Usage:


    \# dump table to SQL
    mysqldump --host=127.0.0.1 -u root -p <dbname> <tablename> > C:\mysqldumps\tablename.sql 
 
    \# ingest raw SQL file into HDFS 
    hadoop fs -put tablename.sql tabledata_raw/
  
    \# complile source  
    javac -classpath \`hadoop classpath\` *.java  
    jar cvf mysqldumpparser.jar *.class
  
    \# run program
    hadoop jar mysqldumpparser.jar MySqlDumpParser tabledata_raw tabledata

Pig Usage:

    import 'mysql_dump_parser.pig';
    mysqltable = mysql_dump_parser('mysqldump_file_or_dir');
    mysqltablewithschema = FOREACH mysqltable GENERATE $0 as col1:int, ...;
    
*Windows to Big Data, April 2014* 