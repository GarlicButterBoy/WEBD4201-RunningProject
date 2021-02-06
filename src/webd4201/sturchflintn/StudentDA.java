/**
 * DatabaseConnect - Used to manage database connectivity
 * @author Nick Sturch-Flint
 * @version 1.1.0 (Feb 6, 2021)
 * @since 1.1.0
 */
package webd4201.sturchflintn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class StudentDA
{
    static Vector<Student> students = new Vector<Student>();	// contains student references
    static Student aStudent;

    // declare variables for the database connection
    static Connection aConnection;
    static Statement aStatement;

    // declare static variables for all student instance attribute values
    /**
     * Private data member to hold id
     */
    private static long id;
    /**
     *Private data member to hold password
     */
    private static String password;
    /**
     *Private data member to hold first name
     */
    private static String firstName;
    /**
     *Private data member to hold last name
     */
    private static String lastName;
    /**
     *Private data member to hold email address
     */
    private static String emailAddress;
    /**
     *Private data member to hold the last access
     */
    private static Date lastAccess;
    /**
     *Private data member to holds the enrol date
     */
    private static Date enrolDate;
    /**
     *Private data member to hold the enabled status
     */
    private static boolean enabled;
    /**
     *Private data member to hold user type
     */
    private static char type;
    /**
     * Private Data variable to store a Program Code
     */
    private static String programCode;
    /**
     * Private Data variable to store a Program Description
     */
    private static String programDescription;
    /**
     * Private Data variable to store the students current year
     */
    private static int year;
    /**
     * Private Vector to store the marks the student has in each class
     */
    private static Vector<Mark> marks;

    //Class Constant
    private static final SimpleDateFormat SQL_DF = new SimpleDateFormat("yyyy-MM-dd");



    // establish the database connection
    /**
     * This method will create a statement to be used in DB connection
     * @param c   A string that holds the DB connection
     */
    public static void initialize(Connection c)
    {
        try {
            aConnection=c;
            aStatement=aConnection.createStatement();
        }
        catch (SQLException e)
        { System.out.println(e);	}
    }

    // close the database connection
    /**
     * This method will terminate (close) a statement that is used in DB connection
     */
    public static void terminate()
    {
        try
        { 	// close the statement
            aStatement.close();
        }
        catch (SQLException e)
        { System.out.println(e);	}
    }

    /**
     * This method checks the id against all in the DB
     * @param m_id      Checks the id of the potential new user against all existing users
     * @return Student  Object form of the data collected for a student
     * @throws NotFoundException
     */
    public static Student retrieve(long m_id) throws NotFoundException
    { // retrieve Customer and Boat data
        aStudent = null;
        // define the SQL query statement using the phone number key
        String sqlQuery = "SELECT id " +
                " FROM users " +
                " WHERE id = '" + m_id +"'" ;

        // execute the SQL query statement
        try
        {
            ResultSet rs = aStatement.executeQuery(sqlQuery);
            // next method sets cursor & returns true if there is data
            boolean gotIt = rs.next();
            if (gotIt)
            {	// extract the data
                m_id = rs.getLong("id");

                // create Student
                try
                {
                    aStudent = new Student(m_id, password, firstName, lastName, emailAddress, lastAccess, enrolDate, enabled, type, programCode, programDescription, year, marks);
                }
                catch (InvalidUserDataException e)
                {
                    System.out.println("Record for " + m_id + " contains an invalid phone number.  Verify and correct.");
                }

            }
            else	// nothing was retrieved
            {
                throw (new NotFoundException("Problem retrieving Student record, ID " + m_id +" does not exist in the system."));
            }
            rs.close();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return aStudent;
    }

    /**
     * Method retrieve all of the records in the students table
     * @return students     A vector containing all of the rows
     */
    public static Vector<Student> retrieveAll()
    {
        // retrieve Customers and their boats
        // define the SQL query statement for get all
        String sqlQuery = "SELECT * FROM students";
        try
        {   // execute the SQL query statement
            ResultSet rs = aStatement.executeQuery(sqlQuery);
            boolean moreData = rs.next();

            while (moreData)
            {	// extract the data
                id = rs.getLong(1);
                password = rs.getString(2);
                firstName = rs.getString(3);
                lastName = rs.getString(4);
                emailAddress = rs.getString(5);
                lastAccess = rs.getDate(6);
                enrolDate = rs.getDate(7);
                enabled = rs.getBoolean(8);
                type = rs.getString(9).charAt(0);
                programCode = rs.getString(10);
                programDescription = rs.getString(11);
                year = rs.getInt(12);

                // try tp create Customer instance
                try{
                    aStudent = new Student(id, password, firstName, lastName, emailAddress, lastAccess, enrolDate, enabled, type, programCode, programDescription, year, marks);
                }catch (InvalidUserDataException e)
                { System.out.println("Record for " + id + " contains an invalid id.  Verify and correct.");}

                students.addElement(aStudent);
                moreData = rs.next();
            }
            rs.close();
        }
        catch (SQLException e)
        { System.out.println(e);}
        return students;
    }

    /**
     * Method accepts a student object to be used to INSERT into the DB
     * @param aStudent      an object that contains student details
     * @return inserted     a boolean to tell if the insert passed/failed
     * @throws DuplicateException
     */
    public static boolean create(Student aStudent) throws DuplicateException
    {
        boolean inserted = false; //insertion success flag
        // retrieve the student attribute values
        id = aStudent.getId();
        password = aStudent.getPassword();
        firstName = aStudent.getFirstName();
        lastName = aStudent.getLastName();
        emailAddress = aStudent.getEmailAddress();
        lastAccess = aStudent.getLastAccess();
        enrolDate = aStudent.getEnrolDate();
        enabled = aStudent.isEnabled();
        type = aStudent.getType();
        programCode = aStudent.getProgramCode();
        programDescription = aStudent.getProgramDescription();
        year = aStudent.getYear();
        marks = aStudent.getMarks();

        // create the SQL insert statement using attribute values
        String sqlInsertUser = "INSERT INTO users (id, password, first_name, last_name, email_address, last_access, enrol_date, enabled, type) " +
                "VALUES (" + id + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + emailAddress + "', '" + lastAccess
                + "', '" + enrolDate + "', '" + enabled + "', '" + type
                + "');";

        String sqlInsertStudent = "INSERT INTO Students (id, program_code, program_description, year) " +
                "VALUE (" + id + "', '" + programCode + "', '" + programDescription + "', '" + year +"');'";

        // see if this customer already exists in the database
        try
        {
            retrieve(id);
            throw (new DuplicateException("Problem with creating Student record, the Student ID: " + id +"; already exists in the system."));
        }
        // if NotFoundException, add customer to database
        catch(DuplicateException | NotFoundException e)
        {
            try
            {  // execute the SQL update statement
                inserted = aStatement.execute(sqlInsertUser);
                inserted = aStatement.execute(sqlInsertStudent);
            }
            catch (SQLException ee)
            { System.out.println(ee);	}
        }
        return inserted;
    }

    /**
     * Method that deletes a user and student from the DB
     * @param aStudent      object containing data
     * @return records      number of records changed
     * @throws NotFoundException
     */
    public static int delete(Student aStudent) throws NotFoundException
    {
        int records = 0;
        // retrieve the id (key)
        id = aStudent.getId();
        // create the SQL delete statement
        String sqlDeleteStudent = "DELETE FROM students " + "WHERE id = '" + id +"'";
        String sqlDeleteUser = "DELETE FROM users " + "WHERE id = '" + id +"'";

        // see if this customer already exists in the database
        try
        {
            retrieve(id);  //used to determine if record exists for the passed Customer
            // if found, execute the SQL update statement
            records = aStatement.executeUpdate(sqlDeleteStudent);
            records = aStatement.executeUpdate(sqlDeleteUser);
        }catch(NotFoundException e)
        {
            throw new NotFoundException("Student with id " + id
                    + " cannot be deleted, does not exist.");
        }catch (SQLException e)
        { System.out.println(e);	}
        return records;
    }

    /**
     * Method updates a user in both the users and students table
     * @param aStudent      object that contains data
     * @return records      number of records updated
     * @throws NotFoundException
     */
    public static int update(Student aStudent) throws NotFoundException
    {
        int records = 0;  //records updated in method

        // retrieve the student argument attribute values
        id = aStudent.getId();
        password = aStudent.getPassword();
        firstName = aStudent.getFirstName();
        lastName = aStudent.getLastName();
        emailAddress = aStudent.getEmailAddress();
        lastAccess = aStudent.getLastAccess();
        enrolDate = aStudent.getEnrolDate();
        enabled = aStudent.isEnabled();
        type = aStudent.getType();
        programCode = aStudent.getProgramCode();
        programDescription = aStudent.getProgramDescription();
        year = aStudent.getYear();
        marks = aStudent.getMarks();

        // define the SQL query statement
        String sqlUpdateUsers = "UPDATE users " +
                "SET first_name = '" + firstName + "', " +
                "last_name = '" + lastName + "', " +
                "email_address = '" + emailAddress + "', " +
                "last_access = '" + lastAccess + "', " +
                "enrol_date = '" + enrolDate + "', " +
                "type = '" + type + "', " +
                "enabled = '" + enabled + "' " +
                "WHERE id = '" + id + "';";

        String sqlUpdateStudents = "UPDATE students " +
                "SET program_code = '" + programCode + "', " +
                "program_description = '" + programDescription + "', " +
                "year = '" + year + "', " +
                "WHERE id = '" + id + "';";


        // see if this customer exists in the database
        // NotFoundException is thrown by find method
        try
        {
            retrieve(id);  //determine if there is a student record to be updated
            // if found, execute the SQL update statement
            records = aStatement.executeUpdate(sqlUpdateUsers);
            records = aStatement.executeUpdate(sqlUpdateStudents);
        }catch(NotFoundException e)
        {
            throw new NotFoundException("Student with id " + id
                    + " cannot be updated, does not exist in the system.");
        }catch (SQLException e)
        { System.out.println(e);}
        return records;
    }
}
