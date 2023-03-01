using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyRangedAttack : MonoBehaviour
{

    // A bullet that can be fired from the gun. Set in Unity Editor
    public GameObject bullet;

    // The spawning point of the bullet. Set in Unity Editor
    public Transform firePoint; 

    // The speed of the bullet. Set in Unity Editor. Defaults to 3
    public float fireForce = 3.0f;

    // Spawns a bullet and move it towards the target
    public void Fire(Vector2 target)
    {
        Vector2 aimDirection = target - (Vector2)firePoint.position;
        float aimAngle = Mathf.Atan2(aimDirection.y, aimDirection.x) * Mathf.Rad2Deg;
        firePoint.rotation = Quaternion.Euler(Vector3.forward * (aimAngle));


        GameObject projectile = Instantiate(bullet, firePoint.position, firePoint.rotation);
        projectile.GetComponent<Rigidbody2D>().AddForce(firePoint.right * fireForce, ForceMode2D.Impulse);
    }
}
