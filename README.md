# frc-idea

frc-idea is a plugin for IntelliJ IDEA that allows for FRC teams to create and deploy WPILib projects designed for robot code.

## Building

* First, clone the repository via `git clone https://github.com/Team334/frc-idea.git`.
* In IntelliJ, import the project and right click `frc-idea`.
* Click `Prepare plugin for deployment` and IntelliJ will compile and create a jar called `frc-idea.jar`.
* The jar will be located at `../frc-idea/frc-idea.jar`

## Installing

* Open up IntelliJ Settings which can be accessed via `ctrl+alt+s`.
* Go to the plugins tab and select `Install plugin from disk...`.
* Select frc-idea.jar, submit and restart, the plugin should then work.

When further progress is made in the plugin, it will be submitted for review to the Jetbrains plugin repository for official release.
Afterwards, the plugin can then be installed from there.
