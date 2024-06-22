# Bugs
No known bugs remaining in the server.

# Token Scheme
Inside the LabyrinthOpenApiBridge class the TokenManager interface is initiated. To interact with this interface we have created the TokenManagement class which has:
- A static field with a map of the player and their respective token. 
- A constructor to initiate an admin key to delete all games.
- A method to add player tokens based on the player object 
- A method to validate a player token and return their player object. 

There is also a getPlayerToken method inside the ContextBasedRequestView class to implement proper authorization. 
For authorization, we validate the player token when we receive the request, and get the player object that corresponds to the given player token. We then compare the player objects and take action if they match.

Examples of our extra checks are to be found in:
- shoveTile() function in LabyrinthService interface.
- movePlayer() function in LabyrinthService interface.

## Id Generator
The IdGenerator class creates a randomly generated token whenever people join a game. This protects players from having malicious users copy their player token.

**Note: our generator is not real MD5, it is a dummy version**

# YAML file
We made some changes in the YAML file regarding game creation. We added hardcore as a game mode in the possible game modes (enum).
Along with that we added extra optional data "mazeCols" and "mazeRows" for variable maze sizes.
