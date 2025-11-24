# Smart Lighting System

## Problem Statement
The smart lighting system automatically controls room lighting based on occupancy detection. It uses a motion sensor to detect presence and adjusts the lights accordingly, ensuring energy efficiency and user convenience.

The system turns on the lights when a person is detected in the room and turns them off when the room is empty. The controller can also periodically request the sensor status to verify the current state of the room.

## Actors
* **Sensor (S)** - detects presence
* **Controller (C)** - controls the system

## Signals (minimum 2 in each direction)

### S → C:
1. **personDetected** - sensor detects a person
2. **roomEmpty** - sensor detects that the room is empty

### C → S:
1. **requestStatus** - controller requests the sensor state
2. **acknowledgeSensor** - controller confirms receipt of data

## Scenario
1. Both actors start in idle state
2. Sensor detects a person and sends personDetected to Controller
3. Controller receives the signal, turns the light ON and sends acknowledgeSensor
4. After some time, Sensor detects that the room is empty and sends roomEmpty
5. Controller receives the signal and turns the light OFF
6. Controller sends requestStatus to verify the sensor state
7. The cycle repeats
