package numericalmethods;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Author: Ayush Pancholy
 * Date of creation: 8 May 2018
 * 
 * The Nbody class models the positions and velocities of a number of masses based on Newton's law of universal
 * gravitation. When the main method is run, the user will be prompted for a control file path. The control file
 * contains the output file path, input file path (or the user can specify that random masses should be generated),
 * the duration of the run in seconds, the time step in seconds, and the update step in seconds. The update step
 * specifies how often the time value is printed, how often graphics are updated, and how often positions and
 * velocities of each mass are written to the output file. The graphics display a real-time two-dimensional rendering
 * of the system while it is in motion.
 *
 */
public class Nbody
{
    //GRAVITATIONAL_CONSTANT is the constant used to find gravitational acceleration
    public static final double GRAVITATIONAL_CONSTANT = 6.674e-11;
    
    //RANDOM_CONSTANTS is an int value representing the number of bodies in the random N-body simulation
    public static final int RANDOM_OBJECTS = 100;
    
    //RANDOM_DIMENSION is a double value representing the side length of the cubical space in which random bodies
    //will be randomly placed in meters
    public static final double RANDOM_DIMENSION = 4e15;
    
    //MAX_MASS is a double value representing the maximum mass in kilograms of a random body in the random N-body
    //simulation
    public static final double MAX_MASS = 2e31;
    
    //MAX_VELOCITY is a double value representing the maximum velocity of each component of each random mass in
    //meters per second
    public static final double MAX_VELOCITY = 1e3;
    
    //SOLAR_MASS is a double value that represents the number of kilograms in a single solar mass
    public static final double SOLAR_MASS = 1.989e30;
    
    //PARSEC_CONVERSION is a double value that represents the number of meters in a parsec
    public static final double PARSEC_CONVERSION = 3.086e16;
    
    //KILOMETER_CONVERSION is a double value that represents the number of meters in a kilometer
    public static final double KILOMETER_CONVERSION = 1e3;
    
    
    //bodies is an ArrayList that holds Body objects, representing masses in the system and holding velocity, position,
    //and mass values
    public ArrayList<Body> bodies;
    
    //n is an int value representing the number of bodies in the system
    public int n;
    
    //duration is a double that represents the length of the run in seconds
    public double duration;
    
    //timeStep is a double that represents the amount by which the time should be incremented each iteration in seconds
    public double timeStep;
    
    //update is a double that represents the intervals at which the graphics should be updated, time should be printed,
    //and positions/velocities are written to the output file
    public double update;
    
    //data is an object that is an instance of a graphics class; its constructor accepts the number of bodies as an
    //argument
    public GraphingData data;
    
    //out is the BufferedWriter representing the writer used to output data
    public BufferedWriter out;
    
    //output represents the output file itself
    public File output;
    
    /**
     * The constructor creates a new Nbody object by prompting the user for a path to a control file. It then reads the
     * output file from the given control file and sets instance variables accordingly. The next item read from the
     * control file is the input file path. If the user specifies that the input file should be random (or if the user
     * inputs a file that does not exist), bodies of random mass, velocity, and position (within bounds specified by 
     * constants) are added to the ArrayList of bodies. Otherwise, initial masses, velocities, and positions are added
     * based on the input file. The instance variable n is updated to represent the number of bodies in the system.
     * The duration, timeStep, and update instance variables are also set based on the specifications of the control
     * file. Finally, the data instance variable, representing the graphics class, is initialized based on the number of
     * bodies in the system. If there is a file i/o error, the exception is caught and an appropriate error message is
     * printed.
     */
    public Nbody()
    {
        //random represents whether the user has specified that the bodies should have random position, velocity, and mass
        boolean random = false;
        
        //Prompts the user for the path to the control file and stores the file path as a string
        System.out.println("Enter the control file path.");
        Scanner fileNameInput = new Scanner(System.in);
        String filename = fileNameInput.next();
        fileNameInput.close();
        
        try
        {
            //Creates a Scanner object for the control file
            File contFile = new File(filename);
            Scanner controlFile = new Scanner(contFile);
            
            //Extracts output file path from control file and updates instance variables accordingly
            String outFile = controlFile.next();
            output = new File(outFile);
            out = new BufferedWriter(new FileWriter(output));            
            
            //Determines the file path to the input file (or specification for random objects) and updates the random
            //boolean appropriately
            String inFile = controlFile.next();
            if(inFile.toLowerCase().equals("random"))
            {
                random = true;
            }
            
            //If the specified file does not exist, random values will be used
            File pCode = new File(inFile);
            if(!random && !pCode.isFile())
            {
                System.out.println("Input file not found. Using random masses instead.");
                random = true;
            }
            
            //doubles representing components of position and velocity as well as mass are declared and will be used to
            //store temporary values for body creation
            double xPos, yPos, zPos;
            double xVel, yVel, zVel;
            double mass;
            
            bodies = new ArrayList<Body>();
            
            if(!random)
            {
                //Creates a scanner based on input file
                Scanner bodyAdder = new Scanner(pCode);
                
                while (bodyAdder.hasNextDouble())
                {
                    //Converts all position, velocity and mass values to mks units and adds new bodies with these values
                    //to the ArrayList
                    
                    xPos = PARSEC_CONVERSION * bodyAdder.nextDouble();
                    yPos = PARSEC_CONVERSION * bodyAdder.nextDouble();
                    zPos = PARSEC_CONVERSION * bodyAdder.nextDouble();
                    
                    xVel = KILOMETER_CONVERSION * bodyAdder.nextDouble();
                    yVel = KILOMETER_CONVERSION * bodyAdder.nextDouble();
                    zVel = KILOMETER_CONVERSION * bodyAdder.nextDouble();
                    
                    mass = SOLAR_MASS * bodyAdder.nextDouble();
                    
                    bodies.add(new Body(xPos, yPos, zPos, xVel, yVel, zVel, mass));
                }
                
                bodyAdder.close();
            }
            else
            {
                for(int i = 0; i < RANDOM_OBJECTS; i++)
                {
                    //If using random values, random positions, masses, and velocities are generated and used for body 
                    //creation
                    
                    xPos = (Math.random()*RANDOM_DIMENSION)-(RANDOM_DIMENSION/2);
                    yPos = (Math.random()*RANDOM_DIMENSION)-(RANDOM_DIMENSION/2);
                    zPos = (Math.random()*RANDOM_DIMENSION)-(RANDOM_DIMENSION/2);
                    
                    xVel = (Math.random()*MAX_VELOCITY)-(MAX_VELOCITY/2);
                    yVel = (Math.random()*MAX_VELOCITY)-(MAX_VELOCITY/2);
                    zVel = (Math.random()*MAX_VELOCITY)-(MAX_VELOCITY/2);
                    
                    mass = Math.random()*MAX_MASS;
                    
                    bodies.add(new Body(xPos, yPos, zPos, xVel, yVel, zVel, mass));
                }
            }
            
            //Running conditions are extracted from the control file, and instance variables are updated accordingly
            duration = controlFile.nextDouble();
            timeStep = controlFile.nextDouble();
            update = controlFile.nextDouble();
            
            controlFile.close();
            
            n = bodies.size();
            
            //A new graphics object is created based on the number of objects in the system
            data = new GraphingData(n);
        }
        catch (IOException | InputMismatchException e)
        {
            System.out.println("File i/o error. Please check paths/format and try again.");
        }
    }
    
    /**
     * The main method creates a new Nbody object and runs the method run(), which executes its simulation. Any
     * IOExceptions are handled and an appropriate message is printed.
     */
    public static void main(String[] args)
    {       
        Nbody nbody = new Nbody();
        try
        {
            nbody.run();
        }
        catch(IOException e)
        {
            System.out.println("There was an error accessing or writing to the output file. Please check the filename and try again.");
        }
    }
    
    /**
     * The method run() executes the simulation using a nested for loop, summing the gravitational accelerations of each
     * mass due to all the other masses, and then using difference equations to change velocities. After looping through
     * all the masses, the positions of all masses are updated based on the difference equation and current velocity. 
     * After that, potential collisions are resolved using the conservation of linear momentum. During each update interval,
     * the current time is printed; the position, velocity, and mass values are written to the output file; and the graphics
     * are updated.
     * 
     * @throws IOException: The method can throw an IOException if there are issues accessing or writing to the output file.
     */
    public void run() throws IOException
    {
        //currentBody represents the body to which gravitational acceleration is being applied by every other body (i.e.
        //the mass of the outer loop), and otherBody represents the the mass in the inner loop
        Body currentBody;
        Body otherBody;
        
        /**
         * distance is the distance in meters between currentBody and otherBody. otherMass is the mass of otherBody.
         * factor holds the value of GM/(r^3), the value by which each component of position will be multiplied to
         * obtain acceleration. relativePosition acts as a vector that holds the position of the current object with
         * respect to the other object. currentPosition and currentVelocity are arrays that act as vectors for the
         * position and velocity of currentBody, respectively. otherPosition holds the position vector of otherBody.
         */
        double distance;
        double otherMass;
        double factor;
        double[] relativePosition;
        double[] currentPosition;
        double[] currentVelocity;
        double[] otherPosition;
        
        //velocityAdjustment and positionAdjustment are vectors that hold values by which the velocity and position
        //of currentBody should be changed.
        double[] velocityAdjustment = new double[3];
        double[] positionAdjustment = new double[3];
        
        //samePosition is a boolean that indicates whether the two bodies occupy the same position in the rare event
        //of a collision
        boolean samePosition = true;
        
        //position, velocity, and mass are Strings that hold the components of position and velocity vectors of each object
        //as well as mass. These will be written to the output file.
        String position;
        String velocity;
        String mass;
        
        double time = 0;
        
        while(time < duration)
        {
            //Each time the update interval is met, the time is printed and the BufferedWriter is reinitialized in order
            //to clear the output file
            if(time % update == 0)
            {
                System.out.println("Elapsed Time: " + time + " seconds");
                out = new BufferedWriter(new FileWriter(output));
            }
            
            //First loop goes through each body in the List and calculates velocity adjustment by adding the
            //accelerations caused by gravitational force of each other mass
            for(int i = 0; i < n; i++)
            {
                currentBody = bodies.get(i);
                
                //Creates position, velocity, and mass Strings to write to the output file after converting to parsecs,
                //km/s, and solar masses.
                position = currentBody.getPosition()[0]/PARSEC_CONVERSION + " " 
                        + currentBody.getPosition()[1]/PARSEC_CONVERSION + " " 
                        + currentBody.getPosition()[2]/PARSEC_CONVERSION;
                velocity = currentBody.getVelocity()[0]/KILOMETER_CONVERSION + " " 
                        + currentBody.getVelocity()[1]/KILOMETER_CONVERSION + " " 
                        + currentBody.getVelocity()[2]/KILOMETER_CONVERSION;
                mass = Double.toString(currentBody.getMass()/SOLAR_MASS);
                
                //Each time the update interval is met, the position, velocity, and mass of each object is written to
                //the output file
                if (time % update == 0)
                {
                    out.write(position + "\t" + velocity + "\t" + mass + "\r\n");
                    out.flush();
                }
                
                //Inner loop goes through every body that is not currentBody and sums accelerations due to gravity to find
                //the velocity adjustment for currentBody
                for(int j = 0; j < n; j++)
                {
                    if(i != j)
                    {
                        //Initializes variables to appropriate values of otherBody
                        otherBody = bodies.get(j);
                        otherMass = otherBody.getMass();
                        distance = currentBody.distanceTo(otherBody);
                        relativePosition = otherBody.relativePositionFrom(currentBody);
                        
                        factor = (otherMass*GRAVITATIONAL_CONSTANT*timeStep)/(distance*distance*distance);
                        
                        //The velocity adjustment is incremented by the product of the (MG)/r^3 and the relativePosition
                        for(int k = 0; k < 3; k++)
                        {
                            velocityAdjustment[k] += relativePosition[k]*factor;
                        }
                    }
                }
                
                //The velocity of the currentBody is modified based on the summation of velocity adjustments
                currentBody.addVelocityAdjustment(velocityAdjustment);
                
                //The velocity adjustment is cleared
                for(int p = 0; p < 3; p++)
                {
                    velocityAdjustment[p] = 0;
                }
            }
            
            //After looping through all the masses, the position of each mass is updated according to its velocity and
            //the difference equation
            for(int l = 0; l < n; l++)
            {
                currentBody = bodies.get(l);
                currentPosition = currentBody.getPosition();
                currentVelocity = currentBody.getVelocity();
                
                for(int m = 0; m < 3; m++)
                {
                    positionAdjustment[m] = currentVelocity[m]*timeStep;
                }
                
                currentBody.addPositionAdjustment(positionAdjustment);
            }
            
            //Loops through the masses to check for collisions
            for(int m = 0; m < n; m++)
            {
                currentBody = bodies.get(m);
                currentPosition = currentBody.getPosition();
                
                //Updates the graphics on appropriate interval
                if(time % update == 0)
                {
                    data.addPoint(currentPosition);
                }
                
                //Loops through every other body to determine if any other body occupies the same position
                for(int p = 0; p < n; p++)
                {
                    if(m != p)
                    {
                        otherBody = bodies.get(p);
                        otherPosition = otherBody.getPosition();
                        for(int q = 0; q < 3; q++)
                        {
                            if(currentPosition[q] != otherPosition[q])
                            {
                                samePosition = false;
                            }
                        }
                        if (samePosition)
                        {
                            //Applies conservation of linear momentum in the case of a collision
                           currentBody.conserveMomentum(otherBody);
                           samePosition = true;
                        }
                    }
                }
            }
            
            //Completes graphics update on appropriate interval
            if (time % update == 0)
            {
                data.update();
            }
            
            time += timeStep;
        }
    }
}
