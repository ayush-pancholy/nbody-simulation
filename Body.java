package numericalmethods;

/**
 * Author: Ayush Pancholy
 * Date of creation: 8 May 2018
 * 
 * The Body class represents a mass with some velocity and position vector. It is meant to be used with the Nbody class.
 * It contains methods to get and set velocity, position, and mass values. It can additionally find a relative position
 * vector from another body, as well as a scalar distance value. Finally, it can conserve linear momentum during a collision
 * with another mass.
 *
 */
public class Body
{
    //position and velocity will represent three-dimensional vectors for the body in meters and meters/second, respectively.
    //mass has the unit kilogram.
    public double[] position;
    public double[] velocity;
    public double mass;
    
    /**
     * The constructor creates a new Body object based on a given mass value as well as components of velocity and position.
     * Instance variables are set appropriately.
     */
    public Body(double xPos, double yPos, double zPos, double xVel, double yVel, double zVel, double m)
    {
        position = new double[3];
        velocity = new double[3];
        
        position[0] = xPos;
        position[1] = yPos;
        position[2] = zPos;
        
        velocity[0] = xVel;
        velocity[1] = yVel;
        velocity[2] = zVel;
        
        mass = m;
    }
    
    //Returns the position vector of the body
    public double[] getPosition()
    {
        return position;
    }
    
    //Returns the velocity vector of the body
    public double[] getVelocity()
    {
        return velocity;
    }
    
    //Returns the mass of the body
    public double getMass()
    {
        return mass;
    }
    
    /**
     * Adjusts the mass of the object with the specified new mass.
     * 
     * @param newMass: the value to which the mass should be updated
     */
    public void setMass(double newMass)
    {
        mass = newMass;
    }
    
    /**
     * In the event of a collision, this method can be used to conserve momentum assuming an inelastic collision. The
     * method turns the current Body into the result of the collision and sets the other Body to have a mass of 0.
     * 
     * @param otherBody: the Body into which the current Body collides
     */
    public void conserveMomentum(Body otherBody)
    {
        double firstMass = mass;
        double secondMass = otherBody.getMass();
        double[] firstVelocity = velocity;
        double[] secondVelocity = otherBody.getVelocity();
        
        //Sets mass of current Body to that of the new Body after the inelastic collision
        mass += secondMass;
        
        //Velocity adjusted according to conservation of momentum
        for(int i = 0; i < 3; i++)
        {
            velocity[i] = (firstMass*firstVelocity[i] + secondMass*secondVelocity[i])/(firstMass + secondMass);
        }
        
        //Mass of otherBody set to 0
        otherBody.setMass(0);
    }
    
    /**
     * Adds a position adjustment vector to the current position vector.
     * 
     * @param adjustment: The adjustment vector to be added to the current position.
     */
    public void addPositionAdjustment(double[] adjustment)
    {
        for(int i = 0; i < 3; i++)
        {
            position[i] += adjustment[i];
        }
    }
    
    /**
     * Adds a velocity adjustment vector to the current velocity vector.
     * 
     * @param adjustment: The adjustment vector to be added to the current velocity.
     */
    public void addVelocityAdjustment(double[] adjustment)
    {
        for(int i = 0; i < 3; i++)
        {
            velocity[i] += adjustment[i];
        }
    }
    
    /**
     * Calculates the distance between the current Body and another Body.
     * 
     * @param body2: the Body to which the distance should be calculated
     * @return: a double value representing the distance in meters
     */
    public double distanceTo(Body body2)
    {
        double total = 0;
        double[] position2 = body2.getPosition();
        
        //Uses the Pythagorean distance formula to find the distance between the two bodies
        for (int i = 0; i < 3; i++)
        {
            total += (position[i]-position2[i])*(position[i]-position2[i]);
        }
        
        total = Math.sqrt(total);
        return total;
    }
    
    /**
     * Finds the relative position of the current Body from another Body.
     * 
     * @param body2: the Body from which the position should be calculated
     * @return: an array of doubles representing the position vector
     */
    public double[] relativePositionFrom(Body body2)
    {
        double[] position2 = body2.getPosition();
        double[] relativePosition = new double[3];
        
        //The other position vector is subtracted from the current position vector
        for (int i = 0; i < 3; i++)
        {
            relativePosition[i] = position[i] - position2[i];
        }
        return relativePosition;
    }
}
