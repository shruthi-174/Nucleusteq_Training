1757. select product_id from Products where low_fats='Y' and recyclable= 'Y'
584.  select name from Customer where referee_id != 2 or referee_id is null
1683. select tweet_id from Tweets where length(content)> 15
1148. select distinct author_id as id from Views where author_id =viewer_id order by author_id
595.  select name, population,area from world where area >=3000000 or population >=25000000
     
1378. select eu.unique_id, e.name from employees e left join employeeUNI eu on e.id=eu.id
1068. select p.product_name,s.year,s.price from sales s inner join product p on s.product_id=p.product_id
197.  select w.id from weather w inner join weather w1 on dateDiff(w.recordDate,w1.recordDate)=1 and w.temperature > w1.temperature
1581. select v.customer_id, count(v.visit_id)as count_no_trans  from visits v left join
      transactions t on v.visit_id =t.visit_id where t.visit_id is null group by v.customer_id
577.  select e.name,b.bonus from employee e left join bonus b on e.empId=b.empId where b.bonus<1000 or b.bonus is null
1661. select a1.machine_id, round(avg(a2.timestamp-a1.timestamp),3) as processing_time from activity a1 inner join activity a2
      on a1.machine_id=a2.machine_id and a1.process_id=a2.process_id and a1.activity_type='start' and a2.activity_type='end'
      group by machine_id
1280. select s.student_id, s.student_name,sb.subject_name, count(e.subject_name) as attended_exams from students s cross join subjects sb
       left join examinations e on s.student_id =e.student_id and   sb.subject_name=e.subject_name
       group by s.student_id,s.student_name,sb.subject_name order by s.student_id,sb.subject_name
570.  select e.name from employee e inner join employee m on e.id=m.managerId group by e.id having count(e.id)>=5
1934. with temp as(
      select s.user_id as user_id, count(*)total_count,
      sum(case when action ="confirmed" then 1 
         else 0 
         end)as con_count
      from signups s left join confirmations c
      on s.user_id=c.user_id group by s.user_id)
      select user_id, round(con_count/total_count,2)as confirmation_rate from temp

620.  select * from cinema where description != 'boring' and id&1 order by rating desc
1251. select p.product_id, ifnull(round(sum(u.units * p.price)/sum(u.units),2),0)as average_price from prices p left join unitssold u
      on p.product_id=u.product_id and u.purchase_date between p.start_date and p.end_date
      group by p.product_id
1075. select p.project_id, round(avg(experience_years),2)as average_years from project p join employee e
      on p.employee_id =e.employee_id group by project_id
1633. with temp(total_sum) as (select count(user_id)as total_sum from users)
      select r.contest_id, round((count(r.user_id)*100 /total_sum),2)as percentage from  register r inner join temp
      group by contest_id order by percentage desc,contest_id
1211. select query_name, round(sum(rating/position)/count(query_name),2) as quality, round(100*avg(rating<3),2) as poor_query_percentage
      from queries where query_name is NOT NULL group by query_name 
1193. select date_format(trans_date,'%Y-%m')as month,country,count(*) as trans_count,sum(if (state='approved',1,0))as approved_count,sum(amount) as trans_total_amount,sum(if               	(state='approved',amount,0)) as approved_total_amount from transactions group by country,month      
1174. with temp as (select *,row_number()over(partition by customer_id order by order_date)as 'rn',
      (case when order_date=customer_pref_delivery_date then 1.0 else 0 end)as 'immediate' from delivery)
      select round(100*(sum(immediate)/count(immediate)),2)as immediate_percentage from temp where rn=1
550.  with temp as (select player_id,event_date,row_number()over(partition by player_id order by event_date)as rn from activity)
      select round(count(*)/ (select count(distinct player_id)from activity),2)as fraction from temp t join activity a
      on t.player_id =a.player_id and datediff(a.event_date,t.event_date)=1 where t.rn=1

     
2356. select teacher_id, count(distinct subject_id)as cnt from teacher group by teacher_id
1141. select activity_date as day, count(distinct user_id)as active_users from activity
      where datediff('2019-07-27',activity_date)<30 and activity_date <= '2019-07-27' group by activity_date
1070. with temp as( select product_id, year as first_year,quantity,price, rank() over
      (partition by product_id order by year)as rn from sales )
      select product_id,first_year,quantity,price from temp  where rn=1
596.  select class from courses group by class having count(class) >=5
1729. select user_id, count(follower_id) as followers_count from followers group by user_id order by user_id
619.  with temp as (select num as n from MyNumbers  group by num having count(num)=1)
      select max(n) as num from temp
1045. select c.customer_id from customer c group by customer_id  having count(distinct product_key)=(select count(product_key)from product)

1731. select e.employee_id, e.name,count(e1.reports_to)as reports_count, round(avg(e1.age))as average_age
      from employees e inner join employees e1 on e.employee_id=e1.reports_to group by e1.reports_to order by employee_id
1789. select employee_id, department_id from employee  where primary_flag ='Y' or employee_id in
      ( select employee_id from employee group by employee_id having count(department_id)=1)
610.  select x,y,z, (case when ((x+y>z) and (z+y>x) and (z+x>y)) then 'Yes' else 'No' end)as triangle from triangle
180.  with temp as( select id,num, lead(num) over (order by id) as next, lag(num) over (order by id) as prev from logs)
      select distinct num as ConsecutiveNums from temp where num=next and num=prev
1164. with temp as( select distinct product_id, max(change_date) over (partition by product_id)as last_day
      from products where change_date<='2019-08-16')
      select t.product_id, p.new_price as price from temp t join products p on t.product_id=p.product_id where t.last_day=p.change_date
      union
      select product_id, 10 as price from products where product_id not in (select product_id from temp)
1204. with temp as (select person_name, sum(weight) over (order by turn) as total from queue)
      select person_name from temp where total<=1000 order by total desc limit 1
1907. with c1 as(select 'Low Salary' as category , count(account_id) accounts_count from Accounts where income <20000),
      c2 as(select 'Average Salary' as category,count(account_id) accounts_count from Accounts where income between 20000 and 50000),
      c3 as(select 'High Salary' as category,count(account_id) accounts_count from Accounts where income > 50000)
      select * from c1
      union all
      select * from c2
      union all
      select * from c3
1978. select employee_id from employees where salary < 30000 and manager_id not in(select employee_id from employees) order by employee_id
626.  select id, case when id%2=0 then (lag(student)over (order by id)) else ifnull(lead(student)over(order by id), student) end as student         from seat
1341. ( select u.name as results from users u inner join MovieRating mr
      on u.user_id = mr.user_id group by u.user_id order by count(mr.rating) desc,u.name asc limit 1)
      union all
      (select m.title as results from movierating mr inner join  movies m  on m.movie_id=mr.movie_id
      where created_at between '2020-02-01' and '2020-02-29' group by mr.movie_id order by avg(mr.rating)desc,m.title asc limit 1)
1321. with  temp as (select visited_on, sum(amount) as total from customer group by visited_on)
      select visited_on,amount,average_amount from (select visited_on,
      sum(total)over(order by visited_on asc rows between 6 preceding and current row )as amount,
      round(avg(total)over(order by visited_on asc rows between 6 preceding and current row),2)as average_amount 
      from temp)as temp1 where date_sub(visited_on,interval 6 day) in (select visited_on from temp) order by visited_on asc
602.  with temp as( (select  requester_id as id from RequestAccepted)
      union all
      (select  accepter_id as id from RequestAccepted))
      select id,count(id) as num from temp group by id order by num desc limit 1
585.  select round(sum(tiv_2016),2)as tiv_2016 from Insurance where 
      concat(lat,lon) in (select concat(lat,lon) from Insurance group by concat(lat,lon) having count(*) =1)
      and tiv_2015 in (select tiv_2015 from Insurance group by tiv_2015 having count(*)>1)
185.  with temp as( select departmentId,name,salary, dense_rank() over(partition by departmentId order by salary desc)as dr from employee )
      select d.name as Department, t.name as Employee,t.salary as Salary from temp t inner join Department d
      on t.departmentId=d.id where t.dr <4

1667. select user_id,concat(upper(substr(name,1,1)), lower(substr(name,2,length(name)))) as name from users order by user_id
1527. select p.patient_id,p.patient_name, p.conditions from patients p where p.conditions like "DIAB1%" or p.conditions like "% DIAB1%"
196.  delete a from person a, person b where a.id>b.id and a.email=b.email
176.  select max(salary) as SecondHighestSalary  from employee where salary not in (select max(salary) from employee)
1484. select sell_date, count(distinct product)as num_sold, group_concat(distinct product)as products
      from activities group by sell_date order by sell_date
1327. select p.product_name, sum(o.unit) as unit from products p inner join orders o
      on p.product_id=o.product_id where o.order_date between '2020-02-01' and '2020-02-29' group by p.product_id having unit>=100
1517. select * from users where mail regexp  '^[a-zA-Z]{1}[a-zA-Z0-9_.-]*@leetcode[.]com$'
