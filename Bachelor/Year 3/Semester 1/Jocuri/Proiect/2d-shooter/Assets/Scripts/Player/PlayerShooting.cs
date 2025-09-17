using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class PlayerShooting : MonoBehaviour
{

    // RigidBody of player. Set in Unity Editor
    public Rigidbody2D rigidbody;

    // An instance of the GunFire class for calling it's methods
    public GunFire gun;

    // Cooldown time between shots. Set in Unity Editor
    public float shotCooldown = 1f;

    // Multiplier increased by shop items. As it increases, the gun should fire bullets more often.
    [HideInInspector] public float attackSpeedMultiplier = 1f;

    // Ui text used for showing the attackSpeedMultiplier
    public TextMeshProUGUI multiplierText;

    // Time after which the player can shoot. 
    private float nextShotTime;

    // Keeps track of whether the player is in god mode or not
    private bool godMode = false;

    // Keeps track of the initial shot cooldown
    private float initialShotCooldown;

    void Start()
    {
        // Initialize nextShotTime to the current time.
        nextShotTime = Time.time;

        // Store the initial shot cooldown
        initialShotCooldown = shotCooldown;

        multiplierText.text = "x" + attackSpeedMultiplier.ToString();
    }

    // If the player can shoot the current time must be greater than nextShotTime
    private bool canShoot()
    {
        return Time.time > nextShotTime;
    }

    // Update is called once per frame
    void Update()
    {
        // Check if the F1 key is pressed
        if (Input.GetKeyDown(KeyCode.F1))
        {
            // toggle god mode
            godMode = !godMode;
            // increase/decrease shotCooldown as per godmode on/off
            shotCooldown = godMode ? initialShotCooldown / 4 : initialShotCooldown;
        }

        Shoot();
    }

    // If the gun is blocked by a wall it shouldn't be able to fire
    void OnTriggerEnter2D(Collider2D other)
    {
        if (other.tag == "Wall")
        {
            gun.gunIsObscured = true;
        }
    }

    // After the gun is no longer blocked by a wall it
    void OnTriggerExit2D(Collider2D other)
    {
        if (other.tag == "Wall")
        {
            gun.gunIsObscured = false;
        }
    }

    // When left-clicking uses the CheckCharging method of the gun
    void Shoot()
    {
        if (Input.GetMouseButton(0)){

            if (canShoot())
            {
                // Add cooldown to nextShotTime
                nextShotTime = Time.time + shotCooldown/attackSpeedMultiplier;
                
                gun.CheckCharging(false);
            }
        }
        if (Input.GetMouseButtonUp(0)){
            gun.CheckCharging(true);
        }
    }
}