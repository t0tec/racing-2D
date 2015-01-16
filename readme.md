# racing-2D. 

[![Travis build status](https://travis-ci.org/t0tec/racing-2D.svg?branch=master)](https://travis-ci.org/t0tec/racing-2D)

## About this project:

For a school project we designed a 2D-racing game in which it should be possible to race on a circuit, to drive and set the fastest lap time.
After that you can watch your replay of your fastest lap time in the desktop application or watch a friend's replay. 
Or try to beat his lap time by adding a ghost to your game to try and find out how you can beat him!
Users can design the circuits in the web application. After this it is possible to play the game through the Java desktop application.
You can also organise tournaments consisting of multiple races. (still the tournament part was a work in progress, so not quite finished)
Also added obstacles (bonuses, penalties) to the game which gives you speed or decreases your speed.

Recently added after finishing project:
* (K)ERS-battery like in F1 to be used by pressing space.
* Added game sounds with AspectJ.
* Java Web Start (JWS) to download the desktop application from the web client.

## In the repository we have 7 directories:

* db-dump: Dump of MySQL server will be placed in the db-dump directory for backup purposes
* documents: All documents regarding the project will go here
* desktop-app: This is the Java Swing desktop application for launching the game
* desktop-executable: Runnable jar or .exe after every release.
* scripts: Here you can find Perl scripts for scraping the web and filling our MySQL DB with data
* uml: All UML diagrams & other designs will go here
* webapp: This is the main web part of the project, under this directory the Domain, REST API and Web Client part of the Racing project is stored

For documentation, howto's... there is a [wiki](https://github.com/t0tec/racing-2D/wiki) page.
