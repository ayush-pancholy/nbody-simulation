When running the main method of the Nbody class, a user is prompted for a
control file. The control file must be of the following format:

Output filepath
Input filepath
Duration of simulation (seconds)
Time step (seconds)
Graphics/output file update interval (seconds)

If using randomly-generated masses, enter "random" instead of an input
filepath. If not using randomly-generated masses, the input file must be
of the following format:

rx [tab] ry [tab] rz [tab] vx [tab] vy [tab] vz [tab] m

r denotes the position in parsecs, v denotes the velocity in km/s, and m 
denotes the mass in solar masses. Each line of the final indicates a different
object. The output file is of the same format and can be used as an input
file to continue a simulation from a previously-saved state.