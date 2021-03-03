package com.example.loadin_app.ui.opengl;

import android.icu.text.MessagePattern;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.storage.StorageManager;


public class Camera {
    private Vector up;  //what is the direction that we consider up
    private Vector location;
    private Vector front; //front of the camera
    private Vector worldUp;
    private Vector right;

    public enum Direction{
        Forward,
        Backward,
        Left,
        Right
    }


    float yaw;
    float pitch;

    public Camera(){
        worldUp = new Vector(0, 1f, 0f);
        location = new Vector(0,0,0);
        front = new Vector(0, 0, -1f);
        up = new Vector(0f, 1f, 0f); //Y IS UP!!!!!  NOT Z!
        yaw = -90f;  //this represents the rotation about the y axis.  in other words, if i turn left or right kind of thing
        pitch = 0f;  //pitch is the degree that i am looking up or down
        updateCameraVectors();
    }


    public void lookAt(Vector pointOfInterest){
        Vector toLookAt = location.add(pointOfInterest.multiply(-1f)).multiply(-1f);  //this gets us the vector to be able to change our camera view
        //toLookAt is a vector from the center of our camera to the point of interest

        //we need to figure out pitch and yaw
        yaw = calculateNewYaw(toLookAt);
        pitch = calculateNewPitch(toLookAt);


        updateCameraVectors(); //apply the changes to the camera vectors from pitch and yaw
    }

    private float calculateNewYaw(Vector toLookAt){
        //we're going to get a new yaw from this vector

        //yaw is between the x and z axis


        float length = toLookAt.getLength(); //always positive
        float a = Math.abs(toLookAt.getX());  //this is just left to right

        float x = toLookAt.getX();
        float z = toLookAt.getZ();


        if(length == 0f)
            return 0f;

        //let's get the arccos as it stands without adjustment

        double cosInRadians = Math.acos(a/length);
        double cosAsAngle = Math.toDegrees(cosInRadians);
        //if x and z are positive, make no adjustments

        if(x < 0 && z > 0)
            cosAsAngle = 180d - cosAsAngle; //second quadrant
        else if(x < 0 && z < 0)
            cosAsAngle += 180d; //third quadrant
        else if(x > 0 && z < 0)
            cosAsAngle = 360d - cosAsAngle;  //forth quadrant

        return  (float)cosAsAngle;

    }
    private float calculateNewPitch(Vector toLookAt){
        //pitch is between the x and y axis
        //z axis is on the same plane as x

        //pitch is the angle we are trying to figure out

        float length = toLookAt.getLength();
        float b = toLookAt.getY();  //y indicates up or down from the current location

        if(length == 0f)
            return 0f;

        return  (float)Math.toDegrees(Math.asin(b/length));



    }

    public void focusOn(Vector toFocusOn, float distanceInInches ){
        //we're going to get close to the point and then look at it

        Vector gotoPlace = toFocusOn.add(new Vector(-distanceInInches, distanceInInches, -distanceInInches));  //two feet above, two feet back

        placeCamera(gotoPlace);  //float above the location
        lookAt(toFocusOn); //look at that item


    }


    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        updateCameraVectors();
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        updateCameraVectors();
    }

    public Vector getLocation() {
        return location;
    }


    public void move( Direction dir ){
        float velocity = 0.25f * 12f;
        switch(dir){
            case Left:
                location = location.add(right.multiply(-velocity));
                break;
            case Right:
                location = location.add(right.multiply(velocity));
                break;
            case Backward:
                location = location.add(front.multiply(-velocity));
                break;
            case Forward:
                location = location.add(front.multiply(velocity));
                break;
        }

    }




    private void updateCameraVectors(){
        if(pitch > 89.0f){  //enforce constraints with pitch so we don't get weird effects
            pitch = 89.0f;
        }
        else if(pitch < -89.0f){
            pitch = -89.0f;
        }

        yaw = yaw % 360f;

        double yawInRadians = Math.toRadians(yaw);
        double pitchInRadians = Math.toRadians(pitch);

        front = new Vector(
                (float)(Math.cos(yawInRadians) * Math.cos( pitchInRadians)),
                (float)Math.sin(pitchInRadians),
                (float)( Math.sin(yawInRadians) * Math.cos(pitchInRadians))
        ).normalize();
        right = front.crossProduct(worldUp).normalize();
        up = right.crossProduct(front).normalize();
    }

    public float[]  getLookatMatrix(){


        float[] result = new float[16];
        Vector direction = location.add(front);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(result, 0,
                location.getX() * World.INCHES_TO_WORLD_SCALE,
                location.getY() * World.INCHES_TO_WORLD_SCALE,
                location.getZ() * World.INCHES_TO_WORLD_SCALE,
                direction.getX() * World.INCHES_TO_WORLD_SCALE,
                direction.getY() * World.INCHES_TO_WORLD_SCALE,
                direction.getZ() * World.INCHES_TO_WORLD_SCALE,
                up.getX(),
                up.getY(),
                up.getZ());

        return result;
    }

    public void placeCamera(Vector location){
        this.location = location;
    }





}
