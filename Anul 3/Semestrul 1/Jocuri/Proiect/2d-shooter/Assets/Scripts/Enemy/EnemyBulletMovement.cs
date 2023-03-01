using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyBulletMovement : MonoBehaviour
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

    // Called when the bullet enters another 2d collider
    void OnTriggerEnter2D(Collider2D other)
    {
        switch (other.gameObject.tag) 
        {
            // Bullet hits a wall. destroy itself
            case "Wall":
                destroyBullet();
                break;

            // Bullet hits an enemy. Damage the enemy and destroy the bullet.
            case "Player":
                // Damage enemy
                var enemyHealth = other.gameObject.GetComponent<Health>();
                enemyHealth.TakeDamage(1);

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

