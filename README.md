Team name - LeakNFreak Members: Ammar Saifee (2023080) Aaron Sam (2023009)

Git Repository name - AP_Project Owner - ammarsksk Collaborator - Aaron23009

The GUI for our game was developed using LibGDX and the actual game implementation uses Box2D Library. The game is run using Lwjgl3Launcher.java.

Upon starting the game users can press "PLAY" to enter the level screen and further choose the required level. The functionalities that are present in our game include:
-> Using a finite number of birds to kill all pigs on the level. You lose if you run out of birds before you can kill all the pigs, else you win.
-> Different birds deal different amount of damage based on their type, while different materials and pigs can also take different out of max_hits. In addition collisions such as materials colliding with the pigs, or pigs falling on the ground have also been handled.
-> We have implemented 4 birds, 3 pigs and 3 materials.
-> Special abilities for the Red, White and Yellow bird can be used by pressing 'SPACE'. (bonus)
-> Serialization has been implemented to save the game state upon pausing the screen or going back to level screen which is loaded upon re-entering the level.
-> Upon dragging we can view the trajectory the bird will take after launch. (bonus)

GitHub Link: https://github.com/ammarsksk/AP_Project/tree/main
