
# Group 17 - Software Engeneering Project

GitHub Repository for the Source Code and Project Deliverables for group number 17.
## Table of Contents
- [Group 17 - Software Engeneering Project](#group-17---software-engeneering-project)
  * [Features](#features)
    + [Additional features](#additional-features)
  * [Getting Started](#getting-started)
    + [Prerequisites](#prerequisites)
  * [Launching the Application](#launching-the-application---windows--linux)
    + [Launching the Application (easy) - Windows & Linux](#launching-the-application---windows--linux)
    + [Launching the Application (expert) - Linux](#launching-the-application---linux)
    + [Launching the Application (expert) - Windows](#launching-the-application---windows)
  * [UML Class Diagrams](#uml-class-diagrams)


## Features

We developed all the base features, including:
- Complete Game Rules
- CLI
- GUI
- Socket
- RMI

### Additional features

We have chosen to implement a Server that can run Multiple Matches at the same time.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Java FX SDK 11 dependency for your Operating System (for Windows and Linux user you can use the one from deliverables/app folder)
- The complete deliverables/app folder

```
git clone git@github.com:CosimoRusso/ing-sw-2019-17.git
```
## Launching the Application - Windows & Linux
### Plug and Play configuration - Server
Browse to `deliverables/app`  and click on `StartServer.sh` file if you are using Linux or on  `StartServer.bat` for Windows users.

### Plug and Play configuration on localhost - Client
Browse to `deliverables/app`  and click on `StartClientGUI.sh` or `StartClientCLI.sh` file if you are using Linux or on  `StartClientGUI.bat` or `StartClientCLI.bat` for Windows users.
### Plug and Play configuration for remote game - Client
Browse to `deliverables/app`  and edit the shell (Linux users) or BAT file (Windows users) adding the  `--hostname <server-ip>` parameter. Save and exit.
Then click on `StartClientGUI.sh` or `StartClientCLI.sh` file if you are using Linux or on  `StartClientGUI.bat` or `StartClientCLI.bat` for Windows users.


## Launching the Application - Linux
For Windows and Linux user you can follow the easier plug-and-play guide. 
### Launch Server - Linux
The production server configuration does not use any options.

```
cd deliverables/app
java -jar ing-sw-2019-17-server.jar
```

This commands start the Adrenaline Server on localhost.

### Launch Client with localhost connection - Linux

If you are trying to launch the application with a different JavaFX dependency name, please change the `--module-path` parameter accordingly.

If you would like to launch Adrenaline Clients **on the same machine** where the Adrenaline Server is, there's no need to use any option.
#### For localhost GUI:
```
cd deliverables/app
java --module-path ./javafx-sdk-11.0.2-linux/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar --gui
```
#### For localhost CLI:
```
cd deliverables/app
java --module-path ./javafx-sdk-11.0.2-linux/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar
```  

### Launch Client to connect Remotely - Linux

If you are trying to launch the application with a different JavaFX dependency name, please change the `--module-path` parameter accordingly.

If you would like to launch Adrenaline Clients **on a different machine** where the Adrenaline Server is, you shoud use the `--hostname <server-ip>` parameter
#### For localhost GUI:
```
cd deliverables/app
java --module-path ./javafx-sdk-11.0.2-linux/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar --gui --hostname <server-ip>
```
#### For localhost CLI:
```
cd deliverables/app
java --module-path ./javafx-sdk-11.0.2-linux/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar --hostname <server-ip>
```  
## Launching the Application - Windows
For Windows and Linux user you can follow the easier plug-and-play guide. 
### Launch Server - Windows
The production server configuration does not use any options.

```
cd deliverables/app
java -jar ing-sw-2019-17-server.jar
```

This commands starts the Adrenaline Server on localhost.

### Launch Client with localhost connection - Windows

If you are trying to launch the application with a different JavaFX dependency name, please change the `--module-path` parameter accordingly.

If you would like to launch Adrenaline Clients **on the same machine** where the Adrenaline Server is, there's no need to use any option.
#### For localhost GUI:
```
cd deliverables/app
start javaw --module-path ./javafx-sdk-11.0.2-win/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar --gui
```
#### For localhost CLI:
```
cd deliverables/app
start javaw --module-path ./javafx-sdk-11.0.2-win/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar
```  

### Launch Client to connect Remotely - Windows

If you are trying to launch the application with a different JavaFX dependency name, please change the `--module-path` parameter accordingly.

If you would like to launch Adrenaline Clients **on a different machine** where the Adrenaline Server is, you shoud use the `--hostname <server-ip>` parameter.
#### For localhost GUI:
```
cd deliverables/app
start javaw --module-path ./javafx-sdk-11.0.2-win/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar --gui --hostname <server-ip>
```
#### For localhost CLI:
```
cd deliverables/app
start javaw --module-path ./javafx-sdk-11.0.2-win/lib --add-modules javafx.controls,javafx.fxml -jar ing-sw-2019-17-client.jar --hostname <server-ip>
```  
## UML Class Diagrams
You can find the complete UML Class Diagram and the packages UML Class Diagrams on `deliverables/app` folder.
