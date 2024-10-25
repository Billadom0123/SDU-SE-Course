public class TwoTwo {
	public static void main(String[] args) {
		Person[] personlist=new Person[2];
		personlist[0]=new Person("LiPing");
		personlist[1]=new Student("SunMing",2008001);
		for(int i=0;i<2;i++)
			personlist[i].writeOutput();
	}

}
class Person
{
	private String name; //人的姓名
	public Person(String initialName) {
		name = initialName;
		System.out.println("In Constructor Person");
	}
	public void outputClassName() {
		System.out.println("Person");
	}
	public void writeOutput() {
		outputClassName();
		System.out.println("Name: " + name);
	}
}
class Student extends Person
{
	private int studentNumber;//学生学号
	public Student(String initialName, int initialStudentNumber) {
		super(initialName);
		studentNumber = initialStudentNumber;
		System.out.println("In Constructor Student");
	}
	public void writeOutput()
	{
		super.writeOutput();
		System.out.println("Student Number: " + studentNumber);
	}
	public void outputClassName() {
		System.out.println("Student");
	}
}