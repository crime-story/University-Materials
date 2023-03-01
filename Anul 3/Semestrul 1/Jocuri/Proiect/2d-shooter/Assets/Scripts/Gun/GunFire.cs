using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GunFire : MonoBehaviour
{
    // Used for getting the position of the main camera. Set in Unity Editor
    public Camera sceneCamera;

    // A bullet that can be fired from the gun. Set in Unity Editor
    public GameObject bullet;

    // A bar indicating how much the gun has been charged. Set in Unity Editor
    public GameObject ChargeBar;

    // The spawning point of the bullet. Set in Unity Editor
    public Transform firePoint; 

    // The current position of the mouse
    private Vector2 mousePosition;

    // The speed of the bullet. Set in Unity Editor. Defaults to 10
    public float fireForce = 10.0f;

    // Sound when player is shoting
    public AudioSource shootSound;

    // Variable to check if the gun is blocked by a wall
    public bool gunIsObscured = false;

    // The bullet modifiers.
    public List<Powerup> bulletModifiers = new List<Powerup>();

    // The number of bullets shot when the gun fires
    private int bulletsFired = 1;

    // Number of degrees between bullets shot at the same time
    private int bulletSpread = 10;

    // Variable indicating if the gun can be charged for a bigger shot
    private bool hasChargedShot = false;

    // Variable indicating if the gun can be charged for multiple shots
    private bool hasRepeatingShot = false;

    // How much is the next shot charged (starts at -1 because it also increases by 1 when the player clicks the mouse button to begin charging)
    private int currentChargeLevel = -1;

    // The maximum charge level
    private int maxChargeLevel = 3;

    // Increase the number of bullets fired
    public void AddShots(int nr)
    {
        bulletsFired += nr;
    }

    public void setChargedShot(bool flag)
    {
        hasChargedShot = flag;
    }

    public void setRepeatingShot(bool flag)
    {
        hasRepeatingShot = flag;
    }

    void Start()
    {
        // Hide the charge bar
        ChargeBar.SetActive(false);

        // Change the color of all charge levels to gray
        foreach(Transform child in ChargeBar.transform)
        {
            child.GetChild(0).GetComponent<SpriteRenderer>().color = new Color(0.2f, 0.2f, 0.2f, 1.0f);
        }
    }

    void Update()
    {
        updateMousePosition();
    }

    // Independent of framerate
    void FixedUpdate()
    {
        RotateGun();
    }

    // Get current mouse position in reference to the main camera
    void updateMousePosition() {
       mousePosition = sceneCamera.ScreenToWorldPoint(Input.mousePosition);
    }

    // Rotate the gun after the mouse
    void RotateGun() 
    {
        Vector2 aimDirection = mousePosition - (Vector2)transform.position;
        float aimAngle = Mathf.Atan2(aimDirection.y, aimDirection.x) * Mathf.Rad2Deg;
        transform.rotation = Quaternion.Euler(Vector3.forward * (aimAngle-90));
    }

    // Spawns a bullet then makes it move to the right of the spawn point (where the mouse is pointed)
    public void Fire(int chargeLevel)
    {
        if(!gunIsObscured)
        {
            // Shoot multiple bullets at the same time in a cone
            for (int i = 0; i < bulletsFired; i++)
            {
                // Create a bullet
                GameObject projectile = Instantiate(bullet, firePoint.position, Quaternion.Euler(0, 0, bulletSpread*( ((float) bulletsFired-1) / 2 - i ))*firePoint.rotation) as GameObject;

                if(hasChargedShot)
                {
                    // Change the size, damage and lighting of the bullet based on charge level
                    projectile.transform.localScale *= (1 + Mathf.Max(chargeLevel, 0));
                    projectile.transform.GetChild(0).GetComponent<UnityEngine.Rendering.Universal.Light2D>().pointLightOuterRadius *= (1 + Mathf.Max(chargeLevel, 0));
                    projectile.GetComponent<BulletMovement>().bulletDamage *= (1 + Mathf.Max(chargeLevel, 0));
                }

                // Add all the powerups to the bullet
                foreach (Powerup bulletModifier in bulletModifiers)
                {
                    bulletModifier.Apply(projectile);
                }

                // Fire the bullet
                projectile.GetComponent<Rigidbody2D>().velocity = (Quaternion.Euler(0, 0, bulletSpread*( ((float) bulletsFired-1) / 2 - i )) * firePoint.right) * fireForce;

                // Play shooting sound
                shootSound.Play();
            }
        }
    }

    // Before firing check if the gun was charged
    public void CheckCharging(bool buttonReleased)
    {
        // If the mouse button is released check if the gun was charged at least once
        if(buttonReleased)
        {
            if(currentChargeLevel >= 0)
            {
                if(hasRepeatingShot)
                {
                    StartCoroutine(RepeatingShot(currentChargeLevel));
                }
                else
                {
                    Fire(currentChargeLevel);
                }
                currentChargeLevel = -1;
            }

            //Hide the charge bar
            ChargeBar.SetActive(false);

            // Change the color of all charge levels to gray
            foreach(Transform child in ChargeBar.transform)
            {
                child.GetChild(0).GetComponent<SpriteRenderer>().color = new Color(0.2f, 0.2f, 0.2f, 1.0f);
            }
                
        }
        // If the mouse button is held and the gun can be charged add a charge, otherwise just fire a bullet
        else
        {
            if(hasChargedShot || hasRepeatingShot)
            {
                // If the gun was charged at least once, show the charge bar
                if(currentChargeLevel >= 0)
                {
                    //Show the charge bar
                    ChargeBar.SetActive(true);
                    
                    // Change the color of a charge level to green
                    ChargeBar.transform.GetChild(Mathf.Min(currentChargeLevel, maxChargeLevel - 1)).GetChild(0).GetComponent<SpriteRenderer>().color = new Color(0f, 0.6f, 0f, 1.0f);
                }

                // Increase the charge level
                currentChargeLevel = Mathf.Min(currentChargeLevel + 1, maxChargeLevel);

            }
            else
            {
                Fire(0);
            }
        }
    }

    // Fire multiple bullets with delay between them
    public IEnumerator RepeatingShot(int chargeLevel)
    {
        for (int i = 0; i <= chargeLevel; i++)
        {
            Fire(chargeLevel);
            yield return new WaitForSeconds(0.075f);
        }
    }
}

