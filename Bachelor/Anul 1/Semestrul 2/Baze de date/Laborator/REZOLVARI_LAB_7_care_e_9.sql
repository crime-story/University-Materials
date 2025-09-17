SELECT DISTINCT employee_id 
FROM works_on a 
WHERE NOT EXISTS (SELECT 1 
                    FROM project p 
                    WHERE budget=10000 
                    AND NOT EXISTS (SELECT 'x'
                                    FROM works_on b 
                                    WHERE p.poject_id=b.prroject_id 
                                    AND b.employee_id=a.employee_id));
                                    
SELECT employee_id 
FROM works_on 
WHERE project_id IN (   SELECT project_id 
                        FROM project 
                        WHERE budget=10000) 
GROUP BY employee_id 
HAVING COUNT(project_id)= (SELECT COUNT(*) 
                            FROM project 
                            WHERE budget=10000);
                            
SELECT employee_id 
FROM works_on 
MINUS 
SELECT employee_id from (SELECT employee_id, project_id 
                        FROM (SELECT DISTINCT employee_id FROM works_on) t1, 
                            (SELECT project_id FROM project WHERE budget=10000) t2 
                        MINUS 
                        SELECT employee_id, project_id FROM works_on ) t3;
    

                        
SELECT DISTINCT employee_id 
FROM works_on a 
WHERE NOT EXISTS ( (SELECT project_id 
                    FROM project p 
                    WHERE budget=10000) 
                    MINUS 
                    (SELECT p.project_id 
                        FROM project p, works_on b 
                        WHERE p.project_id=b.project_id 
                        AND b.employee_id=a.employee_id));                        
                        
--1
-- metoda 2

select employee_Id
from works_on
where project_id in (
                        select project_id
                        from project
                        where to_char(start_date, 'yyyy') = 2006 and
                        to_char(start_date, 'MM') <=6)
GROUP BY employee_id
having count(project_id) = (select count(*)
                            from project
                            where to_char(start_date, 'yyyy') = 2006 and
                            to_char(start_date, 'MM') <=6);












