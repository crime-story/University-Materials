using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Movement : MonoBehaviour
{

    // RigidBody of the object. Set in Unity Editor
    public Rigidbody2D rigidbody;

    // Movement speed. Set in Unity Editor
    public float moveSpeed;

    // If the enemy is currently being knocked back
    private bool isKnockedback = false;

    // Time after which the enemy in no longer being knocked back
    private float notKnockedbackTime;

    // Sprite renderer used to flip the sprite based on horizontal movement
    SpriteRenderer spriteRenderer;

    // Animator used to update animation
    Animator animator;

    void Start()
    {
        spriteRenderer = GetComponent<SpriteRenderer>();
        animator = GetComponent<Animator>();
    }

    // Move towards a position with moveSpeed
    public void MoveTowards(Vector2 position) 
    {
        if (!isKnockedback && Vector2.Distance(transform.position, position) > 0.1f)
        {
            if (Vector2.Distance(transform.position, position) > 0.1f) {
                var moveDirection = (position - new Vector2(transform.position.x, transform.position.y)).normalized;
                rigidbody.velocity = new Vector2(moveDirection.x * moveSpeed, moveDirection.y * moveSpeed);
            } else {
                rigidbody.velocity = new Vector2(0.0f, 0.0f);
            }
        }
    }

    // Stop all game object movement
    public void Stop() 
    {
        if (!isKnockedback) 
        {
            rigidbody.velocity = new Vector2(0.0f, 0.0f);
        }
    }

    // Knockback object
    public void Knockback(Vector2 knockbackVelocity, float knockbackTime)
    {
        // Update knockback time
        isKnockedback = true;
        notKnockedbackTime = Time.time + knockbackTime;

        // Apply knockback force
        rigidbody.velocity = knockbackVelocity;
    }

    void FixedUpdate()
    {
        // Check if no longer knocked back
        if (isKnockedback) 
        {
            if (Time.time > notKnockedbackTime) 
            {
                isKnockedback = false;
            }
        }
    }

    void Update()
    {
        if (rigidbody)
        {
            // Update animation depending on velocity
            if (animator)
            {
                if (rigidbody.velocity.magnitude > 0.1f)
                {
                    animator.SetBool("IsMoving", true);
                } else {
                    animator.SetBool("IsMoving", false);
                }
            }

            if (rigidbody.velocity.x > 0) {
                // normal
                spriteRenderer.flipX = false;
            } else if (rigidbody.velocity.x < 0) {
                // moving to the left -> flip
                spriteRenderer.flipX = true;
            }
        }
    }
}
