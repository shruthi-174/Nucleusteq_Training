show databases;
create database ntq;
use ntq;
show tables;

create table student(
sid int primary key,
fname varchar(50),
lname varchar(50),
dob date,
did int,
foreign key(did) references dept(did)
);
insert into student (sid,fname,lname,dob,did)
VALUES
    (1, 'Amit', 'Sharma', '1998-03-20', 101),
    (2, 'Kavita', 'Patil', '1997-08-15', 102),
    (3, 'Rahul', 'Verma', '1999-05-10', 103),
    (4, 'Deepika', 'Singh', '1996-11-28', 104),
    (5, 'Sandeep', 'Kumar', '1997-07-02', 105);
select *from student;
drop table student;
create table faculty(
fid INT PRIMARY KEY,
    fname VARCHAR(50),
    lname VARCHAR(50),
    salary int,
    did INT,
    dname varchar(50),
    FOREIGN KEY (did) REFERENCES dept(did)
);


insert into faculty(fid,fname,lname,salary,did,dname)
values  (101, 'Dr. Ananya', 'Mukherjee',60000, 101,'Mathematics'),
    (102, 'Prof. Sanjay', 'Chatterjee',30000, 102,'Chemistry'),
    (103, 'Dr. Rohini', 'Nair',15000, 103,'Mathematics'),
    (104, 'Prof. Vikram', 'Rao',45000, 104,'Biology'),
    (105, 'Dr. Aisha', 'Khan',55000, 105,'Chemistry');
    select *from faculty;


create table dept(
did int primary key,
dname varchar(50)
);
insert into dept(did,dname)
values (101, 'Computer Science'),
    (102, 'Mathematics'),
    (103, 'Physics'),
    (104, 'Chemistry'),
    (105, 'Biology');
select *from dept;

select sid,fname,lname from student where sid=5;
select fname, lname,salary from faculty where salary between 40000 and 60000;
select fname, lname,dname from faculty where dname ='chemistry' or dname='biology';
select fname, lname,dname from faculty where dname not in ('chemistry')
select fname,lname from faculty where lname like '%e'
select fname,lname from faculty where fname like '%a%'
select fname from faculty where fname like 'a_'
select fname, lname from student order by fname
select fname, lname,salary from faculty order by salary desc
select distinct dname as new_name from faculty
select dname from faculty group by dname
select count(dname) as cd from faculty 
select dname, count(dname)as cd from faculty group by dname having cd >1

alter table faculty add job_type varchar(50);
alter table faculty modify job_type int;
alter table faculty change column job_type job varchar(40)
alter table faculty drop column job
alter table department rename to dept
-- update faculty set job ='teacher'  (cannot be used without where clause)
update student set fname ='shruthi' where sid=2
update faculty set job ='teacher' where fid in (104,102,101)
replace into faculty(fid,job)values(102,'HOD')

-- on delete cascade and on update cascade
create table demo(
gid INT PRIMARY KEY,
 fname varchar(50),
    did INT,
    dname varchar(50),
    FOREIGN KEY (did) REFERENCES demo1(did) 
);
create table demo1(
did int primary key,
dname varchar(50)
);
insert into demo1(did,dname)
values (101, null),
    (102, 'Mathematics'),
    (103, null),
    (104, 'Chemistry'),
    (105, 'Biology'),
    (106, 'physics'),
    (107,null),
	(108, 'Chemistry'),
	(109, 'Computer Science'),
     (110,null);
    
insert into demo(gid,fname,did,dname)
values (101,"shruthi",101,null),
    (102,"ashu",102, 'Mathematics'),     
    (104,"gourav",104,null),
    (105,"rishi",105, 'Biology'),
	(106,"niharika",106, 'physics'),
	(107,"garima",107,null),
	(108,"samay",108, 'Chemistry'),
	(109,"tanmay",109, 'Computer Science'),
     (110,"nishant",110,null)
    
    select *from demo
    drop table demo1
delete from demo1 where did =103;
update demo set did=102 where did=102;


-- joins
select s.sid, s.fname,d.dname
from student s Inner join dept d 
on s.did=d.did
 
select *
from student s left join demo d 
on s.did=d.did

select *
from student s right join demo d
on s.did=d.did


create table emp(
eid int primary key,
fname varchar(50),
lname varchar(50),
salary int , 
dname varchar (50));
INSERT INTO emp(eid, fname, lname, salary, dname) VALUES
(1, 'Alice', 'Johnson', 60000,  'Computer Science'),
(2, 'Bob', 'Smith', 55000,  'Mathematics'),
(3, 'Charlie', 'Miller', 70000,"Physics"),
(4, 'David', 'Davis', 65000,  'Chemistry'),
(5, 'Eva', 'Brown', 50000,'Biology'),
(6, 'John', 'Doe', 50000,'Computer Science'),
(7, 'Jane', 'Smith', 55000,'Mathematics'),
(8, 'Michael', 'Johnson', 75000,  'Physics'),
(9, 'Emily', 'Williams', 65000, 'Chemistry'),
(10, 'Daniel', 'Brown', 60000, 'Biology'),
(11, 'Jessica', 'Miller', 58000, 'Computer Science'),
(12, 'Christopher', 'Wilson', 62000,  'Mathematics'),
(13, 'Ashley', 'Davis', 68000,  'Physics'),
(14, 'Brian', 'Martinez', 63000,  'Chemistry'),
(15, 'Megan', 'Anderson', 59000, 'Biology');
drop table emp
select * from emp;


-- Subqueries
-- fetch employees who' salary is more than the average salary earned by all employees
select * 
from emp where salary >(select avg(salary) from emp)

-- fetch the employees who earn the highest salary in each dept
select * from  emp
where (dname,salary) in (select dname, max(salary)as a_sal
from emp group by dname); 

-- find departments that don't have in any employees, (demo1 parent, demo child) 
select * from demo where dname not in (select distinct dname from demo1)
select * from demo d where not exists(select 1 from demo d1 where d1.dname=d.dname)

-- find the employees in each dept who earns more than the avg salary in that dept
select * from emp e 
where e.salary > (select avg(salary) from emp e1 where e.dname = e1.dname);

-- fetch dept whos's salary is better than average salary across all the dept
select *
from (select eid,sum(salary)as t_sal from emp group by eid) total_salary
join (select avg(t_sal) a_sal from 
	 (select eid,sum(salary)as t_sal from emp group by eid)x)avg_salary 
on total_salary.t_sal > avg_salary .a_sal

-- fetch all employees details and add remarks to those employees who earn more than the average pay
select *
, (case when e.salary > (select avg(salary)from emp) then "morw than avg pay"
else null
end) as mes_sal
from emp e;

-- other way
select *
, (case when e.salary > av_sal.a_Sal then "morw than avg pay"
else null
end) as mes_sal
from emp e cross join (select avg(salary) as a_Sal from emp) av_sal;



-- CASE FUNCTIONS
select fname,salary,
	(case when salary > 60000 then "higher salary"
		when salary < 60000 then "lower salary"
		else "average salary"
	end) as salary_value
from emp;

select fname,dname, 
(case dname
when "Biology" then "works at Biology dept"
when "Physics" then "works at Physics dept"
when "Mathematics" then "works at Mathematics dept"
else "other depts"
end) as depts
from emp;



-- CTE or WITH clause
-- fetch employees who earn more than avg salary of all employees
with avg_salary(avg_sal) as
(select convert (avg(salary) , signed) from emp)
select * 
from emp e join avg_salary av
where e.salary > av.avg_sal

-- fetch employees with average salaries in each department
with 
adep_sal(dname,ad_sal) as
(select dname, avg(salary) from emp group by dname)
select e.eid,e.fname,e.dname,e.salary,ads.ad_sal as average_dept_salary from emp e join adep_sal ads
on e.dname=ads.dname

-- fetch department who's salary where better than average salary across all department
with total_sal(eid,t_sal) as
(select eid,sum(salary)as t_sal from emp group by eid),
average_sal(a_sal)as
(select avg(t_sal)as a_sal from total_sal)
select * from total_sal tsl join average_sal asl on tsl.t_sal > asl.a_sal



-- window function
-- fetch the records of employee having highest salary
select e.*,
max(salary) over() as ms
from emp e;

-- fetch the records of employee having highest salary in each department
select dname, max(salary) as ms from emp group by dname

select e.*,
max(salary) over(partition by dname) as ms
from emp e;

-- row_number, rank, dense_rank,lead, lag
select e.*,
row_number() over() as rn
from emp e;

select e.*,
row_number() over(partition by dname) as rn
from emp e;

-- fetch the first 2 employees of each dept to join the company
select * from (
	select e.*,
	row_number() over(partition by dname) as rn
	from emp e) x
where x.rn <3

-- fetch the top 2 employees in each department earning the max salary
select * from 
 (select e.*,
 rank() over (partition by dname order by salary desc) as rk
 from emp e) x
 where x.rk <3

 select e.*,
 dense_rank() over (partition by dname order by salary desc) as dk
 from emp e;
 
 select e.*,
 lag(salary) over (partition by dname order by eid) as prev_sal
 from emp e;
 
  select e.*,
 lag(salary,2) over (partition by dname order by eid) as lg
 from emp e;
 
 -- fetch a query to display if the salary of an employee is higher,lower or equal to the previous employee
 select e.*,
 lag(salary) over (partition by dname order by eid) as prev_sal,
 case when e.salary > lag(salary) over (partition by dname order by eid) then "higher than prev emp"
	  when e.salary < lag(salary) over (partition by dname order by eid) then "lower than prev emp"
	  when e.salary = lag(salary) over (partition by dname order by eid)  then "equal to prev emp"
end sal_range
from emp e;
 
select e.*,
lead(salary) over (partition by dname order by eid) as next_sal
from emp e;
 
 
 