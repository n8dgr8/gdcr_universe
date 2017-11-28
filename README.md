# gdcr_universe
This program was used at the 2016 & 2017 Des Moines [Global Day of Code Retreat](http://coderetreat.org/) event.


Attendees implemented logic behind a single cell in the language of their choice and utilized the REST API methods of this server to

* Register with the universe, receive their Battleship-style coordinate in return (A5, B7, etc.)
* GET cell state (DEAD / ALIVE / UNKNOWN) of their neighbors for any generation
* POST their cell state (DEAD / ALIVE) for any generation

## Objects
*Universe* - Contains generations of cells.  Has a width and height.

*Generation* - Contains cells and their state in this generation.

*Cell* - Can be dead, alive, or unknown in any generation.

## Pre-requisites
* gradle
* Redis
* Java 1.8+

## How To Run
### Be sure the redis-server is running.
For ubuntu this means either configuring redis to run at start of the system or run from the command line. See the [Redis Quick Start](https://redis.io/topics/quickstart) for details. The quicker quick start run from the command line:

```shell
sudo apt-get install -y redis-*
redis-server&
```

### Build the Universe jar
```shell
cd /path/to/cdcr_universe
./gradlew jar
```

### Run the jar
The path to the jar after the build command above will be: `/path/to/gdcr_universe/build/libs/`.

```
# java -jar /path/to/jar/universe-1.0.0.jar
```

## Viewing The Universe of Cells
http://{your_ip}:8080/universe/@currentUniverse/generation/0


## REST API

[View here!](https://gdcr-universe.herokuapp.com/swagger-ui.html)
### Creating A Universe

POST http://{your_ip}:8080/universe/

POST Body
```
{"width":"25","height":"25"}
```

### Registering With The Universe

GET http://{your_ip}:8080/universe/@currentUniverse/register

Response: Battleship-style coordinate

### GETting Cell State From A Generation

GET http://{your_ip}:8080/universe/@currentUniverse/generation/{generation_index}/cell/{coordinate}

Response: 
```
{
    "cellState": "DEAD"
}
```

### POSTing Cell State For A Generation

POST http://{{your_ip}}:8080/universe/@currentUniverse/generation/{generation_index}/cell/{coordinate}

Body: "ALIVE" | "DEAD"

TODO - Generate this from Swagger

## Redis Keys And What They Do

| key name | Type | What it does |
|:----------|:------|:--------------|
| current_universe | String | GUID pointing to the currently active universe |
|universes | List | Contains GUIDs of known universes |
|universe:{GUID} | Hash | Contains attributes of a universe |
| | | &nbsp;&nbsp;&nbsp;width |
| | | &nbsp;&nbsp;&nbsp;height |
| universe:{GUID}:generations | Hash | Contains hash of generations for a universe (Index, generation GUID) |
| generation:{GUID} | Hash | Hash of cells for each generation (CellID, CellState) |


## Badges!

[![Stories in Ready](https://badge.waffle.io/n8dgr8/gdcr_universe.png?label=ready&title=Ready)](https://waffle.io/n8dgr8/gdcr_universe)

[![Build Status](https://drone.io/github.com/n8dgr8/gdcr_universe/status.png)](https://drone.io/github.com/n8dgr8/gdcr_universe/latest)

[SonarQube Analysis](https://sonarcloud.io/dashboard?id=gdcr_universe)

