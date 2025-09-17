select DB_UNIQUE_NAME, name, dbid
from   V$DATABASE;

select name, value
from   v$parameter
where  name in 
   ('db_domain',
    'db_name',
    'db_unique_name',
    'global_names',
    'instance_name',
    'service_names')
order by 1;
