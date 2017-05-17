/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author joel_
 */
public class PathFinder {
    /**
     * 1. At initialization add the starting location to the open list and empty the closed list
     * 2. While there are still more possible next steps in the open list and we haven’t found the target:
     *      1. Select the most likely next step (based on both the heuristic and path costs)
     *      2. Remove it from the open list and add it to the closed
     *      3. Consider each neighbor of the step. For each neighbor:
     *          1. Calculate the path cost of reaching the neighbor
     *          2. If the cost is less than the cost known for this location then remove it from the open or closed lists (since we’ve now found a better route)
     *          3. If the location isn’t in either the open or closed list then record the costs for the location and add it to the open list (this means it’ll be considered in the next search). Record how we got to this location
     *
     * Needed functions:
     * 1. getBlocked(). Is a point blocked? -> collides(new Rect(x,y,1,1))
     * 2. 
     */
    
    
    
}
