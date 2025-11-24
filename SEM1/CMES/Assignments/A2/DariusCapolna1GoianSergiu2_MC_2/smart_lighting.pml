/* 
 * Smart Lighting System - Model Checking
 * Actors: Sensor, Controller
 */

mtype = {personDetected, roomEmpty, acknowledgeSensor, requestStatus};

chan sensor_to_controller = [0] of {mtype}; 
chan controller_to_sensor = [0] of {mtype}; 

bool lightIsOn = false;
bool personPresent = false;
bool waitingAck = false;

active proctype Sensor() {
    do
    :: atomic {
        !personPresent -> 
        personPresent = true;
        printf("Sensor: Person detected\n");
        sensor_to_controller!personDetected;
        controller_to_sensor?acknowledgeSensor;
        printf("Sensor: Received acknowledgment\n");
       }
       
    :: atomic {
        personPresent ->  
        printf("Sensor: Room empty\n");
        sensor_to_controller!roomEmpty;
        personPresent = false;
       }
       
    :: atomic {
        controller_to_sensor?requestStatus;
        printf("Sensor: Status requested\n");
        if
        :: personPresent -> sensor_to_controller!personDetected;
        :: !personPresent -> sensor_to_controller!roomEmpty;
        fi
       }
    od
}

active proctype Controller() {
    mtype signal;
    
    do
    :: atomic {
        sensor_to_controller?signal;
        if
        :: signal == personDetected ->
            printf("Controller: Turning light ON\n");
            lightIsOn = true;
            controller_to_sensor!acknowledgeSensor;
            
        :: signal == roomEmpty ->
            printf("Controller: Turning light OFF\n");
            lightIsOn = false;
        fi
       }
       
    :: atomic {
        lightIsOn -> 
        controller_to_sensor!requestStatus;
        sensor_to_controller?signal;
        printf("Controller: Received status update\n");
       }
    od
}

/*ltl ltl1 { [] (personPresent -> <> lightIsOn) } */
/* ltl ltl2 { [] (!personPresent -> <> !lightIsOn) } */
ltl ltl3 { [] ((personPresent && lightIsOn) || (!personPresent && !lightIsOn) || <> ((personPresent && lightIsOn) || (!personPresent && !lightIsOn))) }