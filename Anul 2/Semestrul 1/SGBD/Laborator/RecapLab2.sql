--2 -b) tema
--3 tema

--4
--pt fiecare categorie afisati cate exemplare s-au imprumutat

select max(category), max(count(*))
from title t, rental r
where t.title_id = r.title_id
group by category;

select category, count(*), count( distinct title), count(r.copy_Id)
from title t, rental r
where t.title_id = r.title_id
group by category
having count(*) = (select max(count(*))
                    from title t, rental r
                    where t.title_id = r.title_id
                    group by category);
                    
--5
-- statusul este corect setat
select title_id, count(*)
from title_copy
where status = 'AVAILABLE'
group by title_id 
order by 1;

-- statusul nu este corect setat

select title_id, count(*)
from title_copy
where (copy_id, title_id)  not in (select copy_id, title_id
                                    from rental
                                    where act_ret_date is null)
group by title_id 
order by 1;


select title_id, count(*)
from (
        select copy_id, title_id
        from title_copy
        minus
        select copy_id, title_id
        from rental
        where act_ret_date is null   
    )
group by title_id 
order by 1;


--6

select title, copy_id, status as status_Setat,
case when (copy_id, a.title_id)  not in (select copy_id, title_id
                                        from rental
                                        where act_ret_date is null)
then 'AVAILABLE'
else 'RENTED'
end as status_Corect
from title_copy a, title b
where a.title_id = b.title_id;


--7
--a

select count(*)
from (
        select title, copy_id, status as status_Setat,
        case when (copy_id, a.title_id)  not in (select copy_id, title_id
                                                from rental
                                                where act_ret_date is null)
        then 'AVAILABLE'
        else 'RENTED'
        end as status_Corect
        from title_copy a, title b
        where a.title_id = b.title_id
    )
where status_Setat != status_Corect;

--b

create table title_copy_prof as
select * from title_copy;

update title_copy_prof a
set status = case when (copy_id, a.title_id) not in (select copy_id, title_id
                                                from rental
                                                where act_ret_date is null)
                then 'AVAILABLE'
                else 'RENTED'
                end
where status <> case when (copy_id, a.title_id) not in (select copy_id, title_id
                                                from rental
                                                where act_ret_date is null)
                then 'AVAILABLE'
                else 'RENTED'
                end;
commit;


--8
-- explicatii suplimentare la urmatorul laborator
select case when count(*) = 0 
            then 'DA'
            else 'NU'
        end raspuns
from(
    select res_date, member_id, title_id
    from reservation
    minus
    select book_date, member_id, title_id
    from rental
);

























