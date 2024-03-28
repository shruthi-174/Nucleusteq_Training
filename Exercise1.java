/* Your company employs 200 people. They keep records of all the employee information in the *Employees.csv* file, which contains the following
columns: first name, last name, department, position, and salary. However, they would like to sort the data; first, they would like to group
the rows by department lexicographically, and then they would like to sort the rows by salary.
As a Java developer, you have been assigned to create a Java application that is capable of carrying out this task.
*/
package Exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Creating Employee class
class Employee{
	private String firstname;
	private String lastname;
	private String department;
	private String email;
	private int salary;
	
// parameterized constructor
	Employee(String firstname, String lastname, String department, String email, int salary){
		this.firstname=firstname;
		this.lastname=lastname;
		this.department=department;
		this.email=email;
		this.salary=salary;
	}
	
//override toString() method to seperate data with comma(,)
	public String toString() {
		return firstname+","+lastname+","+department+","+email+","+salary;
	}
	
//function to return department
	public String getDepartment() {
		return department;
	}

//function to return salary
	public int getSalary() {
		return salary;
	}
}

//MAIN CLASS
public class Exercise1 {
    public static void main(String[] args) {
    	
      // to read file
      List<Employee> employees=readEmployees("E:\\nucleusteq\\files\\Employees.csv");
      sortEmployees(employees);

      //printing employees data
      for(Employee emp:employees) {
    	  System.out.println(emp);
      }
    }

    //performing sorting operations
	private static void sortEmployees(List<Employee> employees) {
		employees.sort(Comparator.comparing(Employee::getDepartment)
				.thenComparingInt(Employee::getSalary));
		
	}

	//fetching data from file into list
	private static List<Employee> readEmployees(String filename) {
		List<Employee> employee=new ArrayList<>();
		
		try {
			BufferedReader br= new BufferedReader(new FileReader(filename));
			boolean firstline=true;
			String str;
			while((str=br.readLine())!=null) {
				if(firstline) {
					firstline=false;
					continue;
				}
				String[] column_value=str.split(",");
				if(column_value.length==5) {
					String firstname=column_value[0];
					String lastname=column_value[1];
					String department=column_value[2];
					String email=column_value[3];
					int salary;
					
					// to handle NumberFormatException as some values in salary column has '-'
					try {
						salary=column_value[4].isEmpty() ? 0 : Integer.parseInt(column_value[4]);
						
					}catch(NumberFormatException e) {
						salary=0;
						System.out.println("Invalid salary input for "+firstname+" "+lastname);
					}
					employee.add(new Employee(firstname,lastname,department,email,salary));
				}		
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return employee;
	}
}
