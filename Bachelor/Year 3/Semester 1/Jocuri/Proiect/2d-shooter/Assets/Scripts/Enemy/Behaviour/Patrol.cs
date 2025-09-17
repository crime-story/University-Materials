using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Pathfinding;


// EnemyBehaviour that moves towards random points
[CreateAssetMenu(menuName = "EnemyBehaviour/Patrol")]
public class Patrol : EnemyBehaviour
{

    // Movement component of game object
    Movement movement;

    // Distance from target at which to stop chasing. Set in the Unity Editor. Defaults to 1
    public float stoppingDistance = 1f;

    // PATHFINDING

    // How close to be to a waypoint before moving to the next one. Set in Unity Editor. Defaults to 2
    public float nextWaypointDistance = 2f;
    
    // Path to follow (list of waypoints)
    Path path;

    // Index of the current waypoint
    int currentWaypoint = 0;

    // True if the final waypoint was reached
    bool reachedEndOfPath = false;

    // Seeker script of game object
    Seeker seeker;

    // Current target point, chosen at random.
    Vector2 point;

    // Time interval at which to execute the repetable behaviour
    public float repetableBehaviourCooldown = 5f;

    // Distance to look for a random waypoint
    public float patrolDistance = 5f;

    public override void Init(EnemyAI enemyAI)
    {
        // Get components
        seeker = enemyAI.gameObject.GetComponent<Seeker>();
        movement = enemyAI.gameObject.GetComponent<Movement>();
    }

    public override float GetRepetableBehaviourCooldown() 
    {
        return repetableBehaviourCooldown;
    }
    
    public override void RepetableBehaviour(EnemyAI enemyAI)
    {
        UpdatePath(enemyAI);
    }

    void UpdatePath(EnemyAI enemyAI) 
    {
        if (seeker.IsDone()) 
        {
            point = enemyAI.transform.position + ((Vector3) Random.insideUnitCircle.normalized * patrolDistance);
            
            // Calculate path
            seeker.StartPath(enemyAI.transform.position, point, OnPathComplete);
        }
    }

    // Callback function when a path is calculated
    void OnPathComplete(Path p) 
    {
        if (!p.error) 
        {
            // Update the path to the newly generated one and set currentWaypoint to 0
            path = p;
            currentWaypoint = 0;
        }
    }

    public override void Think(EnemyAI enemyAI) 
    {
        if (path == null || 
            currentWaypoint >= path.vectorPath.Count || 
            Vector2.Distance(enemyAI.transform.position, path.vectorPath[path.vectorPath.Count-1]) < stoppingDistance)
        {
            reachedEndOfPath = true;
            movement.Stop();
            return;
        }
        
        reachedEndOfPath = false;

        // Move towards waypoint
        movement.MoveTowards(path.vectorPath[currentWaypoint]);

        // Check if the distance was reached and update the current waypoint
        float distanceToWaypoint = Vector2.Distance(enemyAI.transform.position, path.vectorPath[currentWaypoint]);

        if (distanceToWaypoint < nextWaypointDistance) 
        {
            currentWaypoint++;
        }
    }
}
