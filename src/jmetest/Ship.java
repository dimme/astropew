package jmetest;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Ship extends Node {
	
	
	private static final Vector3f tempVa = new Vector3f();
	
	private Spatial model;
	private float velocity;
    private float acceleration;
    private float braking;
    private float turnSpeed;
    private float maxSpeed; 
    private float minSpeed;
    
 	public Ship(String id, Spatial model) {
        super(id);
        setModel(model);
    }
    
    public Ship(String id, Spatial model, float maxSpeed, float minSpeed, 
    		float acceleration, float braking, float turnSpeed) {
        super(id);
        setModel(model);
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.acceleration = acceleration;
        this.braking = braking;
        this.turnSpeed = turnSpeed;
    }
 
    /**
     * retrieves the acceleration of this vehicle.
     * @return the acceleration of this vehicle.
     */
    public float getAcceleration() {
        return acceleration;
    }
 
    /**
     * set the acceleration rate of this vehicle
     * @param acceleration the acceleration rate of this vehicle
     */
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
 
    /**
     * retrieves the braking speed of this vehicle.
     * @return the braking speed of this vehicle.
     */
    public float getBraking() {
        return braking;
    }
 
    /**
     * set the braking speed of this vehicle
     * @param braking the braking speed of this vehicle
     */
    public void setBraking(float braking) {
        this.braking = braking;
    }
 
    /**
     * retrieves the model Spatial of this vehicle.
     * @return the model Spatial of this vehicle.
     */
    public Spatial getModel() {
        return model;
    }
 
    /**
     * sets the model spatial of this vehicle. It first
     * detaches any previously attached models.
     * @param model the model to attach to this vehicle.
     */
    public void setModel(Spatial model) {
        this.detachChild(this.model);
        this.model = model;
        this.attachChild(this.model);
    }
 
    /**
     * retrieves the velocity of this vehicle.
     * @return the velocity of this vehicle.
     */
    public float getVelocity() {
        return velocity;
    }
 
    /**
     * set the velocity of this vehicle
     * @param velocity the velocity of this vehicle
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
    
    /**
     * retrieves the turn speed of this vehicle.
     * @return the turn speed of this vehicle.
     */
    public float getTurnSpeed() {
        return turnSpeed;
    }
 
    /**
     * set the turn speed of this vehicle
     * @param turnSpeed the turn speed of this vehicle
     */
    public void setTurnSpeed(float turnSpeed) {
        this.turnSpeed = turnSpeed;
    }
    
    /**
     * retrieves the maximum speed of this vehicle.
     * @return the maximum speed of this vehicle.
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }
 
    /**
     * sets the maximum speed of this vehicle.
     * @param maxSpeed the maximum speed of this vehicle.
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
 
    /**
     * retrieves the minimum speed of this vehicle.
     * @return the minimum speed of this vehicle.
     */
    public float getMinSpeed() {
        return minSpeed;
    }
 
    /**
     * sets the minimum speed of this vehicle.
     * @param minSpeed the minimum speed of this vehicle.
     */
    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
    }
    
    /**
     * brake adjusts the velocity of the vehicle based on the braking speed. If the
     * velocity reaches 0, braking will put the vehicle in reverse up to the minimum 
     * speed.
     * @param time the time between frames.
     */
    public void brake(float time) {
        velocity -= time * braking;
        if(velocity < -minSpeed) {
            velocity = -minSpeed;
        }
    }
    
    /**
     * accelerate adjusts the velocity of the vehicle based on the acceleration. The velocity
     * will continue to raise until maxSpeed is reached, at which point it will stop.
     * @param time the time between frames.
     */
    public void accelerate(float time) {
        velocity += time * acceleration;
        if(velocity > maxSpeed) {
            velocity = maxSpeed;
        }
    }
    
    public void update(float time) {
        this.localTranslation.addLocal(this.localRotation.getRotationColumn(2, tempVa)
                .multLocal(velocity * time));
    }
}
