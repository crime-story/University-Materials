using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BulletMovement : MonoBehaviour
{

    // RigidBody of bullet. Set in Unity Editor
    public Rigidbody2D rigidbody;

    // Effect played before a bullet is destroyed. Set in Unity Editor
    public GameObject destroyBulletAnimation;

    // Effect played when an enemy is hit. Set in Unity Editor
    public GameObject enemyHitAnimation;

    // Speed to knockback the enemy. Set in Unity Editor
    public float knockbackSpeed;

    // Time for which to knockback the enemy. Set in Unity Editor
    public float knockbackTime;

    // Updates velocity
    Vector2 lastVelocity;

    // True if the bullet bounces off of walls
    bool isBouncy = false;

    // Number of times the bullet can bounce off
    int bouncesRemaining = 0;

    // True if the bullet follows enemies
    bool isHoming = false;

    // How fast can a homing bullet turn
    float homingSpeed = 400;

    // The minimum distance from an enemy, after which the bullet starts homing
    float homingDistance = 8;

    // Damage dealt to an enemy
    public float bulletDamage = 1;

    public void SetIsBouncy(bool flag)
    {
        isBouncy = flag;

        // Number of times the bullet is supposed to bounce
        bouncesRemaining = 4;
    }

    public void SetIsHoming(bool flag)
    {
        isHoming = flag;
    }

    void Start(){
        // Stop the rigidbody of the bullet from rotating
        rigidbody.freezeRotation = true;

        // Add the size of the bullet to homing distance so bigger bullets still have homing
        homingDistance += transform.localScale.x;
    }

    void Update() {
        
    }

    void FixedUpdate(){
        lastVelocity = rigidbody.velocity;
        

        if(isHoming){

            // Find the nearest enemy and rotate the bullet towards it

            GameObject[] enemyList;
            enemyList = GameObject.FindGameObjectsWithTag("Enemy");
            GameObject homingTarget = null;
            float minDistance = Mathf.Infinity;
            Vector3 position = transform.position;
            foreach (GameObject enemy in enemyList)
            {
                Vector3 diff = enemy.transform.position - position;
                float curDistance = diff.sqrMagnitude;
                if (curDistance < minDistance)
                {
                    homingTarget = enemy;
                    minDistance = curDistance;
                }
            }
            if (!homingTarget) {
                rigidbody.freezeRotation = true;
                return;
            }
            if (minDistance <= homingDistance) {

                // Re-enable rigidbody rotation 
                rigidbody.freezeRotation = false;

                // Start rotating the bullet
                Vector2 direction = (Vector2)homingTarget.transform.position - rigidbody.position;
                direction.Normalize();
                float rotateAmount = Vector3.Cross(direction, transform.right).z;
                rigidbody.angularVelocity = -rotateAmount * homingSpeed;

                // Bullet moves in the direction it is rotated
                var speed = lastVelocity.magnitude;
                rigidbody.velocity = transform.right*speed;
            }
            else{
                rigidbody.freezeRotation = true;
            }

        }
        
    }

    // Regular collider of the bullet(without trigger)
    private void OnCollisionEnter2D(Collision2D other) {

        switch (other.gameObject.tag) 
        {
            // Bullet hits a wall. Destroys itself if it isn't bouncy
            case "Wall":

                if (isBouncy && bouncesRemaining > 0)
                {
                    bouncesRemaining-=1;

                    // Change the direction the bullet id facing when it hits a wall
                    var direction = Vector3.Reflect(lastVelocity.normalized, other.contacts[0].normal);
                    Quaternion newRotation = Quaternion.LookRotation(Vector3.forward, Quaternion.Euler(0, 0, 90) * direction);
                    transform.rotation = newRotation;

                    // Bullet moves in the new direction
                    var speed = lastVelocity.magnitude;
                    rigidbody.velocity = transform.right*speed;

                } else {
                    destroyBullet();
                }
                break;
        }
    }

    // Collider with trigger
    void OnTriggerEnter2D(Collider2D other)
    {
        switch (other.gameObject.tag) 
        {
            

            // Bullet hits an enemy. Damage the enemy and destroy the bullet.
            case "Enemy":
                // Damage enemy
                var enemyHealth = other.gameObject.GetComponent<Health>();
                enemyHealth.TakeDamage(bulletDamage);

                var movement = other.gameObject.GetComponent<Movement>();
                if (movement)
                {
                    // Knockback enemy
                    Vector2 knockbackVelocity = (other.transform.position - transform.position).normalized * knockbackSpeed;

                    movement.Knockback(knockbackVelocity, knockbackTime);
                }

                // Destroy bullet with hit animation
                destroyBullet(true);
                break;

            // Add other checkers here...
        }
    }

    // Destroy the bullet and play an animation
    void destroyBullet(bool hitEnemy = false) 
    {
        if (hitEnemy) {
            Instantiate(enemyHitAnimation, transform.position, Quaternion.identity);
        } else {
            Instantiate(destroyBulletAnimation, transform.position, Quaternion.identity);
        }
        Destroy(gameObject);
    }
}