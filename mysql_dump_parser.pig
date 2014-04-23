/* 
*		mysql_dump_parser.pig
*		Pig Macro to Parse MySQL Table Dumps
*
*		Written by Jeffrey Aven
*
*		USAGE:
*
*		import 'mysql_dump_parser.pig';
*		mysqltable = mysql_dump_parser('mysqldump_file_or_dir');
*		mysqltablewithschema = FOREACH mysqltable GENERATE $0 as col1:int, ...;
*
*/

define mysql_dump_parser (MYSQLDUMPLOC)
returns result {
mysqldump = LOAD '$MYSQLDUMPLOC' USING TextLoader() AS (data:chararray);
mysqldump_insertsonly = FILTER mysqldump BY (data MATCHES '.*INSERT INTO.*');
mysqldump_valuesonly = FOREACH mysqldump_insertsonly GENERATE  REPLACE(data, '^INSERT.*` VALUES\\s', '') AS data;
mysqldump_values = FOREACH mysqldump_valuesonly GENERATE REPLACE(data, ';|\'', '') AS data;
mysqldump_values_delim = FOREACH mysqldump_values GENERATE REPLACE(data, '\\),\\(', '\\)\t\\(') AS data;
values_tokenized = FOREACH mysqldump_values_delim GENERATE FLATTEN(TOKENIZE(data, '\t')) AS data;
values_tuples = FOREACH values_tokenized GENERATE REPLACE(data, '^\\(|\\)$', '') AS data;
$result = FOREACH values_tuples GENERATE FLATTEN(STRSPLIT(data, ','));
};