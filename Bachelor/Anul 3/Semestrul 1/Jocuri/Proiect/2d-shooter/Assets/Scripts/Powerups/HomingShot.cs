using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "Powerups/Bullets/Homing")]

public class HomingShot : Powerup
{
    public override void Apply(GameObject bullet)
    {
        bullet.GetComponent<BulletMovement>().SetIsHoming(true);
    }
}
